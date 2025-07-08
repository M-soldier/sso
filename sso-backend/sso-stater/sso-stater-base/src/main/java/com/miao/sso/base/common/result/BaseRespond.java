package com.miao.sso.base.common.result;

import com.miao.sso.base.common.result.code.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.beans.Transient;
import java.io.Serializable;
import java.time.Instant;

/**
 * 响应体格式
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/03/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BaseRespond<T> implements Serializable {
    private static final long serialVersionUID = 8111849402361032665L;
    private Integer code;
    private Integer status;
    private String message;
    private String path;
    private Instant timestamp;
    // private Map<String, Object> data = new HashMap<>();
    private T data;

    public BaseRespond(Integer code, HttpStatus status, String message, String path, T data) {
        this.code = code;
        this.status = status.value();
        this.message = message;
        this.path = path;
        this.timestamp = Instant.now();
        this.data = data;
    }

    /**
     * 判断是否是成功的消息
     *
     * @return boolean
     */
    @Transient
    public boolean isSuccess() {
        return SuccessCode.SUCCESS.getCode().equals(code);
    }
}
