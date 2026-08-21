package com.fzujxl.aicomputerplatform.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {SortValidValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SortValid {
    String message() default "无效的排序参数，只允许'hot'或为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}