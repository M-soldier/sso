package com.miao.sso.base.common.result;

import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.common.result.code.SuccessCode;

/**
 * 结果工具类
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/19
 */
public class ResultUtil {
    /**
     * 成功
     *
     * @param path 路径
     * @return {@code BaseRespond }
     */
    public static <T> BaseRespond<T> success(String path) {
        return new BaseRespond<>(SuccessCode.SUCCESS.getCode(), SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMsg(), path, null);
    }

    /**
     * 成功
     *
     * @param path    路径
     * @param message 消息
     * @return {@code BaseRespond }
     */
    public static <T> BaseRespond<T> success(String path, String message) {
        return new BaseRespond<>(SuccessCode.SUCCESS.getCode(), SuccessCode.SUCCESS.getStatus(), message, path, null);
    }

    /**
     * 成功
     *
     * @param path 路径
     * @param data 数据
     * @return {@code BaseRespond }
     */
    public static <T> BaseRespond<T> success(String path, T data) {
        return new BaseRespond<>(SuccessCode.SUCCESS.getCode(), SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMsg(), path, data);
    }

    /**
     * 成功
     *
     * @param path    路径
     * @param message 消息
     * @param data    数据
     * @return {@code BaseRespond }
     */
    public static <T> BaseRespond<T> success(String path, String message, T data) {
        return new BaseRespond<>(SuccessCode.SUCCESS.getCode(), SuccessCode.SUCCESS.getStatus(), message, path, data);
    }

    /**
     * 错误
     *
     * @param errorCode 错误代码
     * @param path      路径
     * @return {@code BaseRespond }
     */
    public static <T> BaseRespond<T> error(ErrorCode errorCode, String path) {
        return new BaseRespond<>(errorCode.getCode(), errorCode.getStatus(), errorCode.getMsg(), path, null);
    }

    /**
     * 错误
     *
     * @param errorCode 错误代码
     * @param path      路径
     * @param message   消息
     * @return {@code BaseRespond }
     */
    public static <T> BaseRespond<T> error(ErrorCode errorCode, String path, String message) {
        return new BaseRespond<>(errorCode.getCode(), errorCode.getStatus(), message, path, null);
    }

    /**
     * 错误
     *
     * @param errorCode 错误代码
     * @param path      路径
     * @param data      数据
     * @return {@code BaseRespond }
     */
    public static <T> BaseRespond<T> error(ErrorCode errorCode, String path, T data) {
        return new BaseRespond<>(errorCode.getCode(), errorCode.getStatus(), errorCode.getMsg(), path, data);
    }

    /**
     * 错误
     *
     * @param errorCode 错误代码
     * @param path      路径
     * @param message   消息
     * @param data      数据
     * @return {@code BaseRespond }
     */
    public static <T> BaseRespond<T> error(ErrorCode errorCode, String path, String message, T data) {
        return new BaseRespond<>(errorCode.getCode(), errorCode.getStatus(), message, path, data);
    }
}
