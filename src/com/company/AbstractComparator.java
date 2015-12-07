package com.company;

import com.company.enums.LogicComputeOperator;
import com.company.enums.Operator;
import com.company.enums.CheckType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.company.enums.CheckType.*;
import static com.company.enums.Operator.*;

/**
 * Created by henry on 15/11/26.
 */
public abstract class AbstractComparator<T> implements Comparator<T>, MapAccessor {

    static RelationSheet<CheckType, Operator, Operatable> operatables = new RelationSheet<>();

    static {
        operatables.put((Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) > 0, NUMBER, LARGER_THAN);
        operatables.put((Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) >= 0, NUMBER, LARGER_OR_EQUAL, BETWEEN);
        operatables.put((Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) == 0, NUMBER, EQUAL, IN, INTERSECT);
        operatables.put((Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) <= 0, NUMBER, LESS_OR_EQUAL);
        operatables.put((Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) < 0, NUMBER, LESS_OR_EQUAL);
        operatables.put((Operatable<Boolean, BigDecimal>) (var1, var2) -> var1.compareTo(var2) != 0, NUMBER, NOT_EQUAL, NOT_IN);
        operatables.put((Operatable<Boolean, Boolean>) Boolean::equals, BOOLEAN, EQUAL);
        operatables.put((Operatable<Boolean, Boolean>) (var1, var2) -> var1 != var2, BOOLEAN, NOT_EQUAL);
    }

    MultiKeySetMap<Operator, Operatable> opertableMap;
    EnumMap<Operator, LogicComputeOperator> logicMap;
    EnumMap<Operator, LogicComputeOperator> _LogicMap;

    public AbstractComparator() {
        init();
    }

    protected abstract void init();

    public boolean doCompare(CompareObject<T> co) {
        Operator operator = co.getOperator();
        if (logicMap.get(operator) != null)
            co.setLogic(logicMap.get(operator));
        if (_LogicMap.get(operator) != null)
            co.set_logic(_LogicMap.get(operator));
        return compare(preProcess(co));
    }

    public CompareObject<T> preProcess(CompareObject<T> co) {
        return co;
    }

    @Override
    public List<Object> compare(CompareObject<T> co) {
        List<Boolean> res1 = new ArrayList<>();
        for (T var1 : co.getVals()) {
            List<Boolean> res2 = new ArrayList<>();
            for (T var2 : co.get_vals()) {
                res2.add((Boolean) (opertableMap.get(co.getOperator())).operate(var1, var2));
            }
            res1.add(co.get_logic().operate(res2.toArray(new Boolean[res2.size()])));
        }

        return co.getLogic().operate(res1.toArray(new Boolean[res1.size()]));
    }

    @Override
    public void setOperatables(Map operatables) {
        this.opertableMap = (MultiKeySetMap) operatables;
    }

    @Override
    public void setLogic(Map logicMap) {
        this.logicMap = (EnumMap) logicMap;
    }

    @Override
    public void set_Logic(Map _LogicMap) {
        this._LogicMap = (EnumMap) _LogicMap;
    }
}