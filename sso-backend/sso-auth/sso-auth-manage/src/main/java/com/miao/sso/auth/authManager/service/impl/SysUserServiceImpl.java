package com.miao.sso.auth.authManager.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miao.sso.auth.authManager.model.dto.user.UserQueryRequest;
import com.miao.sso.auth.mapper.SysUserMapper;
import com.miao.sso.auth.model.entity.SysUser;
import com.miao.sso.auth.authManager.service.SysUserService;
import com.miao.sso.base.common.exception.ValidateFailedException;
import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.model.rpc.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author CX330
* @description 针对表【sys_user(用户)】的数据库操作Service实现
* @createDate 2025-04-19 17:14:05
*/
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public TokenUser getLoginTokenUser(SysUser user) {
        if(user == null){
            return null;
        }
        TokenUser tokenUser = new TokenUser();
        BeanUtils.copyProperties(user, tokenUser);
        return tokenUser;
    }

    @Override
    public List<TokenUser> getLoginTokenUserList(List<SysUser> userList) {
        if(CollUtil.isEmpty(userList)){
            return new ArrayList<>();
        }
        return userList.stream().map(this::getLoginTokenUser).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<SysUser> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new ValidateFailedException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjUtil.isNotNull(id), "id", id);
        queryWrapper.eq(StrUtil.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }
}




