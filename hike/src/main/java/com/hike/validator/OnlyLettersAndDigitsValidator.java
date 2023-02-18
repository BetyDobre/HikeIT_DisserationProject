package com.hike.validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OnlyLettersAndDigitsValidator implements ConstraintValidator<OnlyLettersAndDigits, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value.matches("[a-zA-Z0-9]+");
    }
}
