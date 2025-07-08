package com.miao.sso.auth.validator;

import cn.hutool.core.util.StrUtil;
import com.miao.sso.auth.utils.ValidatorUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMD5PasswordValidator implements ConstraintValidator<IsMD5Password,String> {
    private boolean required;

    @Override
    public void initialize(IsMD5Password constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return ValidatorUtil.passwordValidator(value);
        }else {
            if(StrUtil.isEmpty(value)){
                return true;
            }else {
                return ValidatorUtil.passwordValidator(value);
            }
        }
    }
}
