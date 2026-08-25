package com.fzujxl.aicomputerplatform.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidModelTypeValidator implements ConstraintValidator<ValidModelType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 校验器不处理null值，如果希望字段非空，请配合 @NotBlank 或 @NotNull
        if (value == null) {
            return true;
        }
        return "IMAGE".equalsIgnoreCase(value) || "TEXT".equalsIgnoreCase(value);
    }
}