package com.miao.sso.client.filter;

import com.miao.sso.base.constants.BaseConstant;
import com.miao.sso.client.ClientProperties;
import com.miao.sso.client.common.ClientContextHolder;
import com.miao.sso.client.session.RpcTokenStorage;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 注销过滤器
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/15
 */

@Order(10)
@Slf4j
@Getter
@Setter
@AllArgsConstructor
public class LogoutFilter extends AbstractClientFilter{
    private ClientProperties clientProperties;
    private RpcTokenStorage rpcTokenStorage;

    @Override
    public boolean isAccessAllowed() throws IOException {
        HttpServletRequest request = ClientContextHolder.getRequest();
        String path = request.getServletPath();
        log.info("path: {}", path);
        if (!clientProperties.getLogoutPath().equals(path)) {
            return true;
        }
        String accessToken = request.getHeader(BaseConstant.LOGOUT_PARAMETER_NAME);
        log.info("accessToken from logoutFilter: {}", accessToken);
        if (accessToken == null) {
            return true;
        }
        rpcTokenStorage.remove(accessToken);
        return false;
    }
}
