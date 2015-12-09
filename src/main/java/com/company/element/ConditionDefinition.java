package com.company.element;

/**
 * Created by henry on 15/12/4.
 */
public class ConditionDefinition extends AbstractElementDefinition {
    private String msg;
    private CheckDefinition[] refConditions;
    private AbstractElementDefinition[] subConditions;

    public  ConditionDefinition(String id, int lineNum, String docName) {
        super(id, lineNum, docName);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CheckDefinition[] getRefConditions() {
        return refConditions;
    }

    public void setRefConditions(CheckDefinition[] refConditions) {
        this.refConditions = refConditions;
    }

    public AbstractElementDefinition[] getSubConditions() {
        return subConditions;
    }

    public void setSubConditions(AbstractElementDefinition[] subConditions) {
        this.subConditions = subConditions;
    }

}
