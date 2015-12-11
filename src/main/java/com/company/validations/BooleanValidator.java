package com.company.validations;

import com.company.element.CheckDefinition;
import com.company.element.ConditionValidateObject;
import com.company.enums.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by henry on 15/11/5.
 */
public class BooleanValidator implements TypeValidator {

//    public List<String> validate(ConditionValidateObject cvo) {
//        List<String> validateResult = new ArrayList<String>();
//
//        boolean conditionValue = Boolean.parseBoolean(conditionValueStr[0]);
//        boolean fieldValue = (Boolean) fieldValueObj;
//        Operator operator = Operator.getOperator(condition.getOperator());
//        switch (operator) {
//            case EQUAL:
//                if (conditionValue != fieldValue)
//                    validateResult.add(condition.getFieldName() + " must be " + conditionValue);
//                break;
//            case NOT_EQUAL:
//                if (conditionValue == fieldValue)
//                    validateResult.add(condition.getFieldName() + " must be " + conditionValue);
//                break;
//            default:
//                throw new RuntimeException("unsupported operator \"" + condition.getOperator() + "\"in type boolean");
//        }
//
//        return validateResult;
//    }

    @Override
    public boolean validate(CheckDefinition cd, Map<Class, Set<Object>> objectClassMap) {
        return false;
    }

    @Override
    public Map<Class, Set<Object>> filter(CheckDefinition cd, Map<Class, Set<Object>> objectClassMap) {
        return null;
    }
}
