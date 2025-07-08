package com.miao.sso.auth.entity.expiration;

/**
 * 是否到期
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/07
 */
public interface Expiration {
    /**
     * 获取过期时间（秒）
     *
     * @return int
     */
    int getExpiresIn();
}
