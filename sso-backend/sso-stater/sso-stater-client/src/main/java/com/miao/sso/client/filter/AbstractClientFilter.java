package com.miao.sso.client.filter;


import com.miao.sso.base.common.result.ResultUtil;
import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.common.result.code.SuccessCode;
import com.miao.sso.base.utils.JsonUtil;
import com.miao.sso.client.common.ClientContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * 客户端拦截器规范
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/02
 */
public abstract class AbstractClientFilter {
    /**
     * 是否允许访问
     *
     * @return boolean
     * @throws IOException IOException
     */
    public abstract boolean isAccessAllowed() throws IOException;

    /**
     * http响应数据
     *
     * @param errorCode 错误代码
     * @param message   消息
     * @throws IOException IOException
     */
    protected void responseJson(ErrorCode errorCode, String message) throws IOException {
        HttpServletResponse response = ClientContextHolder.getResponse();
        HttpServletRequest request = ClientContextHolder.getRequest();
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(errorCode.getStatus().value());
        try (PrintWriter out = response.getWriter()) {
            out.write(Objects.requireNonNull(JsonUtil.toString(ResultUtil.error(errorCode, request.getRequestURI(), message))));
        }
    }

    /**
     * http响应数据
     *
     * @param successCode 成功代码
     * @param message     消息
     * @throws IOException IOException
     */
    protected void responseJson(SuccessCode successCode, String message) throws IOException {
        HttpServletResponse response = ClientContextHolder.getResponse();
        HttpServletRequest request = ClientContextHolder.getRequest();
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(successCode.getStatus().value());
        try (PrintWriter out = response.getWriter()) {
            out.write(Objects.requireNonNull(JsonUtil.toString(ResultUtil.success(request.getRequestURI(), message))));
        }
    }
}
