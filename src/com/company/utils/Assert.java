package com.company.utils;

import com.company.Exceptions.EntityIDException;
import com.company.Exceptions.IllegalDefinitionException;

/**
 * Created by henry on 15/11/16.
 */
public abstract class Assert {

    public Assert() {
    }

    public static void runtimeException(String msg) {
        throw new RuntimeException(msg);
    }

    public static void runtimeException(String msg, Exception e) {
        throw new RuntimeException(msg, e);
    }

    public static void illegalDefinitionException(String msg, int lineNum, String docName) {
        throw new IllegalDefinitionException(msg, lineNum, docName);
    }

    public static void entityIDException(String msg, String id, int lineNum, String docName) {
        throw new EntityIDException(msg, id, lineNum, docName);
    }

}
