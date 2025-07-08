package com.miao.sso.auth.authManager.model.dto.user;


import com.miao.sso.auth.constant.UserConstants;
import com.miao.sso.auth.validator.IsPhoneNum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 用户创建请求
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddRequest implements Serializable {
    private static final long serialVersionUID = 6747940961119284714L;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @Length(min = 3, max = 8, message = "昵称应为3-8个字符")
    private String userName;

    /**
     * 用户帐户
     */
    @IsPhoneNum
    @NotEmpty(message = "手机号码不能为空")
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user、admin、vip
     */
    private String userRole = UserConstants.USER_ROLE;
}
