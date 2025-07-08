package com.miao.sso.base.common.exception;

import com.miao.sso.base.common.result.code.ErrorCode;

import java.util.Map;

public class DatabaseException extends BaseException {
    public DatabaseException(ErrorCode error) {
        super(error);
    }

    public DatabaseException(ErrorCode error, String message) {
        super(error, message);
    }

    public DatabaseException(ErrorCode error, Map<String, Object> data) {
        super(error, data);
    }

    public DatabaseException(ErrorCode error, String message, Map<String, Object> data) {
        super(error, message, data);
    }
}
