package com.company;


import com.company.element.ClassDefinition;
import com.company.parsing.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 15/11/14.
 */
public class ConfigContext {

    String docName;
    Entity[] entities;
    Map<String, ClassDefinition> classes = new HashMap<>();


    public ConfigContext(String docName, Entity[] entities) {
        this.docName = docName;
        this.entities = entities;
    }

    public Map<String, ClassDefinition> getClasses() {
        return classes;
    }

    public void setClasses(Map<String, ClassDefinition> classes) {
        this.classes = classes;
    }

    public Entity[] getEntities() {
        return entities;
    }

    public String getDocName() {
        return docName;
    }

}