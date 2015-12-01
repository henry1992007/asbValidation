package com.company.validations;


import com.company.element.ConditionValidateObject;

import java.util.List;

/**
 * Created by henry on 15/11/6.
 */
public interface TypeValidator {

    List<String> validate(ConditionValidateObject cvo);

}
