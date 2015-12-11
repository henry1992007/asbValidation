package com.company.validations;

import com.company.element.CheckDefinition;
import com.company.enums.Operator;
import com.company.validations.TypeValidator;

import java.util.*;

/**
 * Created by henry on 15/11/6.
 */
public class MapValidator implements TypeValidator {

//    public List<String> validate(Object fieldValueObj, String[] conditionValueStr, Map<String, Object> otherFieldValues,Condition condition) throws Exception {
//
//        List<String> validateResult = new ArrayList<String>();
//
//        Map map = (Map) fieldValueObj;
//        if (map.keySet().isEmpty()) {
//            validateResult.add("empty map");
//            return validateResult;
//        }
//
//        for (Object o : map.keySet()) {
//            Class keyType = o.getClass();
//        }
//        Operator operator = Operator.getOperator(condition.getOperator());
//        System.out.println(fieldValueObj.getClass().getGenericSuperclass().getTypeName());
//        switch (operator) {
//            case CONTAINS_KEY:
//                break;
//            case CONTAINS_KEY_VALUE:
//                break;
//            default:
//                throw new RuntimeException("unsupported operator \"" + condition.getOperator() + "\"in type map");
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
