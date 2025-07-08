package com.miao.sso.auth.authCenter.controller;

import com.miao.sso.auth.authCenter.manager.AppManager;
import com.miao.sso.auth.authCenter.manager.UserManager;
import com.miao.sso.auth.entity.content.CodeContent;
import com.miao.sso.auth.entity.content.TicketGrantingTicketContent;
import com.miao.sso.auth.entity.content.TokenContent;
import com.miao.sso.auth.manager.AbstractCodeManager;
import com.miao.sso.auth.manager.AbstractTicketGrantingTicketManager;
import com.miao.sso.auth.manager.AbstractTokenManager;
import com.miao.sso.base.common.exception.ValidateFailedException;
import com.miao.sso.base.common.result.BaseRespond;
import com.miao.sso.base.common.result.ResultUtil;
import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.constants.BaseConstant;
import com.miao.sso.base.enums.GrantTypeEnum;
import com.miao.sso.base.model.rpc.RpcToken;
import com.miao.sso.base.model.rpc.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(BaseConstant.AUTH_PATH)
public class SSOOauth2Controller {
    private final AppManager appManager;
    private final UserManager userManager;
    private final AbstractCodeManager codeManager;
    private final AbstractTokenManager tokenManager;
    private final AbstractTicketGrantingTicketManager tgtManager;

    public SSOOauth2Controller(UserManager userManager, AppManager appManager,AbstractCodeManager codeManager,
                               AbstractTokenManager tokenManager, AbstractTicketGrantingTicketManager tgtManager) {
        this.appManager = appManager;
        this.userManager = userManager;
        this.codeManager = codeManager;
        this.tokenManager = tokenManager;
        this.tgtManager = tgtManager;
    }

    @GetMapping("/access-token")
    public BaseRespond<RpcToken> getAccessToken(
            @RequestParam(value = BaseConstant.GRANT_TYPE) String grantType,
            @RequestParam(value = BaseConstant.APP_ID) String appId,
            @RequestParam(value = BaseConstant.APP_SECRET) String appSecret,
            @RequestParam(value = BaseConstant.AUTH_CODE) String code,
            @RequestParam(value = BaseConstant.LOGOUT_URI) String logoutUri,
            @RequestParam(value = BaseConstant.REDIRECT_URI, required = false) String redirectUri){
        // 校验验证方式
        if(!GrantTypeEnum.AUTHORIZATION_CODE.getValue().equals(grantType)){
            throw new ValidateFailedException(ErrorCode.GRANT_TYPE_ERROR, "仅支持授权码方式");
        }

        // todo: 校验应用
        BaseRespond<Void> app = appManager.validateApp(appId, appSecret);
        if(!app.isSuccess()){
            throw new ValidateFailedException(ErrorCode.APP_SECRET_ERROR, app.getMessage());
        }

        // 校验授权码
        CodeContent codeContent = codeManager.get(code);
        log.info("codeContent:{}", codeContent);
        if(codeContent == null || !codeContent.getAppId().equals(appId)){
            throw new ValidateFailedException(ErrorCode.CODE_ERROR, "code有误或已过期");
        }
        codeManager.remove(code);

        // 校验登录凭证
        String tgt = codeContent.getTgt();
        TicketGrantingTicketContent tgtContent = tgtManager.get(tgt);
        if (tgtContent == null) {
            throw new ValidateFailedException(ErrorCode.TGT_ERROR, "tgt有误或已过期");
        }

        // 校验登录信息
        BaseRespond<TokenUser> user = userManager.getUser(tgtContent.getUserId());
        if(!user.isSuccess()){
            throw new ValidateFailedException(ErrorCode.USER_NOT_EXIT_ERROR, user.getMessage());
        }

        // 创建token并保存
        TokenContent tokenContent = tokenManager.create(tgtContent.getUserId(), logoutUri, codeContent);

        // 刷新登录凭证
        tgtManager.refresh(tgt);

        // 返回token
        return ResultUtil.success("/authServer/oauth2/access-token",
                new RpcToken(tokenContent.getAccessToken(), tokenManager.getAccessTokenExpired(),
                        tokenContent.getRefreshToken(), tokenManager.getRefreshTokenExpired(),
                        user.getData()));
    }

    @GetMapping("refresh-token")
    public BaseRespond<RpcToken> refreshToken(
            @RequestParam(value = BaseConstant.APP_ID) String appId,
            @RequestParam(value = BaseConstant.REFRESH_TOKEN) String refreshToken){
        // todo：校验应用
        BaseRespond<Long> app = appManager.validateApp(appId);
        if(!app.isSuccess()){
            throw new ValidateFailedException(ErrorCode.APP_NOT_EXIT_ERROR, app.getMessage());
        }

        TokenContent tokenContent = tokenManager.get(refreshToken);
        if(tokenContent == null){
            throw new ValidateFailedException(ErrorCode.TOKEN_ERROR, "refreshToken有误或已过期");
        }

        // 校验登录信息
        BaseRespond<TokenUser> user = userManager.getUser(tokenContent.getUserId());
        if(!user.isSuccess()){
            throw new ValidateFailedException(ErrorCode.USER_NOT_EXIT_ERROR, user.getMessage());
        }

        // 删除旧的token
        tokenManager.remove(refreshToken);

        // 生成新的token
        TokenContent newTokenContent = tokenManager.create(tokenContent);

        // 刷新登录凭据
        tgtManager.refresh(newTokenContent.getTgt());

        return ResultUtil.success("/authServer/oauth2/refresh-token",
                new RpcToken(newTokenContent.getAccessToken(), tokenManager.getAccessTokenExpired(),
                        newTokenContent.getRefreshToken(), tokenManager.getRefreshTokenExpired(),
                        user.getData()));
    }
}
