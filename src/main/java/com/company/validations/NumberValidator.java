package com.company.validations;

import com.company.*;
import com.company.Comparator;
import com.company.element.CheckDefinition;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.ReflectUtils;
import com.company.utils.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by henry on 15/11/5.
 */
public class NumberValidator extends AbstractValidator<BigDecimal> {

    private static final String CONDITION_VALUE = "conditionValue";

    private NumberComparator comparator;

    @Override
    protected Comparator<BigDecimal> getComparator() {
        if (comparator == null)
            comparator = new NumberComparator();
        return comparator;
    }


    public List<BigDecimal> parseObject(Collection<Object> list) {
        List<BigDecimal> res = new ArrayList<>();
        for (Object o : list) {
            if (o == null) {
                res.add(null);
            } else if (!o.getClass().equals(BigDecimal.class)) {
                o = new BigDecimal(((Number) o).doubleValue());
                res.add((BigDecimal) o);
            }
        }
        return res;
    }

    public List<BigDecimal> parseString(List<String> list) {
        List<BigDecimal> res = new ArrayList<>();
        for (String s : list)
            if (s.equals("null"))
                setCheckNull(true);
            else
                res.add(new BigDecimal(s));
        return res;
    }

    private String getMapDesc(Map<String, Comparable> map) {
        StringBuilder sb = new StringBuilder("[");

        boolean flag = true;
        for (String s : map.keySet()) {
            if (!flag)
                sb.append(",");
            flag = false;
            sb.append(s.contains(CONDITION_VALUE) ? CONDITION_VALUE : "\"" + s + "\"").append(":").append(map.get(s));
        }
        sb.append("]");

        return sb.toString();
    }

}
