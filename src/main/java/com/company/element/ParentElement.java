package com.company.element;

import java.util.List;

/**
 * Created by henry on 15/12/12.
 */
public interface ParentElement {

    void addCheck(CheckDefinition definition);

    void addCondition(ConditionDefinition definition);

    List<CheckDefinition> getRefChecks();

    List<CheckDefinition> getSubChecks();

    List<ConditionDefinition> getSubConditions();

}
