package com.company.validations;

import com.company.element.CheckDefinition;
import com.company.element.ConditionDefinition;
import com.company.element.ParentElement;
import com.company.element.ValidationDefinition;
import com.company.utils.Assert;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by henry on 15/11/17.
 */
public class ValidationChecker {

    //todo:change modifier to default
    public static Map<String, ValidationDefinition> validations = new HashMap<>();

    private ValidationDefinition vd;
    private Map<Class, Set<Object>> objectClassMap = new HashMap<>();


    private ValidationChecker(String id) {
        if (validations.get(id) == null)
            Assert.runtimeException("validation id:'" + id + "' not found");
        vd = validations.get(id);
    }

    public static ValidationChecker get(String id) {
        return new ValidationChecker(id);
    }

    public List<String> check(Object... objects) {
        resolveObjectClass(objects);
        doCheck(vd, objectClassMap);
        return results;
    }

    private void resolveObjectClass(Object... objs) {
        for (Object obj : objs) {
            if (!objectClassMap.containsKey(obj.getClass()))
                objectClassMap.put(obj.getClass(), Sets.newHashSet(obj));
            else
                objectClassMap.get(obj.getClass()).add(obj);
        }
        if (!Sets.newHashSet(vd.getClasses().values()).equals(objectClassMap.keySet())) {
            Assert.runtimeException("classes of the passed in check objects:" + Arrays.toString(objectClassMap.keySet().toArray()) +
                    "does not match the validation definition class pattern " + Arrays.toString(vd.getClasses().values().toArray()));
        }
    }

    List<String> results = new ArrayList<>();

    private void doCheck(ParentElement element, Map<Class, Set<Object>> objectClassMap) {
        for (CheckDefinition check : element.getRefChecks())
            objectClassMap = check.getCheckType().getTypeValidator().filter(check, objectClassMap);

        results.addAll(processChecks(element.getSubChecks(), objectClassMap));

        for (ConditionDefinition subCd : element.getSubConditions())
            doCheck(subCd, objectClassMap);
    }

    private List<String> processChecks(List<CheckDefinition> cds, Map<Class, Set<Object>> objectClassMap) {
        return cds.stream().filter(
                cd -> !cd.getCheckType().getTypeValidator().validate(cd, objectClassMap)).map(
                CheckDefinition::getMsg).collect(
                Collectors.toList());
    }


}
