package com.company.validations;

import com.company.AbstractComparator;
import com.company.Operatable;
import com.company.enums.LogicComputeOperator;
import com.company.enums.Operator;
import com.company.utils.MapUtils;
import com.company.utils.StringUtils;
import com.google.common.collect.Sets;

/**
 * Created by henry on 15/11/26.
 */
public class StringComparator extends AbstractComparator<String> {

    public void init() {
        setOperatables(MapUtils.newMultiKeySetMap(
                new MapUtils.Entry<>(Sets.newHashSet(Operator.EQUAL, Operator.IN, Operator.INTERSECT), (Operatable<Boolean, String>) StringUtils::equals),
                new MapUtils.Entry<>(Sets.newHashSet(Operator.NOT_EQUAL, Operator.NOT_IN), (Operatable<Boolean, String>) StringUtils::notEquals)
        ));
        setLogic(MapUtils.newEnumMap(Operator.class));
        set_Logic(MapUtils.newEnumMap(Operator.class,
                new MapUtils.Entry<>(Operator.EQUAL, LogicComputeOperator.OR),
                new MapUtils.Entry<>(Operator.IN, LogicComputeOperator.OR),
                new MapUtils.Entry<>(Operator.NOT_IN, LogicComputeOperator.AND),
                new MapUtils.Entry<>(Operator.INTERSECT, LogicComputeOperator.OR)
        ));
    }


}
