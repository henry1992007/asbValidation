package com.company.enums;

import com.company.ComputeOperator;
import com.company.LogicOperator;
import com.company.OperatorAttribute;

import java.util.Arrays;

/**
 * Created by henry on 15/11/19.
 */
public enum LogicComputeOperator implements ComputeOperator<Boolean>, OperatorAttribute {

    AND("and", "&&", "&") {
        public Boolean[] operate(Boolean... var) {
            for (boolean b : var)
                if (!b) return getRes(false);
            return getRes(true);
        }

        public ComputeOperator getdefault() {
            return this;
        }
    },
    OR("or", "||", "|") {
        public Boolean[] operate(Boolean... var) {
            for (boolean b : var)
                if (b) return getRes(true);
            return getRes(false);
        }

        @Override
        public ComputeOperator getdefault() {
            return AND;
        }
    },
    XOR("xor") {
        public Boolean[] operate(Boolean... var) {
            for (boolean b : var)
                if (var[0] != b) return getRes(true);
            return getRes(false);
        }
    };

    private String[] names;

    LogicComputeOperator(String... names) {
        this.names = names;
    }

    public static LogicComputeOperator fromName(String name) {
        for (LogicComputeOperator e : values())
            if (Arrays.asList(e.getNames()).contains(name.toLowerCase()))
                return e;
        return null;
    }

    public static LogicComputeOperator getDefault() {
        return AND;
    }


    public String[] getNames() {
        return names;
    }

    private static Boolean[] getRes(boolean b) {
        return new Boolean[]{b};
    }
}
