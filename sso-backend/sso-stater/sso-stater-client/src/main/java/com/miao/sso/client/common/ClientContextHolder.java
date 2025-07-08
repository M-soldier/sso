package com.miao.sso.client.common;

import com.miao.sso.base.model.rpc.TokenUser;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 客户端上下文
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/19
 */
public class ClientContextHolder {
    private static final ThreadLocal<ClientContext> CLIENT_CONTEXT_HOLDER = new ThreadLocal<>();

    public static void reset() {
        CLIENT_CONTEXT_HOLDER.remove();
    }

    public static void init(HttpServletRequest request, HttpServletResponse response) {
        CLIENT_CONTEXT_HOLDER.set(new ClientContext(request, response));
    }

    public static HttpServletRequest getRequest() {
        return CLIENT_CONTEXT_HOLDER.get().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return CLIENT_CONTEXT_HOLDER.get().getResponse();
    }

    private static ClientContext getClientContext() {
        return CLIENT_CONTEXT_HOLDER.get();
    }

    public static void setUser(TokenUser user) {
        ClientContext clientContext = getClientContext();
        if (clientContext != null) {
            clientContext.setTokenUser(user);
        }
    }

    public static TokenUser getUser() {
        ClientContext clientContext = getClientContext();
        if (clientContext != null) {
            return clientContext.getTokenUser();
        }
        return null;
    }

    @Data
    private static class ClientContext {
        private HttpServletRequest request;
        private HttpServletResponse response;
        private TokenUser tokenUser;

        public ClientContext(HttpServletRequest request, HttpServletResponse response) {
            this.request = request;
            this.response = response;
        }

    }
}

