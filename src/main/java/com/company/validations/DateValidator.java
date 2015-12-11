package com.company.validations;

import com.company.element.CheckDefinition;
import com.company.element.ConditionValidateObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by henry on 15/12/5.
 */
public class DateValidator implements TypeValidator {

    @Override
    public boolean validate(CheckDefinition cd, Map<Class, Set<Object>> objectClassMap) {
        return false;
    }

    @Override
    public Map<Class, Set<Object>> filter(CheckDefinition cd, Map<Class, Set<Object>> objectClassMap) {
        return null;
    }
}
