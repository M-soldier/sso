package com.miao.sso.auth.manager;

import cn.hutool.core.util.StrUtil;
import com.miao.sso.auth.entity.content.TicketGrantingTicketContent;
import com.miao.sso.auth.utils.UUIDUtil;
import com.miao.sso.base.model.entity.LifeCycleManager;
import com.miao.sso.base.utils.CookieUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

@Slf4j
@Getter
@Setter
public abstract class AbstractTicketGrantingTicketManager implements LifeCycleManager<TicketGrantingTicketContent> {
    private AbstractTokenManager tokenManager;
    private int tgtExpired;
    private String cookieName;

    public AbstractTicketGrantingTicketManager(AbstractTokenManager tokenManager, int tgtExpired, String cookieName) {
        this.tokenManager = tokenManager;
        this.tgtExpired = tgtExpired;
        this.cookieName = cookieName;
    }

    /**
     * 刷新TGT有效期
     */
    public abstract void refresh(String tgt);

    /**
     * 获取TGT（分页）
     *
     * @param userIds     用户id
     * @param currentPage 当前页面
     * @param pageSize    页面大小
     * @return {@code Map<String, TicketGrantingTicketContent> }
     */
    public abstract Map<String, TicketGrantingTicketContent> getTGTMap(Set<Long> userIds, long currentPage, long pageSize);

    /**
     * 创建tgt
     *
     * @param tgt    tgt
     * @param userId 用户id
     */
    public void create(String tgt, Long userId) {
        create(tgt, new TicketGrantingTicketContent(userId, System.currentTimeMillis()));
    }

    /**
     * 从cookie中获取tgt
     *
     * @param request http请求
     * @return {@code String }
     */
    public String getTGTFromCookie(HttpServletRequest request) {
        return CookieUtil.getCookieValue(this.cookieName, request);
    }

    /**
     * 获取tgt
     *
     * @param request http请求
     * @return {@code String }
     */
    public String getTGT(HttpServletRequest request) {
        // 从cookie中获取tgt
        String tgt = getTGTFromCookie(request);
        // 校验tgt是否过期或不存在
        if (StrUtil.isEmpty(tgt) || get(tgt)==null) {
            return null;
        }
        return tgt;
    }

    /**
     * 获取或创建tgt
     *
     * @param userId   用户id
     * @param request  http请求
     * @param response http响应
     * @return {@code String }
     */
    public String getOrCreateTGT(Long userId, HttpServletRequest request, HttpServletResponse response) {
        String tgt = getTGT(request);
        if (StrUtil.isEmpty(tgt)){
            tgt = "TGT-" + UUIDUtil.getUUID();
            create(tgt, userId);
            // 将TGT存储在Cookie中
            CookieUtil.setCookieValue(cookieName, tgt, "/", request, response);
        }
        return tgt;
    }

    /**
     * 使tgt无效
     *
     * @param tgt tgt
     */
    public void invalidateTGT(String tgt) {
        // 删除登录凭证
        remove(tgt);
        // 删除tgt对应的所有token，通知所有客户端退出，注销其他本地token
        tokenManager.removeByTGT(tgt);
    }

    /**
     * 使tgt无效
     *
     * @param request  http请求
     * @param response http响应
     */
    public void invalidateTGT(HttpServletRequest request, HttpServletResponse response) {
        String tgt = getTGTFromCookie(request);
        log.info("invalidateTGT: {}", tgt);
        if (StrUtil.isNotEmpty(tgt)) {
            invalidateTGT(tgt);
            // 删除Cookie
            CookieUtil.removeCookieValue(cookieName, "/", response);
        }
    }
}
