package com.company.validations;


import com.company.element.CheckDefinition;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by henry on 15/11/6.
 */
public interface TypeValidator {

    boolean validate(CheckDefinition cd, Map<Class, List<Object>> objectClassMap);

    Map<Class, List<Object>> filter(CheckDefinition cd, Map<Class, List<Object>> objectClassMap);

}
