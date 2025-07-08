package com.miao.sso.auth.manager.redis;

import cn.hutool.core.util.StrUtil;
import com.miao.sso.auth.entity.content.CodeContent;
import com.miao.sso.auth.manager.AbstractCodeManager;
import com.miao.sso.base.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisCodeManager extends AbstractCodeManager {
    private static final String CODE_KEY = "SERVER_CODE_";
    private final StringRedisTemplate redisTemplate;

    public RedisCodeManager(int codeExpired, StringRedisTemplate redisTemplate) {
        super(codeExpired);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void create(String code, CodeContent codeContent) {
        redisTemplate.opsForValue().set(
                CODE_KEY + code,
                Objects.requireNonNull(JsonUtil.toString(codeContent)),
                this.getCodeExpired(),
                TimeUnit.SECONDS);
        log.info("Redis授权码创建成功, code:{}", code);
    }

    @Override
    public CodeContent get(String key) {
        String str = redisTemplate.opsForValue().get(CODE_KEY + key);
        if(StrUtil.isEmpty(str)){
            return null;
        }
        return JsonUtil.parseObject(str, CodeContent.class);
    }

    @Override
    public void remove(String key) {
        CodeContent codeContent = get(key);
        if(codeContent == null){
            return;
        }
        redisTemplate.delete(CODE_KEY + key);
    }
}
