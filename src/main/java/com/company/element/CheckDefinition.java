package com.company.element;

import com.company.enums.CheckMode;
import com.company.validations.AggregateOperator;
import com.company.enums.CheckType;
import com.company.enums.Operator;
import com.company.validations.MultivariateOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henry on 15/11/16.
 */
@Getter
@Setter
public class CheckDefinition extends AbstractElementDefinition {
    private CheckType checkType;
    private CheckMode checkMode;
    private List<FieldPath> fields;
    private List<String> vals = new ArrayList<>();
    private MultivariateOperator cmpt;
    private AggregateOperator<Boolean> logic, objectLogic;
    private Operator operator;
    private List<FieldPath> _fields;
    private List<String> _vals = new ArrayList<>();
    private MultivariateOperator _cmpt;
    private AggregateOperator<Boolean> _logic, _objectLogic;
    private String msg;


    public CheckDefinition(int lineNum, String docName) {
        super(lineNum, docName);
    }

}
