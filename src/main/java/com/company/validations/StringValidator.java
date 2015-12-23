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
        return comparator == null ? comparator = AbstractComparator.<String>defaultComparator(CheckType.STRING) : comparator;
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
