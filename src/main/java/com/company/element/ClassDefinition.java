package com.company.element;

/**
 * Created by henry on 15/11/16.
 */
public class ClassDefinition extends AbstractElementDefinition {

    private Class clazz;


    public ClassDefinition(String id, int lineNum) {
        super(lineNum);
        //todo: check id not null
        this.id = id;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

}
