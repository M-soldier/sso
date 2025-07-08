package com.miao.sso.client.session.redis;

import cn.hutool.core.util.StrUtil;
import com.miao.sso.base.model.rpc.TokenWrapper;
import com.miao.sso.base.utils.JsonUtil;
import com.miao.sso.client.session.RpcTokenStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisTokenStorage implements RpcTokenStorage {
    private static final String ACCESS_TOKEN_KEY = "CLIENT_ACCESS_TOKEN_";
    private static final String REFRESH_TOKEN_KEY = "CLIENT_REFRESH_TOKEN_";

    private final StringRedisTemplate redisTemplate;

    public RedisTokenStorage(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void create(String accessToken, TokenWrapper tokenWrapper) {
        redisTemplate.opsForValue().set(
                ACCESS_TOKEN_KEY + accessToken,
                Objects.requireNonNull(JsonUtil.toString(tokenWrapper)),
                tokenWrapper.getObject().getRefreshExpired(),
                TimeUnit.SECONDS);

        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_KEY + tokenWrapper.getObject().getRefreshToken(),
                accessToken,
                tokenWrapper.getObject().getRefreshExpired(),
                TimeUnit.SECONDS);

        log.info("Redis服务凭证创建成功, accessToken:{}", accessToken);
    }

    @Override
    public TokenWrapper get(String accessToken) {
        String str = redisTemplate.opsForValue().get(ACCESS_TOKEN_KEY + accessToken);
        if(StrUtil.isEmpty(str)){
            return null;
        }

        return JsonUtil.parseObject(str, TokenWrapper.class);
    }

    @Override
    public void remove(String accessToken) {
        TokenWrapper tokenWrapper = get(accessToken);
        if (tokenWrapper == null) {
            return;
        }

        redisTemplate.delete(ACCESS_TOKEN_KEY + accessToken);
        redisTemplate.delete(REFRESH_TOKEN_KEY + tokenWrapper.getObject().getRefreshToken());
    }

    @Override
    public String getAccessTokenByRefreshToken(String refreshToken) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY + refreshToken);
    }
}
