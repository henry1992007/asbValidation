package com.company.element;

import com.company.AssociativeOperator;
import com.company.enums.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 15/11/16.
 */
public class CheckDefinition extends AbstractElementDefinition {
    private CheckType checkType;
    private Map<Class, String[]> fields;
    private List<String> vals = new ArrayList<>();
    private AssociativeOperator cmpt;
    private AssociativeOperator logic;
    private Operator operator;
    private Map<Class, String[]> _fields;
    private List<String> _vals = new ArrayList<>();
    private AssociativeOperator _cmpt;
    private AssociativeOperator _logic;
    private String msg;


    public CheckDefinition(int lineNum, String docName) {
        super(lineNum, docName);
    }

    public List<String> getVals() {
        return vals;
    }

    public void setVals(List<String> vals) {
        this.vals = vals;
    }

    public List<String> get_vals() {
        return _vals;
    }

    public void set_vals(List<String> _vals) {
        this._vals = _vals;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public void setCheckType(CheckType checkType) {
        this.checkType = checkType;
    }

    public AssociativeOperator getCmpt() {
        return cmpt;
    }

    public void setCmpt(AssociativeOperator cmpt) {
        this.cmpt = cmpt;
    }

    public AssociativeOperator getLogic() {
        return logic;
    }

    public void setLogic(AssociativeOperator logic) {
        this.logic = logic;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Map<Class, String[]> getFields() {
        return fields;
    }

    public void setFields(Map<Class, String[]> fields) {
        this.fields = fields;
    }

    public Map<Class, String[]> get_fields() {
        return _fields;
    }

    public void set_fields(Map<Class, String[]> _fields) {
        this._fields = _fields;
    }


    public AssociativeOperator get_cmpt() {
        return _cmpt;
    }

    public void set_cmpt(AssociativeOperator _cmpt) {
        this._cmpt = _cmpt;
    }

    public AssociativeOperator get_logic() {
        return _logic;
    }

    public void set_logic(AssociativeOperator _logic) {
        this._logic = _logic;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
