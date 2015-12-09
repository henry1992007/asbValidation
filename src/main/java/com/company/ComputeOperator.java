package com.company;

/**
 * Created by henry on 15/11/20.
 */
@SuppressWarnings("unchecked")
public interface ComputeOperator<T> {
    T[] operate(T... var);
}
