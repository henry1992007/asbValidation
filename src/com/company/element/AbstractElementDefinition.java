package com.company.element;

/**
 * Created by henry on 15/11/16.
 */
public abstract class AbstractElementDefinition {
    protected String id;
    protected int lineNum;
    protected String docName;

    protected AbstractElementDefinition(String id, int lineNum, String docName) {
        this.id = id;
        this.lineNum = lineNum;
        this.docName = docName;
    }

    public String getId() {
        return id;
    }

    public int getLineNum() {
        return lineNum;
    }

    public String getDocName() {
        return docName;
    }

}
