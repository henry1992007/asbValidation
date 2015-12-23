package com.company;

/**
 * Created by henry on 15/12/23.
 */
public interface Compare<T> {
    boolean apply(T t1, T t2);
}
