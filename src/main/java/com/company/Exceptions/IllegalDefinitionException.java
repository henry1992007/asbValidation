package com.company.Exceptions;

/**
 * Created by henry on 15/12/4.
 */
public class IllegalDefinitionException extends RuntimeException {

    public IllegalDefinitionException(String msg) {
        super(msg);
    }

    public IllegalDefinitionException(String msg, int lineNum, String docName) {
        super(msg + ", at line " + lineNum + " in " + docName);
    }

    public IllegalDefinitionException(String msg, int lineNum, String docName, Throwable cause) {
        super(msg + ", at line " + lineNum + " in " + docName, cause);
    }

}
