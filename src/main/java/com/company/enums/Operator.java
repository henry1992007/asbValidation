package com.company.enums;

/**
 * Created by henry on 15/11/4.
 */
public enum Operator {

    LARGER_THAN(">", " must be larger than "),
    LARGER_OR_EQUAL(">=", " must be larger than or equal to "),
    EQUAL("=", " must be equal to "),
    LESS_OR_EQUAL("lt=", " must be less than or equal to "),
    LESS_THAN("lt", " must be less than "),
    NOT_EQUAL("!=", " must not be equal to "),
    IN("in", " must be one of "),
    NOT_IN("not", " must not be any of "),
    BETWEEN("between", ""),
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

    public static Operator fromName(String operator) {
        for (Operator e : values())
            if (e.getValue().equals(operator.toLowerCase()))
                return e;
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
