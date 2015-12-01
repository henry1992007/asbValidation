package com.company.enums;


import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.company.enums.Operator.*;

/**
 * Created by henry on 15/11/10.
 */
public enum SupportedType {

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
    MAP("map", Map.class);

    private String name;
    private Class clazz;
    private Operator[] supportedOperators;

    SupportedType(String name, Class clazz, Operator... operators) {
        this.name = name;
        this.clazz = clazz;
        this.supportedOperators = operators;
    }

    public static SupportedType fromName(String name) {
        for (SupportedType e : values())
            if (e.getName().equals(name))
                return e;
        return null;
    }

    public static SupportedType fromClass(Class<?> clazz) {
        for (SupportedType e : values())
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
