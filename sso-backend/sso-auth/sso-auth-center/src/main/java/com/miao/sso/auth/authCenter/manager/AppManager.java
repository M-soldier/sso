package com.miao.sso.auth.authCenter.manager;

import com.miao.sso.base.common.result.BaseRespond;

public interface AppManager {
    /**
     * 验证应用程序
     *
     * @param appId 应用ID
     * @return {@code BaseRespond<Long> }
     */
    BaseRespond<Long> validateApp(String appId);

    /**
     * 验证应用程序
     *
     * @param appId     应用ID
     * @param appSecret 应用程序密钥
     * @return {@code BaseRespond<Long> }
     */
    BaseRespond<Void> validateApp(String appId, String appSecret);
}
