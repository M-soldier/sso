package com.miao.sso.auth.entity.content;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class TokenContent extends CodeContent implements Serializable {
    private static final long serialVersionUID = -1985598459045608781L;
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String logoutUri;

    public TokenContent(String accessToken, String refreshToken, Long userId, String logoutUri, String appId, String tgt) {
        super(tgt, appId);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.logoutUri = logoutUri;
    }
}
