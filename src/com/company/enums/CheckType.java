package com.company.enums;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.company.enums.Operator.*;

/**
 * Created by henry on 15/11/10.
 */
public enum CheckType {

    OBJECT("object", Object.class),
    NUMBER("number", Number.class,
            LARGER_THAN,
            LARGER_OR_EQUAL,
            EQUAL,
            LESS_OR_EQUAL,
            LESS_THAN,
            NOT_EQUAL,
            IN,
            NOT_IN,
            BETWEEN,
            SUM,
            PRODUCT,
            INTERSECT),
    STRING("string", String.class,
            EQUAL,
            NOT_EQUAL,
            IN,
            NOT_IN,
            INTERSECT),
    BOOLEAN("boolean", Boolean.class,
            EQUAL,
            NOT_EQUAL),
    DATE("date", Date.class),
    LIST("list", List.class),
    MAP("map", Map.class),
    UNKNOWN("", null);

    private String name;
    private Class clazz;
    private Operator[] supportedOperators;

    CheckType(String name, Class clazz, Operator... operators) {
        this.name = name;
        this.clazz = clazz;
        this.supportedOperators = operators;
    }

    public static CheckType fromName(String name) {
        if (name == null)
            return null;
        for (CheckType e : values())
            if (e.getName().equals(name.toLowerCase()))
                return e;
        return UNKNOWN;
    }

    public static CheckType fromClass(Class<?> clazz) {
        for (CheckType e : values())
            if (e.getClazz().equals(clazz))
                return e;
        return null;
    }

    public String getName() {
        return name;
    }

    public Class getClazz() {
        return clazz;
    }

    public Operator[] getSupportedOperators() {
        return supportedOperators;
    }
}
