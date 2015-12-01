package com.company.parsing;

import java.util.List;

/**
 * @author henry
 */
public class ConditionEntity extends Entity {
    private String ref;
    private String field;
    private String val;
    private String cmpt;
    private String logic;
    private String optr;
    private String _field;
    private String _val;
    private String _cmpt;
    private String _logic;
    private String msg;
    private List<ConditionEntity> subConditions;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getCmpt() {
        return cmpt;
    }

    public void setCmpt(String cmpt) {
        this.cmpt = cmpt;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public String getOptr() {
        return optr;
    }

    public void setOptr(String optr) {
        this.optr = optr;
    }

    public String get_field() {
        return _field;
    }

    public void set_field(String _field) {
        this._field = _field;
    }

    public String get_val() {
        return _val;
    }

    public void set_val(String _val) {
        this._val = _val;
    }

    public String get_cmpt() {
        return _cmpt;
    }

    public void set_cmpt(String _cmpt) {
        this._cmpt = _cmpt;
    }

    public String get_logic() {
        return _logic;
    }

    public void set_logic(String _logic) {
        this._logic = _logic;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ConditionEntity> getSubConditions() {
        return subConditions;
    }

    public void setSubConditions(List<ConditionEntity> subConditions) {
        this.subConditions = subConditions;
    }
}
