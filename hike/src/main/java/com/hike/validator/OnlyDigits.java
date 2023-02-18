package com.hike.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OnlyDigitsValidator.class)
@Documented
public @interface OnlyDigits {

    String message() default "Only digits required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
