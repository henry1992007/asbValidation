package com.company.Exceptions;

/**
 * Created by henry on 15/12/4.
 */
public class EntityIDException extends IllegalDefinitionException {

    public EntityIDException(String msg) {
        super(msg);
    }

    public EntityIDException(String msg, String id, int lineNum, String docName) {
        super(msg + " id:'" + id + "'", lineNum, docName);
    }

}
