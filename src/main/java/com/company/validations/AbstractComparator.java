package com.company.validations;

import com.company.Comparator;
import com.company.Compare;
import com.company.CompareObject;
import com.company.enums.CheckType;
import com.company.enums.Operator;
import com.company.utils.RelationSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.company.enums.CheckType.*;
import static com.company.enums.Operator.*;
import static com.company.utils.Utils.info;
import static com.company.validations.MultivariateOperators.*;

/**
 * Created by henry on 15/11/26.
 */
public abstract class AbstractComparator<T> implements Comparator<T> {

    static Compare<Comparable> ABSTRACT_GREATER_THAN = (c1, c2) -> c1.compareTo(c2) > 0;
    static Compare<Comparable> ABSTRACT_NO_LESS_THAN = (c1, c2) -> c1.compareTo(c2) >= 0;
    static Compare<Comparable> ABSTRACT_EQUAL_TO = (c1, c2) -> c1.compareTo(c2) == 0;
    static Compare<Comparable> ABSTRACT_NO_GREATER_THAN = (c1, c2) -> c1.compareTo(c2) <= 0;
    static Compare<Comparable> ABSTRACT_LESS_THAN = (c1, c2) -> c1.compareTo(c2) < 0;
    static Compare<Comparable> ABSTRACT_NOT_EQUAL_TO = (c1, c2) -> c1.compareTo(c2) < 0;


    static RelationSheet<Compare, CheckType, Operator> compares = new RelationSheet<>();
    static Map<Operator, AggregateOperator<Boolean>> logicOperatorBinding = new HashMap<>();
    static Map<Operator, AggregateOperator<Boolean>> _logicOperatorBinding = new HashMap<>();

    private CheckType checkType;

    public AbstractComparator(CheckType checkType) {
        this.checkType = checkType;
    }

    @Override
    public boolean compare(CompareObject<T> co) {
        Operator operator = co.getOperator();
        AggregateOperator<Boolean> ao = logicOperatorBinding.get(operator);
        if (ao != null) {
            info("logic operator override to '" + MultivariateOperators.getName(ao) + "' by checker because '" + co.getOperator().getValue() + "' does not support " + MultivariateOperators.getName(co.getLogic()));
            co.setLogic(ao);
        }
        MultivariateOperator _ao = _logicOperatorBinding.get(operator);
        if (_ao != null) {
            info("_logic operator override to '" + MultivariateOperators.getName(_ao) + "' by checker because '" + co.getOperator().getValue() + "' does not support " + MultivariateOperators.getName(co.get_logic()));
            co.set_logic(ao);
        }
        return doCompare(preProcess(co));
    }

    public CompareObject<T> preProcess(CompareObject<T> co) {
        return co;
    }

    public boolean doCompare(CompareObject<T> co) {
        List<Boolean> res1 = new ArrayList<>();
        for (T var1 : co.getVals()) {
            List<Boolean> res2 = new ArrayList<>();
            Operator operator = co.getOperator();
            for (T var2 : co.get_vals()) {
                res2.add((compares.get(checkType, operator)).apply(var1, var2));
            }
            if (co.isCheckNull())
                res2.add(operator.equals(NOT_EQUAL) ? var1 != null : var1 == null);
            res1.add(co.get_logic().operate(res2));
        }
        return co.getLogic().operate(res1);
    }

    public static <T> AbstractComparator<T> defaultComparator(CheckType checkType) {
        return new AbstractComparator<T>(checkType) {
        };
    }

    static {
        compares.put(NUMBER, ABSTRACT_GREATER_THAN, LARGER_THAN);
        compares.put(NUMBER, ABSTRACT_NO_LESS_THAN, LARGER_OR_EQUAL, BETWEEN);
        compares.put(NUMBER, ABSTRACT_EQUAL_TO, EQUAL, IN, INTERSECT);
        compares.put(NUMBER, ABSTRACT_NO_GREATER_THAN, LESS_OR_EQUAL);
        compares.put(NUMBER, ABSTRACT_LESS_THAN, LESS_OR_EQUAL);
        compares.put(NUMBER, ABSTRACT_NOT_EQUAL_TO, NOT_EQUAL, NOT_IN);
        compares.put(STRING, ABSTRACT_EQUAL_TO, EQUAL, IN, INTERSECT);
        compares.put(STRING, ABSTRACT_NOT_EQUAL_TO, NOT_EQUAL, NOT_IN);
        compares.put(BOOLEAN, ABSTRACT_EQUAL_TO, EQUAL);
        compares.put(BOOLEAN, ABSTRACT_NOT_EQUAL_TO, NOT_EQUAL);
        compares.put(DATE, ABSTRACT_GREATER_THAN, AFTER, BETWEEN);
        compares.put(DATE, ABSTRACT_EQUAL_TO, EQUAL);
        compares.put(DATE, ABSTRACT_NOT_EQUAL_TO, NOT_EQUAL);
        compares.put(DATE, ABSTRACT_LESS_THAN, BEFORE);

        _logicOperatorBinding.put(IN, OR);
        _logicOperatorBinding.put(EQUAL, OR);
        _logicOperatorBinding.put(NOT_IN, AND);
        _logicOperatorBinding.put(INTERSECT, OR);
        _logicOperatorBinding.put(BETWEEN, XOR);
    }

}