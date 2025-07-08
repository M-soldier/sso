package com.miao.sso.auth.authCenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miao.sso.auth.authCenter.manager.AppManager;
import com.miao.sso.auth.mapper.SysAppMapper;
import com.miao.sso.auth.model.entity.SysApp;
import com.miao.sso.auth.authCenter.service.SysAppService;
import com.miao.sso.base.common.exception.DatabaseException;
import com.miao.sso.base.common.result.BaseRespond;
import com.miao.sso.base.common.result.ResultUtil;
import com.miao.sso.base.common.result.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* @author CX330
* @description 针对表【sys_app(应用表)】的数据库操作Service实现
* @createDate 2025-05-06 21:25:08
*/
@Slf4j
@Service
public class SysAppServiceImpl extends ServiceImpl<SysAppMapper, SysApp> implements SysAppService, AppManager {

    @Override
    public BaseRespond<Long> validateApp(String appId) {
        SysApp app = this.getSysAppByAppId(appId);
        if (app == null) {
            log.error("非法应用，appId:{}", appId);
            throw new DatabaseException(ErrorCode.APP_NOT_EXIT_ERROR, "非法应用");
        }
        return ResultUtil.success(null, app.getId());
    }

    @Override
    public BaseRespond<Void> validateApp(String appId, String appSecret) {
        SysApp app = this.getSysAppByAppId(appId);
        if (app == null) {
            log.error("appId不存在，appId:{}", appId);
            throw new DatabaseException(ErrorCode.APP_NOT_EXIT_ERROR, "appId不存在");
        }
        if (!app.getAppSecret().equals(appSecret)) {
            log.error("appId不存在，appId:{}", appId);
            throw new DatabaseException(ErrorCode.APP_SECRET_ERROR, "appSecret错误");
        }
        return ResultUtil.success(null);
    }

    @Override
    public SysApp getSysAppByAppId(String appId) {
        QueryWrapper<SysApp> queryWrapper = new QueryWrapper<SysApp>();
        queryWrapper.eq("appId", appId);
        return this.baseMapper.selectOne(queryWrapper);
    }
}




