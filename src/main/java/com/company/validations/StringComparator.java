package com.company.validations;

import com.company.AbstractComparator;
import com.company.Operatable;
import com.company.enums.LogicAssociativeOperator;
import com.company.enums.Operator;
import com.company.utils.MapUtils;
import com.company.utils.MultiKeySetMap;
import com.company.utils.StringUtils;
import com.google.common.collect.Sets;

import java.util.Map;

/**
 * Created by henry on 15/11/26.
 */
public class StringComparator extends AbstractComparator<String> {

    MultiKeySetMap<Operator, Operatable> opertableMap = MapUtils.newMultiKeySetMap(
            new MapUtils.Entry<>(Sets.newHashSet(Operator.EQUAL, Operator.IN, Operator.INTERSECT), (Operatable<Boolean, String>) StringUtils::equals),
            new MapUtils.Entry<>(Sets.newHashSet(Operator.NOT_EQUAL, Operator.NOT_IN), (Operatable<Boolean, String>) StringUtils::notEquals)
    );

    Map<Operator, LogicAssociativeOperator> logicOptrMap = MapUtils.newEnumMap(Operator.class);
    Map<Operator, LogicAssociativeOperator> _logicOptrMap = MapUtils.newEnumMap(Operator.class,
            new MapUtils.Entry<>(Operator.EQUAL, LogicAssociativeOperator.OR),
            new MapUtils.Entry<>(Operator.IN, LogicAssociativeOperator.OR),
            new MapUtils.Entry<>(Operator.NOT_IN, LogicAssociativeOperator.AND),
            new MapUtils.Entry<>(Operator.INTERSECT, LogicAssociativeOperator.OR)
    );


    @Override
    public MultiKeySetMap<Operator, Operatable> getOperatables() {
        return opertableMap;
    }

    @Override
    public Map<Operator, LogicAssociativeOperator> getLogic() {
        return logicOptrMap;
    }

    @Override
    public Map<Operator, LogicAssociativeOperator> get_Logic() {
        return _logicOptrMap;
    }


    @Override
    protected void init() {

    }
}
