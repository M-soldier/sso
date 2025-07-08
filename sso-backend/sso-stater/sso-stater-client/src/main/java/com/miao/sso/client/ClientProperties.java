package com.miao.sso.client;

import com.miao.sso.base.common.exception.ValidateFailedException;
import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.client.constant.ClientConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@Data
@ConfigurationProperties(prefix = "sso")
public class ClientProperties {
    /**
     * 客户端Filter容器名称
     */
    private String name = "clientContainer";

    /**
     * 服务器配置
     */
    private Server server;

    /**
     * 客户端配置
     */
    private Client client;

    /**
     * 是否启用前后端分离
     */
    private boolean h5Enabled = false;

    /**
     * 拦截urls，默认拦截全路径
     */
    private String[] urlPatterns = {ClientConstant.URL_FUZZY_MATCH};

    /**
     * 忽略拦截urls
     */
    private String[] excludeUrls;

    /**
     * 过滤器排序，默认10
     */
    private int order = 10;

    /**
     * 客户端注销地址
     */
    private String logoutPath = "/logout";

    /**
     * 存放在cookie或者Header中的token名称前缀
     */
    private String tokenNamePrefix = "sso-oauth2-token-";

    @Data
    public static class Server {
        String loginUrl;
        String appServerUrl;
        String ssoServerUrl;
    }

    @Data
    public static class Client {
        String id;
        String secret;
    }

    @PostConstruct
    public void validate() {
        if (h5Enabled) {
            if (server == null || server.getAppServerUrl() == null || server.getAppServerUrl().isEmpty()) {
                throw new ValidateFailedException(ErrorCode.PARAMS_ERROR, "sso.server.appServerUrl is required when sso.h5-enabled is true");
            }
        }
    }


    // sso:
    //     appName: "clientContainer"
    //     server:
    //         login-url: http://sso.authentication.com:8080
    //         app-server-url: http://client.xiaomiao.com:8082
    //         sso-server-url: http://sso.authentication.com:8081
    //     client:
    //         id: 6047682481
    //         secret: c9f94c2e215d4371b970723a4a8de007
    //     h5-enabled: true
    //     url-patterns: ["/*"]
    //     exclude-urls: ["/v2"]
    //     logout-path: "/logout"
    //     token-appName-prefix: "sso-oauth2-token-"
}
