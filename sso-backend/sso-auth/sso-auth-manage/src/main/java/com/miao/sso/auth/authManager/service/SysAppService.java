package com.miao.sso.auth.authManager.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.miao.sso.auth.model.entity.SysApp;

/**
* @author CX330
* @description 针对表【sys_app(应用表)】的数据库操作Service
* @createDate 2025-05-06 21:25:08
*/
public interface SysAppService extends IService<SysApp> {
    String generateAppId();
}
