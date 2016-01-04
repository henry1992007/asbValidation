package com.company.validations;

import com.company.MappingOperator;
import com.company.enums.CheckMode;
import com.company.utils.CollectionUtils;
import com.company.utils.StringUtils;
import com.google.common.collect.HashBiMap;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.company.enums.CheckMode.*;

/**
 * Created by henry on 15/12/13.
 */
public abstract class MultivariateOperators {

    public MultivariateOperators() {
    }

    static HashBiMap<String, MultivariateOperator> operators = HashBiMap.create();

    static AggregateOperator<Boolean> AND = v -> CollectionUtils.reduce(v, Boolean::logicalAnd);
    static AggregateOperator<Boolean> OR = var -> CollectionUtils.reduce(var, Boolean::logicalOr);
    static AggregateOperator<Boolean> XOR = CollectionUtils::xor;
    static MappingOperator<Boolean> REVERSE = var -> CollectionUtils.map(var, b -> !b);


    static AggregateOperator<BigDecimal> SUM = var -> CollectionUtils.reduce(var, BigDecimal::add);
    static AggregateOperator<BigDecimal> PRODUCT = var -> CollectionUtils.reduce(var, BigDecimal::multiply);
    static AggregateOperator<BigDecimal> MAX = var -> CollectionUtils.reduce(var, BigDecimal::max);
    static AggregateOperator<BigDecimal> MIN = var -> CollectionUtils.reduce(var, BigDecimal::min);
    static AggregateOperator<BigDecimal> AVERAGE = var -> SUM.operate(var).divide(new BigDecimal(var.size()), 3, BigDecimal.ROUND_FLOOR);
    static MappingOperator<BigDecimal> FLOAT = var -> CollectionUtils.map(var, bigDecimal -> new BigDecimal(bigDecimal.scale()));


    static MappingOperator<String> LOWER = var -> CollectionUtils.map(var, String::toLowerCase);
    static MappingOperator<String> UPPER = var -> CollectionUtils.map(var, String::toUpperCase);
    static MappingOperator<String> FCAP = var -> CollectionUtils.map(var, StringUtils::firstToCapital);


//    static MappingOperator<Date> RESET_HOUR = var -> CollectionUtils.map(var, );


    static MappingOperator DEFAULT = var -> var;

    public static MultivariateOperator get(String name) {
        return operators.get(name);
    }

    public static boolean contains(String name) {
        return operators.containsKey(name);
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

    static Map<MultivariateOperator, CheckMode> defaultSemanteme = new HashMap<>();

    static {
        defaultSemanteme.put(SUM, RELATED);
        defaultSemanteme.put(PRODUCT, RELATED);
        defaultSemanteme.put(MAX, RELATED);
        defaultSemanteme.put(MIN, RELATED);
        defaultSemanteme.put(AVERAGE, RELATED);
        defaultSemanteme.put(FLOAT, SINGLE);
        defaultSemanteme.put(LOWER, SINGLE);
        defaultSemanteme.put(UPPER, SINGLE);
        defaultSemanteme.put(FCAP, SINGLE);
    }

}
