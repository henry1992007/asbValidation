package com.company.parsing;

import com.company.ConfigContext;
import com.company.MultiKeySetMap;
import com.company.element.*;
import com.company.enums.*;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.ReflectUtils;
import com.company.utils.StringUtils;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static com.company.utils.Assert.*;

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


    Map<String, ClassDefinition> classes = new HashMap<>();
    Map<String, ValidationDefinition> validations = new HashMap<String, ValidationDefinition>();

    ConfigContext[] configContexts;


    Map<String, CheckDefinition> conditionIdMap = new HashMap<>();


    public void read(ConfigContext[] contexts) {
        this.configContexts = contexts;
        for (ConfigContext context : configContexts) {
            for (Entity entity : context.getEntities()) {
                String name = entity.getName();
                switch (ElementType.fromString(name)) {
                    case ClASS:
                        ClassDefinition cd = readClasses(entity);
                        if (context.getClasses().containsKey(cd.getId()))
                            Assert.runtimeException("duplicated class id definition:'" + cd.getId() + "', at line " + cd.getLineNum());
                        context.getClasses().put(cd.getId(), cd);
                    case VALIDATION:
                        readValidations(entity);
                }
            }
        }
    }

    private ClassDefinition readClasses(Entity entity) {
        ClassDefinition cd = new ClassDefinition(entity.getId(), entity.getLineNum(), entity.getDocName());
        try {
            cd.setClazz(Class.forName(entity.getProperty().get("class")));
        } catch (ClassNotFoundException e) {
            Assert.runtimeException("a class definition should specify id and a class name of an existing Class. At line " + entity.getLineNum());
        }
        return cd;
    }


    private void readValidations(Entity entity) {
        if (StringUtils.isEmpty(entity.getId()))
            Assert.illegalDefinitionException("validation id unspecified", entity.getLineNum(), entity.getDocName());
        if (validations.containsKey(entity.getId()))
            Assert.entityIDException("duplicated validation id definition,", entity.getId(), entity.getLineNum(), entity.getDocName());

        ValidationDefinition vd = new ValidationDefinition(entity.getId(), entity.getLineNum(), entity.getDocName());
        Map<String, Class> classMap = new HashMap<>();
        String[] classNames = entity.getProperty().get("class").split(",");
        for (String name : classNames)
            try {
                Class clazz = Class.forName(name);
                classMap.put(ReflectUtils.getRefClassName(name), clazz);
            } catch (ClassNotFoundException e) {
                ClassDefinition clazz = classes.get(name);
                if (clazz == null)
                    Assert.runtimeException("class name:'" + name + "' defined in validation id:" + entity.getId() + " does not refer to an class definition or existing Class, at line " + entity.getLineNum());
                classMap.put(name, clazz.getClazz());
            }

        vd.setClasses(classMap);
        vd.setDefinitions(resolveDefinition(entity.getSubs(), vd));
        validations.put(vd.getId(), vd);
    }

    private AbstractElementDefinition[] resolveDefinition(List<Entity> entities, ValidationDefinition vd) {
        List<AbstractElementDefinition> cds = new ArrayList<>();

        next:
        for (Entity entity : entities) {
            DefinitionType definitionType = DefinitionType.fromName(entity.getName());
            switch (definitionType) {
                case CHECK:
                    AbstractElementDefinition check = new CheckDefinition(entity.getId(), entity.getLineNum(), entity.getDocName());
                    setCheckProperty(entity, (CheckDefinition) check, vd);
                    cds.add(check);
                    break;
                case CONDITION:
                    ConditionDefinition condition = new ConditionDefinition(entity.getId(), entity.getLineNum(), entity.getDocName());
                    if (entity.getProperty().containsKey("ref")) {
                        setConditionProperty(entity, condition, vd);
                    } else {
                        if (entity.getProperty().containsKey("id")) {
                            String id = entity.getProperty().get("id");
                            if (vd.getIdMap().containsKey(id))
                                Assert.entityIDException("duplicated check id defined", id, entity.getLineNum(), entity.getDocName());
                            vd.getIdMap().put(id, condition);
                        }
                        CheckDefinition subCd = new CheckDefinition(null, entity.getLineNum(), entity.getDocName());
                        setCheckProperty(entity, subCd, vd);
                        condition.setRefConditions(new CheckDefinition[]{subCd});
                    }
                    if (CollectionUtils.isNotEmpty(entity.getSubs()))
                        condition.setSubConditions(resolveDefinition(entity.getSubs(), vd));
                    cds.add(condition);
                    break;
                case UNKNOWN:
                    break next;
            }
        }

        return CollectionUtils.listToArray(cds);
    }


    private void setCheckProperty(Entity entity, CheckDefinition cd, ValidationDefinition vd) {
        int lineNum = entity.getLineNum();
        String docName = entity.getDocName();
        Map<String, String> property = entity.getProperty();

        CheckType type = CheckType.fromName(property.get("type"));
        if (type == null)
            Assert.illegalDefinitionException("check type undefined", lineNum, docName);
        if (type.equals(CheckType.UNKNOWN))
            Assert.illegalDefinitionException("unknown check type:'" + type.getName() + "'", lineNum, docName);
        cd.setCheckType(type);

        cd.setFields(resolveFields(property.get("field"), vd.getClasses(), lineNum));
        cd.set_fields(resolveFields(property.get("_field"), vd.getClasses(), lineNum));
        if (property.containsKey("val"))
            cd.setVals(property.get("val").split(","));
        if (property.containsKey("_val"))
            cd.set_vals(property.get("_val").split(","));

        if (property.containsKey("cmpt")) {
            NumberComputeOperate operate = NumberComputeOperate.fromName(property.get("cmpt"));
            if (operate != null) {
                if (operate.equals(NumberComputeOperate.UNKNOWN))
                    Assert.runtimeException("unknown logic operator:'" + property.get("cmpt") + "' at line " + lineNum);
                else
                    cd.setCmpt(operate);
            }
        }
        if (property.containsKey("_cmpt")) {
            NumberComputeOperate _operate = NumberComputeOperate.fromName(property.get("_cmpt"));
            if (_operate != null) {
                if (_operate.equals(NumberComputeOperate.UNKNOWN))
                    Assert.runtimeException("unknown logic operator:'" + property.get("_cmpt") + "' at line " + lineNum);
                else
                    cd.setCmpt(_operate);
            }
        }

        if (property.containsKey("logic")) {
            LogicComputeOperator logicOperator = LogicComputeOperator.fromValue(property.get("logic"));
            if (logicOperator != null) {
                if (logicOperator.equals(LogicComputeOperator.UNKNOWN))
                    Assert.runtimeException("unknown logic operator:'" + property.get("logic") + "' at line " + lineNum);
                else
                    cd.setLogic(logicOperator);
            }
        }
        if (property.containsKey("_logic")) {
            LogicComputeOperator _logicOperator = LogicComputeOperator.fromValue(property.get("_logic"));
            if (_logicOperator != null) {
                if (_logicOperator.equals(LogicComputeOperator.UNKNOWN))
                    Assert.runtimeException("unknown logic operator:'" + property.get("_logic") + "' at line " + lineNum);
                else
                    cd.set_logic(_logicOperator);
            }
        }

        Operator operator = Operator.getOperator(property.get("optr"));
        if (operator == null)
            Assert.runtimeException("operator not specified at line " + lineNum);
        if (operator.equals(Operator.UNKNOWN))
            Assert.runtimeException("unsupported operator '" + operator + "' at line " + lineNum);
        cd.setOperator(operator);

        if (property.containsKey("msg"))
            cd.setMsg(property.get("msg"));
        checkParamsLegal(cd);
    }

    private void checkParamsLegal(CheckDefinition cd) {
        if (cd.getCheckType().equals(CheckType.NUMBER)) {
            for (String s : cd.getVals())
                if (!StringUtils.isDigit(s))
                    runtimeException("the " + s + " is not a digit in val, at line " + cd.getLineNum());
            for (String s : cd.get_vals())
                if (!StringUtils.isDigit(s))
                    runtimeException("the " + s + " is not a digit in _val, at line " + cd.getLineNum());
        }
        if (!Arrays.asList(cd.getCheckType().getSupportedOperators()).contains(cd.getOperator()))
            runtimeException("unsupported operator:'" + cd.getOperator().getValue() + "' in check type " + cd.getCheckType().getName() + " ,at line " + cd.getLineNum());
    }

    private void setConditionProperty(Entity entity, ConditionDefinition cd, ValidationDefinition vd) {
        String[] refIDs = entity.getProperty().get("ref").split(",");
        List<CheckDefinition> refs = new ArrayList<>();
        for (String s : refIDs) {
            if (vd.getIdMap().get(s) == null)
                Assert.entityIDException("cannot find check with specified id.", s, entity.getLineNum(), entity.getDocName());
            refs.addAll(Arrays.asList(vd.getIdMap().get(s).getRefConditions()));
        }
        if (entity.getProperty().containsKey("msg"))
            cd.setMsg(entity.getProperty().get("msg"));
        cd.setRefConditions(CollectionUtils.listToArray(refs));
    }

//    private ConditionEntity overrideAndExtendCondition(ConditionEntity ref, ConditionEntity target) {
//        if (StringUtils.isEmpty(target.getFields())) target.setFields(ref.getFields());
//        if (StringUtils.isEmpty(target.getOperator())) target.setOperator(ref.getOperator());
//        if (StringUtils.isEmpty(target.getValue())) target.setValue(ref.getValue());
//        if (StringUtils.isEmpty(target.getOtherFields())) target.setOtherFields(ref.getOtherFields());
//        if (StringUtils.isEmpty(target.getLogic())) target.setLogic(ref.getLogic());
//        if (StringUtils.isEmpty(target.getMsg())) target.setMsg(ref.getMsg());
//        return target;
//    }

    private ConditionField[] resolveFields(String fieldProperty, Map<String, Class> classMap, int lineNum) {
        List<ConditionField> fieldList = new ArrayList<>();

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
                    conditionField.setFields(s.split("."));
                    checkFieldAccessible((Class) classMap.values().toArray()[0], s, lineNum);
                }
                fieldList.add(conditionField);
            }
        }

        return fieldList.toArray(new ConditionField[fieldList.size()]);
    }

    private List<Field> checkFieldAccessible(Class clazz, String fieldNameStr, int line) {
        String[] fieldName = fieldNameStr.split("[.]");
        List<Field> fields = new ArrayList<>();

        for (int i = 0; i < fieldName.length; i++) {
            Field field = null;
            try {
                field = clazz.getField(fieldName[i]);
                if (!Modifier.isPublic(field.getModifiers()))
                    clazz.getMethod(ReflectUtils.genGetMethod(fieldName[i]));
                if (ReflectUtils.isMapClass(field.getType()))
                    break;
            } catch (NoSuchFieldException e) {
                Assert.runtimeException("field '" + fieldName[i] + "' in Class " + clazz.toString() + " does not exist, at line " + line);
            } catch (NoSuchMethodException e) {
                Assert.runtimeException("cannot access field '" + fieldName[i] + "' in Class " + clazz.toString() + ", the field is neither public or without " +
                        "a get method " + ReflectUtils.genGetMethod(fieldName[i]) + "may be set, at line " + line);
            }
            clazz = field.getType();
        }

        return fields;
    }


}
