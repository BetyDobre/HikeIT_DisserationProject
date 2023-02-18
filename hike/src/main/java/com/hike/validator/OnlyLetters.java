package com.hike.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OnlyLettersValidator.class)
@Documented
public @interface OnlyLetters {

    String message() default "Only letters required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}