package com.company.validations;

import com.company.element.CheckDefinition;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by henry on 15/11/7.
 */
public class ListValidator implements TypeValidator {

    @Override
    public boolean validate(CheckDefinition cd, Map<Class, List<Object>> objectClassMap) {
        return false;
    }

    @Override
    public Map<Class, List<Object>> filter(CheckDefinition cd, Map<Class, List<Object>> objectClassMap) {
        return null;
    }
}
