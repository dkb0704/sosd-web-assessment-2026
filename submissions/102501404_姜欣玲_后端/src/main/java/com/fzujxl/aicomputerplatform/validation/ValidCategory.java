package com.fzujxl.aicomputerplatform.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 自定义校验注解：允许值为PAINTING、LITERATURE、MUSIC或OTHER。
 * 用于校验艺术作品的分类字段。
 */

@Documented
//注解可以应用在什么地方，这里应用在字段上
@Target(ElementType.FIELD)
//注解的运行时保留策略，这里保留到运行时
@Retention(RetentionPolicy.RUNTIME)
//注解的校验器实现类，这里是ValidCategoryValidator
@Constraint(validatedBy = ValidCategoryValidator.class)
//注解的默认消息，这里是"分类必须为PAINTING、LITERATURE、MUSIC或OTHER"

public @interface ValidCategory {
    String message() default "分类必须为PAINTING、LITERATURE、MUSIC或OTHER";

    //注解的默认值，这里是默认是空数组
    Class<?>[] groups() default {};

    //注解的默认值，这里是默认是空数组
    Class<? extends Payload>[] payload() default {};
}
