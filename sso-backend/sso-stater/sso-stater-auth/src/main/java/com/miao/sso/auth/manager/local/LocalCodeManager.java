package com.miao.sso.auth.manager.local;

import com.miao.sso.auth.entity.content.CodeContent;
import com.miao.sso.auth.manager.AbstractCodeManager;
import com.miao.sso.base.model.entity.ExpirationPolicy;
import com.miao.sso.base.model.entity.ExpirationWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 授权码管理器
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/22
 */
@Slf4j
public class LocalCodeManager extends AbstractCodeManager implements ExpirationPolicy {

    Map<String, ExpirationWrapper<CodeContent>> codeMap = new ConcurrentHashMap<>();

    public LocalCodeManager(int codeExpired) {
        super(codeExpired);
    }

    @Override
    public void create(String code, CodeContent codeContent) {
        codeMap.put(code, new ExpirationWrapper<>(codeContent, this.getCodeExpired()));
        log.info("授权码创建成功：{}", code);
    }

    @Override
    public CodeContent get(String code) {
        ExpirationWrapper<CodeContent> wrapper = this.codeMap.get(code);
        if (wrapper != null && wrapper.isExpired()) {
            return wrapper.getObject();
        }
        return null;
    }

    @Override
    public void remove(String key) {
        this.codeMap.remove(key);
    }

    @Override
    public void verifyExpired() {
        codeMap.forEach((String code, ExpirationWrapper<CodeContent> wrapper) -> {
            if (!wrapper.isExpired()) {
                codeMap.remove(code);
                log.info("授权码已失效：{}", code);
            }
        });
    }
}
