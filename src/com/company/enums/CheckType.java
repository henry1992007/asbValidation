package com.company.enums;

import com.company.MapValidator;
import com.company.validations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.company.enums.Operator.*;

/**
 * Created by henry on 15/11/10.
 */
public enum CheckType {

    OBJECT("object", null),
    NUMBER("number", new NumberValidator(),
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
    STRING("string", new StringValidator(),
            EQUAL,
            NOT_EQUAL,
            IN,
            NOT_IN,
            INTERSECT),
    BOOLEAN("boolean", new BooleanValidator(),
            EQUAL,
            NOT_EQUAL),
    DATE("date", new DateValidator()),
    LIST("list", new ListValidator()),
    MAP("map", new MapValidator()),
    UNKNOWN("", null);

    private String name;
    //    private Class clazz;
    private TypeValidator typeValidator;
    private Operator[] supportedOperators;

    CheckType(String name, TypeValidator typeValidator, Operator... operators) {
        this.name = name;
        this.typeValidator = typeValidator;
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

//    public static CheckType fromClass(Class<?> clazz) {
//        for (CheckType e : values())
//            if (e.getClazz().equals(clazz))
//                return e;
//        return null;
//    }

    public String getName() {
        return name;
    }

//    public Class getClazz() {
//        return clazz;
//    }

    public TypeValidator getTypeValidator() {
        return typeValidator;
    }

    public Operator[] getSupportedOperators() {
        return supportedOperators;
    }

}
