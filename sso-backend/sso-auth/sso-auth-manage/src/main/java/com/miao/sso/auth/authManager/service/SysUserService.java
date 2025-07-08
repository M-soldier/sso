package com.miao.sso.auth.authManager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.miao.sso.auth.authManager.model.dto.user.UserQueryRequest;
import com.miao.sso.auth.model.entity.SysUser;
import com.miao.sso.base.model.rpc.TokenUser;


import java.util.List;

/**
* @author CX330
* @description 针对表【sys_user(用户)】的数据库操作Service
* @createDate 2025-04-19 17:14:05
*/
public interface SysUserService extends IService<SysUser> {
    TokenUser getLoginTokenUser(SysUser user);

    List<TokenUser> getLoginTokenUserList(List<SysUser> userList);

    QueryWrapper<SysUser> getQueryWrapper(UserQueryRequest userQueryRequest);
}
