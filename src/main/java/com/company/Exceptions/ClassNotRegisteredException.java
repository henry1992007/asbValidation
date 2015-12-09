package com.company.Exceptions;

/**
 * Created by henry on 15/11/9.
 */
public class ClassNotRegisteredException extends RuntimeException {

    public ClassNotRegisteredException() {
        super();
    }

    public ClassNotRegisteredException(String s) {
        super(s);
    }
}
