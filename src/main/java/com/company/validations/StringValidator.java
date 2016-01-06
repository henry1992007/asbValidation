package com.company.validations;

import com.company.Comparator;
import com.company.enums.CheckType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by henry on 15/11/5.
 */
public class StringValidator extends AbstractValidator<String> {

    AbstractComparator<String> comparator;

    @Override
    protected Comparator<String> getComparator() {
        return comparator == null ?
                comparator = AbstractComparator.<String>defaultComparator(CheckType.STRING) :
                comparator;
    }

    @Override
    protected String parseObject(Object o) {
        return o == null ? null : o.toString();
    }

    @Override
    protected String parseString(String s) {
        switch (s) {
            case "null":
                setCheckNull(true);
                return null;
            case "\\null":
                return "null";
            default:
                return s;
        }
    }

}
