package com.company;

/**
 * Created by henry on 15/11/25.
 */
public interface Operatable<R, T> {
    R operate(T var1, T var2);
}
