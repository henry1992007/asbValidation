package com.company.element;

/**
 * Created by henry on 15/11/16.
 */
public abstract class AbstractElementDefinition implements ElementDefinition {
    protected String id;
    protected int lineNum;
    protected String docName;

    protected AbstractElementDefinition() {
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

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }
}
