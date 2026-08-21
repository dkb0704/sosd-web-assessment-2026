package com.fzujxl.aicomputerplatform.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 自定义校验注解：允许值为ADMIN或USER。
 * 完美适用于“管理员更新”场景下的DTO字段校验。
 */

@Documented
//注解可以应用在什么地方，这里应用在字段上
@Target(ElementType.FIELD)
//注解的运行时保留策略，这里保留到运行时
@Retention(RetentionPolicy.RUNTIME)
//注解的校验器实现类，这里是ValidRoleValidator
@Constraint(validatedBy = ValidRoleValidator.class)
//注解的默认消息，这里是"角色必须为ADMIN或USER"

public @interface ValidRole {

    String message() default "角色必须为ADMIN或USER";

    //注解的分组，这里默认为空数组
    Class<?>[] groups() default {};

    //注解的额外信息，这里默认为空数组
    Class<? extends Payload>[] payload() default {};
}


