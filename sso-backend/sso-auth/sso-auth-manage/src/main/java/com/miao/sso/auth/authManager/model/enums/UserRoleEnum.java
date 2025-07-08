package com.miao.sso.auth.authManager.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户角色枚举
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/20
 */
@Getter
@AllArgsConstructor
public enum UserRoleEnum {
    USER("用户","user"),
    ADMIN("管理员", "admin");

    private final String grantType;
    private final String value;


    /**
     * 根据value值获取枚举类型，如果枚举类型特别多的话考虑MAP缓存
     *
     * @param value 值
     * @return {@code UserRoleEnum }
     */
    public static UserRoleEnum getByValue(String value) {
        for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
            if (roleEnum.getValue().equals(value)) {
                return roleEnum;
            }
        }
        return null;
    }
}
