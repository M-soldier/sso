package com.miao.sso.client.session;

import com.miao.sso.base.model.entity.LifeCycleManager;
import com.miao.sso.base.model.rpc.RpcToken;
import com.miao.sso.base.model.rpc.TokenWrapper;

/**
 * Session本地存储哈希表接口
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/02
 */
public interface RpcTokenStorage extends LifeCycleManager<TokenWrapper> {
    default void create(RpcToken token) {
        create(token.getAccessToken(), new TokenWrapper(token, token.getAccessExpired(), token.getRefreshExpired()));
    }

    String getAccessTokenByRefreshToken(String refreshToken);
}
