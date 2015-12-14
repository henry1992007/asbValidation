package com.company.element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henry on 15/12/4.
 */
public class ConditionDefinition extends AbstractElementDefinition implements ParentElement {
    private String msg;
    private List<CheckDefinition> refConditions = new ArrayList<>();
    private List<CheckDefinition> subChecks = new ArrayList<>();
    private List<ConditionDefinition> subConditions = new ArrayList<>();

    public ConditionDefinition(int lineNum, String docName) {
        super(lineNum, docName);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CheckDefinition> getRefConditions() {
        return refConditions;
    }

    public void setRefConditions(List<CheckDefinition> refConditions) {
        this.refConditions = refConditions;
    }

    public void addRefCondition(CheckDefinition cd) {
        refConditions.add(cd);
    }

    @Override
    public void addCheck(CheckDefinition definition) {
        subChecks.add(definition);
    }

    @Override
    public void addCondition(ConditionDefinition definition) {
        subConditions.add(definition);
    }
}
