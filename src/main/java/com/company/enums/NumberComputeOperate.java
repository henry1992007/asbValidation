package com.company.enums;

import com.company.ComputeOperator;
import com.company.EntityAttribute;

import java.math.BigDecimal;

/**
 * Created by henry on 15/11/21.
 */
public enum NumberComputeOperate implements ComputeOperator<BigDecimal>, EntityAttribute {

    SUM("sum") {
        public BigDecimal[] operate(BigDecimal... var) {
            BigDecimal[] res = new BigDecimal[]{new BigDecimal(0)};
            for (BigDecimal b : var)
                res[0] = res[0].add(b);
            return res;
        }
    },
    PRODUCT("product") {
        public BigDecimal[] operate(BigDecimal... var) {
            BigDecimal[] res = new BigDecimal[]{new BigDecimal(0)};
            for (BigDecimal b : var)
                res[0] = res[0].multiply(b);
            return res;
        }
    },
    MAX("max") {
        public BigDecimal[] operate(BigDecimal... var) {
            BigDecimal[] res = new BigDecimal[]{var[0]};
            for (BigDecimal b : var)
                if (b.compareTo(res[0]) > 0)
                    res[0] = b;
            return res;
        }
    },
    MIN("min") {
        public BigDecimal[] operate(BigDecimal... var) {
            BigDecimal[] res = new BigDecimal[]{var[0]};
            for (BigDecimal b : var)
                if (b.compareTo(res[0]) < 0)
                    res[0] = b;
            return res;
        }
    },
    BIG("big") {
        @Override
        public BigDecimal[] operate(BigDecimal... var) {
            BigDecimal[] res = new BigDecimal[var.length];
            for (int i = 0; i < var.length; i++) {
                res[i] = new BigDecimal(0);
                String s = String.valueOf(var[0].doubleValue());
                if (s.contains("."))
                    res[i] = new BigDecimal(s.length() - s.indexOf(".") - 1);
            }
            return res;
        }
    },
    DEFAULT("unknown") {
        @Override
        public BigDecimal[] operate(BigDecimal... var) {
            return var;
        }
    };


    private String name;

    NumberComputeOperate(String name) {
        this.name = name;
    }

    public static NumberComputeOperate fromName(String name) {
        for (NumberComputeOperate e : values())
            if (e.getName().equals(name))
                return e;
        return null;
    }

    public static NumberComputeOperate getDefault() {
        return DEFAULT;
    }

    public String getName() {
        return name;
    }

}
