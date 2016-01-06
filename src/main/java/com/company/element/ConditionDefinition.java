package com.company.element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henry on 15/12/4.
 */
public class ConditionDefinition extends AbstractElementDefinition implements ParentElement {
    private String msg;
    private List<CheckDefinition> refChecks = new ArrayList<>();
    private List<CheckDefinition> subChecks = new ArrayList<>();
    private List<ConditionDefinition> subConditions = new ArrayList<>();

    public ConditionDefinition(int lineNum) {
        super(lineNum);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void addRefCheck(CheckDefinition cd) {
        refChecks.add(cd);
    }

    public List<CheckDefinition> getRefChecks() {
        return refChecks;
    }

    public void setRefChecks(List<CheckDefinition> refChecks) {
        this.refChecks = refChecks;
    }

    public List<CheckDefinition> getSubChecks() {
        return subChecks;
    }

    public List<ConditionDefinition> getSubConditions() {
        return subConditions;
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
