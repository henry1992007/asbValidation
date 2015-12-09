package com.company.element;

/**
 * Created by henry on 15/11/16.
 */
public class ClassDefinition extends AbstractElementDefinition {

    private Class clazz;

    public ClassDefinition(String id, int lineNum, String docName) {
        super(id, lineNum, docName);
    }


    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }
}
