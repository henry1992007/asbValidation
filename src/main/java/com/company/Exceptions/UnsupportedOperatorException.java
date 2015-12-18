package com.company.Exceptions;

import com.company.enums.CheckType;
import com.company.enums.Operator;

/**
 * Created by henry on 15/12/14.
 */
public class UnsupportedOperatorException extends IllegalDefinitionException {

    public UnsupportedOperatorException(String msg) {
        super(msg);
    }

    public UnsupportedOperatorException(String operator, CheckType checkType, int lineNum, String docName) {
        super("unsupported operator:'" + operator + "' in check type " + checkType.getName(), lineNum, docName);
    }
}
