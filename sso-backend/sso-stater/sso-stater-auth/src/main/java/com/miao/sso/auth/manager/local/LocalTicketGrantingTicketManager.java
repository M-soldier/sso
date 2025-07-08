package com.miao.sso.auth.manager.local;

import com.miao.sso.auth.entity.content.TicketGrantingTicketContent;
import com.miao.sso.auth.manager.AbstractTicketGrantingTicketManager;
import com.miao.sso.auth.manager.AbstractTokenManager;
import com.miao.sso.base.model.entity.ExpirationPolicy;
import com.miao.sso.base.model.entity.ExpirationWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LocalTicketGrantingTicketManager extends AbstractTicketGrantingTicketManager implements ExpirationPolicy {
    private final Map<String, ExpirationWrapper<TicketGrantingTicketContent>> tgtMap = new ConcurrentHashMap<>();

    public LocalTicketGrantingTicketManager(AbstractTokenManager tokenManager, int tgtExpired, String cookieName) {
        super(tokenManager, tgtExpired, cookieName);
    }

    @Override
    public void create(String tgt, TicketGrantingTicketContent tgtContent) {
        ExpirationWrapper<TicketGrantingTicketContent> wrapper = new ExpirationWrapper<>(tgtContent, getTgtExpired());
        tgtMap.put(tgt, wrapper);
        log.debug("登录凭证创建成功, tgt:{}", tgt);
    }

    @Override
    public TicketGrantingTicketContent get(String tgt) {
        ExpirationWrapper<TicketGrantingTicketContent> wrapper = tgtMap.get(tgt);
        if (wrapper != null && wrapper.isExpired()) {
            return wrapper.getObject();
        }
        return null;
    }

    @Override
    public void remove(String tgt) {
        tgtMap.remove(tgt);
        log.info("登录凭证删除成功, tgt:{}", tgt);
    }

    @Override
    public void refresh(String tgt) {
        ExpirationWrapper<TicketGrantingTicketContent> wrapper = tgtMap.get(tgt);
        if (wrapper != null && wrapper.isExpired()) {
            wrapper.setExpiredAt(System.currentTimeMillis() + wrapper.getExpired()*1000L);
        }
    }

    @Override
    public Map<String, TicketGrantingTicketContent> getTGTMap(Set<Long> userIds, long currentPage, long pageSize) {
        Map<String, TicketGrantingTicketContent> dataMap = new LinkedHashMap<>();
        List<Map.Entry<String, ExpirationWrapper<TicketGrantingTicketContent>>> entries = new ArrayList<>(tgtMap.entrySet());

        long start = (currentPage - 1) * pageSize;
        long end = start + pageSize;
        long count = 0;

        for(Map.Entry<String, ExpirationWrapper<TicketGrantingTicketContent>> entry : entries) {
            ExpirationWrapper<TicketGrantingTicketContent> wrapper = entry.getValue();
            if(wrapper == null || !wrapper.isExpired() || wrapper.getObject() == null) {
                continue;
            }

            TicketGrantingTicketContent content = wrapper.getObject();

            if(!CollectionUtils.isEmpty(userIds) && !userIds.contains(content.getUserId())) {
                continue;
            }
            // 只有当count在分页范围内时才添加到结果中
            if(count >= start && count < end) {
                dataMap.put(entry.getKey(), content);
            }
            count += 1;

            // 取到整页数据，退出循环
            if(count >= end) {
                break;
            }
        }
        return dataMap;
    }

    @Override
    public void verifyExpired() {
        tgtMap.forEach((tgt, wrapper) -> {
            if (!wrapper.isExpired()) {
                tgtMap.remove(tgt);
            }
        });
    }
}
