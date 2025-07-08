package com.miao.sso.auth.authManager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miao.sso.auth.authManager.model.dto.app.AppAddRequest;
import com.miao.sso.auth.authManager.service.SysAppService;
import com.miao.sso.auth.model.entity.SysApp;
import com.miao.sso.auth.utils.MD5Util;
import com.miao.sso.base.common.exception.ThrowUtil;
import com.miao.sso.base.common.exception.ValidateFailedException;
import com.miao.sso.base.common.result.BaseRespond;
import com.miao.sso.base.common.result.ResultUtil;
import com.miao.sso.base.common.result.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController()
@RequestMapping("/app")
public class AppController {
    SysAppService sysAppService;

    public AppController(SysAppService sysAppService) {
        this.sysAppService = sysAppService;
    }

    @PostMapping("/add")
    // @AuthCheck(mustRole = UserConstants.ADMIN_ROLE)
    public BaseRespond<Long> addApp(@RequestBody AppAddRequest appAddRequest) {
        ThrowUtil.throwIf(appAddRequest == null, ErrorCode.PARAMS_ERROR, "应用注册信息不能为空");
        log.info(appAddRequest.toString());

        // 校验应用编码是否重复
        QueryWrapper<SysApp> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", appAddRequest.getCode());
        long count = this.sysAppService.getBaseMapper().selectCount(queryWrapper);
        if (count > 0) {
            throw new ValidateFailedException(ErrorCode.PARAMS_ERROR, "应用编码重复");
        }

        // 拷贝注册信息
        SysApp app = new SysApp();
        BeanUtils.copyProperties(appAddRequest, app);

        // 生成appId
        app.setAppId(sysAppService.generateAppId());

        // 生成appSecret
        app.setAppSecret(MD5Util.md5(app.getAppId() + System.currentTimeMillis()));

        boolean result = sysAppService.save(app);

        ThrowUtil.throwIf(!result, ErrorCode.OPERATION_ERROR, "添加失败");

        return ResultUtil.success("/app/add", app.getId());
    }
}
