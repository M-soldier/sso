package com.miao.sso.base;

import com.miao.sso.base.model.entity.ExpirationPolicy;
import com.miao.sso.base.scheduler.ExpirationScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@EnableScheduling
@Configuration(proxyBeanMethods = false)
public class BaseAutoConfiguration {
    @Bean
    public ExpirationScheduler expirationScheduler(List<ExpirationPolicy> expirationPolicyList) {
        return new ExpirationScheduler(expirationPolicyList);
    }
}
