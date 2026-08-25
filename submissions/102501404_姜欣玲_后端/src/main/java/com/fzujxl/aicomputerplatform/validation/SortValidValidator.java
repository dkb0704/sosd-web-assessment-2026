package com.fzujxl.aicomputerplatform.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SortValidValidator implements ConstraintValidator<SortValid, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 允许为null
        if (value == null) {
            return true;
        }
        // 允许为"hot"（不区分大小写）
        return "hot".equalsIgnoreCase(value);
    }
}