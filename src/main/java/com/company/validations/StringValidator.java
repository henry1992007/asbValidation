package com.company.validations;

import com.company.element.CheckDefinition;
import com.company.enums.Operator;

import java.util.*;
import java.util.concurrent.locks.Condition;

/**
 * Created by henry on 15/11/5.
 */
public class StringValidator implements TypeValidator {

//    public List<String> validate(Object fieldValueObj, String[] conditionValue, Map<String, Object> otherFieldValues, Condition condition) {
//        List<String> validateResult = new ArrayList<String>();
//
//        Operator operator = Operator.getOperator(condition.getOperator());
//        switch (operator) {
//            case EQUAL:
//                if (conditionValue.length > 1)
//                    throw new RuntimeException("too many args in type String with operator \"" + condition.getOperator() + "\"");
//                if (!conditionValue[0].equals(fieldValueObj))
//                    validateResult.add(condition.getFieldName() + " must be " + conditionValue[0]);
//                break;
//            case IN:
//                boolean flag = false;
//                for (String s : conditionValue)
//                    if (fieldValueObj.equals(s)) {
//                        flag = true;
//                        break;
//                    }
//                if (!flag)
//                    validateResult.add(condition.getFieldName() + " must be one of " + Arrays.toString(conditionValue));
//                break;
//            case NOT_EQUAL:
//            case NOT_IN:
//                for (String s : conditionValue) {
//                    if (fieldValueObj.equals(s))
//                        validateResult.add(condition.getFieldName() + " must not be any of " + Arrays.toString(conditionValue));
//                }
//                break;
//            default:
//                throw new RuntimeException("unsupported operator \"" + condition.getOperator() + "\"in type string");
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
