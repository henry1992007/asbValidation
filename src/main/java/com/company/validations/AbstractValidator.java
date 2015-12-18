package com.company.validations;

import com.company.*;
import com.company.element.CheckDefinition;
import com.company.utils.Assert;
import com.company.utils.ReflectUtils;
import com.company.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;

import com.company.Comparator;
import com.google.common.collect.Lists;

import java.util.stream.Collectors;

import static com.company.utils.Utils.info;

/**
 * Created by henry on 15/12/15.
 */
public abstract class AbstractValidator<T> implements TypeValidator {

    private boolean checkNull = false;

    @Override
    public boolean validate(CheckDefinition cd, Map<Class, Set<Object>> objectClassMap) {
        Map<Class, String[]> fields = cd.getFields();
        Map<String, Object> fieldValues = new HashMap<>();
        for (Class clazz : fields.keySet())
            for (Object obj : objectClassMap.get(clazz))
                fieldValues.put(Arrays.toString(fields.get(clazz)) + obj, getFieldValue(obj, fields.get(clazz)));

        Map<Class, String[]> _fields = cd.get_fields();
        Map<String, Object> _fieldValues = new HashMap<>();
        for (Class clazz : _fields.keySet())
            for (Object obj : objectClassMap.get(clazz))
                _fieldValues.put(Arrays.toString(_fields.get(clazz)) + obj, getFieldValue(obj, _fields.get(clazz)));

        List<T> val = parseObject(fieldValues.values());
        val.addAll(parseString(cd.getVals()));
        List<T> _val = parseObject(_fieldValues.values());
        _val.addAll(parseString(cd.get_vals()));

        CompareObject<T> co = new CompareObject<>(val, cd.getLogic(), cd.getOperator(), _val, cd.get_logic(), checkNull);
        info(co.toString());
        return getComparator().compare(co);
    }

    @Override
    public Map<Class, Set<Object>> filter(CheckDefinition cd, Map<Class, Set<Object>> objectClassMap) {
        for (Class clazz : cd.getFields().keySet()) {
            Set<Object> resultSet = new HashSet<>();
            for (Object o : objectClassMap.get(clazz)) {
                List<T> val = parseObject(Lists.newArrayList(getFieldValue(o, cd.getFields().get(o.getClass()))));

                Map<Class, String[]> _fields = cd.get_fields();
                Map<String, Object> _fieldValues = new HashMap<>();
                for (Class clazz1 : _fields.keySet())
                    for (Object obj : objectClassMap.get(clazz1))
                        _fieldValues.put(Arrays.toString(_fields.get(clazz1)), getFieldValue(obj, _fields.get(clazz)));
                List<T> _val = parseObject(_fieldValues.values());
                _val.addAll(parseString(cd.get_vals()));

                CompareObject<T> co = new CompareObject<>(val, cd.getLogic(), cd.getOperator(), _val, cd.get_logic(), checkNull);
                if (getComparator().compare(co)) {
                    resultSet.add(o);
                }
            }
            objectClassMap.put(clazz, resultSet);
        }

        return objectClassMap;
    }

    protected abstract Comparator<T> getComparator();

    protected Object getFieldValue(Object checkObj, String[] path) {
        Object target = checkObj;

        for (int i = 0; i < path.length; i++) {
            if (ReflectUtils.isMapClass(target.getClass())) {
                target = ((Map) target).get(path[i]);
                //todo : handle null object
            } else {
                try {
                    Field field = target.getClass().getDeclaredField(path[i]);
                    if (Modifier.isPublic(field.getModifiers())) {
                        target = target.getClass().getField(path[i]).get(target);
                    } else {
                        String getMethodName = ReflectUtils.genGetMethod(path[i]);
                        target = target.getClass().getDeclaredMethod(getMethodName).invoke(target);
                    }
                } catch (NoSuchFieldException e) {
                    Assert.runtimeException("field " + Arrays.toString(path) + " of object Class: " + checkObj.getClass() + " does not exist");
                } catch (NoSuchMethodException e) {
                    Assert.runtimeException("couldn't access field " + Arrays.toString(path) + " of object Class: " + checkObj.getClass() +
                            ", this field is not public and does not have a get method");
                } catch (IllegalAccessException | InvocationTargetException e) {
                    Assert.runtimeException("couldn't access field " + Arrays.toString(path) + " of object Class: " + checkObj.getClass());
                }
            }
        }

        return target;
    }

    protected abstract List<T> parseObject(Collection<Object> list);

    protected abstract List<T> parseString(List<String> list);

    protected boolean setCheckNull(boolean b) {
        return this.checkNull = b;
    }

}
