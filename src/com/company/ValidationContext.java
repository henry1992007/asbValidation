package com.company;

import com.company.element.*;
import com.company.enums.CheckType;
import com.company.enums.DefinitionType;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.ReflectUtils;
import com.company.utils.StringUtils;
import com.company.validations.NumberValidator;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by henry on 15/11/17.
 */
public class ValidationContext {

    private ValidationDefinition ved;
    private List<CheckDefinition> ceds;
    private Map<String, Class> classMap;
    private Map<Class, Set<Object>> objectClassMap = new HashMap<Class, Set<Object>>();

    public ValidationContext(ValidationDefinition ved) {
        this.ved = ved;
    }

    public void validate(Object... objs) {
        for (Object obj : objs) {
            if (!objectClassMap.containsKey(obj.getClass()))
                objectClassMap.put(obj.getClass(), Sets.newHashSet(obj));
            else
                objectClassMap.get(obj.getClass()).add(obj);
        }

        if (CollectionUtils.isNotEmpty(ved.getConditions())) {
            ceds = ved.getConditions();
            classMap = ved.getClasses();

            for (CheckDefinition ced : ceds) {
                switch (ced.getConditionType()) {
                    case NORMAL:

                        break;
                    case IF:
                        break;
                }
            }
        }
    }

    List<CheckDefinition> definitions;

    public void entry(AbstractElementDefinition[] aeds, Object... objs) {
        for (AbstractElementDefinition aed : aeds) {
            if (aed instanceof CheckDefinition) {
                //todo: 抽象
                ((CheckDefinition) aed).getCheckType().getTypeValidator().validate();
            } else if (aed instanceof ConditionDefinition) {
                boolean flag = true;
                CheckDefinition[] checkDefinitions = ((ConditionDefinition) aed).getRefConditions();
                for (CheckDefinition cd : checkDefinitions) {
                    if (!cd.getCheckType().getTypeValidator().validate(aed, objs))
                        flag = false;
                }
                if (flag)
                    entry(((ConditionDefinition) aed).getSubConditions());
            }
        }
    }

    public void validateNormal(CheckDefinition ced, Object... objs) {
        ConditionField[] fields = ced.getFields();
        Map<String, Object> fieldValues = new HashMap<>();

        ConditionField[] _fields = ced.get_fields();
        Map<String, Object> otherFieldValues = new HashMap<>();

        String[] vals = ced.getVals();
        String[] _vals = ced.get_vals();

        for (ConditionField conditionField : fields)
            for (Object obj : objectClassMap.get(conditionField.getClazz()))
                fieldValues.put(conditionField.toString(), getFieldValue(obj, conditionField.getFields()));
        for (ConditionField conditionField : _fields)
            for (Object obj : objectClassMap.get(conditionField.getClazz()))
                otherFieldValues.put(conditionField.toString(), getFieldValue(obj, conditionField.getFields()));

        NumberValidator numberValidator = new NumberValidator();
        numberValidator.validate(fieldValues, ced.getOperator(), values, otherFieldValues);

    }

    private Object getFieldValue(Object checkObj, String[] path) {
        Object target = checkObj;
        String getMethodName = "";

        try {
            for (int i = 0; i < path.length; i++) {
                if (i != 0) {
                    if (ReflectUtils.isMapClass(target.getClass())) {
                        target = ((Map) target).get(path[i]);
                    } else {
                        target = target.getClass().getMethod(getMethodName).invoke(target);
                    }
                    if (target == null)
                        Assert.runtimeException("null object");
                }

                if (ReflectUtils.isMapClass(target.getClass())) {

                } else {
                    getMethodName = "get" + StringUtils.firstToCapital(path[i]);
                }
            }

            return target.getClass().getMethod(getMethodName).invoke(target);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {

        }

        return null;
    }

    public void setValidation(ValidationDefinition ved) {
        this.ved = ved;
    }


}
