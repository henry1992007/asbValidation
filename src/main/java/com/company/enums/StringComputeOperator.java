package com.company.enums;

import com.company.ComputeOperator;
import com.company.utils.CollectionUtils;
import com.company.utils.StringUtils;

import java.util.List;

/**
 * Created by henry on 15/12/15.
 */
public enum StringComputeOperator implements ComputeOperator<String> {

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

    StringComputeOperator(String name) {
        this.name = name;
    }

    public static StringComputeOperator fromName(String name) {
        for (StringComputeOperator o:values())
            if (o.getName().equals(name))
                return o;
        return null;
    }

    public String getName() {
        return name;
    }
}
