package com.company.validations;

import com.company.AssociativeOperator;
import com.company.MappingOperator;
import com.company.utils.CollectionUtils;
import com.company.utils.StringUtils;
import com.company.validations.MultivariateOperator;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by henry on 15/12/13.
 */
public abstract class MultivariateOperators {

    public MultivariateOperators() {
    }

    static HashBiMap<String, MultivariateOperator> operators = HashBiMap.create();

    static AssociativeOperator<Boolean> AND = v -> CollectionUtils.reduce(v, Boolean::logicalAnd);
    static AssociativeOperator<Boolean> OR = var -> CollectionUtils.reduce(var, Boolean::logicalOr);
    static AssociativeOperator<Boolean> XOR = CollectionUtils::xor;
    static MappingOperator<Boolean> REVERSE = var -> CollectionUtils.map(var, b -> !b);


    static AssociativeOperator<BigDecimal> SUM = var -> CollectionUtils.reduce(var, BigDecimal::add);
    static AssociativeOperator<BigDecimal> PRODUCT = var -> CollectionUtils.reduce(var, BigDecimal::multiply);
    static AssociativeOperator<BigDecimal> MAX = var -> CollectionUtils.reduce(var, BigDecimal::max);
    static AssociativeOperator<BigDecimal> MIN = var -> CollectionUtils.reduce(var, BigDecimal::min);
    static AssociativeOperator<BigDecimal> AVERAGE = var -> SUM.operate(var).divide(new BigDecimal(var.size()), 3, BigDecimal.ROUND_FLOOR);
    static MappingOperator<BigDecimal> FLOAT = var -> CollectionUtils.map(var, bigDecimal -> new BigDecimal(bigDecimal.scale()));


    static MappingOperator<String> LOWER = var -> CollectionUtils.map(var, String::toLowerCase);
    static MappingOperator<String> UPPER = var -> CollectionUtils.map(var, String::toUpperCase);
    static MappingOperator<String> FCAP = var -> CollectionUtils.map(var, StringUtils::firstToCapital);


    static MappingOperator DEFAULT = var -> CollectionUtils.map(var, Function.identity());

    public static MultivariateOperator get(String name) {
        return operators.get(name);
    }

    public static String getName(MultivariateOperator operator) {
        return operators.inverse().get(operator);
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
        operators.put("reverse", REVERSE);
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
