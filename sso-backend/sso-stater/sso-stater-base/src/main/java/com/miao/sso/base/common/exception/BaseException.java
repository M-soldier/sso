package com.miao.sso.base.common.exception;

import com.miao.sso.base.common.result.code.ErrorCode;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 基异常（继承自运行时异常）
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/03/31
 */
@Getter
public abstract class BaseException extends RuntimeException {
    private final ErrorCode error;
    private final String message;
    private final Object data;

    public BaseException(ErrorCode error){
        super(error.getMsg());
        this.error = error;
        this.message = error.getMsg();
        this.data = null;
    }

    public BaseException(ErrorCode error, String message) {
        super(message);
        this.error = error;
        this.message = message;
        this.data = null;
    }

    public BaseException(ErrorCode error, Object data) {
        super(error.getMsg());
        this.error = error;
        this.message = error.getMsg();
        this.data = data;
    }

    public BaseException(ErrorCode error, String message, Object data) {
        super(message);
        this.error = error;
        this.message = message;
        this.data = data;
    }

    protected BaseException(ErrorCode error, String message, Object data, Throwable cause) {
        super(message, cause);
        this.error = error;
        this.message = message;
        this.data = data;
    }
}
