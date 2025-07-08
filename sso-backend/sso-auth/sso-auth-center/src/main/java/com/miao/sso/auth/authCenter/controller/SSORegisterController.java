package com.miao.sso.auth.authCenter.controller;

import com.miao.sso.auth.authCenter.manager.UserManager;
import com.miao.sso.base.common.result.BaseRespond;
import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.common.exception.ValidateFailedException;
import com.miao.sso.auth.authCenter.model.dto.sysUser.UserRegisterRequest;
import com.miao.sso.base.constants.BaseConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(BaseConstant.REGISTER_PATH)
public class SSORegisterController {
    UserManager userManager;

    @Autowired
    public SSORegisterController(UserManager userManager) {
        this.userManager = userManager;
    }

    @RequestMapping(method = RequestMethod.POST)
    public BaseRespond<Long> register(@RequestBody @Validated UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        if(userRegisterRequest == null) {
            throw new ValidateFailedException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        String username = userRegisterRequest.getUserName();
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getPassword();
        String confirmPassword = userRegisterRequest.getConfirmPassword();

        return userManager.registerUser(username, userAccount, password, confirmPassword);
    }
}
