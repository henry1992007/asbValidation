package com.company.element;

import com.company.utils.Assert;
import com.company.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 15/11/16.
 */
public class ValidationDefinition extends AbstractElementDefinition implements ParentElement{
    private Map<String, Class> classes;
    private List<CheckDefinition> checks = new ArrayList<>();
    private List<ConditionDefinition> conditions = new ArrayList<>();
    private Map<String, ConditionDefinition> conditionIdMap = new HashMap<>();


    public ValidationDefinition(String id, int lineNum, String docName) {
        super(lineNum, docName);
        if (StringUtils.isEmpty(id))
            Assert.illegalDefinitionException(Assert.VALIDATION_ID_UNSPECIFIED, lineNum, docName);
        setId(id);
    }

    public void addCheck(CheckDefinition cd) {
        checks.add(cd);
    }

    public void addCondition(ConditionDefinition cd) {
        conditions.add(cd);
    }

    public Map<String, Class> getClasses() {
        return classes;
    }

    public void setClasses(Map<String, Class> classes) {
        this.classes = classes;
    }

    public List<CheckDefinition> getChecks() {
        return checks;
    }

    public List<ConditionDefinition> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionDefinition> conditions) {
        this.conditions = conditions;
    }

    public void setIdMap(Map<String, ConditionDefinition> conditionIdMap) {
        this.conditionIdMap = conditionIdMap;
    }

    public Map<String, ConditionDefinition> getConditionIdMap() {
        return conditionIdMap;
    }
}
