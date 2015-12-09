package com.company.element;

import com.company.enums.LogicComputeOperator;
import com.company.enums.NumberComputeOperate;
import com.company.enums.Operator;

import java.util.Map;

/**
 * Created by henry on 15/11/24.
 */
public class ConditionValidateObject {
    Object[] checkObjs;
    Map<String, Object> fieldObjs;
    String[] conditionStrs;
    LogicComputeOperator logic;
    Operator operator;
    Map<String, Object> _fieldObjs;
    String[] _conditionStrs;
    LogicComputeOperator _logic;


    public ConditionValidateObject(Map<String, Object> fieldObjs, String[] conditionStrs, LogicComputeOperator logic, Operator operator, Map<String, Object> _fieldObjs, String[] _conditionStrs, LogicComputeOperator _logic) {
        this.fieldObjs = fieldObjs;
        this.conditionStrs = conditionStrs;
        this.logic = logic;
        this.operator = operator;
        this._fieldObjs = _fieldObjs;
        this._conditionStrs = _conditionStrs;
        this._logic = _logic;
    }

    public Map<String, Object> getFieldObjs() {
        return fieldObjs;
    }

    public String[] getConditionStrs() {
        return conditionStrs;
    }

    public LogicComputeOperator getLogic() {
        return logic;
    }

    public Operator getOperator() {
        return operator;
    }

    public Map<String, Object> get_fieldObjs() {
        return _fieldObjs;
    }

    public String[] get_conditionStrs() {
        return _conditionStrs;
    }

    public LogicComputeOperator get_logic() {
        return _logic;
    }

}
