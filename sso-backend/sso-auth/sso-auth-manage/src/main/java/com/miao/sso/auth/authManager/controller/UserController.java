package com.miao.sso.auth.authManager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.miao.sso.auth.authManager.annotation.AuthCheck;
import com.miao.sso.auth.authManager.model.dto.user.UserAddRequest;
import com.miao.sso.auth.authManager.model.dto.user.UserQueryRequest;
import com.miao.sso.auth.authManager.model.dto.user.UserUpdateRequest;
import com.miao.sso.auth.constant.UserConstants;
import com.miao.sso.auth.model.entity.SysUser;
import com.miao.sso.auth.utils.MD5Util;
import com.miao.sso.base.common.exception.ThrowUtil;
import com.miao.sso.auth.authManager.service.SysUserService;
import com.miao.sso.base.common.exception.ValidateFailedException;
import com.miao.sso.base.common.result.BaseRespond;
import com.miao.sso.base.common.result.ResultUtil;
import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.model.rpc.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/06
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    SysUserService sysUserService;

    public UserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    /**
     * 创建用户
     *
     * @param userAddRequest 用户添加请求包装类
     * @return {@code BaseRespond }
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstants.ADMIN_ROLE)
    public BaseRespond<Long> addUser(@RequestBody @Validated UserAddRequest userAddRequest) {
        ThrowUtil.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR, "注册信息不能为空");
        log.info(userAddRequest.toString());

        // 校验手机号是否重复
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAddRequest.getUserAccount());
        long count = this.sysUserService.getBaseMapper().selectCount(wrapper);
        if(count > 0){
            throw new ValidateFailedException(ErrorCode.PARAMS_ERROR, "手机号重复");
        }

        // 拷贝注册信息
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userAddRequest, user);

        // 设置默认密码并写入到数据库中
        final String DEFAULT_PASSWORD = "12345678";
        String md5Password = MD5Util.inputPassToDBPass(DEFAULT_PASSWORD, UserConstants.SERVER_SALT);
        user.setUserPassword(md5Password);
        boolean result = sysUserService.save(user);

        ThrowUtil.throwIf(!result, ErrorCode.OPERATION_ERROR, "添加失败");


        return ResultUtil.success("/user/add", user.getId());
    }

    /**
     * 根据ID获取用户所有信息（仅管理员）
     *
     * @param userId 用户id
     * @return {@code BaseRespond }
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstants.ADMIN_ROLE)
    public BaseRespond<SysUser> getUser(@RequestParam("userId") Long userId) {
        ThrowUtil.throwIf(userId<0, ErrorCode.PARAMS_ERROR, "用户ID不合法");
        SysUser user = sysUserService.getById(userId);
        ThrowUtil.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        return ResultUtil.success("/user/get", user);
    }

    /**
     * 获取ID获取用户包装类
     *
     * @param userId 用户id
     * @return {@code BaseRespond }
     */
    @GetMapping("/get/vo")
    public BaseRespond<TokenUser> getTokenUser(@RequestParam("userId") Long userId) {
        ThrowUtil.throwIf(userId<0, ErrorCode.PARAMS_ERROR, "用户ID不合法");
        SysUser user = sysUserService.getById(userId);
        ThrowUtil.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        return ResultUtil.success("/user/get/vo", sysUserService.getLoginTokenUser(user));
    }

    /**
     * 根据ID删除用户
     *
     * @param userId 用户id
     * @return {@code BaseRespond }
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstants.ADMIN_ROLE)
    public BaseRespond<Void> deleteUser(@RequestParam Long userId) {
        ThrowUtil.throwIf(userId<0, ErrorCode.PARAMS_ERROR, "用户ID不合法");
        boolean result = sysUserService.removeById(userId);
        if(result){
            return ResultUtil.success("/user/delete");
        }
        return ResultUtil.error(ErrorCode.OPERATION_ERROR, "/user/delete", "删除失败");
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest 用户更新请求包装类
     * @return {@code BaseRespond }
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstants.ADMIN_ROLE)
    public BaseRespond<Void> updateUser(@RequestBody @Validated UserUpdateRequest userUpdateRequest) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = sysUserService.updateById(user);
        if(result){
            ResultUtil.success("/user/update");
        }
        return ResultUtil.error(ErrorCode.OPERATION_ERROR, "/user/update", "更新失败");
    }

    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstants.ADMIN_ROLE)
    public BaseRespond<Page<TokenUser>> listTokenUser(@Validated UserQueryRequest userQueryRequest) {
        ThrowUtil.throwIf(userQueryRequest==null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        long currentPage = userQueryRequest.getCurrentPage();
        long pageSize = userQueryRequest.getPageSize();
        Page<SysUser> userPage = sysUserService.page(new Page<>(currentPage, pageSize),
                sysUserService.getQueryWrapper(userQueryRequest));
        List<TokenUser> TokenUserList = sysUserService.getLoginTokenUserList(userPage.getRecords());

        Page<TokenUser> TokenUserPage = new Page<>(currentPage, pageSize, userPage.getTotal());
        TokenUserPage.setRecords(TokenUserList);

        return ResultUtil.success("/user/list/page/vo", TokenUserPage);
    }

}
