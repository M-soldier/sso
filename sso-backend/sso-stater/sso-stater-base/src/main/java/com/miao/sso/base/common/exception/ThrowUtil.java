package com.miao.sso.base.common.exception;

import com.miao.sso.base.common.result.code.ErrorCode;

/**
 * 异常抛出类
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/06
 */
public class ThrowUtil {
    /**
     * 条件成立则抛出异常
     *
     * @param condition        条件
     * @param runtimeException 异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if(condition){
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛出异常
     *
     * @param condition 条件
     * @param errorCode 错误代码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new ValidateFailedException(errorCode));
    }

    /**
     * 条件成立则抛出异常
     *
     * @param condition 条件
     * @param errorCode 错误代码
     * @param message   消息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new ValidateFailedException(errorCode, message));
    }
}
