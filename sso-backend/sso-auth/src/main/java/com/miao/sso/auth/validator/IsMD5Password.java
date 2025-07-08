package com.miao.sso.auth.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {IsMD5PasswordValidator.class}
)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsMD5Password {
    boolean required() default true;
    String message() default "前端没有对密码进行加密";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
