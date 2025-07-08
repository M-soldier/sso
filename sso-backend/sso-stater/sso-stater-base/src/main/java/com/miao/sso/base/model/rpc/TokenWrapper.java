package com.miao.sso.base.model.rpc;

import com.miao.sso.base.model.entity.ExpirationWrapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * session令牌
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/02
 */
@Getter
@Setter
@NoArgsConstructor
public class TokenWrapper extends ExpirationWrapper<RpcToken> implements Serializable {
    private static final long serialVersionUID = -1985598459045608781L;

    /**
     * refreshToken过期时间
     */
    private Long refreshExpiredAt;

    public TokenWrapper(RpcToken token, int accessExpired, int refreshExpired) {
        super(token, accessExpired);
        this.refreshExpiredAt = System.currentTimeMillis() + refreshExpired * 1000L;
    }

    /**
     * 判断token是否已过期
     *
     * @return boolean
     */
    public boolean refreshIsExpired() {
        return System.currentTimeMillis() < refreshExpiredAt;
    }
}
