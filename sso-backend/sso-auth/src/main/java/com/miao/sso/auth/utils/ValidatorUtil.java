package com.miao.sso.auth.utils;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Pattern;

/**
 * 验证手机号码
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/03/30
 */
public class ValidatorUtil {
    private static final Pattern phoneNumPattern =  Pattern.compile("^1[3456789][0-9]{9}$");

    private static final Pattern MD5_PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-f]).{32}$");

    public static boolean isPhoneNum(String phoneNumber) {
        if (StrUtil.isEmpty(phoneNumber)) {
            return false;
        }
        return phoneNumPattern.matcher(phoneNumber).matches();
    }

    public static boolean passwordValidator(String password) {
        if (StrUtil.isEmpty(password)) {
            return false;
        }
        return MD5_PASSWORD_PATTERN.matcher(password).matches();
    }
}
