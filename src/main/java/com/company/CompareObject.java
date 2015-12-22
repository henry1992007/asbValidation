package com.company;

import com.company.enums.Operator;

import java.util.List;

/**
 * @author 何建恒
 */
public class CompareObject<T> {

    /**
     * 分别代表操作符operator前后的值数组
     */
    private List<T> vals, _vals;

    /**
     * 操作符前后多值分别单独比较的结果集的处理逻辑
     */
    private AssociativeOperator logic, _logic;

    /**
     * 操作符
     */
    private Operator operator;

    private boolean checkNull = false;

    public CompareObject(List<T> vals, AssociativeOperator logic, Operator operator, List<T> _vals, AssociativeOperator _logic, boolean checkNull) {
        this.vals = vals;
        this.logic = logic;
        this.operator = operator;
        this._vals = _vals;
        this._logic = _logic;
        this.checkNull = checkNull;
    }

    public CompareObject(List<T> vals, Operator operator, List<T> _vals, boolean checkNull) {
        this.vals = vals;
        this.operator = operator;
        this._vals = _vals;
        this.checkNull = checkNull;
    }

    public boolean getCheckNull() {
        return checkNull;
    }

    public void setVals(List<T> vals) {
        this.vals = vals;
    }

    public void set_vals(List<T> _vals) {
        this._vals = _vals;
    }

    public void set_logic(AssociativeOperator _logic) {
        this._logic = _logic;
    }

    public void setLogic(AssociativeOperator logic) {
        this.logic = logic;
    }

    public List<T> getVals() {
        return vals;
    }

    public List<T> get_vals() {
        return _vals;
    }

    public AssociativeOperator getLogic() {
        return logic;
    }

    public AssociativeOperator get_logic() {
        return _logic;
    }

    public Operator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("values:").append(vals).append(" operator:").append(operator.getValue()).append(" _values:").append(_vals);
        return sb.toString();
    }
}
