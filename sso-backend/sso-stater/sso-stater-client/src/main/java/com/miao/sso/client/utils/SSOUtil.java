package com.miao.sso.client.utils;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.miao.sso.base.common.result.BaseRespond;
import com.miao.sso.base.common.result.ResultUtil;
import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.constants.BaseConstant;
import com.miao.sso.base.model.rpc.RpcToken;
import com.miao.sso.base.model.rpc.TokenWrapper;
import com.miao.sso.base.utils.CookieUtil;
import com.miao.sso.client.ClientProperties;
import com.miao.sso.client.common.ClientContextHolder;
import com.miao.sso.client.session.RpcTokenStorage;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class SSOUtil {
    private static ClientProperties clientProperties;
    private static RpcTokenStorage rpcTokenStorage;

    public static void init(ClientProperties clientProperties, RpcTokenStorage rpcTokenStorage) {
        SSOUtil.clientProperties = clientProperties;
        SSOUtil.rpcTokenStorage = rpcTokenStorage;
    }

    public static String getTokenName() {
        return clientProperties.getTokenNamePrefix() + clientProperties.getClient().getId();
    }

    public static String getAccessToken() {
        String tokenName = getTokenName();
        HttpServletRequest request = ClientContextHolder.getRequest();
        String accessToken = CookieUtil.getCookieValue(tokenName, request);
        if (!StrUtil.isEmpty(accessToken)) {
            return accessToken;
        }
        return request.getHeader(tokenName);
    }

    public static TokenWrapper getTokenWrapper() {
        String accessToken = getAccessToken();
        if (StrUtil.isNotEmpty(accessToken)) {
            return rpcTokenStorage.get(accessToken);
        }
        return null;
    }

    public static BaseRespond<RpcToken> getHttpAccessToken(String code) throws JsonProcessingException {
        BaseRespond<RpcToken> result = Oauth2Util.getAccessToken( clientProperties.getServer().getSsoServerUrl(), clientProperties.getClient().getId(),
                clientProperties.getClient().getSecret(), code, getLocalUrl()+ BaseConstant.CLIENT_LOGOUT_PATH);
        if(result.isSuccess()){
            RpcToken rpcToken = result.getData();
            rpcTokenStorage.create(rpcToken);
        }else{
            log.error("getHttpAccessToken has error, message:{}", result.getMessage());
        }
        return result;
    }

    public static void addAccessTokenToCookie(String accessToken) {
        CookieUtil.setCookieValue(getTokenName(), accessToken, "/", ClientContextHolder.getRequest(),
                ClientContextHolder.getResponse());
    }

    public static BaseRespond<RpcToken> getHttpAccessTokenAndSaveInCookie(String code) throws JsonProcessingException {
        BaseRespond<RpcToken> result = getHttpAccessToken(code);
        if(result.isSuccess()){
            addAccessTokenToCookie(result.getData().getAccessToken());
        }
        return result;
    }

    public static BaseRespond<RpcToken> getHttpRefreshToken(String refreshToken) throws JsonProcessingException {
        String accessToken = rpcTokenStorage.getAccessTokenByRefreshToken(refreshToken);
        if(StrUtil.isEmpty(accessToken)){
            return ResultUtil.error(ErrorCode.FORBIDDEN_ERROR, null, "accessToken is null or expired!");
        }
        BaseRespond<RpcToken> result = Oauth2Util.getRefreshAccessToken(clientProperties.getServer().getSsoServerUrl(),
                clientProperties.getClient().getId(), refreshToken);
        if(result.isSuccess()){
            rpcTokenStorage.remove(accessToken);
            rpcTokenStorage.create(result.getData());
        }else {
            log.error("getHttpRefreshToken has error, message:{}", result.getMessage());
        }
        return result;
    }

    public static BaseRespond<RpcToken> getHttpRefreshTokenAndUpdateInCookie(String refreshToken) throws JsonProcessingException {
        BaseRespond<RpcToken> result = getHttpRefreshToken(refreshToken);
        if(result.isSuccess()){
            // Response写入cookie（真正的更新）
            addAccessTokenToCookie(result.getData().getAccessToken());
            // 更新当前Request中Cookie的token值（伪更新，目的是让这个请求在之后也能在Cookie中拿到新的token值）
            CookieUtil.updateCookie(getTokenName(), result.getData().getAccessToken(), ClientContextHolder.getRequest());
        }
        return result;
    }

    public static String buildLoginUrlForH5() {
        return clientProperties.getServer().getSsoServerUrl() + BaseConstant.LOGIN_PATH + "?"
                + BaseConstant.APP_ID + "=" + clientProperties.getClient().getId() + "&"
                + BaseConstant.APP_SERVER_URI + "=" + clientProperties.getServer().getAppServerUrl();
    }

    public static String buildLoginUrl(String redirectUri) {
        return clientProperties.getServer().getSsoServerUrl() + BaseConstant.LOGIN_PATH + "?"
                + BaseConstant.APP_ID + "=" + clientProperties.getClient().getId() + "&"
                + BaseConstant.REDIRECT_URI + "=" + redirectUri;
    }

    public static String buildLogoutUrl(String redirectUri) {
        redirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        return clientProperties.getServer().getSsoServerUrl() + BaseConstant.LOGOUT_PATH + "?"
                + BaseConstant.REDIRECT_URI + "=" + redirectUri;
    }

    public static String buildLogoutUrl() {
        return buildLogoutUrl(getLocalUrl());
    }


    public static String getLocalUrl(){
        HttpServletRequest request = ClientContextHolder.getRequest();
        StringBuilder url = new StringBuilder();
        url.append(request.getScheme()).append("://").append(request.getServerName());
        if (request.getServerPort() != 80 || request.getServerPort() != 443) {
            url.append(":").append(request.getServerPort());
        }
        // url.append(request.getContextPath());
        return url.toString();
    }
}
