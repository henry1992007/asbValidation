package com.company.element;

/**
 * Created by henry on 15/11/16.
 */
public abstract class AbstractElementDefinition {

    protected String id;

    protected int lineNum;

    protected AbstractElementDefinition(int lineNum) {
        this.lineNum = lineNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLineNum() {
        return lineNum;
    }

}
