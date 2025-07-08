package com.miao.sso.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sso.auth")
@Data
public class AuthProperties {
    /**
     * 线程池大小（执行远程通知客户端退出请求线程池大小）
     */
    private int threadPoolSize = 2;

    /**
     * 授权码超时时间，10分钟（单位秒）
     */
    private int codeExpired = 600;

    /**
     * 调用凭证accessToken超时时间，30分钟（单位秒）
     */
    private int accessTokenExpired = 1800;

    /**
     * 刷新凭证refreshToken超时时间，2小时（单位秒）
     */
    private int refreshTokenExpired = 7200;

    /**
     * 单点登录超时时间，3小时（单位秒）
     */
    private int tgtExpired = 10800;

    /**
     * cookie中TGT名称
     */
    private String cookieName = "TGT";

    /**
     * 登录URL
     */
    private String loginUrl;
}
