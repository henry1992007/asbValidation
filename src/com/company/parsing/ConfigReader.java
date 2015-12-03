package com.company.parsing;

import com.company.ComputeOperator;
import com.company.ConfigContext;
import com.company.MultiKeySetMap;
import com.company.element.CheckDefinition;
import com.company.element.ClassDefinition;
import com.company.element.ConditionField;
import com.company.element.ValidationDefinition;
import com.company.enums.*;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.StringUtils;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static com.company.utils.Assert.runtimeException;

/**
 * Created by henry on 15/11/16.
 */
public class ConfigReader {

    private static MultiKeySetMap<Class, Class> parentClassMap = new MultiKeySetMap<Class, Class>();

    static {
        parentClassMap.put(Sets.newHashSet(new Class[]{Integer.class, Float.class, Double.class, Long.class, Short.class, Byte.class, BigDecimal.class, BigInteger.class}), Number.class);
        parentClassMap.put(Sets.newHashSet(new Class[]{Boolean.class}), Boolean.class);
        parentClassMap.put(Sets.newHashSet(new Class[]{String.class}), String.class);
        parentClassMap.put(Sets.newHashSet(new Class[]{HashMap.class, LinkedHashMap.class}), Map.class);
        parentClassMap.put(Sets.newHashSet(new Class[]{ArrayList.class}), List.class);
    }

    private ConfigContext context;
    private List<Entity> entities;

    Map<String, ClassDefinition> classes = new HashMap<>();
    Map<String, CheckDefinition> refConditions = new HashMap<String, CheckDefinition>();
    Map<String, ValidationDefinition> validations = new HashMap<String, ValidationDefinition>();

    Map<String, ClassEntity> classEntities = new HashMap<String, ClassEntity>();
    Map<String, ConditionEntity> refConditionEntities = new HashMap<String, ConditionEntity>();
    Map<String, ValidationEntity> validationEntities = new HashMap<String, ValidationEntity>();

    public ConfigReader(ConfigContext context) {
        this.context = context;
    }

    public void setContext(ConfigContext context) {
        this.context = context;
    }

    public void read() {
        for (Entity entity : entities) {
            String name = entity.getName();
            switch (ElementType.fromString(name)) {
                case ClASS:
                    readClasses(entity);
                case NUMBER:
                    readRefChecks(entity);
                case VALIDATION:
                    readValidations(entity);
            }
            if (entity instanceof ClassEntity) {
                if (StringUtils.isEmpty(entity.getId()))
                    continue;
                if (classEntities.containsKey(entity.getId()))
                    Assert.runtimeException("duplicated class definition ID:" + entity.getId() + " at line " + entity.getLineNum());
                classEntities.put(entity.getId(), (ClassEntity) entity);
            } else if (entity instanceof ConditionEntity) {
                if (StringUtils.isEmpty(entity.getId()))
                    continue;
                if (refConditionEntities.containsKey(entity.getId()))
                    Assert.runtimeException("duplicated refCondition definition ID:" + entity.getId() + " at line " + entity.getLineNum());
                refConditionEntities.put(entity.getId(), (ConditionEntity) entity);
            } else if (entity instanceof ValidationEntity) {
                if (StringUtils.isEmpty(entity.getId()))
                    entity.setId("validation@" + entity.hashCode());
                if (validationEntities.containsKey(entity.getId()))
                    Assert.runtimeException("duplicated validation definition ID:" + entity.getId() + " at line " + entity.getLineNum());
                validationEntities.put(entity.getId(), (ValidationEntity) entity);
            }
        }
    }

    private void readClasses(Entity entity) {
        ClassDefinition cd = new ClassDefinition(entity.getId(), entity.getLineNum());
        try {
            cd.setClazz(Class.forName(entity.getProperty().get("class")));
        } catch (ClassNotFoundException e) {
            Assert.runtimeException("a class definition should specify id and a class name of an existing Class. At line " + entity.getLineNum());
        }
        if (classes.containsKey(cd.getId()))
            Assert.runtimeException("duplicated class id definition:'" + cd.getId() + "', at line " + cd.getLineNum());
        classes.put(cd.getId(), cd);
    }

    private void readRefChecks(Entity entity) {
        CheckDefinition cd = new CheckDefinition(entity.getId(), entity.getLineNum());
        Map<String, String> property = entity.getProperty();
        cd.setType(SupportedType.fromName(property.get("name")));
        cd.setFields(property.get("field").split(""));


        Operator operator = Operator.getOperator(entity.getOptr());
        if (operator.equals(Operator.UNKNOWN))
            Assert.runtimeException("unsupported operator type " + entity.getOptr() + " at line " + entity.getLineNum());
        cd.setOperator(operator);

    }

    private void readValidations(Entity entity) {
        ValidationDefinition vd = new ValidationDefinition(entity.getId(), entity.getLineNum());

        if (validations.containsKey(entity.getId()))
            Assert.runtimeException("duplicated validation id definition:'" + entity.getId() + "' at line " + entity.getLineNum());

        Map<String, Class> classMap = new HashMap<String, Class>();
        String[] classNames = entity.getProperty().get("class").split(",");
        for (String name : classNames)
            try {
                Class clazz = Class.forName(name);
                classMap.put(name.substring(name.lastIndexOf(".") + 1, name.length()), clazz);
            } catch (ClassNotFoundException e) {
                ClassDefinition clazz = classes.get(name);
                if (clazz == null)
                    Assert.runtimeException("class name:'" + name + "' defined in validation id:" + entity.getId() + " does not refer to an class definition or existing Class, at line " + entity.getLineNum());
                classMap.put(name, clazz.getClazz());
            }
        vd.setClasses(classMap);

        vd.setConditions(resolveCondition(entity.getSubs(), vd));
        validations.put(vd.getId(), vd);
    }

    private void checkParamsLegal(CheckDefinition cd) {
        if (cd.getType().equals(SupportedType.NUMBER)) {
            for (String s : cd.getVals())
                if (!StringUtils.isDigit(s))
                    runtimeException("the " + s + " is not a digit in val, at line " + cd.getLineNum());
            for (String s : cd.get_vals())
                if (!StringUtils.isDigit(s))
                    runtimeException("the " + s + " is not a digit in _val, at line " + cd.getLineNum());

            if (!Arrays.asList(cd.getType().getSupportedOperators()).contains(cd.getOperator()))
                runtimeException("unsupported operator:'" + cd.getOperator().getValue() + "' in check type " + cd.getType().getName() + " ,at line " + cd.getLineNum());


        }
    }

    private CheckDefinition[] resolveCondition(List<Entity> entities, ValidationDefinition ved) {
        List<CheckDefinition> cds = new ArrayList<CheckDefinition>();
        for (Entity entity : entities) {
            int lineNum = entity.getLineNum();
            CheckDefinition cd = new CheckDefinition(entity.getId(), entity.getLineNum());
            Map<String, String> property = entity.getProperty();

//            if (StringUtils.isNotEmpty(entity.getRef())) {
//                String[] refIDs = entity.getRef().split(",");
//                for (String id : refIDs) {
//
//                }
//                ConditionEntity ref = refConditionEntities.get(entity.getRef());
//                if (ref == null) {
//                    Assert.runtimeException("reference condition id:'" + entity.getRef() + "' does not exist, at line " + entity.getLineNum());
//                } else {
//                    overrideAndExtendCondition(ref, entity);
//                }
//            }
            SupportedType type = SupportedType.fromName(property.get("name"));
            if (type == null || type.equals(SupportedType.UNKNOWN))
                Assert.runtimeException("unknown check type at line " + lineNum);
            cd.setType(SupportedType.fromName(property.get("name")));

            cd.setFields(resolveFields(property.get("field"), ved.getClasses(), entity.getLineNum()));
            cd.set_fields(resolveFields(property.get("_field"), ved.getClasses(), entity.getLineNum()));
            cd.setVals(property.get("val").split(","));
            cd.set_vals(property.get("val").split(","));

//            ComputeOperator co = ComputeOperator.f

            Operator operator = Operator.getOperator(property.get("optr"));
            if (operator == null)
                Assert.runtimeException("operator not specified at line " + lineNum);
            if (operator.equals(Operator.UNKNOWN))
                Assert.runtimeException("unsupported operator '" + operator + "' at line " + lineNum);
            cd.setOperator(operator);


            LogicComputeOperator logicOperator = LogicComputeOperator.fromValue(property.get("logic"));
            if (logicOperator != null) {
                if (logicOperator.equals(LogicComputeOperator.UNKNOWN))
                    Assert.runtimeException("unknown logic operator:'" + property.get("logic") + "' at line " + lineNum);
                else
                    cd.setLogic(logicOperator);
            }
            LogicComputeOperator _logicOperator = LogicComputeOperator.fromValue(property.get("_logic"));
            if (_logicOperator != null) {
                if (_logicOperator.equals(LogicComputeOperator.UNKNOWN))
                    Assert.runtimeException("unknown logic operator:'" + property.get("_logic") + "' at line " + lineNum);
                else
                    cd.setLogic(_logicOperator);
            }

            cd.setMsg(property.get("msg"));
            checkParamsLegal(cd);
//            ced.setConditionType(entity.getConditionType());
            if (CollectionUtils.isNotEmpty(entity.getSubs()))
                cd.setSubConditions(resolveCondition(entity.getSubs(), ved));
            cds.add(cd);
        }

        return cds.toArray(new CheckDefinition[cds.size()]);
    }

    private ConditionEntity overrideAndExtendCondition(ConditionEntity ref, ConditionEntity target) {
        if (StringUtils.isEmpty(target.getFields())) target.setFields(ref.getFields());
        if (StringUtils.isEmpty(target.getOperator())) target.setOperator(ref.getOperator());
        if (StringUtils.isEmpty(target.getValue())) target.setValue(ref.getValue());
        if (StringUtils.isEmpty(target.getOtherFields())) target.setOtherFields(ref.getOtherFields());
        if (StringUtils.isEmpty(target.getLogic())) target.setLogic(ref.getLogic());
        if (StringUtils.isEmpty(target.getMsg())) target.setMsg(ref.getMsg());
        return target;
    }

    private ConditionField[] resolveFields(String fieldProperty, Map<String, Class> classMap, int lineNum) {
        List<ConditionField> fieldList = new ArrayList<ConditionField>();

        if (StringUtils.isNotEmpty(fieldProperty)) {
            String[] fieldNames = fieldProperty.split(",");
            for (String s : fieldNames) {
                ConditionField conditionField = new ConditionField();
                //多类比较必须指定Class
                String clazzName = StringUtils.firstToCapital(s.substring(0, s.indexOf(".")));
                if (classMap.get(clazzName) == null) {
                    if (classMap.size() > 1) {
                        Assert.runtimeException("class not specified in multi Classes validation or" +
                                "reference class:'" + s + "' does not exist, at line " + lineNum);
                    } else {
                        checkFieldAccessible((Class) classMap.values().toArray()[0], s, lineNum);
                        conditionField.setClazz((Class) classMap.values().toArray()[0]);
                        conditionField.setFields(s.split("."));
                    }
                } else {
                    conditionField.setClazz(classMap.get(clazzName));
                    conditionField.setFields(checkFieldAccessible(classMap.get(clazzName), s.substring(s.indexOf(".") + 1, s.length()), lineNum));
                }
                fieldList.add(conditionField);
            }
        }

        return fieldList.toArray(new ConditionField[fieldList.size()]);
    }

    private List<Field> checkFieldAccessible(Class clazz, String fieldNameStr, int line) {
        String[] fieldName = fieldNameStr.split("[.]");
        List<Field> fields = new ArrayList<Field>();

        for (int i = 0; i < fieldName.length; i++) {
            Field field = null;
            try {
                field = clazz.getField(fieldName[i]);
                if (!Modifier.isPublic(field.getModifiers()))
                    clazz.getMethod(StringUtils.genGetMethod(fieldName[i]));
                if (isMapClass(field.getType()))
                    break;
            } catch (NoSuchFieldException e) {
                Assert.runtimeException("field '" + fieldName[i] + "' in Class " + clazz.toString() + " does not exist, at line " + line);
            } catch (NoSuchMethodException e) {
                Assert.runtimeException("cannot access field '" + fieldName[i] + "' in Class " + clazz.toString() + ", the field is neither public or without " +
                        "a get method " + StringUtils.genGetMethod(fieldName[i]) + "may be set, at line " + line);
            }
            clazz = field.getType();
        }

        return fields;
    }

    private boolean isMapClass(Class clazz) {
        while (!clazz.equals(Object.class)) {
            if (Arrays.asList(clazz.getInterfaces()).contains(Map.class))
                return true;
            clazz = clazz.getSuperclass();
        }
        return false;
    }


}
