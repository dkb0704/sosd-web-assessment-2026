package com.fzujxl.aicomputerplatform.validation;

import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * {@link PermitNullButNotEmpty} 注解的校验器实现。
 */
//校验器实现类，用于校验字符串值是否符合PermitNullButNotEmpty注解的规则
public class PermitNullButNotEmptyValidator implements ConstraintValidator<PermitNullButNotEmpty, String> {

    @Override
    //ConstraintValidatorContext context提供了一些与当前校验操作相关的上下文信息和工具
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果值为null，校验通过，因为此注解允许null值。
        if (value == null) {
            return true;
        }
        // 如果值非null，则使用StringUtils.hasText来判断它是否包含实际内容（非空且非纯空格）。
        return StringUtils.hasText(value);
    }
}