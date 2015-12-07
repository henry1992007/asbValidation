package com.company.validations;

import com.company.CompareObject;
import com.company.element.CheckDefinition;
import com.company.element.ConditionField;
import com.company.element.ConditionValidateObject;
import com.company.enums.LogicComputeOperator;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.ReflectUtils;
import com.company.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by henry on 15/11/5.
 */
public class NumberValidator implements TypeValidator {

    private static final String CONDITION_VALUE = "conditionValue";

    private final static NumberComparator comparator = new NumberComparator();

    public List<String> validate(ConditionValidateObject cvo) {
        List<String> validateResult = new ArrayList<>();

        List<BigDecimal> fieldValues = parseObject(cvo.getFieldObjs().values().toArray());
        List<BigDecimal> values = parseString(cvo.getConditionStrs());
        List<BigDecimal> _fieldValues = parseObject(cvo.get_fieldObjs().values().toArray());
        List<BigDecimal> _values = parseString(cvo.get_conditionStrs());

        NumberComparator comparator = new NumberComparator();
        comparator.compare(CollectionUtils.listsToArray(fieldValues, values), LogicComputeOperator.AND, cvo.getOperator(), CollectionUtils.listsToArray(_fieldValues, _values), LogicComputeOperator.OR);

        return validateResult;
    }

    public Map<String, Set<Object>> validate(CheckDefinition cd, Map<Class, Set<Object>> objectClassMap) {
        ConditionField[] fields = cd.getFields();
        Map<String, Object> fieldValues = new HashMap<>();
        for (ConditionField conditionField : fields)
            for (Object obj : objectClassMap.get(conditionField.getClazz()))
                fieldValues.put(conditionField.toString(), getFieldValue(obj, conditionField.getFields()));

        ConditionField[] _fields = cd.get_fields();
        Map<String, Object> _fieldValues = new HashMap<>();
        for (ConditionField conditionField : _fields)
            for (Object obj : objectClassMap.get(conditionField.getClazz()))
                _fieldValues.put(conditionField.toString(), getFieldValue(obj, conditionField.getFields()));

        String[] vals = cd.getVals();
        String[] _vals = cd.get_vals();


        BigDecimal[] val = CollectionUtils.listsToArray(parseObject(fieldValues.values().toArray()), parseString(vals));
        BigDecimal[] _val = CollectionUtils.listsToArray(parseObject(_fieldValues.values().toArray()), parseString(_vals));

        CompareObject<BigDecimal> co = new CompareObject<>(val, cd.getLogic(), cd.getOperator(), _val, cd.get_logic());
        comparator.doCompare(co);
    }

    private List<BigDecimal> parseObject(Object[] list) {
        List<BigDecimal> res = new ArrayList<>();
        for (Object o : list) {
            if (!o.getClass().equals(BigDecimal.class))
                o = new BigDecimal(((Number) o).doubleValue());
            res.add((BigDecimal) o);
        }

        return res;
    }

    private List<BigDecimal> parseString(String[] list) {
        List<BigDecimal> res = new ArrayList<>();
        for (String s : list)
            res.add(new BigDecimal(s));

        return res;
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
