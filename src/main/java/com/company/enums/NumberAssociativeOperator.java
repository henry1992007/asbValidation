package com.company.enums;

import com.company.AssociativeOperator;
import com.company.CommonEnum;
import com.company.utils.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Created by henry on 15/11/21.
 */
public enum NumberAssociativeOperator implements AssociativeOperator<BigDecimal>, CommonEnum {

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
    AVERAGE("average") {
        public List<BigDecimal> operate(List<BigDecimal> var) {
            return Collections.singletonList(new BigDecimal(Double.toString(SUM.operate(var).get(0).doubleValue() / var.size())));
        }
    },
    FLOAT("float") {
        public List<BigDecimal> operate(List<BigDecimal> var) {
            return CollectionUtils.map(var, bigDecimal -> new BigDecimal(bigDecimal.scale()));
        }
    },
    DEFAULT("unknown") {
        public List<BigDecimal> operate(List<BigDecimal> var) {
            return CollectionUtils.map(var, Function.<BigDecimal>identity());
        }
    };

    private String name;

    NumberAssociativeOperator(String name) {
        this.name = name;
    }

    public static NumberAssociativeOperator fromName(String name) {
        for (NumberAssociativeOperator e : values())
            if (e.getName().equals(name))
                return e;
        return null;
    }

    public static NumberAssociativeOperator getDefault() {
        return DEFAULT;
    }

    public String getName() {
        return name;
    }

}
