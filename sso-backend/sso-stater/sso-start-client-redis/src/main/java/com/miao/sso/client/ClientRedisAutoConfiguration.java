package com.miao.sso.client;

import com.miao.sso.client.session.RpcTokenStorage;
import com.miao.sso.client.session.redis.RedisTokenStorage;
import com.miao.sso.client.utils.SSOUtil;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@AutoConfigureBefore({ClientAutoConfiguration.class})
public class ClientRedisAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(RpcTokenStorage.class)
    public RpcTokenStorage rpcTokenStorage(ClientProperties clientProperties, StringRedisTemplate stringRedisTemplate) {
        RpcTokenStorage rpcTokenStorage = new RedisTokenStorage(stringRedisTemplate);
        SSOUtil.init(clientProperties, rpcTokenStorage);
        return rpcTokenStorage;
    }
}
