package com.miao.sso.auth.manager.local;

import com.miao.sso.auth.entity.content.TokenContent;
import com.miao.sso.auth.manager.AbstractTokenManager;
import com.miao.sso.base.model.entity.ExpirationPolicy;
import com.miao.sso.base.model.entity.ExpirationWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LocalTokenManager extends AbstractTokenManager implements ExpirationPolicy {
    private final Map<String, ExpirationWrapper<String>> accessTokenMap = new ConcurrentHashMap<>();
    private final Map<String, ExpirationWrapper<TokenContent>> refreshTokenMap = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> tgtMap = new ConcurrentHashMap<>();

    public LocalTokenManager(int accessTokenExpired, int refreshTokenExpired, int theadPoolSize) {
        super(accessTokenExpired, refreshTokenExpired, theadPoolSize);
    }

    @Override
    public void create(String refreshToken, TokenContent tokenContent) {
        ExpirationWrapper<String> accessTokenWrapper = new ExpirationWrapper<>(refreshToken, this.getAccessTokenExpired());
        accessTokenMap.put(tokenContent.getAccessToken(), accessTokenWrapper);

        ExpirationWrapper<TokenContent> refreshTokenWrapper = new ExpirationWrapper<>(tokenContent, this.getRefreshTokenExpired());
        refreshTokenMap.put(tokenContent.getRefreshToken(), refreshTokenWrapper);


        tgtMap.computeIfAbsent(tokenContent.getTgt(), k -> new HashSet<>()).add(refreshToken);
        log.info("调用凭证创建成功, accessToken:{}, refreshToken:{}", tokenContent.getAccessToken(), refreshToken);
    }

    @Override
    public TokenContent get(String refreshToken) {
        ExpirationWrapper<TokenContent> wrapper = refreshTokenMap.get(refreshToken);
        if (wrapper != null && wrapper.isExpired()) {
            return wrapper.getObject();
        }
        return null;
    }

    @Override
    public TokenContent getByAccessToken(String accessToken) {
        ExpirationWrapper<String> wrapper = accessTokenMap.get(accessToken);
        if(wrapper == null || !wrapper.isExpired()) {
            return null;
        }
        return get(wrapper.getObject());
    }

    @Override
    public Map<String, Set<String>> getAppIdMapByTGT(Set<String> tgtSet) {
        Map<String, Set<String>> appIdMap = new HashMap<>();
        tgtSet.forEach(tgt -> {
            Set<String> refreshTokenSet = tgtMap.get(tgt);
            Set<String> appIdSet = new HashSet<>();
            if(!CollectionUtils.isEmpty(refreshTokenSet)) {
                refreshTokenSet.forEach(refreshToken -> {
                    ExpirationWrapper<TokenContent> wrapper = refreshTokenMap.get(refreshToken);
                    if(wrapper == null) {
                        return;
                    }
                    TokenContent tokenContent = wrapper.getObject();
                    if(tokenContent == null) {
                        return;
                    }
                    appIdSet.add(tokenContent.getAppId());
                });
            }
            appIdMap.put(tgt, appIdSet);
        });
        return appIdMap;
    }

    @Override
    public void remove(String refreshToken) {
        // 删除refreshToken
        ExpirationWrapper<TokenContent> wrapper = refreshTokenMap.remove(refreshToken);
        if(wrapper != null) {
            // 删除accessToken
            accessTokenMap.remove(wrapper.getObject().getAccessToken());

            // 删除tgt映射中的refreshToken
            Set<String> refreshTokenSet = tgtMap.get(wrapper.getObject().getTgt());
            if(!CollectionUtils.isEmpty(refreshTokenSet)) {
                refreshTokenSet.remove(refreshToken);
            }
        }
    }

    @Override
    public void removeByTGT(String tgt) {
        Set<String> refreshTokenSet = tgtMap.remove(tgt);
        log.info("refreshTokenSet: {}", refreshTokenSet);
        if(!CollectionUtils.isEmpty(refreshTokenSet)) {
            submitRemoveToken(refreshTokenSet);
        }
    }

    @Override
    public void processRemoveToken(String refreshToken) {
        ExpirationWrapper<TokenContent> wrapper = refreshTokenMap.remove(refreshToken);
        if(wrapper == null) {
            return;
        }

        TokenContent tokenContent = wrapper.getObject();
        if(tokenContent == null) {
            return;
        }

        accessTokenMap.remove(tokenContent.getAccessToken());
        log.info("发起客户端退出请求, accessToken:{}, refreshToken:{}, logoutUri:{}", tokenContent.getAccessToken(), refreshToken, tokenContent.getLogoutUri());
        sendLogoutRequest(tokenContent.getLogoutUri(), tokenContent.getAccessToken());
    }

    @Override
    public void verifyExpired() {
        accessTokenMap.forEach((accessToken, wrapper) -> {
            if(!wrapper.isExpired()) {
                accessTokenMap.remove(accessToken);
                log.info("调用凭证已失效, accessToken:{}", accessToken);
            }
        });

        refreshTokenMap.forEach((refreshToken, wrapper) -> {
            if(!wrapper.isExpired()) {
                remove(refreshToken);
                log.info("刷新凭证已失效, accessToken:{}, refreshToken:{}", wrapper.getObject().getAccessToken(), refreshToken);
            }
        });
    }
}
