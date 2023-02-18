package com.hike.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OnlyLettersAndDigitsValidator.class)
@Documented
public @interface OnlyLettersAndDigits {

    String message() default "Only letters and digits required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}