package com.fzujxl.aicomputerplatform.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 自定义校验注解：只允许值为 IMAGE 或 TEXT (不区分大小写)。
 * 用于校验模型类型字段。
 */
@Documented
@Constraint(validatedBy = {ValidModelTypeValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidModelType {
    String message() default "模型类型必须为 IMAGE 或 TEXT";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}