package com.company.enums;

import com.company.AssociativeOperator;
import com.company.OperatorAttribute;
import com.company.utils.CollectionUtils;

import java.util.List;

/**
 * Created by henry on 15/11/19.
 */
public enum LogicAssociativeOperator implements AssociativeOperator<Boolean>, OperatorAttribute {

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
            return
        }
    };

    private String name;

    LogicAssociativeOperator(String name) {
        this.name = name;
    }

    public static LogicAssociativeOperator fromName(String name) {
        for (LogicAssociativeOperator e : values())
            if (e.getName().equals(name.toLowerCase()))
                return e;
        return null;
    }

    public static LogicAssociativeOperator getDefault() {
        return AND;
    }

    public String getName() {
        return name;
    }

}
