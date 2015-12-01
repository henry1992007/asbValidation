package com.company.validations;

import com.company.element.ConditionValidateObject;
import com.company.enums.LogicComputeOperator;
import com.company.utils.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 15/11/5.
 */
public class NumberValidator implements TypeValidator {

    private static final String CONDITION_VALUE = "conditionValue";

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
