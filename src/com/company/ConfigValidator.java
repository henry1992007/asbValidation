//package com.company;
//
//import com.company.Exceptions.DuplicateIDException;
//import com.company.utils.CollectionUtils;
//import com.company.utils.MapUtils;
//import com.company.utils.StringUtils;
//import com.company.utils.Utils;
//import com.company.element.*;
//import com.company.element.Class;
//
//import java.util.*;
//
///**
// * Created by henry on 15/11/10.
// */
//public class ConfigValidator {
//
//    ValidationConfig config;
//    ConfigContext configContext;
//
//    Map<String, ClassDefinition> classes = new HashMap<String, ClassDefinition>();
//    Map<String, CheckDefinition> refConditions = new HashMap<String, CheckDefinition>();
//    Map<String, ValidationDefinition> validations = new HashMap<String, ValidationDefinition>();
//
//    public Map<String, Class> classIDMap = new HashMap<String, Class>();
//    public Map<String, Condition> refConditionIDMap = new HashMap<String, Condition>();
//    public Map<String, Validation> validationIDMap = new HashMap<String, Validation>();
//
//    public ConfigValidator(ValidationConfig config) {
//        this.config = config;
//    }
//
//    public ValidationConfig check() {
//        checkClasses();
//        checkIdDuplicate();
//        checkRefConditionNotExist();
//        return config;
//    }
//
//    public void checkClasses() {
//        List<Class> classes = configContext.getClasses();
//
//        if (CollectionUtils.isNotEmpty(classes)) {
//            for (Class c : classes) {
//                checkClassExist(c.getClassName());
//                if (classIDMap.containsKey(c.getId()))
//                    throw new RuntimeException("duplicated class id definition:" + c.getId());
//                else
//                    classIDMap.put(c.getId(), c);
//            }
//        }
//    }
//
//    public void checkRefCondition() {
//        List<Condition> refCondtions = configContext.getRefConditions();
//
//        if (CollectionUtils.isNotEmpty(refCondtions)) {
//            for (Condition c : refCondtions) {
//                if (refConditionIDMap.containsKey(c.getId()))
//                    throw new RuntimeException("duplicated condition id definition:" + c.getId());
//                else
//                    refConditionIDMap.put(c.getId(), c);
//            }
//        }
//    }
//
//    public void checkValidation() {
//        List<Validation> validations = configContext.getValidations();
//
//        if (CollectionUtils.isNotEmpty(validations))
//            for (Validation v : validations) {
//                if (validationIDMap.containsKey(v.getId()))
//                    throw new RuntimeException("duplicated validation id definition:" + v.getId());
//                else if (StringUtils.isEmpty(v.getId()))
//                    validationIDMap.put("validation@" + v.hashCode(), v);
//                else validationIDMap.put(v.getId(), v);
//
//                List<String> className = v.getClassName();
//                Map<String, java.lang.Class> classMap = new HashMap<String, java.lang.Class>();
//                for (String s : className) {
//                    try {
//                        checkClassExist(s);
//                    } catch (Exception unused) {
//                        if (classIDMap.get(s) == null)
//                            throw new RuntimeException("class:" + s + " in validation does not exist");
//                        try {
//                            classMap.put(s, java.lang.Class.forName(classIDMap.get(s).getClassName()));
//                        } catch (ClassNotFoundException unused1) {
//                        }
//                    }
//                }
//                v.setClassMap(classMap);
//            }
//    }
//
//
//    public void checkIdDuplicate() {
//        List<Validation> validations = config.getValidations();
//        Map<String, Condition> refConditions = config.getRefConditions();
//
//        if (MapUtils.isNotEmpty(refConditions)) {
//            for (Condition c : refConditions.values()) {
//                String id = c.getId();
//                if (StringUtils.isNotEmpty(id)) {
//                    if (refConditionIDMap.containsKey(c.getId()))
//                        throw new RuntimeException("duplicated condition ID:" + id);
//                    refConditionIDMap.put(id, c);
//                } else {
//                    refConditionIDMap.put("condition" + refConditionIDMap.size(), c);
//                }
//            }
//        }
//
//        if (Utils.CollectionNotEmpty(validations)) {
//            for (Validation v : validations) {
//                List<String> className = v.getClassName();
//                for (String s : className) {
//                    try {
//                        checkClassExist(s);
//                    } catch (Exception e) {
//                        if (!classIDMap.containsKey(s))
//                            throw new RuntimeException("reference classID:" + s + " does not exist." + e.toString());
//                    }
//                }
//                if (Utils.CollectionNotEmpty(v.getConditions())) {
//                    List<Condition> conditions = v.getConditions();
//                    checkConditionIdDuplicate(conditions);
//                }
//            }
//        }
//    }
//
//    private void checkConditionIdDuplicate(List<Condition> conditions) {
//        for (Condition c : conditions) {
//            String id = c.getId();
//            if (StringUtils.isNotEmpty(id)) {
//                if (refConditionIDMap.containsKey(c.getId()))
//                    throw new DuplicateIDException("duplicated condition ID:" + id);
//                refConditionIDMap.put(id, c);
//            } else {
//                refConditionIDMap.put("condition" + refConditionIDMap.size(), c);
//            }
//
//            if (Utils.CollectionNotEmpty(c.getSubConditions())) {
//                checkConditionIdDuplicate(c.getSubConditions());
//            }
//        }
//    }
//
//    public void checkRefConditionNotExist() {
//        for (Condition c : refConditionIDMap.values()) {
//            String ref = c.getRef();
//            if (StringUtils.isNotEmpty(ref) && !refConditionIDMap.containsKey(ref))
//                throw new RuntimeException("reference condition \"" + ref + "\" does not exist");
//        }
//    }
//
//    private void checkClassExist(String className) {
//        try {
//            java.lang.Class.forName(className);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException("defined class:" + className + " does not exist." + e.toString());
//        }
//    }
//
//}
