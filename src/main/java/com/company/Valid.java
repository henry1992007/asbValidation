package com.company;

import com.company.Exceptions.ClassNotRegisteredException;
import com.company.utils.CollectionUtils;
import com.company.element.*;
import com.company.validations.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.lang.Class;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by henry on 15/11/4.
 */
public class Valid {

    private static MultiKeySetMap<Class, TypeValidator> classValidatorMap = new MultiKeySetMap<Class, TypeValidator>();

    static {
        classValidatorMap.put(Sets.newHashSet(new Class[]{Integer.class, Float.class, Double.class, Long.class, Short.class, Byte.class, BigDecimal.class, BigInteger.class}), new NumberValidator());
        classValidatorMap.put(Sets.newHashSet(new Class[]{Boolean.class}), new BooleanValidator());
        classValidatorMap.put(Sets.newHashSet(new Class[]{String.class}), new StringValidator());
        classValidatorMap.put(Sets.newHashSet(new Class[]{HashMap.class, LinkedHashMap.class}), new MapValidator());
        classValidatorMap.put(Sets.newHashSet(new Class[]{ArrayList.class}), new ListValidator());
    }

    List<ValidationDefinition> validations;
//    ValidationConfig validationConfig;
//    ContextValidator contextValidator = new ContextValidator();

    public Valid(List<ValidationDefinition> validations) {
        this.validations = validations;
    }

    public List<String> check(Object... checkObj) throws Exception {
        List<String> invalidResult = Lists.newArrayList();

        Set<Class> objClass = new HashSet<Class>();
        for (Object o : checkObj)
            objClass.add(o.getClass());

        if (!CollectionUtils.isEmpty(validations)) {
            boolean flag = false;
            for (ValidationDefinition validation : validations) {
                if (objClass.equals(new HashSet<Class>(validation.getClasses().values()))) {
                    flag = true;
//                        contextValidator.validate(checkObj,validation);
//                        for (Condition condition : validation.getConditions()) {
//                            Object fieldValue = getFieldValue(checkObj, condition.getFieldName());
//                            String[] conditionValueStrs = condition.getConditionValue().split(",");
//                            Map<String, Object> otherFields = getOtherFieldValues(checkObj, condition.getOtherFields());
//                            TypeValidator validator = classValidatorMap.get(fieldValue.getClass());
//                            if (validator == null)
//                                throw new RuntimeException("not supported type");
//                            invalidResult.addAll(validator.validate(fieldValue, conditionValueStrs, otherFields, condition));
//                        }
                }
            }

            if (!flag)
                throw new ClassNotRegisteredException("pass in objects class pattern " + Arrays.toString(objClass.toArray()) + " is not registered in any config files");
        }

        return invalidResult;
    }


//    private Validation preProcess(Validation validation, ValidationConfig config) {
//        /**
//         * 在validation的class标签定义的class，
//         * 如果是全量类名，则取类名和对应的Class
//         * 如果是引用名，则取引用的class标签实体的类名，以及对应的Class
//         */
//        List<String> classNames = validation.getClassName();
//        Map<String, Class> classMap = new HashMap<String, Class>();
//        for (int i = 0; i < classNames.size(); i++) {
//            String className = classNames.get(i);
//            String refName;
//            try {
//                Class clazz = Class.forName(className);
//                refName = className.substring(className.lastIndexOf(".") + 1, className.length());
//                classMap.put(refName, clazz);
//            } catch (ClassNotFoundException e) {
//                refName = className;
//                className = config.getClasses().get(className).getClassName();
//                classNames.set(i, className);
//                try {
//                    classMap.put(refName, Class.forName(className));
//                } catch (ClassNotFoundException e1) {
//                }
//            }
//        }
//
//        validation.setClassMap(classMap);
//
//        List<Condition> conditions = validation.getConditions();
//        preProcessCondition(conditions, validation, config);
//
//        return validation;
//    }
//
//    private void preProcessCondition(List<Condition> conditions, Validation validation, ValidationConfig config) {
//        for (Condition condition : conditions) {
//            if (StringUtils.isEmpty(condition.getRef()))
//                continue;
//            Condition refCondition = config.getRefConditions().get(condition.getRef());
//            overrideAndExtendCondition(refCondition, condition);
//            checkConditionClassNameIllegal(condition, validation);
//            if (CollectionUtils.isNotEmpty(condition.getSubConditions()))
//                preProcessCondition(condition.getSubConditions(), validation, config);
//        }
//    }

//    private Condition overrideAndExtendCondition(Condition ref, Condition target) {
//        if (StringUtils.isEmpty(target.getFieldName())) target.setFieldName(ref.getFieldName());
//        if (StringUtils.isEmpty(target.getOperator())) target.setFieldName(ref.getOperator());
//        if (StringUtils.isEmpty(target.getConditionValue())) target.setFieldName(ref.getConditionValue());
//        if (StringUtils.isEmpty(target.getOtherFields())) target.setFieldName(ref.getOtherFields());
//        if (StringUtils.isEmpty(target.getInvalidMsg())) target.setFieldName(ref.getInvalidMsg());
//        return target;
//    }
//
//    private void checkConditionClassNameIllegal(Condition condition, Validation validation) {
//        String[] fieldNames = condition.getFieldName().split(",");
//        Map<String, Class> classMap = validation.getClassMap();
//        for (String fieldName : fieldNames) {
//            if (classMap.size() > 1) {
//                String s = StringUtils.firstToCapital(fieldName.substring(0, fieldName.indexOf(".")));
//                if (classMap.get(s) == null)
//                    throw new RuntimeException("unspecified class/field in multi class validation");
//            }
//        }
//    }

    private <T> Object getFieldValue(T checkObj, String fieldNameStr) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] fieldName = fieldNameStr.split("[.]");
        Object target = checkObj;
        String getMethodName = "";

        for (int i = 0; i < fieldName.length; i++) {
            if (i != 0)
                target = target.getClass().getMethod(getMethodName).invoke(target);
            getMethodName = "get" + fieldName[i].substring(0, 1).toUpperCase() + fieldName[i].substring(1, fieldName[i].length());
        }

        return target.getClass().getMethod(getMethodName).invoke(target);
    }

    private <T> Map<String, Object> getOtherFieldValues(T checkObj, String otherFieldValueStr) throws Exception {
        Map<String, Object> otherFieldValues = new HashMap<String, Object>();
        if (otherFieldValueStr != null && !otherFieldValueStr.isEmpty()) {
            String[] otherField = otherFieldValueStr.split(",");
            for (String s : otherField) {
                otherFieldValues.put(s, getFieldValue(checkObj, s));
            }
        }
        return otherFieldValues;
    }

}
