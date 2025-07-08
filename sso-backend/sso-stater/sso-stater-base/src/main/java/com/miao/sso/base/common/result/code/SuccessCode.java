package com.miao.sso.base.common.result.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * 成功代码
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/03/31
 */
@Getter
@ToString
@AllArgsConstructor
public enum SuccessCode {
    SUCCESS(20000, HttpStatus.OK, "success"),

    ;
    private final Integer code;
    private final HttpStatus status;
    private final String msg;
}
