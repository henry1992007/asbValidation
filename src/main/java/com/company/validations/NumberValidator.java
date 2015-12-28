package com.company.validations;

import com.company.Comparator;
import com.company.CompareObject;
import com.company.enums.CheckType;
import com.company.enums.Operator;
import com.company.utils.CollectionUtils;
import com.company.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 15/11/5.
 */
public class NumberValidator extends AbstractValidator<BigDecimal> {

    private static final String CONDITION_VALUE = "conditionValue";

    private AbstractComparator<BigDecimal> comparator;

    @Override
    protected Comparator<BigDecimal> getComparator() {
        return comparator == null ? comparator = new AbstractComparator<BigDecimal>(CheckType.NUMBER) {
            @Override
            public CompareObject<BigDecimal> preProcess(CompareObject<BigDecimal> co) {
                if (co.getOperator().equals(Operator.BETWEEN)) {
                    List<BigDecimal> res = co.get_vals();
                    res.add(CollectionUtils.reduce(co.get_vals(),BigDecimal::max).add(new BigDecimal(1)));
                }
                return co;
            }
        } : comparator;
    }

    public List<BigDecimal> parseObject(Collection<Object> list) {
        List<BigDecimal> res = new ArrayList<>();
        for (Object o : list) {
            if (o == null) {
                res.add(null);
            } else {
                try {
                    res.add(new BigDecimal(o.toString()));
                } catch (NumberFormatException e) {
                    //todo:exception
                    throw new RuntimeException("incompatible type:" + o, e);
                }
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
