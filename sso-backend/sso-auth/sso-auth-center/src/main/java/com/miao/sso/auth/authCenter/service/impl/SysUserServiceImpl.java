package com.miao.sso.auth.authCenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miao.sso.auth.constant.UserConstants;
import com.miao.sso.auth.authCenter.manager.UserManager;
import com.miao.sso.base.common.result.BaseRespond;
import com.miao.sso.base.common.result.ResultUtil;
import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.model.rpc.TokenUser;
import com.miao.sso.base.common.exception.DatabaseException;
import com.miao.sso.base.common.exception.ValidateFailedException;
import com.miao.sso.auth.mapper.SysUserMapper;
import com.miao.sso.auth.model.entity.SysUser;
import com.miao.sso.auth.authCenter.service.SysUserService;
import com.miao.sso.auth.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author CX330
* @description 针对表【sys_user(用户)】的数据库操作Service实现
* @createDate 2025-04-19 17:14:05
*/
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService, UserManager {
    private final String SALT = UserConstants.SERVER_SALT;
    @Override
    public BaseRespond<Long> registerUser(String username, String userAccount, String password, String confirmPassword) {
        // 1. 校验，通过注解完成，无需在此处写校验逻辑

        // 2. 检查是否重复
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        long count = this.baseMapper.selectCount(wrapper);
        if(count > 0){
            throw new ValidateFailedException(ErrorCode.PARAMS_ERROR, "手机号重复");
        }

        // 3. 加密
        String md5Password = MD5Util.formPassToDBPass(password, SALT);

        // 4. 插入数据
        SysUser user = new SysUser();
        user.setUserName(username);
        user.setUserAccount(userAccount);
        user.setUserPassword(md5Password);

        if(!this.save(user)){
            throw new DatabaseException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }

        return ResultUtil.success(null, user.getId());
    }

    // @Transactional
    @Override
    public BaseRespond<Long> validateUser(String userAccount, String password) {
        // 1. 校验，通过注解进行校验

        // 2. 密码加密
        String md5Password = MD5Util.formPassToDBPass(password, SALT);

        // 3. 查询
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", md5Password);
        SysUser user = this.baseMapper.selectOne(queryWrapper);
        if(user == null){
            log.error("账号或密码错误，userAccount:{}", userAccount);
            throw new DatabaseException(ErrorCode.ACCOUNT_ERROR, "用户不存在或密码错误");
        }
        UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .set("lastLoginTime", new Date())
                .set("loginCount", user.getLoginCount() + 1)
                .eq("id", user.getId());
        this.baseMapper.update(user, updateWrapper);
        return ResultUtil.success(null, user.getId());
    }

    @Override
    public BaseRespond<TokenUser> getUser(Long userId) {
        SysUser user = this.getById(userId);
        if(user != null){
            return ResultUtil.success(null, getLoginTokenUser(user));
        }
        return ResultUtil.error(ErrorCode.USER_NOT_EXIT_ERROR, null);
    }

    @Override
    public TokenUser getLoginTokenUser(SysUser user) {
        if(user == null){
            return null;
        }
        TokenUser tokenUser = new TokenUser();
        BeanUtils.copyProperties(user, tokenUser);
        return tokenUser;
    }
}




