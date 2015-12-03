package com.company.element;

import java.util.Map;

/**
 * Created by henry on 15/11/16.
 */
public class ValidationDefinition extends AbstractElementDefinition {
    private Map<String, Class> classes;
    private CheckDefinition[] conditions;


    public ValidationDefinition(String id, int lineNum) {
        setId(id);
        setLineNum(lineNum);
    }

    public Map<String, Class> getClasses() {
        return classes;
    }

    public void setClasses(Map<String, Class> classes) {
        this.classes = classes;
    }

    public CheckDefinition[] getConditions() {
        return conditions;
    }

    public void setConditions(CheckDefinition[] conditions) {
        this.conditions = conditions;
    }

}
