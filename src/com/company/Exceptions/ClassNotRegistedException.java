package com.company.Exceptions;

/**
 * Created by henry on 15/11/9.
 */
public class ClassNotRegistedException extends RuntimeException {

    public ClassNotRegistedException() {
        super();
    }

    public ClassNotRegistedException(String s) {
        super(s);
    }
}
