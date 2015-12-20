package com.company.enums;

import com.company.ComputeOperator;
import com.company.CommonEnum;
import com.company.validations.MapValidator;
import com.company.validations.*;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

import static com.company.enums.Operator.*;

/**
 * Created by henry on 15/11/10.
 */
public enum CheckType implements CommonEnum {

    NUMBER("number", new NumberValidator(), NumberComputeOperator.values(),
            LARGER_THAN,
            LARGER_OR_EQUAL,
            LESS_OR_EQUAL,
            LESS_THAN,
            IN,
            NOT_IN,
            BETWEEN,
            INTERSECT),
    STRING("string", new StringValidator(), StringComputeOperator.values(),
            IN,
            NOT_IN,
            INTERSECT),
    BOOLEAN("boolean", new BooleanValidator(), new ComputeOperator[]{}),
    DATE("date", new DateValidator(), new ComputeOperator[]{},
            BEFORE,
            AFTER,
            BETWEEN),
    LIST("list", new ListValidator(), new ComputeOperator[]{}),
    MAP("map", new MapValidator(), new ComputeOperator[]{}),
    UNKNOWN("", null, new ComputeOperator[]{});

    private String name;
    private TypeValidator typeValidator;
    private List<Operator> supportedOperators = Lists.newArrayList(EQUAL, NOT_EQUAL);
    private List<ComputeOperator> computeOperators;

    CheckType(String name, TypeValidator typeValidator, ComputeOperator[] computeOperators, Operator... operators) {
        this.name = name;
        this.typeValidator = typeValidator;
        supportedOperators.addAll(Arrays.asList(operators));
        this.computeOperators = Arrays.asList(computeOperators);
    }


    public String getName() {
        return name;
    }

    public static CheckType fromName(String name) {
        for (CheckType e : values())
            if (e.getName().equals(name))
                return e;
        return UNKNOWN;
    }

    public TypeValidator getTypeValidator() {
        return typeValidator;
    }

    public List<Operator> getSupportedOperators() {
        return supportedOperators;
    }

    public List<ComputeOperator> getComputeOperators() {
        return computeOperators;
    }

    public CommonEnum[] getValues() {
        return values();
    }

    public CommonEnum getDefault() {
        return UNKNOWN;
    }
}
