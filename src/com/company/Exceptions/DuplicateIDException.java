package com.company.Exceptions;

/**
 * Created by henry on 15/11/10.
 */
public class DuplicateIDException extends RuntimeException {

    public DuplicateIDException() {
        super();
    }

    public DuplicateIDException(String s) {
        super(s);
    }

}
