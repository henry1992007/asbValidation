package com.company.element;

import com.company.enums.CheckType;

import java.util.Map;

/**
 * Created by henry on 15/12/18.
 */
public class ConstantDefinition<T> extends AbstractElementDefinition {
    private CheckType Type;
    private Map<Class, String[]> fields;
    private T value;

    public ConstantDefinition(int lineNum, String docName) {
        super(lineNum, docName);
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

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
