package com.miao.sso.base.common.exception;

import com.miao.sso.base.common.result.code.ErrorCode;

import java.util.Map;

/**
 * 验证失败异常
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/03/31
 */
public class ValidateFailedException extends BaseException {
    public ValidateFailedException(ErrorCode error) {
        super(error);
    }

    public ValidateFailedException(ErrorCode error, String message) {
        super(error, message);
    }

    public ValidateFailedException(ErrorCode error, Object data) {
        super(error, data);
    }

    public ValidateFailedException(ErrorCode error, String message, Object data) {
        super(error, message, data);
    }
}
