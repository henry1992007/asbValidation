package com.company.element;

/**
 * Created by henry on 15/11/16.
 */
public abstract class AbstractElementDefinition {
    protected String id;
    protected int lineNum;
    protected String docName;

    protected AbstractElementDefinition(int lineNum, String docName) {
        this.lineNum = lineNum;
        this.docName = docName;
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

    public String getDocName() {
        return docName;
    }

}
