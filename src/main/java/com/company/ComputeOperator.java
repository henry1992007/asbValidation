package com.company;

import java.util.List;

/**
 * Created by henry on 15/11/20.
 */
@SuppressWarnings("unchecked")
public interface ComputeOperator<T> {
    List<T> operate(List<T> var);
}
