package com.company;

import com.company.enums.LogicComputeOperator;
import com.company.enums.Operator;

/**
 * @author 何建恒
 */
public class CompareObject<T> {

    /**
     * 分别代表操作符operator前后的值数组
     */
    T[] vals, _vals;

    /**
     * 操作符前后多值分别单独比较的结果集的处理逻辑
     */
    LogicComputeOperator logic, _logic;

    /**
     * 操作符
     */
    Operator operator;

    public CompareObject(T[] vals, LogicComputeOperator logic, Operator operator, T[] _vals, LogicComputeOperator _logic) {
        this.vals = vals;
        this.logic = logic;
        this.operator = operator;
        this._vals = _vals;
        this._logic = _logic;
    }

    public T[] getVals() {
        return vals;
    }

    public void setVals(T[] vals) {
        this.vals = vals;
    }

    public LogicComputeOperator getLogic() {
        return logic;
    }

    public void setLogic(LogicComputeOperator logic) {
        this.logic = logic;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public T[] get_vals() {
        return _vals;
    }

    public void set_vals(T[] _vals) {
        this._vals = _vals;
    }

    public LogicComputeOperator get_logic() {
        return _logic;
    }

    public void set_logic(LogicComputeOperator _logic) {
        this._logic = _logic;
    }
}
