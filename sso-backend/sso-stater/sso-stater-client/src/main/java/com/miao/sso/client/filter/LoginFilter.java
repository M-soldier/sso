package com.miao.sso.client.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.miao.sso.base.common.result.BaseRespond;
import com.miao.sso.base.common.result.ResultUtil;
import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.constants.BaseConstant;
import com.miao.sso.base.model.rpc.RpcToken;
import com.miao.sso.base.model.rpc.TokenWrapper;
import com.miao.sso.client.common.ClientContextHolder;
import com.miao.sso.client.ClientProperties;

import com.miao.sso.client.utils.SSOUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录拦截器
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/02
 */
@Slf4j
@Order(20)
@AllArgsConstructor
public class LoginFilter extends AbstractClientFilter {
    private ClientProperties clientProperties;

    @Override
    public boolean isAccessAllowed() throws IOException {
        TokenWrapper tokenWrapper = SSOUtil.getTokenWrapper();
        if (tokenWrapper != null) {
            if(tokenWrapper.isExpired()) {
                ClientContextHolder.setUser(tokenWrapper.getObject().getTokenUser());
                return true;
            }
            if(tokenWrapper.refreshIsExpired()){
                BaseRespond<RpcToken> result = SSOUtil.getHttpRefreshTokenAndUpdateInCookie(tokenWrapper.getObject().getRefreshToken());
                if(result.isSuccess()){
                    ClientContextHolder.setUser(result.getData().getTokenUser());
                    return true;
                }
            }
        }
        String code = ClientContextHolder.getRequest().getParameter(BaseConstant.AUTH_CODE);
        if(StrUtil.isNotEmpty(code) && SSOUtil.getHttpAccessTokenAndSaveInCookie(code).isSuccess()){
            redirectUrlRemoveCode();
        }else {
            redirectToLogin();
        }
        return false;
    }


    protected boolean isAjaxRequest() {
        String requestedWith = ClientContextHolder.getRequest().getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }


    private void redirectToLogin() throws IOException {
        HttpServletRequest request = ClientContextHolder.getRequest();
        HttpServletResponse response = ClientContextHolder.getResponse();
        // AJAX 请求（异步请求）：如果服务器返回 重定向响应，前端不会自动跳转，可能导致异常或错误。
        // if (isAjaxRequest()) {
        //     responseJson(ErrorCode.SYSTEM_ERROR, "不支持异步请求");
        // } else
        if (clientProperties.isH5Enabled()) {
            // 返回登录页面地址
            String loginUrl = SSOUtil.buildLoginUrlForH5();
            BaseRespond<String> baseRespond = ResultUtil.error(ErrorCode.NO_AUTH_ERROR, request.getRequestURI(), "未登录，请登录", loginUrl);
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            String jsonString = JSONObject.toJSONString(baseRespond);
            PrintWriter out = response.getWriter();
            out.print(jsonString);
            out.flush();
            out.close();
        } else {
            // 客户端重定向
            response.sendRedirect(SSOUtil.buildLoginUrl(request.getRequestURL().toString()));
        }
    }

    private void redirectUrlRemoveCode() throws IOException {
        HttpServletRequest request = ClientContextHolder.getRequest();
        HttpServletResponse response = ClientContextHolder.getResponse();
        if(clientProperties.isH5Enabled()) {
            String redirectUri = request.getParameter("redirectUri");
            if (redirectUri != null) {
                response.sendRedirect(redirectUri);
            }
        }else {
            String currentUrl = request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            currentUrl = currentUrl.substring(0, currentUrl.indexOf(BaseConstant.AUTH_CODE) - 1);
            response.sendRedirect(currentUrl);
        }

    }
}
