package com.fzujxl.aicomputerplatform.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * {@link ValidCategory} 注解的校验器实现。
 */

//校验器实现类，用于校验字符串值是否符合ValidCategory注解的规则
public class ValidCategoryValidator implements ConstraintValidator<ValidCategory, String> {

    @Override
    public boolean isValid( String category, ConstraintValidatorContext context) {

        if (category == null) {
            return true;
        }

        return "PAINTING".equalsIgnoreCase(category) || "LITERATURE".equalsIgnoreCase(category)
                || "MUSIC" .equalsIgnoreCase(category)||"OTHER".equalsIgnoreCase(category);
    }
}