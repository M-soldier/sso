package com.miao.sso.base.common.result;

import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;

import java.time.Instant;
import java.util.Map;

/**
 * 错误响应
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/03/31
 */
@Slf4j
public class ErrorRespond<T> extends BaseRespond<T> {
    private static final long serialVersionUID = -5884813840527774042L;

    public  ErrorRespond(Exception ex, String path) {
        if (ex instanceof BaseException) {
            BaseException baseException = (BaseException) ex;
            this.setFields(baseException.getError(), path, ex.getMessage(), (T) baseException.getData());
            log.error("请求{}发生异常，{}", path, baseException.getMessage());
        } else if (ex instanceof BindException) {
            this.setFields(ErrorCode.REQUEST_VALIDATION_ERROR, path, "参数绑定异常", null);
            BindException bindException = (BindException) ex;
            StringBuilder sb = new StringBuilder();
            for (ObjectError objectError : bindException.getBindingResult().getAllErrors()) {
                sb.append(objectError.getDefaultMessage());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            // 细化校验错误信息
            this.setMessage(this.getMessage() + " : " + sb);
            log.error("请求{}发生绑定异常，{}", path, this.getMessage() + " : " + sb);
        }else {
            this.setFields(ErrorCode.SYSTEM_ERROR, path, ex.getMessage(), null);
        }
    }

    /**
     * 设置字段
     *
     * @param errorCode 错误代码
     * @param path      路径
     * @param data      数据包
     */
    protected void setFields(ErrorCode errorCode, String path, String message, T data) {
        this.setCode(errorCode.getCode());
        this.setStatus(errorCode.getStatus().value());
        this.setMessage(message);
        this.setPath(path);
        this.setTimestamp(Instant.now());
        this.setData(data);
    }
}
