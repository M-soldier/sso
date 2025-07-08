package com.miao.sso.auth;

import com.miao.sso.auth.manager.AbstractCodeManager;
import com.miao.sso.auth.manager.AbstractTicketGrantingTicketManager;
import com.miao.sso.auth.manager.AbstractTokenManager;
import com.miao.sso.auth.manager.redis.RedisCodeManager;
import com.miao.sso.auth.manager.redis.RedisTicketGrantingTicketManager;
import com.miao.sso.auth.manager.redis.RedisTokenManager;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@AutoConfigureBefore({AuthAutoConfiguration.class})
@EnableConfigurationProperties({AuthProperties.class})
public class AuthRedisAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(AbstractCodeManager.class)
    public AbstractCodeManager abstractCodeManager(AuthProperties authProperties, StringRedisTemplate stringRedisTemplate) {
        return new RedisCodeManager(authProperties.getCodeExpired(), stringRedisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(AbstractTokenManager.class)
    public AbstractTokenManager abstractTokenManager(AuthProperties authProperties, StringRedisTemplate stringRedisTemplate){
        return new RedisTokenManager(
                authProperties.getAccessTokenExpired(),
                authProperties.getRefreshTokenExpired(),
                authProperties.getThreadPoolSize(),
                stringRedisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean(AbstractTicketGrantingTicketManager.class)
    public AbstractTicketGrantingTicketManager abstractTicketGrantingTicketManager(AuthProperties authProperties, StringRedisTemplate stringRedisTemplate, AbstractTokenManager abstractTokenManager) {
        return new RedisTicketGrantingTicketManager(
                abstractTokenManager,
                authProperties.getTgtExpired(),
                authProperties.getCookieName(),
                stringRedisTemplate);
    }
}
