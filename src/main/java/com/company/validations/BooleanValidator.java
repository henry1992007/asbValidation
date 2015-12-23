package com.company.validations;

import com.company.Comparator;
import com.company.element.CheckDefinition;
import com.company.enums.CheckType;

import java.util.*;

/**
 * Created by henry on 15/11/5.
 */
public class BooleanValidator extends AbstractValidator<Boolean> {

    AbstractComparator<Boolean> comparator;

    @Override
    protected Comparator<Boolean> getComparator() {
        return comparator == null ? AbstractComparator.<Boolean>defaultComparator(CheckType.BOOLEAN) : comparator;
    }

    @Override
    protected List<Boolean> parseObject(Collection<Object> list) {
        return null;
    }

    @Override
    protected List<Boolean> parseString(List<String> list) {
        return null;
    }

}
