package com.company.validations;

import com.company.Comparator;
import com.company.CompareObject;
import com.company.element.CheckDefinition;
import com.company.element.FieldPath;
import com.company.enums.CheckMode;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.ReflectUtils;
import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static com.company.utils.Utils.info;

/**
 * Created by henry on 15/12/15.
 */
public abstract class AbstractValidator<T> implements TypeValidator {

//    protected AbstractComparator<T> comparator;

    private static CheckMode defaultCheckMode = CheckMode.RELATED;
    private boolean checkNull = false;

    @Override
    public boolean validate(CheckDefinition cd, Map<Class, List<Object>> objectClassMap) {
        setCheckNull(false);

//        CheckMode checkMode = cd.getCheckMode();
//        if (checkMode == null) {
//            if (cd.getCmpt().equals(MultivariateOperators.DEFAULT) ||
//                    MultivariateOperators.defaultSemanteme.get(cd.getCmpt()) == null) {
//                checkMode = defaultCheckMode;
//            } else {
//                checkMode = MultivariateOperators.defaultSemanteme.get(cd.getCmpt());
//            }
//        }

//        switch (checkMode) {
//            case SINGLE:
        return checkBySingle(cd, objectClassMap);
//            case RELATED:
//                return checkByRelated(cd, objectClassMap);
//            default:
//                return false;
//        }
    }

    /**
     * 以mainClass为标准
     *
     * @param cd
     * @param objectClassMap
     * @return
     */
    private boolean checkBySingle(CheckDefinition cd, Map<Class, List<Object>> objectClassMap) {
        List<FieldPath> fields = cd.getFields();
        List<FieldPath> _fields = cd.get_fields();

        Optional<FieldPath> fieldPath = CollectionUtils.findFirstMatch(fields, FieldPath::isMain);
        if (!fieldPath.isPresent())
            throw new RuntimeException("error");
        Class mainClass = fieldPath.get().getClazz();
        List<Object> objects = objectClassMap.get(mainClass);

        List<Boolean> result = new ArrayList<>();
        for (Object o : objects) {
            List<T> tempVal = resolveValues(o, cd, fields, objectClassMap);
            tempVal.addAll(fromStrings(cd.getVals()));

            List<T> _tempVal = resolveValues(o, cd, _fields, objectClassMap);
            _tempVal.addAll(fromStrings(cd.get_vals()));

            CompareObject<T> co = new CompareObject<>(tempVal, cd.getLogic(), cd.getOperator(), _tempVal, cd.get_logic(), checkNull);
            info(co.toString());
            result.add(getComparator().compare(co));
        }
        return cd.getLogic().operate(result);
    }

    private List<T> resolveValues(Object o, CheckDefinition cd, List<FieldPath> fields, Map<Class, List<Object>> objectClassMap) {
        List<T> values = new ArrayList<>();
        for (FieldPath path : fields) {
            if (path.isMain()) {
                values.add(parseObject(getFieldValue(o, path.getPath())));
            } else {
                values.addAll(fromObjects(getFieldValues(objectClassMap.get(path.getClazz()), path.getPath())));
            }
        }
        values.addAll(fromStrings(cd.getVals()));
        values = compute(values, cd.getCmpt());

        return values;
    }

//    private boolean checkByRelated(CheckDefinition cd, Map<Class, List<Object>> objectClassMap) {
//        List<FieldPath> fields = cd.getFields();
//        List<FieldPath> _fields = cd.get_fields();
//
//        Optional<FieldPath> fieldPath = CollectionUtils.findFirstMatch(fields, FieldPath::isMain);
//        if (!fieldPath.isPresent())
//            throw new RuntimeException("error");
//        Class mainClass = fieldPath.get().getClass();
//        List<Object> objects = objectClassMap.get(mainClass);
//
//        List<T> tempVal = new ArrayList<>();
//        List<T> _tempVal = new ArrayList<>();
//        for (FieldPath path : fields) {
//            List<Object> objs = objectClassMap.get(path.getClazz());
//            tempVal.addAll(fromObjects(getFieldValues(objs, path.getPath())));
//            tempVal = compute(tempVal, cd.getCmpt());
//        }
//        tempVal.addAll(compute(fromStrings(cd.getVals()), cd.getCmpt()));
//        tempVal = compute(tempVal, cd.getCmpt());
//
//        List<Boolean> result = new ArrayList<>();
//        for (Object o : objects) {
//            List<T> tempVal = resolveValues(o, cd, fields, objectClassMap);
//            List<T> _tempVal = resolveValues(o, cd, _fields, objectClassMap);
//
//            CompareObject<T> co = new CompareObject<>(tempVal, cd.getValLogic(), cd.getOperator(), _tempVal, cd.get_valLogic(), checkNull);
//            info(co.toString());
//            result.add(getComparator().compare(co));
//        }
//        return cd.getLogic().operate(result);
//    }

    @SuppressWarnings("unchecked")
    private List<T> compute(List<T> val, MultivariateOperator mo) {
        Object result = mo.operate(val);
        if (mo instanceof AggregateOperator) {
            return Lists.newArrayList((T) result);
        } else {
            return (List<T>) result;
        }
    }

    /**
     * fixme: 确保返回一个新的map
     * @param cd
     * @param objectClassMap
     * @return
     */
    @Override
    public Map<Class, List<Object>> filter(CheckDefinition cd, Map<Class, List<Object>> objectClassMap) {
        setCheckNull(false);

        List<FieldPath> fields = cd.getFields();
        List<FieldPath> _fields = cd.get_fields();

        Optional<FieldPath> fieldPath = CollectionUtils.findFirstMatch(fields, FieldPath::isMain);
        if (!fieldPath.isPresent())
            throw new RuntimeException("error");
        Class mainClass = fieldPath.get().getClass();
        List<Object> objects = objectClassMap.get(mainClass);

        List<Object> result = new ArrayList<>();
        for (Object o : objects) {
            List<T> tempVal = resolveValues(o, cd, fields, objectClassMap);
            List<T> _tempVal = resolveValues(o, cd, _fields, objectClassMap);

            CompareObject<T> co = new CompareObject<>(tempVal, cd.getLogic(), cd.getOperator(), _tempVal, cd.get_logic(), checkNull);
            info(co.toString());
            if (getComparator().compare(co))
                result.add(o);
        }
        objectClassMap.put(mainClass, result);
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

    private List<Object> getFieldValues(List<Object> checkObj, String[] path) {
        return checkObj.stream().map(o -> getFieldValue(o, path)).collect(Collectors.toList());
    }

    protected abstract T parseObject(Object o);

    private List<T> fromObjects(List<Object> objects) {
        return objects.stream().map(this::parseObject).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private List<T> fromObject(Object o) {
        return Lists.newArrayList(parseObject(o));
    }

    protected abstract T parseString(String s);

    private List<T> fromString(String s) {
        return Lists.newArrayList(parseString(s));
    }

    private List<T> fromStrings(List<String> strings) {
        return strings.stream().map(this::parseString).filter(o -> o != null).collect(Collectors.toList());
    }

    protected boolean setCheckNull(boolean b) {
        return this.checkNull = b;
    }

}
