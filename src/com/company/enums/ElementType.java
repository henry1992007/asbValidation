package com.company.enums;

/**
 * Created by henry on 15/11/14.
 */
public enum ElementType {

    ClASS("class"),
    CONDITION("condition"),
    VALIDATION("validation"),
    NUMBER("number"),
    STRING("string"),
    BOOLEAN("boolean"),
    DATE("date"),
    LIST("list"),
    MAP("map");


    ElementType(String value) {
        this.value = value;
    }

    public static ElementType fromString(String value) {
        for (ElementType e : values())
            if (e.getValue().equals(value.toLowerCase()))
                return e;
        return null;
    }

    private String value;

    public String getValue() {
        return value;
    }

}
