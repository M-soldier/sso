package com.miao.sso.auth.validator;

import cn.hutool.core.util.StrUtil;
import com.miao.sso.auth.utils.ValidatorUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号码验证器
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/03/31
 */
@AllArgsConstructor
@NoArgsConstructor
public class IsPhoneNumValidator implements ConstraintValidator<IsPhoneNum, String> {
    private boolean required;
    @Override
    public void initialize(IsPhoneNum constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return ValidatorUtil.isPhoneNum(value);
        }else {
            if (StrUtil.isEmpty(value)) {
                return true;
            }else {
                return ValidatorUtil.isPhoneNum(value);
            }
        }
    }
}
