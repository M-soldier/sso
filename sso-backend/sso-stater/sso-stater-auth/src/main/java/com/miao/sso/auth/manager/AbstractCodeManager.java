package com.miao.sso.auth.manager;

import com.miao.sso.auth.entity.content.CodeContent;
import com.miao.sso.auth.utils.UUIDUtil;
import com.miao.sso.base.model.entity.LifeCycleManager;
import lombok.Data;

/**
 * 授权码管理器
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/22
 */
@Data
public abstract class AbstractCodeManager implements LifeCycleManager<CodeContent> {
    private int codeExpired;

    public AbstractCodeManager(int codeExpired) {
        this.codeExpired = codeExpired;
    }

    /**
     * 创建授权码
     *
     * @param tgt      tgt
     * @param appId 客户端ID
     * @return {@code String }
     */
    public String create(String tgt, String appId) {
        String code = "CODE-" + UUIDUtil.getUUID();
        create(code, new CodeContent(tgt, appId));
        return code;
    }
}
