package com.miao.sso.base.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 含时效功能的对象包装器
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/19
 */
@Data
@NoArgsConstructor
public class ExpirationWrapper<T> {
    private T object;
    /**
     * 超时时间，单位（秒）
     */
    private int expired;
    /**
     * 到期时间
     */
    private Long expiredAt;

    public ExpirationWrapper(T object, int expired) {
        this.object = object;
        this.expired = expired;
        this.expiredAt = System.currentTimeMillis() + expired * 1000L;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() < expiredAt;
    }
}
