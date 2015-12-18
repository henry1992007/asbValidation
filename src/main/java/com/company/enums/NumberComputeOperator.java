package com.company.enums;

import com.company.ComputeOperator;
import com.company.EntityAttribute;
import com.company.utils.CollectionUtils;
import com.company.validations.FunctionXY;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

/**
 * Created by henry on 15/11/21.
 */
public enum NumberComputeOperator implements ComputeOperator<BigDecimal>, EntityAttribute {

    SUM("sum") {
        public List<BigDecimal> operate(List<BigDecimal> var) {
            return CollectionUtils.reduce(var, BigDecimal::add);
        }
    },
    PRODUCT("product") {
        public List<BigDecimal> operate(List<BigDecimal> var) {
            return CollectionUtils.reduce(var, BigDecimal::multiply);
        }
    },
    MAX("max") {
        public List<BigDecimal> operate(List<BigDecimal> var) {
            return CollectionUtils.reduce(var, BigDecimal::max);
        }
    },
    MIN("min") {
        public List<BigDecimal> operate(List<BigDecimal> var) {
            return CollectionUtils.reduce(var, BigDecimal::min);
        }
    },
    FLOAT("float") {
        public List<BigDecimal> operate(List<BigDecimal> var) {
            return CollectionUtils.map(var, bigDecimal -> new BigDecimal(bigDecimal.scale()));
        }
    },
    DEFAULT("unknown") {
        @Override
        public List<BigDecimal> operate(List<BigDecimal> var) {
            return CollectionUtils.map(var, Function.<BigDecimal>identity());
        }
    };


    private String name;

    NumberComputeOperator(String name) {
        this.name = name;
    }

    public static NumberComputeOperator fromName(String name) {
        for (NumberComputeOperator e : values())
            if (e.getName().equals(name))
                return e;
        return null;
    }

    public static NumberComputeOperator getDefault() {
        return DEFAULT;
    }

    public String getName() {
        return name;
    }

}
