package com.miao.sso.auth.manager.redis;

import cn.hutool.core.util.StrUtil;
import com.miao.sso.auth.entity.content.TicketGrantingTicketContent;
import com.miao.sso.auth.manager.AbstractTicketGrantingTicketManager;
import com.miao.sso.auth.manager.AbstractTokenManager;
import com.miao.sso.base.model.entity.ExpirationWrapper;
import com.miao.sso.base.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisTicketGrantingTicketManager extends AbstractTicketGrantingTicketManager {
    private static final String TGT_KEY = "SERVER_TGT_";
    private final StringRedisTemplate redisTemplate;

    public RedisTicketGrantingTicketManager(AbstractTokenManager tokenManager, int tgtExpired, String cookieName, StringRedisTemplate redisTemplate) {
        super(tokenManager, tgtExpired, cookieName);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void create(String tgt, TicketGrantingTicketContent ticketGrantingTicketContent) {
        redisTemplate.opsForValue().set(
                TGT_KEY + tgt,
                Objects.requireNonNull(JsonUtil.toString(ticketGrantingTicketContent)),
                this.getTgtExpired(),
                TimeUnit.SECONDS);

        log.info("Redis登录凭证创建成功, tgt:{}", tgt);
    }

    @Override
    public TicketGrantingTicketContent get(String tgt) {
        String str = redisTemplate.opsForValue().get(TGT_KEY + tgt);
        if(StrUtil.isEmpty(str)){
            return null;
        }

        return JsonUtil.parseObject(str, TicketGrantingTicketContent.class);
    }

    @Override
    public void remove(String tgt) {
        TicketGrantingTicketContent ticketGrantingTicketContent = get(tgt);
        if(ticketGrantingTicketContent == null){
            return;
        }
        redisTemplate.delete(TGT_KEY + tgt);
        log.info("Redis登录凭证删除成功, tgt:{}", tgt);

    }

    @Override
    public void refresh(String tgt) {
        redisTemplate.expire(TGT_KEY + tgt, this.getTgtExpired(), TimeUnit.SECONDS);
    }

    @Override
    public Map<String, TicketGrantingTicketContent> getTGTMap(Set<Long> userIds, long currentPage, long pageSize) {
        Map<String, TicketGrantingTicketContent> dataMap = new LinkedHashMap<>();
        Set<String> tgtKeySet = redisTemplate.keys(TGT_KEY + "*");

        if (CollectionUtils.isEmpty(tgtKeySet)) {
            return dataMap;
        }

        List<String> tgtKeyList = new ArrayList<>(tgtKeySet);
        Collections.sort(tgtKeyList);

        long start = (currentPage - 1) * pageSize;
        long end = start + pageSize;
        long count = 0;

        for(String tgtKey : tgtKeyList) {
            String tgt = tgtKey.substring(TGT_KEY.length() + 1);
            String srtTGTContent = redisTemplate.opsForValue().get(tgtKey);

            if(StrUtil.isEmpty(srtTGTContent)){
                continue;
            }

            TicketGrantingTicketContent content = JsonUtil.parseObject(srtTGTContent, TicketGrantingTicketContent.class);
            if(!CollectionUtils.isEmpty(userIds)) {
                assert content != null;
                if (!userIds.contains(content.getUserId())) {
                    continue;
                }
            }
            // 只有当count在分页范围内时才添加到结果中
            if(count >= start && count < end) {
                dataMap.put(tgt, content);
            }
            count += 1;

            // 取到整页数据，退出循环
            if(count >= end) {
                break;
            }
        }
        return dataMap;
    }
}
