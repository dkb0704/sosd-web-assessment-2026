package com.fzujxl.aicomputerplatform.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * {@link ValidWorkType} 注解的校验器实现。
 */
//校验器实现类，用于校验字符串值是否符合ValidWorkType注解的规则
public class ValidWorkTypeValidator implements ConstraintValidator<ValidWorkType, String> {

        @Override
        public boolean isValid( String workType, ConstraintValidatorContext context) {

            if (workType == null) {
                return true;
            }

            return "TEXT".equalsIgnoreCase(workType) || "IMAGE".equalsIgnoreCase(workType);
        }

}