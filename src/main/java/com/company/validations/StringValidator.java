package com.company.validations;

import com.company.*;
import com.company.Comparator;
import com.company.element.CheckDefinition;
import com.company.enums.Operator;
import com.company.utils.CollectionUtils;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by henry on 15/11/5.
 */
public class StringValidator extends AbstractValidator<String> {


    @Override
    protected Comparator<String> getComparator() {
        return null;
    }

    @Override
    protected List<String> parseObject(Collection<Object> list) {
        return list.stream().map(Object::toString).collect(Collectors.toList());
    }

    @Override
    protected List<String> parseString(List<String> list) {
        return CollectionUtils.anyMatchAndThen(list, s -> s.equals("/null"), (Function<AbstractValidator, Boolean>) o -> o.setCheckNull(true), this) ?
                list.stream().filter(s -> s.equals("/null")).collect(Collectors.toList()) : list;
    }

}
