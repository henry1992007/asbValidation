package com.company.element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by henry on 15/11/16.
 */
public class ValidationDefinition extends AbstractElementDefinition {
    private Map<String, Class> classes;
    private AbstractElementDefinition[] definitions;
    private Map<String, ConditionDefinition> idMap = new HashMap<>();

    public  ValidationDefinition(String id, int lineNum, String docName) {
        super(id, lineNum, docName);
    }

    public Map<String, Class> getClasses() {
        return classes;
    }

    public void setClasses(Map<String, Class> classes) {
        this.classes = classes;
    }

    public AbstractElementDefinition[] getDefinitions() {
        return definitions;
    }

    public void setDefinitions(AbstractElementDefinition[] definitions) {
        this.definitions = definitions;
    }

    public Map<String, ConditionDefinition> getIdMap() {
        return idMap;
    }
}
