package com.miao.sso.auth.authCenter.manager;

import com.miao.sso.base.common.result.BaseRespond;
import com.miao.sso.base.model.rpc.TokenUser;

public interface UserManager {
    /**
     * 验证用户
     *
     * @param username 用户名
     * @param password 密码
     * @return {@code BaseRespond<Long> }
     */
    BaseRespond<Long> validateUser(String username, String password);

    /**
     * 注册用户
     *
     * @param username        用户名
     * @param userAccount     用户帐户
     * @param password        密码
     * @param confirmPassword 确认密码
     * @return {@code BaseRespond<Long> }
     */
    BaseRespond<Long> registerUser(String username, String userAccount, String password, String confirmPassword);

    /**
     * 获取需要的用户信息
     *
     * @param userId 用户id
     * @return {@code BaseRespond<TokenUser> }
     */
    BaseRespond<TokenUser> getUser(Long userId);
}
