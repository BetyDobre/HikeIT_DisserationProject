package com.hike.validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OnlyDigitsAndSpacesValidator implements ConstraintValidator<OnlyDigitsAndSpaces, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value.matches("[0-9 ]+");
    }
}
