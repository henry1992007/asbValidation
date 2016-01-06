package com.company.element;

import com.company.enums.CheckType;

import java.util.Map;

/**
 * Created by henry on 15/12/18.
 */
public class ConstantDefinition extends AbstractElementDefinition {

    private CheckType Type;
    private Map<Class, String[]> fields;
    private Object value;

    public ConstantDefinition(int lineNum) {
        super(lineNum);
    }

    public CheckType getType() {
        return Type;
    }

    public void setType(CheckType type) {
        Type = type;
    }

    public Map<Class, String[]> getFields() {
        return fields;
    }

    public void setFields(Map<Class, String[]> fields) {
        this.fields = fields;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
