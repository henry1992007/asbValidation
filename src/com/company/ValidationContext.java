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

    }

    List<CheckDefinition> definitions;

    public List<String> entry(AbstractElementDefinition[] aeds, Map<Class, Set<Object>> objectClassMap) {
        List<String> result = new ArrayList<>();
        for (AbstractElementDefinition aed : aeds) {
            if (aed instanceof CheckDefinition) {
                //todo: 抽象
                if (!((CheckDefinition) aed).getCheckType().getTypeValidator().validate()) {
                    result.add(((CheckDefinition) aed).getMsg());
                }
            } else if (aed instanceof ConditionDefinition) {
                CheckDefinition[] checkDefinitions = ((ConditionDefinition) aed).getRefConditions();
                for (CheckDefinition cd : checkDefinitions)
                    objectClassMap = cd.getCheckType().getTypeValidator().filter(cd, objectClassMap);
                entry(((ConditionDefinition) aed).getSubConditions(), objectClassMap);
            }
        }

        return result;
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
