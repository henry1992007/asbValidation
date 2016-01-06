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
public class ValidationDefinition extends AbstractElementDefinition implements ParentElement {
    private String docPath;
    private Map<String, Class> classes;
    private Class mainClass;
    private List<CheckDefinition> checks = new ArrayList<>();
    private List<ConditionDefinition> conditions = new ArrayList<>();
    private Map<String, ConditionDefinition> conditionIdMap = new HashMap<>();


    public ValidationDefinition(String id, int lineNum, String docPath) {
        super(lineNum);
        this.docPath = docPath;
        //todo:check id not null
        this.id = id;
    }

    public Class getMainClass() {
        return mainClass;
    }

    public void setMainClass(Class mainClass) {
        this.mainClass = mainClass;
    }

    public void addCheck(CheckDefinition cd) {
        checks.add(cd);
    }

    public void addCondition(ConditionDefinition cd) {
        conditions.add(cd);
    }

    public List<CheckDefinition> getRefChecks() {
        return new ArrayList<>();
    }

    public Map<String, Class> getClasses() {
        return classes;
    }

    public void setClasses(Map<String, Class> classes) {
        this.classes = classes;
    }

    public List<CheckDefinition> getSubChecks() {
        return checks;
    }

    public List<ConditionDefinition> getSubConditions() {
        return conditions;
    }

    public void setIdMap(Map<String, ConditionDefinition> conditionIdMap) {
        this.conditionIdMap = conditionIdMap;
    }

    public Map<String, ConditionDefinition> getConditionIdMap() {
        return conditionIdMap;
    }

    public String getDocPath() {
        return docPath;
    }
}
