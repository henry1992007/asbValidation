package com.company.validations;

import com.company.*;
import com.company.Comparator;
import com.company.element.CheckDefinition;
import com.company.enums.Operator;
import com.company.utils.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by henry on 15/11/5.
 */
public class StringValidator extends AbstractValidator<String> {

    StringComparator comparator;

    @Override
    protected Comparator<String> getComparator() {
        if (comparator == null)
            comparator = new StringComparator();
        return comparator;
    }

    @Override
    protected List<String> parseObject(Collection<Object> list) {
        return list.stream().map(Object::toString).collect(Collectors.toList());
    }

    @Override
    protected List<String> parseString(List<String> list) {
        List<String> res = new ArrayList<>();
        for (String s : list)
            switch (s) {
                case "null":
                    setCheckNull(true);
                    break;
                case "\\null":
                    res.add("null");
                    break;
                default:
                    res.add(s);
                    break;
            }
        return res;
    }

}
