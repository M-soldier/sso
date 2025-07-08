package com.miao.sso.base.model.rpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * rpc访问令牌
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/02
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RpcToken {
    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 令牌有效时长，单位（秒）
     */
    private int accessExpired;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 刷新令牌有效时长，单位（秒）
     */
    private int refreshExpired;

    /**
     * 用户信息
     */
    private TokenUser tokenUser;
}
