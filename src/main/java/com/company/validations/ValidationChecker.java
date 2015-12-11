package com.company.validations;

import com.company.element.*;
import com.company.utils.Assert;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * Created by henry on 15/11/17.
 */
public class ValidationChecker {

    static Map<String, ValidationDefinition> validations = new HashMap<>();

    private ValidationDefinition ved;
    private Map<Class, Set<Object>> objectClassMap = new HashMap<Class, Set<Object>>();


    private ValidationChecker(String id) {
        preCheck(id);
        ved = validations.get(id);
    }

    public static ValidationChecker get(String id) {
        return new ValidationChecker(id);
    }

    public List<String> check(Object... objects) {
        resolveObjectClass(objects);
        return doCheck(ved.getDefinitions(), objectClassMap);
    }

    private void resolveObjectClass(Object... objs) {
        for (Object obj : objs) {
            if (!objectClassMap.containsKey(obj.getClass()))
                objectClassMap.put(obj.getClass(), Sets.newHashSet(obj));
            else
                objectClassMap.get(obj.getClass()).add(obj);
        }
        if (!Sets.newHashSet(ved.getClasses().values()).equals(objectClassMap.keySet())) {
            Assert.runtimeException("classes of the passed in check objects:" + Arrays.toString(objectClassMap.keySet().toArray()) +
                    "does not match the validation definition class pattern " + Arrays.toString(ved.getClasses().values().toArray()));
        }
    }

    private List<String> doCheck(AbstractElementDefinition[] aeds, Map<Class, Set<Object>> objectClassMap) {
        List<String> result = new ArrayList<>();
        for (AbstractElementDefinition aed : aeds) {
            if (aed instanceof CheckDefinition) {
                //todo: 抽象
                if (!((CheckDefinition) aed).getCheckType().getTypeValidator().validate((CheckDefinition) aed, objectClassMap)) {
                    result.add(((CheckDefinition) aed).getMsg());
                }
            } else if (aed instanceof ConditionDefinition) {
                CheckDefinition[] checkDefinitions = ((ConditionDefinition) aed).getRefConditions();
                for (CheckDefinition cd : checkDefinitions)
                    objectClassMap = cd.getCheckType().getTypeValidator().filter(cd, objectClassMap);
                doCheck(((ConditionDefinition) aed).getSubConditions(), objectClassMap);
            }
        }

        return result;
    }

    private void preCheck(String id) {
        if (validations.get(id) == null)
            Assert.runtimeException("validation id:'" + id + "' not found");
    }

}
