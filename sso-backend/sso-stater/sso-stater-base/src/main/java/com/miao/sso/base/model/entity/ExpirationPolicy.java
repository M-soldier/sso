package com.miao.sso.base.model.entity;

/**
 * 到期政策
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/07
 */
public interface ExpirationPolicy {
    /**
     * 定时，每5分钟执行一次
     */
    public static final String SCHEDULED_CRON = "0 */5 * * * ?";

    /**
     * 验证是否已过期，定时清理
     */
    void verifyExpired();
}
