package com.company;

import com.company.element.ConditionDefinition;
import com.company.element.ConditionField;
import com.company.element.ValidationDefinition;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
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
    private List<ConditionDefinition> ceds;
    private Map<String, Class> classMap;
    private Map<Class, Set<Object>> objectClassMap = new HashMap<Class, Set<Object>>();

    public ValidationContext(ValidationDefinition ved) {
        this.ved = ved;
    }

    public void validate(Object... objs) {
        for (Object obj : objs) {
            if (!objectClassMap.containsKey(obj.getClass())) {
                Set<Object> set = new HashSet<Object>();
                set.add(obj);
                objectClassMap.put(obj.getClass(), set);
            }
            objectClassMap.get(obj.getClass()).add(obj);
        }

        if (CollectionUtils.isNotEmpty(ved.getConditions())) {
            ceds = ved.getConditions();
            classMap = ved.getClasses();

            for (ConditionDefinition ced : ceds) {
                switch (ced.getConditionType()) {
                    case NORMAL:

                        break;
                    case IF:
                        break;
                }
            }
        }
    }

    public void validateNormal(ConditionDefinition ced, Object... objs) {
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

    private Object getFieldValue(Object checkObj, List<Field> fields) {
        Object target = checkObj;
        String getMethodName = "";

        try {
            for (int i = 0; i < fields.size(); i++) {
                if (i != 0) {
                    target = target.getClass().getMethod(getMethodName).invoke(target);
                    if (target == null)
                        Assert.runtimeException("null object");
                }
                getMethodName = "get" + StringUtils.firstToCapital(fields.get(i).getName());
            }

            return target.getClass().getMethod(getMethodName).invoke(target);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        }

        return null;
    }

    public void setValidation(ValidationDefinition ved) {
        this.ved = ved;
    }

    private void checkTypeCompatible(List<Object> list) {
        Class clazz = parentClassMap.get(list.get(0).getClass());
        for (Object o : list) {
            if (!clazz.equals(parentClassMap.get(o.getClass()))) {
                Assert.runtimeException("incompatible type " + clazz + " and " + parentClassMap.get(o.getClass()) +);
            }
        }
    }

    private static MultiKeySetMap<Class, Class> parentClassMap = new MultiKeySetMap<Class, Class>();

    static {
        parentClassMap.put(Sets.newHashSet(new Class[]{Integer.class, Float.class, Double.class, Long.class, Short.class, Byte.class, BigDecimal.class, BigInteger.class}), Number.class);
        parentClassMap.put(Sets.newHashSet(new Class[]{Boolean.class}), Boolean.class);
        parentClassMap.put(Sets.newHashSet(new Class[]{String.class}), String.class);
        parentClassMap.put(Sets.newHashSet(new Class[]{HashMap.class, LinkedHashMap.class}), Map.class);
        parentClassMap.put(Sets.newHashSet(new Class[]{ArrayList.class}), List.class);
    }


}
