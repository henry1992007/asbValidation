package com.company.element;

import com.company.ComputeOperator;
import com.company.enums.ConditionTypeEnum;
import com.company.enums.LogicComputeOperator;
import com.company.enums.Operator;
import com.company.enums.SupportedType;

/**
 * Created by henry on 15/11/16.
 */
public class ConditionDefinition extends AbstractElementDefinition {
    private Object[] checkObjects;
    private SupportedType type;
    private ConditionField[] fields;
    private String[] vals;
    private ComputeOperator cmpt;
    private LogicComputeOperator logic;
    private Operator operator;
    private ConditionField[] _fields;
    private String[] _vals;
    private ComputeOperator _cmpt;
    private LogicComputeOperator _logic;
    private String msg;
    private ConditionDefinition[] subConditions;
    private ConditionDefinition[] refConditions;
    private ConditionTypeEnum conditionType;


    public ConditionDefinition(String id, int lineNum) {
        setId(id);
        setLineNum(lineNum);
    }

    public SupportedType getType() {
        return type;
    }

    public void setType(SupportedType type) {
        this.type = type;
    }

    public ConditionField[] getFields() {
        return fields;
    }

    public void setFields(ConditionField[] fields) {
        this.fields = fields;
    }

    public String[] getVals() {
        return vals;
    }

    public void setVals(String[] vals) {
        this.vals = vals;
    }

    public ComputeOperator getCmpt() {
        return cmpt;
    }

    public void setCmpt(ComputeOperator cmpt) {
        this.cmpt = cmpt;
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

    public ConditionField[] get_fields() {
        return _fields;
    }

    public void set_fields(ConditionField[] _fields) {
        this._fields = _fields;
    }

    public String[] get_vals() {
        return _vals;
    }

    public void set_vals(String[] _vals) {
        this._vals = _vals;
    }

    public ComputeOperator get_cmpt() {
        return _cmpt;
    }

    public void set_cmpt(ComputeOperator _cmpt) {
        this._cmpt = _cmpt;
    }

    public LogicComputeOperator get_logic() {
        return _logic;
    }

    public void set_logic(LogicComputeOperator _logic) {
        this._logic = _logic;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ConditionDefinition[] getSubConditions() {
        return subConditions;
    }

    public void setSubConditions(ConditionDefinition[] subConditions) {
        this.subConditions = subConditions;
    }

    public ConditionDefinition[] getRefConditions() {
        return refConditions;
    }

    public void setRefConditions(ConditionDefinition[] refConditions) {
        this.refConditions = refConditions;
    }

    public ConditionTypeEnum getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionTypeEnum conditionType) {
        this.conditionType = conditionType;
    }
}
