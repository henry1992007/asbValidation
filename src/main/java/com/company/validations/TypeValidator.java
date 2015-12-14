package com.company.validations;


import com.company.element.CheckDefinition;

import java.util.Map;
import java.util.Set;

/**
 * Created by henry on 15/11/6.
 */
public interface TypeValidator {

    boolean validate(CheckDefinition cd, Map<Class, Set<Object>> objectClassMap);

    Map<Class, Set<Object>> filter(CheckDefinition cd, Map<Class, Set<Object>> objectClassMap);

}
