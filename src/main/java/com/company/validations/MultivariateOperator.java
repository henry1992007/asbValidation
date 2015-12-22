package com.company.validations;

import java.util.List;

/**
 * Created by henry on 15/12/22.
 */
public interface MultivariateOperator<T, R> {
    R operate(List<T> list);
}
