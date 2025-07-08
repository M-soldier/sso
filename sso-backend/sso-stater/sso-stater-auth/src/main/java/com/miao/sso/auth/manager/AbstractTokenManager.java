package com.miao.sso.auth.manager;

import com.miao.sso.auth.entity.content.CodeContent;
import com.miao.sso.auth.entity.content.TokenContent;
import com.miao.sso.auth.utils.UUIDUtil;
import com.miao.sso.base.constants.BaseConstant;
import com.miao.sso.base.model.entity.LifeCycleManager;
import com.miao.sso.base.utils.HttpUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Getter
@Setter
public abstract class AbstractTokenManager implements LifeCycleManager<TokenContent> {
    private int accessTokenExpired;
    private int refreshTokenExpired;
    protected final ExecutorService executorService;

    public AbstractTokenManager(int accessTokenExpired, int refreshTokenExpired, int threadPoolSize) {
        this.accessTokenExpired = accessTokenExpired;
        this.refreshTokenExpired = refreshTokenExpired;
        this.executorService = new ThreadPoolExecutor(
                threadPoolSize, 2*threadPoolSize, 3L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1000));
    }

    /**
     * 通过accessToken获取
     *
     * @param accessToken 访问令牌
     * @return {@code TokenContent }
     */
    public abstract TokenContent getByAccessToken(String accessToken);

    /**
     * 通过tgt删除，此操作即登出
     *
     * @param tgt tgt
     */
    public abstract void removeByTGT(String tgt);

    /**
     * 通过tgt获取用户ID哈希表
     *
     * @param tgtSet tgt集合
     * @return {@code Map<String, Set<String>> }
     */
    public abstract Map<String, Set<String>> getAppIdMapByTGT(Set<String> tgtSet);

    /**
     * （真正执行tgt删除且登出的操作）
     *
     * @param refreshToken 刷新令牌
     */
    public abstract void processRemoveToken(String refreshToken);

    /**
     * 创建Token（包括accessToken和refreshToken）
     *
     * @param tokenContent 令牌内容
     * @return {@code TokenContent }
     */
    public TokenContent create(TokenContent tokenContent) {
        return create(tokenContent.getUserId(), tokenContent.getLogoutUri(), tokenContent);
    }

    /**
     * 创建Token（包括accessToken和refreshToken）
     *
     * @param userId      用户id
     * @param logoutUri   登录
     * @param codeContent 授权码包装类
     * @return {@code TokenContent }
     */
    public TokenContent create(Long userId, String logoutUri, CodeContent codeContent) {
        String accessToken = "AT-" + UUIDUtil.getUUID();
        String refreshToken = "RT-" + UUIDUtil.getUUID();

        TokenContent tokenContent = new TokenContent(accessToken, refreshToken, userId, logoutUri,
                codeContent.getAppId(), codeContent.getTgt());
        create(refreshToken, tokenContent);
        return tokenContent;
    }

    /**
     * 发送注销http请求
     *
     * @param redirectUri 重定向uri
     * @param accessToken 访问令牌
     */
    protected void sendLogoutRequest(String redirectUri, String accessToken) {
        log.info("logout URL: {}", redirectUri);
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(BaseConstant.LOGOUT_PARAMETER_NAME, accessToken);
        HttpUtil.postHeader(redirectUri, headerParams);
    }

    /**
     * 提交删除任务
     *
     * @param refreshTokenSet 刷新令牌集
     */
    protected void submitRemoveToken(Set<String> refreshTokenSet) {
        // 用于存储所有的Future对象，以便后续等待所有任务完成
        List<Future<?>> futures = new ArrayList<>();

        refreshTokenSet.forEach(refreshToken -> {
            // 发起客户端退出请求，提交任务到线程池并获取Future对象
            Future<?> future = executorService.submit(() -> {
                try {
                    processRemoveToken(refreshToken);
                }catch (Exception e) {
                    log.error("执行删除Token操作出现异常：{}", e.getMessage());
                }
            });
            futures.add(future);
        });

        // 等待所有的请求任务都完成
        for (Future<?> future : futures) {
            try {
                future.get();
            }catch (Exception e) {
                log.error("执行删除Token任务出现异常：{}", e.getMessage());
            }
        }
    }
}
