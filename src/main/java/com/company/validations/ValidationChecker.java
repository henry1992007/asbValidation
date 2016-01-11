package com.company.validations;

import com.company.element.CheckDefinition;
import com.company.element.ConditionDefinition;
import com.company.element.ParentElement;
import com.company.element.ValidationDefinition;
import com.company.utils.Assert;
import com.company.utils.MapUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

import static com.company.utils.Utils.info;
import static com.company.utils.Utils.err;

/**
 * Created by henry on 15/11/17.
 */
public class ValidationChecker {

    //todo:change modifier to default
    public static Map<String, ValidationDefinition> validations = new HashMap<>();

    private ValidationDefinition vd;

    public static ValidationChecker get(String id) {
        return new ValidationChecker(id);
    }


    private ValidationChecker(String id) {
        if (validations.get(id) == null)
            Assert.runtimeException("validation id:'" + id + "' not found");
        vd = validations.get(id);
    }

    public String getID() {
        return vd.getId();
    }


    public static void clearAll() {
        validations.clear();
    }


    List<String> results = new ArrayList<>();

    public List<String> check(Object... objects) {
        doCheck(vd, resolveObjectClass(objects));
        return results;
    }

    private Map<Class, List<Object>> resolveObjectClass(Object... objs) {
        Map<Class, List<Object>> objectClassMap = new HashMap<>();
        info("resolving object class...");

        for (Object o : objs) {
            Class oClass = o.getClass();
            if (!objectClassMap.containsKey(oClass))
                objectClassMap.put(oClass, Lists.newArrayList(o));
            else
                objectClassMap.get(oClass).add(o);
        }

        for (Class clazz : objectClassMap.keySet()) {
            info(clazz.toString() + " size:" + objectClassMap.get(clazz).size());
        }

        if (!Sets.newHashSet(vd.getClasses().values()).equals(objectClassMap.keySet())) {
            Assert.runtimeException("classes of the passed in check objects:" + Arrays.toString(objectClassMap.keySet().toArray()) +
                    "does not match the validation definition class pattern " + Arrays.toString(vd.getClasses().values().toArray()));
        }
        return objectClassMap;
    }


    private void doCheck(ParentElement element, Map<Class, List<Object>> objectClassMap) {
        Map<Class, List<Object>> filtered = MapUtils.copy(objectClassMap);
        for (CheckDefinition check : element.getRefChecks())
            filtered = check.getCheckType().getTypeValidator().filter(check, filtered);

        results.addAll(processChecks(element.getSubChecks(), filtered));

        for (ConditionDefinition subCd : element.getSubConditions())
            doCheck(subCd, filtered);
    }

    private List<String> processChecks(List<CheckDefinition> cds, Map<Class, List<Object>> objectClassMap) {
        return cds.stream().filter(
                cd -> !cd.getCheckType().getTypeValidator().validate(cd, objectClassMap)).map(
                CheckDefinition::getMsg).collect(Collectors.toList());
    }

}
