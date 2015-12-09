package com.company.parsing;

import java.util.List;

/**
 * Created by henry on 15/11/16.
 */
public class ValidationEntity extends Entity {
    String className;
    List<ConditionEntity> conditions;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ConditionEntity> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionEntity> conditions) {
        this.conditions = conditions;
    }
}
