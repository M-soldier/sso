package com.miao.sso.auth.authCenter.model.dto.sysUser;

import com.miao.sso.auth.validator.IsMD5Password;
import com.miao.sso.auth.validator.IsPhoneNum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户注册请求
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 5115026576755508721L;

    @IsPhoneNum
    private String userAccount;

    @IsMD5Password
    private String password;
}
