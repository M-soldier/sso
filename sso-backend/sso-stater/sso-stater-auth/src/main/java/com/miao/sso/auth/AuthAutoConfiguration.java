package com.miao.sso.auth;

import com.miao.sso.auth.manager.AbstractCodeManager;
import com.miao.sso.auth.manager.AbstractTicketGrantingTicketManager;
import com.miao.sso.auth.manager.AbstractTokenManager;
import com.miao.sso.auth.manager.local.LocalCodeManager;
import com.miao.sso.auth.manager.local.LocalTicketGrantingTicketManager;
import com.miao.sso.auth.manager.local.LocalTokenManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AuthProperties.class})
public class AuthAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AbstractCodeManager codeManager(AuthProperties authProperties) {
        return new LocalCodeManager(authProperties.getCodeExpired());
    }

    @Bean
    @ConditionalOnMissingBean
    public AbstractTokenManager tokenManager(AuthProperties authProperties) {
        return new LocalTokenManager(
                authProperties.getAccessTokenExpired(),
                authProperties.getRefreshTokenExpired(),
                authProperties.getThreadPoolSize());
    }

    @Bean
    @ConditionalOnMissingBean
    public AbstractTicketGrantingTicketManager ticketGrantingTicketManager(AuthProperties authProperties, AbstractTokenManager tokenManager) {
        return new LocalTicketGrantingTicketManager(
                tokenManager,
                authProperties.getTgtExpired(),
                authProperties.getCookieName()
        );
    }
}
