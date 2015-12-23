package com.company.enums;

import com.company.AssociativeOperator;
import com.company.CommonEnum;
import com.company.validations.MapValidator;
import com.company.validations.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.company.enums.Operator.*;

/**
 * Created by henry on 15/11/10.
 */
public enum CheckType implements CommonEnum {

    NUMBER("number", new NumberValidator(),
            Lists.newArrayList(
                    "sum",
                    "product",
                    "max",
                    "min",
                    "average",
                    "float"),
            Lists.newArrayList(
                    LARGER_THAN,
                    LARGER_OR_EQUAL,
                    LESS_OR_EQUAL,
                    LESS_THAN,
                    IN,
                    NOT_IN,
                    BETWEEN,
                    INTERSECT)),
    STRING("string", new StringValidator(),
            Lists.newArrayList(
                    "lower",
                    "upper",
                    "fcap"),
            Lists.newArrayList(
                    IN,
                    NOT_IN,
                    INTERSECT)),
    BOOLEAN("boolean", new BooleanValidator(),
            Lists.newArrayList(
                    "reverse"),
            new ArrayList<Operator>()),
    DATE("date", new DateValidator(),
            new ArrayList<String>(),
            Lists.newArrayList(
                    BEFORE,
                    AFTER,
                    BETWEEN));
//    LIST("list", new ListValidator(), new ArrayList<String>()),
//    MAP("map", new MapValidator(), new ArrayList<String>()),

    private String name;
    private TypeValidator typeValidator;
    private List<Operator> supportedOperators = Lists.newArrayList(EQUAL, NOT_EQUAL);
    private List<AssociativeOperator> associativeOperators;
    private List<String> cmpt;

//    CheckType(String name, TypeValidator typeValidator, AssociativeOperator[] associativeOperators, Operator... operators) {
//        this.name = name;
//        this.typeValidator = typeValidator;
//        supportedOperators.addAll(Arrays.asList(operators));
//        this.associativeOperators = Arrays.asList(associativeOperators);
//    }

    CheckType(String name, TypeValidator typeValidator, List<String> list, List<Operator> operators) {
        this.name = name;
        this.typeValidator = typeValidator;
        this.supportedOperators.addAll(operators);
        this.cmpt = list;
    }

    public List<String> getCmpt() {
        return cmpt;
    }

    public String getName() {
        return name;
    }

    public static CheckType fromName(String name) {
        for (CheckType e : values())
            if (e.getName().equals(name))
                return e;
        return null;
    }

    public TypeValidator getTypeValidator() {
        return typeValidator;
    }

    public List<Operator> getSupportedOperators() {
        return supportedOperators;
    }

    public List<AssociativeOperator> getAssociativeOperators() {
        return associativeOperators;
    }

    public CommonEnum[] getValues() {
        return values();
    }

}
