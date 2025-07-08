package com.miao.sso.auth.authManager.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 用户更新请求
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = -5892529954203203433L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    @Length(min = 3, max = 8, message = "昵称应为3-8个字符")
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色
     */
    private String userRole;
}
