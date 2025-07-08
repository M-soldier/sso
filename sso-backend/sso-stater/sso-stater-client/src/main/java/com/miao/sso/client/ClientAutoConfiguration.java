package com.miao.sso.client;

import com.miao.sso.client.filter.AbstractClientFilter;
import com.miao.sso.client.filter.LoginFilter;
import com.miao.sso.client.filter.LogoutFilter;
import com.miao.sso.client.session.RpcTokenStorage;
import com.miao.sso.client.session.impl.LocalRpcTokenStorage;
import com.miao.sso.client.utils.SSOUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ClientProperties.class})
public class ClientAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(RpcTokenStorage.class)
    public RpcTokenStorage rpcTokenStorage(ClientProperties clientProperties) {
        RpcTokenStorage rpcTokenStorage = new LocalRpcTokenStorage();
        SSOUtil.init(clientProperties, rpcTokenStorage);
        return rpcTokenStorage;
    }

    @Bean
    @ConditionalOnMissingBean(name = "loginFilter")
    public AbstractClientFilter loginFilter(ClientProperties clientProperties) {
        return new LoginFilter(clientProperties);
    }

    @Bean
    @ConditionalOnMissingBean(name = "logoutFilter")
    public AbstractClientFilter logoutFilter(ClientProperties clientProperties, RpcTokenStorage rpcTokenStorage) {
        return new LogoutFilter(clientProperties, rpcTokenStorage);
    }

    @Bean
    public FilterRegistrationBean<ClientFilterContainer> clientFilter(
            ClientProperties clientProperties, AbstractClientFilter[] filters) {
        ClientFilterContainer filterContainer = new ClientFilterContainer(clientProperties.getExcludeUrls(), filters);

        FilterRegistrationBean<ClientFilterContainer> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filterContainer);
        registrationBean.addUrlPatterns(clientProperties.getUrlPatterns());
        registrationBean.setOrder(clientProperties.getOrder());
        registrationBean.setName(clientProperties.getName());

        return registrationBean;
    }
}
