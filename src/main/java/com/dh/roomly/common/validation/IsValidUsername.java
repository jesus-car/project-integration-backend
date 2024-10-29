package com.dh.roomly.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IsValidUsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface IsValidUsername {
    String message() default "Este campo ya existe";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
