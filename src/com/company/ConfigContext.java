package com.company;


import com.company.element.*;
import com.company.parsing.Entity;

import java.util.List;
import java.util.Map;

/**
 * Created by henry on 15/11/14.
 */
public class ConfigContext {

    List<Entity> entities;

    Map<String, ClassDefinition> classes;
    Map<String, CheckDefinition> refConditions;
    Map<String, ValidationDefinition> validations;

    public ConfigContext(List<Entity> entities) {
        this.entities = entities;
    }

    public List<Entity> getEntities() {
        return entities;
    }

}
