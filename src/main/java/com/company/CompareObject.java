package com.company;

import com.company.enums.Operator;
import com.company.validations.AssociativeOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author 何建恒
 */
@Getter
@Setter
public class CompareObject<T> {

    /**
     * 分别代表操作符operator前后的值数组
     */
    private List<T> vals, _vals;

    /**
     * 操作符前后多值分别单独比较的结果集的处理逻辑
     */
    private AssociativeOperator<Boolean> logic, _logic;


    /**
     * 操作符
     */
    private Operator operator;

    private boolean checkNull = false;

    public CompareObject(List<T> vals, AssociativeOperator<Boolean> logic, Operator operator, List<T> _vals, AssociativeOperator<Boolean> _logic, boolean checkNull) {
        this.vals = vals;
        this.logic = logic;
        this.operator = operator;
        this._vals = _vals;
        this._logic = _logic;
        this.checkNull = checkNull;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("values:").append(vals).append(" operator:").append(operator.getValue()).append(" _values:").append(_vals);
        return sb.toString();
    }

}
