package com.company.enums;

/**
 * Created by henry on 15/11/4.
 */
public enum Operator {

    LARGER_THAN(">", " must be larger than "),
    LARGER_OR_EQUAL(">=", " must be larger than or equal to "),
    EQUAL("==", " must be equal to "),
    LESS_OR_EQUAL("l=", " must be less than or equal to "),
    LESS_THAN("l", " must be less than "),
    NOT_EQUAL("!=", " must not be equal to "),
    IN("in", " must be one of "),
    NOT_IN("not", " must not be any of "),
    BETWEEN("between", ""),
    CHANGE("change", ""),
    SUM("sum", ""),
    PRODUCT("product", ""),
    INTERSECT("intersect", ""),
    CONTAINS_KEY("key", " contains key "),
    CONTAINS_KEY_VALUE("kv", " must contains kv pairs "),
    BEFORE("before", ""),
    AFTER("between", ""),
    FLOATPOINT("fp", ""),
    UNKNOWN("", "");


    private String value;
    private String desc;

    Operator(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static Operator getOperator(String operator) {
        if (operator == null)
            return null;
        String str = operator.toLowerCase();
        for (Operator e : values()) {
            if (str.equals("=>"))
                str = ">=";
            if (str.equals("=l"))
                str = "l=";
            if (e.getValue().equals(str))
                return e;
        }
        return UNKNOWN;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static Operator LARGER_THAN() {
        return LARGER_THAN;
    }
}
