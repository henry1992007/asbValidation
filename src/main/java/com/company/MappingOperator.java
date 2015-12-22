package com.company;

import com.company.validations.MultivariateOperator;

import java.util.List;
import java.util.function.Function;

/**
 * Created by henry on 15/12/22.
 */
public interface MappingOperator<T> extends MultivariateOperator<T, List<T>> {
}
