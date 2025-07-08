package com.miao.sso.base.model.entity;

/**
 * 生命周期管理器
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/19
 */
public interface LifeCycleManager<T> {
    /**
     * 创建
     *
     * @param key   键
     * @param value 值
     */
    void create(String key, T value);

    /**
     * 获取
     *
     * @param key 键
     * @return {@code T }
     */
    T get(String key);

    /**
     * 删除
     *
     * @param key 键
     */
    void remove(String key);
}
