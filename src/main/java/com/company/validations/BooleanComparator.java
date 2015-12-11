package com.company.validations;

import com.company.AbstractComparator;
import com.company.CompareObject;
import com.company.utils.MultiKeySetMap;
import com.company.Operatable;
import com.company.enums.LogicComputeOperator;
import com.company.enums.Operator;
import com.company.utils.MapUtils;
import com.google.common.collect.Sets;

import java.util.Map;

/**
 * Created by henry on 15/11/26.
 */
public class BooleanComparator extends AbstractComparator<Boolean> {

    MultiKeySetMap<Operator, Operatable> opertableMap = MapUtils.newMultiKeySetMap(
            new MapUtils.Entry<>(Sets.newHashSet(Operator.EQUAL), (Operatable<Boolean, Boolean>) Boolean::equals),
            new MapUtils.Entry<>(Sets.newHashSet(Operator.NOT_EQUAL), (Operatable<Boolean, Boolean>) (var1, var2) -> var1 != var2)
    );
    Map<Operator, LogicComputeOperator> _logicOptrMap = MapUtils.newEnumMap(Operator.class,
            new MapUtils.Entry<>(Operator.EQUAL, LogicComputeOperator.OR));
    Map<Operator, LogicComputeOperator> logicOptrMap = MapUtils.newEnumMap(Operator.class);

    @Override
    protected void init() {

    }

    @Override
    public CompareObject<Boolean> preProcess(CompareObject<Boolean> co) {
        return co;
    }


}
