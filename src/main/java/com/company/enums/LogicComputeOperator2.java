package com.company.enums;

import com.company.AssociativeOperator;
import com.company.MappingOperator;
import com.company.utils.CollectionUtils;
import com.company.utils.StringUtils;
import com.company.validations.MultivariateOperator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by henry on 15/12/13.
 */
public abstract class LogicComputeOperator2 {

    public LogicComputeOperator2() {
    }

    private static Map<String, MultivariateOperator> operators = new HashMap<>();

    private static AssociativeOperator<Boolean> AND = v -> CollectionUtils.reduce(v, Boolean::logicalAnd);
    private static AssociativeOperator<Boolean> OR = var -> CollectionUtils.reduce(var, Boolean::logicalOr);
    private static AssociativeOperator<Boolean> XOR = CollectionUtils::xor;


    private static AssociativeOperator<BigDecimal> SUM = var -> CollectionUtils.reduce(var, BigDecimal::add);
    private static AssociativeOperator<BigDecimal> PRODUCT = var -> CollectionUtils.reduce(var, BigDecimal::multiply);
    private static AssociativeOperator<BigDecimal> MAX = var -> CollectionUtils.reduce(var, BigDecimal::max);
    private static AssociativeOperator<BigDecimal> MIN = var -> CollectionUtils.reduce(var, BigDecimal::min);
    private static AssociativeOperator<BigDecimal> AVERAGE = var -> SUM.operate(var).divide(new BigDecimal(var.size()), 3, BigDecimal.ROUND_FLOOR);
    private static MappingOperator<BigDecimal> FLOAT = var -> CollectionUtils.map(var, bigDecimal -> new BigDecimal(bigDecimal.scale()));


    private static MappingOperator<String> LOWER = var -> CollectionUtils.map(var, String::toLowerCase);
    private static MappingOperator<String> UPPER = var -> CollectionUtils.map(var, String::toUpperCase);
    private static MappingOperator<String> FCAP = var -> CollectionUtils.map(var, StringUtils::firstToCapital);

    private static MappingOperator DEFAULT = var -> CollectionUtils.map(var, Function.identity());

    public static MultivariateOperator get(String name) {
        return operators.get(name);
    }

    public static MultivariateOperator getDefault() {
        return operators.get("default");
    }

    public static void extend(String key, MultivariateOperator operator) {
        operators.put(key, operator);
    }


    static {
        operators.put("and", AND);
        operators.put("or", OR);
        operators.put("xor", XOR);
        operators.put("sum", SUM);
        operators.put("product", PRODUCT);
        operators.put("max", MAX);
        operators.put("min", MIN);
        operators.put("average", AVERAGE);
        operators.put("float", FLOAT);
        operators.put("lower", LOWER);
        operators.put("upper", UPPER);
        operators.put("fcap", FCAP);
        operators.put("default", DEFAULT);
    }

}
