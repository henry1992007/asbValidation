package com.company.enums;

import com.company.ComputeOperator;
import com.company.LogicOperator;
import com.company.OperatorAttribute;
import com.company.utils.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by henry on 15/11/19.
 */
public enum LogicComputeOperator implements ComputeOperator<Boolean>, OperatorAttribute {

    AND("and") {
        public List<Boolean> operate(List<Boolean> var) {
            return CollectionUtils.reduce(var, Boolean::logicalAnd);
        }
    },
    OR("or") {
        public List<Boolean> operate(List<Boolean> var) {
            return CollectionUtils.reduce(var, Boolean::logicalOr);
        }
    },
    XOR("xor") {
        public List<Boolean> operate(List<Boolean> var) {
            return Collections.singletonList(!CollectionUtils.equal(var));
        }
    };

    private String name;

    LogicComputeOperator(String name) {
        this.name = name;
    }

    public static LogicComputeOperator fromName(String name) {
        for (LogicComputeOperator e : values())
            if (e.getName().equals(name.toLowerCase()))
                return e;
        return null;
    }

    public static LogicComputeOperator getDefault() {
        return AND;
    }


    public String getName() {
        return name;
    }

}
