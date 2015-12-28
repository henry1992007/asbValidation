package com.company.element;

import com.company.validations.AssociativeOperator;
import com.company.enums.CheckType;
import com.company.enums.Operator;
import com.company.validations.MultivariateOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 15/11/16.
 */
@Getter
@Setter
public class CheckDefinition extends AbstractElementDefinition {
    private CheckType checkType;
    private Map<Class, String[]> fields;
    private List<String> vals = new ArrayList<>();
    private MultivariateOperator cmpt;
    private AssociativeOperator<Boolean> logic;
    private Operator operator;
    private Map<Class, String[]> _fields;
    private List<String> _vals = new ArrayList<>();
    private MultivariateOperator _cmpt;
    private AssociativeOperator<Boolean> _logic;
    private String msg;


    public CheckDefinition(int lineNum, String docName) {
        super(lineNum, docName);
    }

}
