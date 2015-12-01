//package com.company;
//
//import com.company.element.Condition;
//import com.company.element.Validation;
//import com.company.element.ValidationConfig;
//import com.company.utils.CollectionUtils;
//import com.company.utils.MapUtils;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Sets;
//
//import java.lang.reflect.InvocationTargetException;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.*;
//
///**
// * Created by henry on 15/11/12.
// */
//public class ContextValidator {
//
//    private static MultiKeySetMap<Class, TypeValidator> classValidatorMap = new MultiKeySetMap<Class, TypeValidator>();
//
//    static {
//        classValidatorMap.put(Sets.newHashSet(new Class[]{Integer.class, Float.class, Double.class, Long.class, Short.class, Byte.class, BigDecimal.class, BigInteger.class}), new NumberValidator());
//        classValidatorMap.put(Sets.newHashSet(new Class[]{Boolean.class}), new BooleanValidator());
//        classValidatorMap.put(Sets.newHashSet(new Class[]{String.class}), new StringValidator());
//        classValidatorMap.put(Sets.newHashSet(new Class[]{HashMap.class, LinkedHashMap.class}), new MapValidator());
//        classValidatorMap.put(Sets.newHashSet(new Class[]{ArrayList.class}), new ListValidator());
//    }
//
//    public void validate(Validation validation, Object... checkObjs) throws Exception {
//        List<String> invalidResult = Lists.newArrayList();
//
//        List<Condition> conditions = validation.getConditions();
//        for (Condition condition : validation.getConditions()) {
//            String fieldName = condition.getFieldName();
//            String operator = condition.getOperator();
//            String value = condition.getConditionValue();
//            String otherFieldNames = condition.getOtherFields();
//            if (CollectionUtils.isEmpty(condition.getSubConditions())) {
//                String[] fieldNames = fieldName.split(",");
//                if (validation.getClassMap().size() == 1) {
//                    for (Object o : checkObjs) {
//                        Object fieldValue = getFieldValue(o, fieldNames);
//                        String[] conditionValueStrs = condition.getConditionValue().split(",");
//                        Map<String, Object> otherFields = getOtherFieldValues(o, condition.getOtherFields());
//                        TypeValidator validator = classValidatorMap.get(fieldValue.getClass());
//                        if (validator == null)
//                            throw new RuntimeException("not supported type");
//                        invalidResult.addAll(validator.validate(fieldValue, conditionValueStrs, otherFields, condition));
//                    }
//                }
//            } else {
//
//            }
//
//        }
//    }
//
//
//    private List<Object> getFieldValue(Object checkObj, String[] fieldNameStrs) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        List<Object> res = new ArrayList<Object>();
//        for (String fieldNameStr : fieldNameStrs) {
//            List<String> fieldName = Arrays.asList(fieldNameStr.split("[.]"));
//            Object target = checkObj;
//            String getMethodName = "";
//
//            for (int i = 0; i < fieldName.size(); i++) {
//                if (i != 0)
//                    target = target.getClass().getMethod(getMethodName).invoke(target);
////                getMethodName = "get" + fieldName.get(i).substring(0, 1).toUpperCase() + fieldName[i].substring(1, fieldName.get(i).length());
//            }
//            res.add(target.getClass().getMethod(getMethodName).invoke(target));
//        }
//
//        return res;
//    }
//
//    private Map<String, Object> getOtherFieldValues(Object checkObj, String otherFieldValueStr) throws Exception {
//        Map<String, Object> otherFieldValues = new HashMap<String, Object>();
//        if (otherFieldValueStr != null && !otherFieldValueStr.isEmpty()) {
//            String[] otherField = otherFieldValueStr.split(",");
//            for (String s : otherField) {
////                otherFieldValues.put(s, getFieldValue(checkObj, s));
//            }
//        }
//        return otherFieldValues;
//    }
//}
