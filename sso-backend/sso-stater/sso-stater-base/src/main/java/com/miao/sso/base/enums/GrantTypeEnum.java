package com.miao.sso.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 授权类型枚举
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/10
 */
@Getter
@AllArgsConstructor
public enum GrantTypeEnum {
    /**
     * 授权码模式
     */
    AUTHORIZATION_CODE("authorization_code"),
    /**
     * 密码模式
     */
    PASSWORD("password");

    private final String value;
}
