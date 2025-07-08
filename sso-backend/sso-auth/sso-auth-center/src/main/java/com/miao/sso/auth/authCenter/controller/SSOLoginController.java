package com.miao.sso.auth.authCenter.controller;

import cn.hutool.core.util.StrUtil;
import com.miao.sso.auth.AuthProperties;
import com.miao.sso.auth.manager.AbstractCodeManager;
import com.miao.sso.auth.manager.AbstractTicketGrantingTicketManager;
import com.miao.sso.auth.authCenter.manager.AppManager;
import com.miao.sso.auth.authCenter.manager.UserManager;
import com.miao.sso.auth.authCenter.model.dto.sysUser.UserLoginRequest;
import com.miao.sso.base.common.exception.ValidateFailedException;
import com.miao.sso.base.common.result.BaseRespond;
import com.miao.sso.base.common.result.ResultUtil;
import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.constants.BaseConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(BaseConstant.LOGIN_PATH)
@EnableConfigurationProperties({AuthProperties.class})
public class SSOLoginController {
    private final AuthProperties authProperties;
    private AppManager appManager;
    private final UserManager userManager;
    private final AbstractCodeManager codeManager;
    private final AbstractTicketGrantingTicketManager tgtManager;

    public SSOLoginController(AuthProperties authProperties, UserManager userManager, AppManager appManager,
                              AbstractCodeManager codeManager, AbstractTicketGrantingTicketManager tgtManager) {
        this.authProperties = authProperties;
        this.userManager = userManager;
        this.appManager = appManager;
        this.codeManager = codeManager;
        this.tgtManager = tgtManager;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void login(
            @RequestParam(BaseConstant.REDIRECT_URI) String redirectUri,
            @RequestParam(BaseConstant.APP_ID) String appId,
            @RequestParam(value = BaseConstant.APP_SERVER_URI, required = false) String appServerUri,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tgt = tgtManager.getTGT(request);
        if (StrUtil.isEmpty(tgt)) {
            goToLoginPage(authProperties.getLoginUrl(), redirectUri, appId, appServerUri, response);
            return;
        }

        generateCodeAndRedirect(tgt, appId, redirectUri, appServerUri, response);
    }

    @RequestMapping(method = RequestMethod.POST)
    private BaseRespond<String> Login(
            @RequestParam(value = BaseConstant.REDIRECT_URI) String redirectUri,
            @RequestParam(value = BaseConstant.APP_ID) String appId,
            @RequestParam(value = BaseConstant.APP_SERVER_URI, required = false) String appServerUri,
            @RequestBody @Validated UserLoginRequest userLoginRequest,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 应用校验
        BaseRespond<Long> app = appManager.validateApp(appId);
        if(!app.isSuccess()){
            request.setAttribute("errorMessage", app.getMessage());
            goToLoginPage(authProperties.getLoginUrl(), redirectUri, appId, appServerUri, response);
            throw new ValidateFailedException(ErrorCode.APP_NOT_EXIT_ERROR, app.getMessage());
        }

        // 用户校验
        BaseRespond<Long> user = userManager.validateUser(userLoginRequest.getUserAccount(), userLoginRequest.getPassword());
        if (!user.isSuccess()){
            request.setAttribute("errorMessage", user.getMessage());
            goToLoginPage(authProperties.getLoginUrl(), redirectUri, appId, appServerUri, response);
            throw new ValidateFailedException(ErrorCode.USER_NOT_EXIT_ERROR, user.getMessage());
        }

        String tgt = tgtManager.getOrCreateTGT(user.getData(), request, response);
        String code = codeManager.create(tgt, appId);

        return ResultUtil.success(BaseConstant.LOGIN_PATH, "", authRedirectUri(redirectUri, appServerUri, code));
    }


    private void goToLoginPage(String loginUrl, String redirectUri, String appId, String appServerUri, HttpServletResponse response) throws IOException {
        if (StrUtil.isEmpty(appServerUri)) {
            response.sendRedirect(loginUrl + BaseConstant.H5_LOGIN_PATH + "?appId=" + appId + "&redirectUri=" + redirectUri);
        } else {
            response.sendRedirect(loginUrl + BaseConstant.H5_LOGIN_PATH + "?appId=" + appId + "&appServerUri=" + appServerUri + "&redirectUri=" + redirectUri);
        }
    }

    private void generateCodeAndRedirect(String tgt, String appId, String redirectUri, String appServerUri, HttpServletResponse response) throws IOException {
        String code = codeManager.create(tgt, appId);
        response.sendRedirect(authRedirectUri(redirectUri, appServerUri, code));
    }

    private String authRedirectUri(String redirectUri, String appServerUri, String code) {
        if (StrUtil.isEmpty(appServerUri)) {
            StringBuilder sb = new StringBuilder(redirectUri);
            if (redirectUri.contains("?")) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            sb.append(BaseConstant.AUTH_CODE).append("=").append(code);
            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder(appServerUri);
            if (appServerUri.endsWith("/")){
                sb.insert(0, "/");
            }
            if (appServerUri.contains("?")) {
                sb.append("/get/loginStatus");
                sb.append("&");
            } else {
                sb.append("/get/loginStatus");
                sb.append("?");
            }
            sb.append(BaseConstant.REDIRECT_URI).append("=").append(redirectUri);
            sb.append("&").append(BaseConstant.AUTH_CODE).append("=").append(code);
            return sb.toString();
        }
    }
}
