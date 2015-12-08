package com.company.enums;

import com.company.LogicOperator;

import java.util.Arrays;

/**
 * Created by henry on 15/11/19.
 */
public enum LogicComputeOperator implements LogicOperator {

    AND("and", "&&", "&") {
        public Boolean compute(Boolean... var) {
            for (boolean b : var)
                if (!b) return false;
            return true;
        }
    },
    OR("or", "||", "|") {
        public Boolean compute(Boolean... var) {
            for (boolean b : var)
                if (b) return true;
            return false;
        }
    },
    XOR("xor") {
        public Boolean compute(Boolean... var) {
            for (boolean b : var)
                if (var[0] != b) return true;
            return false;
        }
    },
    UNKNOWN("") {
        @Override
        public Boolean compute(Boolean... var) {
            return null;
        }
    };

    private String[] values;

    LogicComputeOperator(String... values) {
        this.values = values;
    }

    public static LogicComputeOperator fromValue(String value) {
        if (value == null)
            return null;
        for (LogicComputeOperator e : values())
            if (Arrays.asList(e.getValue()).contains(value.toLowerCase()))
                return e;
        return UNKNOWN;
    }


    public String[] getValue() {
        return values;
    }
}
