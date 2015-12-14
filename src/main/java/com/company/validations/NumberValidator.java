package com.company.validations;

import com.company.CompareObject;
import com.company.element.CheckDefinition;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.ReflectUtils;
import com.company.utils.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by henry on 15/11/5.
 */
public class NumberValidator implements TypeValidator {

    private static final String CONDITION_VALUE = "conditionValue";

    private NumberComparator comparator;

    public boolean validate(CheckDefinition cd, Map<Class, Set<Object>> objectClassMap) {
        Map<Class, String[]> fields = cd.getFields();
        Map<String, Object> fieldValues = new HashMap<>();
        for (Class clazz : fields.keySet())
            for (Object obj : objectClassMap.get(clazz))
                fieldValues.put(Arrays.toString(fields.get(clazz)), getFieldValue(obj, fields.get(clazz)));

        Map<Class, String[]> _fields = cd.get_fields();
        Map<String, Object> _fieldValues = new HashMap<>();
        for (Class clazz : _fields.keySet())
            for (Object obj : objectClassMap.get(clazz))
                _fieldValues.put(Arrays.toString(_fields.get(clazz)), getFieldValue(obj, _fields.get(clazz)));

        List<BigDecimal> val = parseObject(fieldValues.values());
        val.addAll(parseString(cd.getVals()));
        List<BigDecimal> _val = parseObject(_fieldValues.values());
        _val.addAll(parseString(cd.get_vals()));


        CompareObject<BigDecimal> co = new CompareObject<>(val, cd.getLogic(), cd.getOperator(), _val, cd.get_logic());
        if (comparator == null)
            comparator = new NumberComparator();
        return comparator.doCompare(co);
    }

    public Map<Class, Set<Object>> filter(CheckDefinition cd, Map<Class, Set<Object>> objectClassMap) {
        for (Class clazz : objectClassMap.keySet()) {
            List<Object> objects = Lists.newArrayList(objectClassMap.get(clazz));
            Iterator i = objects.iterator();
            while (i.hasNext()) {
//                Object o = i.next();
//                BigDecimal[] val = CollectionUtils.listsToArray(parseObject(new Object[]{getFieldValue(o, cd.getFields()[0].getFields())}));
//                BigDecimal[] _val = CollectionUtils.listsToArray(parseString(cd.get_vals()));
//                CompareObject<BigDecimal> co = new CompareObject<>(val, cd.getOperator(), _val);
//                if (comparator==null)
//                    comparator = new NumberComparator();
//                if (!comparator.doCompare(co)) {
//                    i.remove();
//                }
            }
            objectClassMap.put(clazz, Sets.newHashSet(objects));
        }

        return objectClassMap;
    }

    private List<BigDecimal> parseObject(Collection<Object> list) {
        List<BigDecimal> res = new ArrayList<>();
        for (Object o : list) {
            if (!o.getClass().equals(BigDecimal.class))
                o = new BigDecimal(((Number) o).doubleValue());
            res.add((BigDecimal) o);
        }
        return res;
    }

    private List<BigDecimal> parseString(List<String> list) {
        return list.stream().map(BigDecimal::new).collect(Collectors.toList());
    }

    private Object getFieldValue(Object checkObj, String[] path) {
        Object target = checkObj;
        String getMethodName = "";

        try {
            for (int i = 0; i < path.length; i++) {
                if (ReflectUtils.isMapClass(target.getClass())) {
                    target = ((Map) target).get(path[i]);
                } else {
                    try {
                        target = target.getClass().getField(path[i]).get(target);
                    } catch (NoSuchFieldException e) {
                        getMethodName = "get" + StringUtils.firstToCapital(path[i]);
                        target = target.getClass().getMethod(getMethodName).invoke(target);
                    }
                }
            }

            return target;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Assert.runtimeException("couldn't access field " + Arrays.toString(path) + " of object Class: " + checkObj.getClass());
        }

        return null;
    }

//    private Map<String, Comparable> getCompareValues(String[] conditionValueStrs, Map<String, Object> otherFieldValues, NumberParse converter) {
//        Map<String, Comparable> compareValues = new HashMap<>();
//        for (int i = 0; i < conditionValueStrs.length; i++) {
//            if (conditionValueStrs[i].isEmpty())
//                continue;
//            compareValues.put(CONDITION_VALUE + (i + 1), (Comparable) converter.parse(conditionValueStrs.[i]);
//        }
//        if (otherFieldValues != null && !otherFieldValues.isEmpty()) {
//            for (String s : otherFieldValues.keySet()) {
//                compareValues.put(s, (Comparable) otherFieldValues.get(s));
//            }
//        }
//
//        return compareValues;
//    }

    private String getMapDesc(Map<String, Comparable> map) {
        StringBuilder sb = new StringBuilder("[");

        boolean flag = true;
        for (String s : map.keySet()) {
            if (!flag)
                sb.append(",");
            flag = false;
            sb.append(s.contains(CONDITION_VALUE) ? CONDITION_VALUE : "\"" + s + "\"").append(":").append(map.get(s));
        }
        sb.append("]");

        return sb.toString();
    }

}
