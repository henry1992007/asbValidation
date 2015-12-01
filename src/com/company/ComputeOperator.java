package com.company;

/**
 * Created by henry on 15/11/20.
 */
public interface ComputeOperator<T> {
    T[] operate(T... var);
}
