package com.company.validations;

import com.company.AbstractComparator;
import com.company.CompareObject;
import com.company.enums.LogicComputeOperator;
import com.company.utils.MultiKeySetMap;
import com.company.Operatable;
import com.company.enums.Operator;
import com.company.utils.MapUtils;
import com.company.utils.Utils;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by henry on 15/11/27.
 */
public class DateComparator extends AbstractComparator<Date> {

    MultiKeySetMap<Operator, Operatable> opertableMap = MapUtils.newMultiKeySetMap(
            new MapUtils.Entry<>(Sets.newHashSet(Operator.BEFORE), (Operatable<Boolean, Date>) (var1, var2) -> var1.compareTo(var2) > 0),
            new MapUtils.Entry<>(Sets.newHashSet(Operator.LARGER_OR_EQUAL, Operator.BETWEEN), (Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) >= 0),
            new MapUtils.Entry<>(Sets.newHashSet(Operator.EQUAL, Operator.IN, Operator.INTERSECT), (Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) == 0),
            new MapUtils.Entry<>(Sets.newHashSet(Operator.LESS_OR_EQUAL), (Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) <= 0),
            new MapUtils.Entry<>(Sets.newHashSet(Operator.LESS_THAN), (Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) < 0),
            new MapUtils.Entry<>(Sets.newHashSet(Operator.NOT_EQUAL, Operator.NOT_IN), (Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) != 0)
    );

    @Override
    protected void init() {

    }

    @Override
    public CompareObject<Date> preProcess(CompareObject<Date> co) {
        if (co.getOperator().equals(Operator.BETWEEN)) {
//            Date foo = new Date(((Date) Utils.getMax(co.get_vals())).getTime() + 1);
            Date foo = null;
            List<Date> res = co.get_vals();
            res.add(foo);
            co.set_vals(res);
        }
        return co;
    }

    @Override
    public MultiKeySetMap<Operator, Operatable> getOperatables() {
        return opertableMap;
    }

    @Override
    public Map<Operator, LogicComputeOperator> getLogic() {
        return new HashMap<>();
    }

    @Override
    public Map<Operator, LogicComputeOperator> get_Logic() {
        return new HashMap<>();
    }

}
