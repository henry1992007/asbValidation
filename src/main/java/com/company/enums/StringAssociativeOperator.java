package com.company.enums;

import com.company.AssociativeOperator;
import com.company.utils.CollectionUtils;

import java.util.List;

/**
 * Created by henry on 15/12/15.
 */
public enum StringAssociativeOperator implements AssociativeOperator<String> {

    LOWER("lower") {
        public List<String> operate(List<String> var) {
            return CollectionUtils.map(var, String::toLowerCase);
        }
    },
    UPPER("upper") {
        public List<String> operate(List<String> var) {
            return CollectionUtils.map(var, String::toUpperCase);
        }
    };

    private String name;

    StringAssociativeOperator(String name) {
        this.name = name;
    }

    public static StringAssociativeOperator fromName(String name) {
        for (StringAssociativeOperator o:values())
            if (o.getName().equals(name))
                return o;
        return null;
    }

    public String getName() {
        return name;
    }
}
