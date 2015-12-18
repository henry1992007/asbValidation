package com.company.utils;

import com.company.Exceptions.EntityIDException;
import com.company.Exceptions.IllegalDefinitionException;
import com.company.Exceptions.UnsupportedOperatorException;
import com.company.enums.CheckType;
import com.company.enums.Operator;

/**
 * Created by henry on 15/11/16.
 */
public abstract class Assert {

    public final static String CLASS_ID_UNSPECIFIED = "class definition id unspecified.";
    public final static String CLASS_NAME_UNSPECIFIED = "class definition Class unspecified.";
    public final static String CLASS_NOT_FOUND = "referenced class not found.";
    public final static String DUPLICATED_CLASS_ID = "duplicated class definition id, there is already class definition with";

    public final static String VALIDATION_ID_UNSPECIFIED = "validation definition id unspecified.";
    public final static String DUPLICATED_VALIDATION_ID = "duplicated class definition id, there is already class definition with";
    public final static String UNKNOWN_ELEMENT = "unknown element defined";

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

    public static void illegalDefinitionException(String msg, int lineNum, String docName, Throwable cause) {
        throw new IllegalDefinitionException(msg, lineNum, docName, cause);
    }

    public static void unknownElementException(String elementName, int lineNum, String docName) {
        illegalDefinitionException(UNKNOWN_ELEMENT + ":'" + elementName + "'", lineNum, docName);
    }

    public static void entityIDException(String msg, String id, int lineNum, String docName) {
        throw new EntityIDException(msg, id, lineNum, docName);
    }

    public static void unsupportedOperator(String operator, CheckType checkType, int lineNum, String docName) {
        throw new UnsupportedOperatorException(operator, checkType, lineNum, docName);
    }

}
