package com.fzujxl.aicomputerplatform.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * {@link ValidRole} 注解的校验器实现。
 */

//校验器实现类，用于校验字符串值是否符合ValidRole注解的规则
public class ValidRoleValidator implements ConstraintValidator<ValidRole, String> {

    @Override
    public boolean isValid( String role, ConstraintValidatorContext context) {

        if (role == null) {
            return true;
        }

        return "ADMIN".equalsIgnoreCase(role) || "USER".equalsIgnoreCase(role);
    }
}
