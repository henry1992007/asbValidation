package com.company;

import com.company.enums.Operator;
import com.company.utils.MultiKeySetMap;

import java.util.Map;

/**
 * Created by henry on 15/11/26.
 */
public interface MapAccessor {

    MultiKeySetMap<Operator, Operatable> getOperatables();

    Map<Operator, AssociativeOperator<Boolean>> getLogic();

    Map<Operator, AssociativeOperator<Boolean>> get_Logic();
}
