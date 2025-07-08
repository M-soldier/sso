package com.miao.sso.client.session.impl;

import com.miao.sso.base.model.entity.ExpirationPolicy;
import com.miao.sso.base.model.entity.ExpirationWrapper;
import com.miao.sso.base.model.rpc.TokenWrapper;
import com.miao.sso.client.session.RpcTokenStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地Session存储
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/02
 */
@Slf4j
public class LocalRpcTokenStorage implements RpcTokenStorage, ExpirationPolicy {
    private final Map<String, TokenWrapper> accessTokenMap = new ConcurrentHashMap<>();
    private final Map<String, ExpirationWrapper<String>> refreshTokenMap = new ConcurrentHashMap<>();

    @Override
    public void create(String accessToken, TokenWrapper tokenWrapper) {
        accessTokenMap.put(accessToken, tokenWrapper);
        refreshTokenMap.put(tokenWrapper.getObject().getRefreshToken(),
                            new ExpirationWrapper<String>(tokenWrapper.getObject().getAccessToken(), tokenWrapper.getObject().getRefreshExpired()));
        log.info("服务凭证创建成功： [AT]-{}", accessToken);
    }

    @Override
    public String getAccessTokenByRefreshToken(String refreshToken) {
        ExpirationWrapper<String> wrapper = refreshTokenMap.get(refreshToken);
        if(wrapper == null || !wrapper.isExpired()) {
            return null;
        }
        return wrapper.getObject();
    }

    @Override
    public TokenWrapper get(String accessToken) {
        TokenWrapper tokenWrapper = accessTokenMap.get(accessToken);
        if(tokenWrapper == null || !tokenWrapper.refreshIsExpired()) {
            return null;
        }
        return tokenWrapper;
    }

    @Override
    public void remove(String accessToken) {
        log.info("accessToken: {}", accessToken);
        TokenWrapper tokenWrapper = accessTokenMap.remove(accessToken);
        if(tokenWrapper == null){
            return;
        }
        log.info("tokenWrapper: {}", tokenWrapper);
        refreshTokenMap.remove(tokenWrapper.getObject().getRefreshToken());
    }

    @Override
    @Scheduled(cron = SCHEDULED_CRON)
    public void verifyExpired() {
        accessTokenMap.forEach((String accessToken, TokenWrapper tokenWrapper) -> {
            // 刷新凭证过期才算过期
            if(!tokenWrapper.refreshIsExpired()) {
                remove(accessToken);
                log.info("服务凭证已失效：[AT]-{}", accessToken);
            }
        });

        refreshTokenMap.forEach((String refreshToken, ExpirationWrapper<String> wrapper) -> {
            if(!wrapper.isExpired()) {
                remove(refreshToken);
            }
        });
    }
}
