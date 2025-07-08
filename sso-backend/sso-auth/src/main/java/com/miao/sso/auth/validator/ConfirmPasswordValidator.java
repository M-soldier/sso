package com.miao.sso.auth.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, Object> {
    String password;
    String confirmPassword;
    String message;
    @Override
    public void initialize(ConfirmPassword constraintAnnotation) {
        password = constraintAnnotation.password();
        confirmPassword = constraintAnnotation.confirmPassword();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            boolean valid = isValid(value);

            // 将错误信息绑定到confirmPassword字段上，而不是整个对象上（类级别）
            if (!valid) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(confirmPassword)
                        .addConstraintViolation();
            }

            return valid;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValid(Object value) throws NoSuchFieldException, IllegalAccessException {
        Field firstField = value.getClass().getDeclaredField(password);
        Field secondField = value.getClass().getDeclaredField(confirmPassword);

        firstField.setAccessible(true);
        secondField.setAccessible(true);

        Object firstValue = firstField.get(value);
        Object secondValue = secondField.get(value);

        return (firstValue == null && secondValue == null)
                || (firstValue != null && firstValue.equals(secondValue));
    }
}
