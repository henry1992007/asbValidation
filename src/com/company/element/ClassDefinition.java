package com.company.element;

/**
 * Created by henry on 15/11/16.
 */
public class ClassDefinition extends AbstractElementDefinition {

    private Class clazz;

    public ClassDefinition() {
    }

    public ClassDefinition(String id, int lineNum) {
        setId(id);
        setLineNum(lineNum);
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }
}
