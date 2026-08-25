package com.fzujxl.aicomputerplatform.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 自定义校验注解：允许值为null，但不允许为空字符串或纯空格字符串。
 * 完美适用于“部分更新”场景下的DTO字段校验。
 */
@Documented
//注解可以应用在什么地方，这里应用在字段上
@Target({ElementType.FIELD})
//注解的运行时保留策略，这里保留到运行时
@Retention(RetentionPolicy.RUNTIME)
//注解的校验器实现类，这里是PermitNullButNotEmptyValidator
@Constraint(validatedBy = PermitNullButNotEmptyValidator.class)
//注解的默认消息，这里是"字段不允许为空或纯空格"

public @interface PermitNullButNotEmpty {

    String message() default "字段不允许为空或纯空格";

    //注解的分组，这里默认为空数组
    Class<?>[] groups() default {};

    //注解的额外信息，这里默认为空数组
    Class<? extends Payload>[] payload() default {};
}