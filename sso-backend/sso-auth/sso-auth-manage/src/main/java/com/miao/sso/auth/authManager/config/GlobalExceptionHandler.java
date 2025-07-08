package com.miao.sso.auth.authManager.config;

import com.miao.sso.base.common.result.ErrorRespond;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理程序
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/03/31
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理基本异常
     * 此处继承了BaseException的异常都可以被捕获
     *
     * @param  ex 异常值
     * @param request http请求
     * @return {@code ErrorBaseRespond }
     */
    @ExceptionHandler(Exception.class)
    public ErrorRespond<Void> handleException(Exception ex, HttpServletRequest request) {
        return new ErrorRespond<>(ex, request.getRequestURI());
    }
}
