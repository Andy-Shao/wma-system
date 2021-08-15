package com.andyshao.application.wma.controller.validation.annotation;

import com.andyshao.application.wma.controller.validation.NoEmptyItemListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/8/15
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoEmptyItemListValidator.class)
public @interface NoEmptyItemList {
    String message() default "{javax.validation.constraints.NotNull.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
