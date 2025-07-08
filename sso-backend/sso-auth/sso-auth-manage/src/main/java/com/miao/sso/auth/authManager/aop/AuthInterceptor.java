package com.miao.sso.auth.authManager.aop;

import com.miao.sso.auth.authManager.model.enums.UserRoleEnum;
import com.miao.sso.client.common.ClientContextHolder;
import com.miao.sso.base.common.exception.ThrowUtil;
import com.miao.sso.auth.authManager.annotation.AuthCheck;
import com.miao.sso.base.common.exception.ValidateFailedException;
import com.miao.sso.base.common.result.code.ErrorCode;
import com.miao.sso.base.model.rpc.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 身份验证拦截器
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/06
 */
@Slf4j
@Aspect
@Component
public class AuthInterceptor {
    /**
     * 进行身份验证检查
     *
     * @param joinPoint 连接点
     * @param authCheck 身份验证检查
     * @return {@code Object }
     * @throws Throwable 可抛出
     */
    @Around("@annotation(authCheck)")
    public Object doAuthCheck(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();

        // 获取当前登录用户
        TokenUser tokenUser = ClientContextHolder.getUser();
        ThrowUtil.throwIf(tokenUser == null, ErrorCode.NO_AUTH_ERROR, "未登录");

        // 校验是否需要权限，如果不需要，则放行
        UserRoleEnum mustRoleEnum = UserRoleEnum.getByValue(mustRole);
        if(mustRoleEnum == null){
            return joinPoint.proceed();
        }

        // 获取用户权限
        UserRoleEnum userRoleEnum = UserRoleEnum.getByValue(tokenUser.getUserRole());

        // 如果用户权限为空，则拒绝
        if(userRoleEnum == null){
            throw new ValidateFailedException(ErrorCode.NO_AUTH_ERROR, "用户权限错误");
        }

        // 需要管理员权限，但是用户没有管理员权限，则拒绝
        if(UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)){
            throw new ValidateFailedException(ErrorCode.NO_AUTH_ERROR, "没有管理员权限");
        }

        // 验证通过，放行
        return joinPoint.proceed();
    }
}
