package com.company;

import com.company.enums.LogicComputeOperator;
import com.company.enums.Operator;

import java.util.ArrayList;
import java.util.List;

import static com.company.enums.Operator.NOT_EQUAL;
import static com.company.utils.Utils.*;

/**
 * Created by henry on 15/11/26.
 */
public abstract class AbstractComparator<T> implements Comparator<T>, MapAccessor {

//    static RelationSheet<Operatable, CheckType, Operator> operatables = new RelationSheet<>();
//
//    static {
//        operatables.put((Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) > 0, NUMBER, LARGER_THAN);
//        operatables.put((Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) >= 0, NUMBER, LARGER_OR_EQUAL, BETWEEN);
//        operatables.put((Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) == 0, NUMBER, EQUAL, IN, INTERSECT);
//        operatables.put((Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) <= 0, NUMBER, LESS_OR_EQUAL);
//        operatables.put((Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) < 0, NUMBER, LESS_OR_EQUAL);
//        operatables.put((Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) != 0, NUMBER, NOT_EQUAL, NOT_IN);
//        operatables.put((Operatable<Boolean, Boolean>) Boolean::equals, BOOLEAN, EQUAL);
//        operatables.put((Operatable<Boolean, Boolean>) (var1, var2) -> var1 != var2, BOOLEAN, NOT_EQUAL);
//    }

    public AbstractComparator() {
        init();
    }

    protected abstract void init();

    @Override
    public boolean compare(CompareObject<T> co) {
        Operator operator = co.getOperator();
        if (getLogic().get(operator) != null) {
            co.setLogic(getLogic().get(operator));
            info("logic operator override to '" + ((LogicComputeOperator) co.getLogic()).getName() + "' by checker because '" + co.getOperator().getValue() + "' does not support the origin logic operator");
        }
        if (get_Logic().get(operator) != null) {
            co.set_logic(get_Logic().get(operator));
            info("_logic operator override to '" + ((LogicComputeOperator) co.get_logic()).getName() + "' by checker because '" + co.getOperator().getValue() + "' does not support the origin _logic operator");
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
                res2.add((Boolean) (getOperatables().get(operator)).operate(var1, var2));
            }
            if (co.getCheckNull())
                res2.add(operator.equals(NOT_EQUAL) ? var1 != null : var1 == null);
            res1.add((Boolean) co.get_logic().operate(res2).get(0));
        }

        return (boolean) co.getLogic().operate(res1).get(0);
    }

}