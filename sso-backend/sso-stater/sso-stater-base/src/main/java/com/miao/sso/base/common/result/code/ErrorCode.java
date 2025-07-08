package com.miao.sso.base.common.result.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * 错误代码
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/03/31
 */
@Getter
@AllArgsConstructor
@ToString
public enum ErrorCode {
    PARAMS_ERROR(40000, HttpStatus.BAD_REQUEST, "请求参数错误"),

    // 登录错误响应码
    // LOGIN_ERROR(1020, HttpStatus.BAD_REQUEST, "手机号码或密码为空"),
    // PHONE_NUMBER_ERROR(1021, HttpStatus.BAD_REQUEST, "手机号码格式不正确"),
    USER_NOT_EXIT_ERROR(40100, HttpStatus.UNAUTHORIZED, "用户不存在"),
    ACCOUNT_ERROR(40110, HttpStatus.UNAUTHORIZED, "账号错误"),
    PASSWORD_ERROR(40111, HttpStatus.UNAUTHORIZED, "密码错误"),

    APP_NOT_EXIT_ERROR(40115, HttpStatus.UNAUTHORIZED, "应用不存在"),
    APP_SECRET_ERROR(40116, HttpStatus.UNAUTHORIZED, "appSecret错误"),

    TGT_ERROR(40120, HttpStatus.UNAUTHORIZED, "tgt过期或不正确"),
    CODE_ERROR(40124, HttpStatus.UNAUTHORIZED, "code有误或已过期"),
    TOKEN_ERROR(40128, HttpStatus.UNAUTHORIZED, "token有误或已过期"),

    NO_AUTH_ERROR(40130, HttpStatus.UNAUTHORIZED, "无权限"),
    GRANT_TYPE_ERROR(40131, HttpStatus.UNAUTHORIZED, "认证方式错误"),


    FORBIDDEN_ERROR(40300, HttpStatus.FORBIDDEN, "禁止访问"),

    NOT_FOUND_ERROR(40400, HttpStatus.NOT_FOUND, "未找到该资源"),
    REQUEST_VALIDATION_ERROR(41100, HttpStatus.LENGTH_REQUIRED, "请求数据格式验证失败"),

    SYSTEM_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "系统异常"),
    OPERATION_ERROR(50001, HttpStatus.INTERNAL_SERVER_ERROR, "操作失败")
    ;
    private final Integer code;
    private final HttpStatus status;
    private final String msg;
}
