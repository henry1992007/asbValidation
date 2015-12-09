package com.company.validations;

import com.company.AbstractComparator;
import com.company.CompareObject;
import com.company.MultiKeySetMap;
import com.company.Operatable;
import com.company.enums.LogicComputeOperator;
import com.company.enums.Operator;
import com.company.utils.MapUtils;
import com.company.utils.Utils;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by henry on 15/11/20.
 */
public class NumberComparator extends AbstractComparator<BigDecimal> {

    MultiKeySetMap<Operator, Operatable> opertableMap = MapUtils.newMultiKeySetMap(
            new MapUtils.Entry<>(Sets.newHashSet(Operator.LARGER_THAN), (Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) > 0),
            new MapUtils.Entry<>(Sets.newHashSet(Operator.LARGER_OR_EQUAL, Operator.BETWEEN), (Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) >= 0),
            new MapUtils.Entry<>(Sets.newHashSet(Operator.EQUAL, Operator.IN, Operator.INTERSECT), (Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) == 0),
            new MapUtils.Entry<>(Sets.newHashSet(Operator.LESS_OR_EQUAL), (Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) <= 0),
            new MapUtils.Entry<>(Sets.newHashSet(Operator.LESS_THAN), (Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) < 0),
            new MapUtils.Entry<>(Sets.newHashSet(Operator.NOT_EQUAL, Operator.NOT_IN), (Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) != 0)
    );
    Map<Operator, LogicComputeOperator> _logicOptrMap = MapUtils.newEnumMap(Operator.class,
            new MapUtils.Entry<>(Operator.IN, LogicComputeOperator.OR),
            new MapUtils.Entry<>(Operator.EQUAL, LogicComputeOperator.OR),
            new MapUtils.Entry<>(Operator.NOT_IN, LogicComputeOperator.AND),
            new MapUtils.Entry<>(Operator.INTERSECT, LogicComputeOperator.OR),
            new MapUtils.Entry<>(Operator.BETWEEN, LogicComputeOperator.XOR)
    );
    Map<Operator, LogicComputeOperator> logicOptrMap = MapUtils.newEnumMap(Operator.class);


    @Override
    protected void init() {

    }

    public CompareObject<BigDecimal> preProcess(CompareObject<BigDecimal> co) {
        if (co.getOperator().equals(Operator.BETWEEN)) {
            List<BigDecimal> res = Arrays.asList(co.get_vals());
            res.add(((BigDecimal) Utils.getMax(co.get_vals())).add(new BigDecimal(1)));
            co.set_vals(res.toArray(new BigDecimal[res.size()]));
        }
        return co;
    }


}
