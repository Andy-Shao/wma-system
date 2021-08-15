package com.andyshao.application.wma.controller.validation;

import com.andyshao.application.wma.controller.validation.annotation.NoEmptyItemList;
import com.github.andyshao.lang.StringOperation;
import com.github.andyshao.util.CollectionOperation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2021/8/15
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class NoEmptyItemListValidator implements ConstraintValidator<NoEmptyItemList, List> {
    @Override
    public boolean isValid(List list, ConstraintValidatorContext constraintValidatorContext) {
        if(CollectionOperation.isEmptyOrNull(list)) return true;
        AtomicBoolean rest = new AtomicBoolean(Boolean.TRUE);
        list.forEach(it -> {
            if(Objects.isNull(it)) rest.set(Boolean.FALSE);
            else if(it instanceof String)  {
                if(StringOperation.isTrimEmptyOrNull((String) it)) rest.set(Boolean.FALSE);
            }
        });
        return rest.get();
    }
}
