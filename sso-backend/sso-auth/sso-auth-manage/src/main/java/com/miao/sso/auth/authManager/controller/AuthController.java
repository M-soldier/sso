package com.miao.sso.auth.authManager.controller;

import cn.hutool.core.util.StrUtil;
import com.miao.sso.client.common.ClientContextHolder;
import com.miao.sso.base.common.result.BaseRespond;
import com.miao.sso.base.common.result.ResultUtil;
import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.constants.BaseConstant;
import com.miao.sso.base.model.rpc.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/get")
public class AuthController {
    @GetMapping("/loginStatus")
    public BaseRespond<TokenUser> getAuthStatus(
            @RequestParam(value = BaseConstant.REDIRECT_URI, required = false) String redirectUri) throws IOException {
        if(StrUtil.isNotEmpty(redirectUri)) {
            ClientContextHolder.getResponse().sendRedirect(redirectUri);
        }
        log.info("user: {}", ClientContextHolder.getUser());
        TokenUser tokenUser = ClientContextHolder.getUser();
        if(tokenUser != null){
            return ResultUtil.success("/get/loginStatus", tokenUser);
        }
        return ResultUtil.error(ErrorCode.NO_AUTH_ERROR, "/get/loginStatus");
    }
}
