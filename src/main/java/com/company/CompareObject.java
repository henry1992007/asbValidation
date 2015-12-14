package com.company;

import com.company.enums.LogicComputeOperator;
import com.company.enums.Operator;

import java.util.List;

/**
 * @author 何建恒
 */
public class CompareObject<T> {

    /**
     * 分别代表操作符operator前后的值数组
     */
    List<T> vals, _vals;

    /**
     * 操作符前后多值分别单独比较的结果集的处理逻辑
     */
    ComputeOperator logic, _logic;

    /**
     * 操作符
     */
    Operator operator;

    public CompareObject(List<T> vals, ComputeOperator logic, Operator operator, List<T> _vals, ComputeOperator _logic) {
        this.vals = vals;
        this.logic = logic;
        this.operator = operator;
        this._vals = _vals;
        this._logic = _logic;
    }

    public CompareObject(List<T> vals, Operator operator, List<T> _vals) {
        this.vals = vals;
        this.operator = operator;
        this._vals = _vals;
    }

    public void setVals(List<T> vals) {
        this.vals = vals;
    }

    public void set_vals(List<T> _vals) {
        this._vals = _vals;
    }

    public void set_logic(ComputeOperator _logic) {
        this._logic = _logic;
    }

    public void setLogic(ComputeOperator logic) {
        this.logic = logic;
    }

    public List<T> getVals() {
        return vals;
    }

    public List<T> get_vals() {
        return _vals;
    }

    public ComputeOperator getLogic() {
        return logic;
    }

    public ComputeOperator get_logic() {
        return _logic;
    }

    public Operator getOperator() {
        return operator;
    }
}
