package com.miao.sso.base.common.exception;

import com.miao.sso.base.common.result.code.ErrorCode;

import java.util.Map;

/**
 * 未找到资源异常(自定义异常) 通过继承BaseException，非常简单！
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/03/31
 */
public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(Map<String, Object> data) {
        super(ErrorCode.NOT_FOUND_ERROR, data);
    }
}
