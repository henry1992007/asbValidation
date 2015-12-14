package com.company.enums;

import com.company.ComputeOperator;
import com.company.Operatable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by henry on 15/12/13.
 */
public abstract class LogicComputeOperator2 implements ComputeOperator<Boolean> {

    private static Map<String, LogicComputeOperator2> values = new HashMap<>();

    public static LogicComputeOperator2 AND = new LogicComputeOperator2() {
        @Override
        public Boolean[] operate(Boolean... var) {
            for (boolean b : var)
                if (b) return getRes(true);
            return getRes(false);
        }
    };

    public static LogicComputeOperator2 OR = new LogicComputeOperator2() {
        @Override
        public Boolean[] operate(Boolean... var) {
            for (boolean b : var)
                if (b) return getRes(true);
            return getRes(false);
        }
    };

    public static LogicComputeOperator2 XOR = new LogicComputeOperator2() {
        public Boolean[] operate(Boolean... var) {
            for (boolean b : var)
                if (var[0] != b) return getRes(true);
            return getRes(false);
        }
    };

//    public LogicComputeOperator2(String name) {
//        this.name = name;
//    }

//    private String name;

    public static LogicComputeOperator2 fromName(String name) {
        return values.get(name);
    }

//    public String getName() {
//        return name;
//    }

    private static Boolean[] getRes(boolean b) {
        return new Boolean[]{b};
    }

    public static LogicComputeOperator2 getDefault() {
        return AND;
    }

    static {
        values.put("and", AND);
        values.put("or", OR);
        values.put("xor", XOR);
    }

}
