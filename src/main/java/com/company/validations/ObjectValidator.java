package com.company.validations;

import com.company.element.CheckDefinition;

import java.util.Map;
import java.util.Set;

/**
 * Created by henry on 15/12/11.
 */
public class ObjectValidator implements TypeValidator{

    @Override
    public boolean validate(CheckDefinition cd, Map<Class, Set<Object>> objectClassMap) {
        return false;
    }

    @Override
    public Map<Class, Set<Object>> filter(CheckDefinition cd, Map<Class, Set<Object>> objectClassMap) {
        return null;
    }

}
