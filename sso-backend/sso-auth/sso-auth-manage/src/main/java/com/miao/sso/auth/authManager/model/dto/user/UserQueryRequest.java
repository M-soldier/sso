package com.miao.sso.auth.authManager.model.dto.user;

import com.miao.sso.auth.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户查询请求
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/06
 */
@EqualsAndHashCode(callSuper = true)
@Data

public class UserQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = -7703716906493493572L;
    /**
     * id
     */
    private Long id;

    /**
     * 用户帐户
     */
    private String userAccount;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色
     */
    private String userRole;
}