package com.miao.sso.auth.manager.redis;

import cn.hutool.core.util.StrUtil;
import com.miao.sso.auth.entity.content.TokenContent;
import com.miao.sso.auth.manager.AbstractTokenManager;
import com.miao.sso.base.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisTokenManager extends AbstractTokenManager {
    private static final String ACCESS_TOKEN_KEY = "SERVER_ACCESS_TOKEN_";
    private static final String REFRESH_TOKEN_KEY = "SERVER_REFRESH_TOKEN_";
    private static final String TGT_REFRESH_TOKEN_KEY = "SERVER_TGT_REFRESH_TOKEN_";

    private final StringRedisTemplate redisTemplate;
    public RedisTokenManager(int accessTokenExpired, int refreshTokenExpired, int threadPoolSize, StringRedisTemplate redisTemplate) {
        super(accessTokenExpired, refreshTokenExpired, threadPoolSize);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void create(String refreshToken, TokenContent tokenContent) {
        redisTemplate.opsForValue().set(
                ACCESS_TOKEN_KEY + tokenContent.getAccessToken(),
                refreshToken,
                this.getAccessTokenExpired(),
                TimeUnit.SECONDS);

        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_KEY + refreshToken,
                Objects.requireNonNull(JsonUtil.toString(tokenContent)),
                this.getRefreshTokenExpired(),
                TimeUnit.SECONDS);

        redisTemplate.opsForSet().add(
                TGT_REFRESH_TOKEN_KEY + tokenContent.getTgt(),
                refreshToken
        );
        redisTemplate.expire(TGT_REFRESH_TOKEN_KEY + tokenContent.getTgt(), this.getRefreshTokenExpired(), TimeUnit.SECONDS);
        log.info("Redis调用凭证创建成功, accessToken:{}, refreshToken:{}", tokenContent.getAccessToken(), refreshToken);
    }

    @Override
    public TokenContent get(String refreshToken) {
        String str = redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY + refreshToken);
        if(StrUtil.isEmpty(str)){
            return null;
        }
        return JsonUtil.parseObject(str, TokenContent.class);
    }

    @Override
    public TokenContent getByAccessToken(String accessToken) {
        String refreshToken = redisTemplate.opsForValue().get(ACCESS_TOKEN_KEY + accessToken);
        if(StrUtil.isEmpty(refreshToken)){
            return null;
        }
        return get(refreshToken);
    }

    @Override
    public Map<String, Set<String>> getAppIdMapByTGT(Set<String> tgtSet) {
        Map<String, Set<String>> appIdMap = new HashMap<>();
        tgtSet.forEach(tgt -> {
            Set<String> refreshTokenSet = redisTemplate.opsForSet().members(TGT_REFRESH_TOKEN_KEY + tgt);
            if(CollectionUtils.isEmpty(refreshTokenSet)) {
                return;
            }

            Set<String> appIdSet = new HashSet<>();
            refreshTokenSet.forEach(refreshToken -> {
                TokenContent tokenContent = get(refreshToken);
                if(tokenContent == null) {
                    return;
                }
                appIdSet.add(tokenContent.getAppId());
            });
            appIdMap.put(tgt, appIdSet);
        });
        return appIdMap;
    }

    @Override
    public void remove(String refreshToken) {
        String tc = redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY + refreshToken);
        if (!StringUtils.hasLength(tc)) {
            return;
        }
        // 删除refreshToken
        redisTemplate.delete(REFRESH_TOKEN_KEY + refreshToken);

        TokenContent tokenContent = JsonUtil.parseObject(tc, TokenContent.class);
        if (tokenContent == null) {
            return;
        }
        // 删除accessToken
        redisTemplate.delete(ACCESS_TOKEN_KEY + tokenContent.getAccessToken());

        // 删除tgt映射中的refreshToken
        redisTemplate.opsForSet().remove(TGT_REFRESH_TOKEN_KEY + tokenContent.getTgt(), refreshToken);

}

    @Override
    public void removeByTGT(String tgt) {
        Set<String> refreshTokenSet = redisTemplate.opsForSet().members(TGT_REFRESH_TOKEN_KEY + tgt);
        if(CollectionUtils.isEmpty(refreshTokenSet)) {
            return;
        }
        // 删除tgt映射中的refreshToken集合
        redisTemplate.delete(TGT_REFRESH_TOKEN_KEY + tgt);
        submitRemoveToken(refreshTokenSet);
    }

    @Override
    public void processRemoveToken(String refreshToken) {
        String tc = redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY + refreshToken);
        if (!StringUtils.hasLength(tc)) {
            return;
        }
        // 删除refreshToken
        redisTemplate.delete(REFRESH_TOKEN_KEY + refreshToken);

        TokenContent tokenContent = JsonUtil.parseObject(tc, TokenContent.class);
        if (tokenContent == null) {
            return;
        }
        // 删除accessToken
        redisTemplate.delete(ACCESS_TOKEN_KEY + tokenContent.getAccessToken());

        // 发起客户端退出请求
        log.info("发起客户端退出请求, accessToken:{}, refreshToken:{}, logoutUri:{}", tokenContent.getAccessToken(), refreshToken, tokenContent.getLogoutUri());
        sendLogoutRequest(tokenContent.getLogoutUri(), tokenContent.getAccessToken());
    }
}
