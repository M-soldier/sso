package com.miao.sso.client.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.miao.sso.base.common.result.BaseRespond;
import com.miao.sso.base.common.result.ResultUtil;
import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.constants.BaseConstant;
import com.miao.sso.base.enums.GrantTypeEnum;
import com.miao.sso.base.model.rpc.RpcToken;
import com.miao.sso.base.utils.HttpUtil;
import com.miao.sso.base.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2工具类
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/09
 */
@Slf4j
public class Oauth2Util {
    /**
     * 获取rpc令牌
     *
     * @param url      网址
     * @param paramMap 参数表
     * @return {@code BaseRespond<RpcToken> }
     * @throws JsonProcessingException json处理异常
     */
    public static BaseRespond<RpcToken> getRpcToken(String url, Map<String, String> paramMap) throws JsonProcessingException {
        String jsonStr = HttpUtil.get(url, paramMap);
        if (jsonStr == null || jsonStr.isEmpty()) {
            log.error("getRpcToken exception, return null. url:{}", url);
            return ResultUtil.error(ErrorCode.SYSTEM_ERROR, null, "获取token失败");
        }

        BaseRespond<RpcToken> result = JsonUtil.parseObject(jsonStr, new TypeReference<BaseRespond<RpcToken>>(){});

        if(result==null){
            log.error("parse accessToken return null. jsonStr:{}", jsonStr);
            return ResultUtil.error(ErrorCode.SYSTEM_ERROR, null, "解析token失败");
        }

        return result;
    }

    /**
     * 获取访问令牌（授权码模式）
     *
     * @param serverUrl    url
     * @param appId     应用ID
     * @param appSecret 应用程序密钥
       * @param code       代码
     * @return {@code BaseRespond }
     */
    public static BaseRespond<RpcToken> getAccessToken(String serverUrl, String appId, String appSecret, String code, String logoutUri) throws JsonProcessingException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(BaseConstant.GRANT_TYPE, GrantTypeEnum.AUTHORIZATION_CODE.getValue());
        paramMap.put(BaseConstant.APP_ID, appId);
        paramMap.put(BaseConstant.APP_SECRET, appSecret);
        paramMap.put(BaseConstant.AUTH_CODE, code);
        paramMap.put(BaseConstant.LOGOUT_URI, logoutUri);
        return getRpcToken(serverUrl+BaseConstant.ACCESS_TOKEN_PATH, paramMap);
    }

    /**
     * 刷新访问令牌
     *
     * @param serverUrl    url
     * @param appId     应用ID
     * @param refreshToken 刷新令牌
     * @return {@code BaseRespond }
     */
    public static BaseRespond<RpcToken> getRefreshAccessToken(String serverUrl, String appId, String refreshToken) throws JsonProcessingException {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(BaseConstant.APP_ID, appId);
        paramMap.put(BaseConstant.REFRESH_TOKEN, refreshToken);
        return getRpcToken(serverUrl+BaseConstant.REFRESH_TOKEN_PATH, paramMap);
    }
}
