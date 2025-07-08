package com.miao.sso.auth.authCenter.model.dto.sysUser;

import com.miao.sso.auth.validator.ConfirmPassword;
import com.miao.sso.auth.validator.IsMD5Password;
import com.miao.sso.auth.validator.IsPhoneNum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * 接受用户注册参数的类
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfirmPassword(password = "password", confirmPassword = "confirmPassword")
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -8009545507320485315L;
    /**
     * 用户昵称
     */
    @Length(min = 3, max = 8, message = "昵称应为3-8个字符")
    private String userName;

    /**
     * 用户帐户
     */
    @NotEmpty(message = "手机号不能为空")
    @IsPhoneNum
    private String userAccount;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @IsMD5Password
    private String password;

    /**
     * 确认密码
     */
    @NotEmpty(message = "确认密码不能为空")
    private String confirmPassword;
}
