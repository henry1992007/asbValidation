package com.company.validations;

import com.company.AssociativeOperator;
import com.company.ConfigContext;
import com.company.element.*;
import com.company.enums.*;
import com.company.parsing.Entity;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.ReflectUtils;
import com.company.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static com.company.utils.Assert.runtimeException;

/**
 * Created by henry on 15/11/16.
 */
public class ConfigReader {

    public final static String PROPERTY_ID = "id";

    public final static String PROPERTY_OPERATOR = "operator";

    public final static String PROPERTY_CLASS = "class";

    public final static String PROPERTY_REF = "ref";

    public final static String PROPERTY_TYPE = "type";
    public final static String PROPERTY_FIELD = "field";
    public final static String PROPERTY__FIELD = "_field";
    public final static String PROPERTY_VALUE = "value";
    public final static String PROPERTY__VALUE = "_value";
    public final static String PROPERTY_CMPT = "cmpt";
    public final static String PROPERTY__CMPT = "_cmpt";
    public final static String PROPERTY_LOGIC = "logic";
    public final static String PROPERTY__LOGIC = "_logic";
    public final static String PROPERTY_MSG = "msg";

    public final static String SPLITTER = ",";

    public final static String DOT = ".";
    public final static String SPLIT_DOT = "[.]";

    Map<String, ClassDefinition> classes = new HashMap<>();

    ConfigContext[] configContexts;

    private ValidationDefinition vd;
    private int lineNum;
    private String docName;
    ConfigContext context;


    Map<String, CheckDefinition> conditionIdMap = new HashMap<>();


    public void read(ConfigContext[] contexts) {
        this.configContexts = contexts;
        for (ConfigContext context : configContexts) {
            this.context = context;
            docName = context.getDocName();
            context.getEntities().stream().filter(e -> e.getName().equals("class")).forEach(e -> readClasses(e, context));
            context.getEntities().stream().filter(e -> e.getName().equals("constant")).forEach(e -> readConstant(e, context));
            context.getEntities().stream().filter(e -> e.getName().equals("validation")).forEach(e -> readValidations(e, context));
        }
    }

    private void readClasses(Entity entity, ConfigContext context) {
        Map<String, String> property = entity.getProperty();
        lineNum = entity.getLineNum();

        ClassDefinition cd = new ClassDefinition(property.get(PROPERTY_ID), entity.getLineNum(), entity.getDocName());
        if (context.getClasses().containsKey(cd.getId()))
            Assert.entityIDException(Assert.DUPLICATED_CLASS_ID, cd.getId(), lineNum, docName);

        try {
            if (StringUtils.isEmpty(property.get("class")))
                Assert.illegalDefinitionException(Assert.CLASS_NAME_UNSPECIFIED, lineNum, docName);
            cd.setClazz(Class.forName(property.get("class")));
        } catch (ClassNotFoundException e) {
            Assert.illegalDefinitionException(Assert.CLASS_NOT_FOUND + ":'" + property.get("class") + "'", lineNum, docName);
        }

        context.getClasses().put(cd.getId(), cd);
    }

    private void readConstant(Entity entity, ConfigContext context) {
        Map<String, String> property = entity.getProperty();
        lineNum = entity.getLineNum();

        ConstantDefinition cd = new ConstantDefinition(entity.getLineNum(), entity.getDocName());
        if (StringUtils.isEmpty(property.get(PROPERTY_ID)))
            Assert.illegalDefinitionException(Assert.VALIDATION_ID_UNSPECIFIED, lineNum, docName);
        cd.setId(property.get(PROPERTY_ID));

        CheckType type = CheckType.fromName(property.get(PROPERTY_TYPE));
        // todo:type compatible
//        if (type.equals())
        // 不能同时定义field和value
        if ((StringUtils.isNotEmpty(property.get(PROPERTY_CLASS)) || StringUtils.isNotEmpty(property.get(PROPERTY_FIELD))) && StringUtils.isNotEmpty(property.get(PROPERTY_VALUE))) {
            Assert.illegalDefinitionException("only define either in constant definition", lineNum, docName);
        }
        String className = property.get(PROPERTY_CLASS);
        String fieldName = property.get(PROPERTY_FIELD);
        if (StringUtils.isNotEmpty(className) && StringUtils.isNotEmpty(fieldName)) {
            Class clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                clazz = context.getClasses().get(className).getClazz();
                if (clazz == null)
                    Assert.illegalDefinitionException("class '" + className + "' does not exist", lineNum, docName);
            }
            try {
                cd.setValue(ReflectUtils.getConstantValue(clazz, fieldName));
                context.getConstants().put(cd.getId(), cd);
            } catch (IllegalAccessException e) {
                Assert.runtimeException("field " + fieldName + " of object Class: " + clazz + " does not exist");
            } catch (NoSuchFieldException e) {
                Assert.runtimeException("couldn't access field " + fieldName + " of object Class: " + clazz);
            }
        }

    }


    private void readValidations(Entity entity, ConfigContext context) {
        Map<String, String> property = entity.getProperty();
        lineNum = entity.getLineNum();

        ValidationDefinition vd = new ValidationDefinition(property.get(PROPERTY_ID), entity.getLineNum(), entity.getDocName());
        this.vd = vd;

        if (ValidationChecker.validations.containsKey(vd.getId()))
            Assert.entityIDException(Assert.DUPLICATED_VALIDATION_ID, vd.getId(), vd.getLineNum(), vd.getDocName());

        Map<String, Class> classMap = new HashMap<>();
        if (StringUtils.isEmpty(property.get(PROPERTY_CLASS)))
            Assert.illegalDefinitionException("class not specified", vd.getLineNum(), vd.getDocName());
        String[] classNames = property.get(PROPERTY_CLASS).split(",");
        for (String name : classNames)
            try {
                Class clazz = Class.forName(name);
                classMap.put(ReflectUtils.getRefClassName(name), clazz);
            } catch (ClassNotFoundException e) {
                ClassDefinition clazz = context.getClasses().get(name);
                if (clazz == null)
                    Assert.runtimeException("class name:'" + name + "' defined in validation id:" + vd.getId() + " does not refer to an class definition or existing Class, at line " + entity.getLineNum() + " in " + entity.getDocName());
                classMap.put(name, clazz.getClazz());
            }

        vd.setClasses(classMap);
        resolveDefinition(entity.getSubs(), vd);
        ValidationChecker.validations.put(vd.getId(), vd);
    }

    private void resolveDefinition(List<Entity> entities, ParentElement pe) {
        next:
        for (Entity entity : entities) {
            Map<String, String> property = entity.getProperty();
            lineNum = entity.getLineNum();
            switch (DefinitionType.fromName(entity.getName())) {
                case CHECK:
                    CheckDefinition check = new CheckDefinition(lineNum, docName);
                    setCheckProperty(entity, check, vd.getClasses());
                    pe.addCheck(check);
                    break;
                case CONDITION:
                    ConditionDefinition condition = new ConditionDefinition(lineNum, docName);
                    if (StringUtils.isNotEmpty(property.get(PROPERTY_REF))) {
                        setConditionProperty(entity, condition, vd);
                    }
                    if (StringUtils.isNotEmpty(property.get(PROPERTY_ID))) {
                        String id = property.get(PROPERTY_ID);
                        if (vd.getConditionIdMap().containsKey(id))
                            Assert.entityIDException("duplicated check id defined", id, entity.getLineNum(), entity.getDocName());
                        vd.getConditionIdMap().put(id, condition);
                    }
                    //todo:change to
                    if (property.containsKey(PROPERTY_FIELD)) {
                        CheckDefinition subCd = new CheckDefinition(entity.getLineNum(), entity.getDocName());
                        setCheckProperty(entity, subCd, vd.getClasses());
                        condition.addRefCheck(subCd);
                    }
                    if (CollectionUtils.isNotEmpty(entity.getSubs()))
                        resolveDefinition(entity.getSubs(), condition);
                    pe.addCondition(condition);
                    break;
                case UNKNOWN:
                    break next;
            }
        }
    }


    private void setCheckProperty(Entity entity, CheckDefinition cd, Map<String, Class> classMap) {
        Map<String, String> property = entity.getProperty();
        checkDefinitionLegal(property);

        int lineNum = entity.getLineNum();
        String docName = entity.getDocName();

        if (StringUtils.isEmpty(property.get(PROPERTY_TYPE)))
            Assert.illegalDefinitionException("check type undefined", lineNum, docName);
//        CheckType type = CheckType.fromName(property.get(PROPERTY_TYPE));
        String s = property.get(PROPERTY_TYPE);
        CheckType type = CheckType.fromName(s);
        if (type.equals(CheckType.UNKNOWN))
            Assert.illegalDefinitionException("unknown check type:'" + type.getName() + "'", lineNum, docName);
        cd.setCheckType(type);


        cd.setFields(resolveFields(property.get(PROPERTY_FIELD), classMap));
        cd.set_fields(resolveFields(property.get(PROPERTY__FIELD), classMap));
        //todo:type compatible with checkDefinition type
        cd.setVals(resolveValue(property.get(PROPERTY_VALUE), context.getConstants()));
        cd.set_vals(resolveValue(property.get(PROPERTY__VALUE), context.getConstants()));

        cd.setCmpt(resolve(property.get(PROPERTY_CMPT), NumberAssociativeOperator.class, type));
        cd.set_cmpt(resolve(property.get(PROPERTY__CMPT), NumberAssociativeOperator.class, type));
        cd.setLogic(resolve(property.get(PROPERTY_LOGIC), LogicAssociativeOperator.class, type));
        cd.set_logic(resolve(property.get(PROPERTY__LOGIC), LogicAssociativeOperator.class, type));

//        cd.setCmpt(resolveCmpt(property.get(PROPERTY_CMPT), lineNum, docName));
//        cd.set_cmpt(resolveCmpt(property.get(PROPERTY__CMPT), lineNum, docName));
//        cd.setLogic(resolveLogic(property.get(PROPERTY_LOGIC), lineNum, docName));
//        cd.set_logic(resolveLogic(property.get(PROPERTY__LOGIC), lineNum, docName));

        cd.setOperator(resolveOperator(property.get(PROPERTY_OPERATOR), lineNum, docName));
        cd.setMsg(property.get("msg"));

        checkParamsLegal(cd);
    }

    private void checkDefinitionLegal(Map<String, String> map) {
        if ((StringUtils.isEmpty(map.get(PROPERTY_FIELD)) && StringUtils.isEmpty(map.get(PROPERTY_VALUE))) ||
                (StringUtils.isEmpty(map.get(PROPERTY__FIELD)) && StringUtils.isEmpty(map.get(PROPERTY__VALUE)))) {
            Assert.illegalDefinitionException("check definition illegal, compare value expected for the operator", lineNum, docName);
        }
    }

    private void checkParamsLegal(CheckDefinition cd) {
        if (!vd.getClasses().values().containsAll(cd.getFields().keySet()) ||
                !vd.getClasses().values().containsAll(cd.get_fields().keySet())) {
            Assert.illegalDefinitionException("class definition error", vd.getLineNum(), vd.getDocName());
        }
        if (cd.getCheckType().equals(CheckType.NUMBER)) {
            cd.getVals().stream().filter(s -> (!s.equals("null")) && (!StringUtils.isDigit(s))).forEach(s -> Assert.illegalDefinitionException("the value '" + s + "' is not a digit in val", cd.getLineNum(), cd.getDocName()));
            cd.setVals(cd.getVals().stream().filter(s -> StringUtils.isDigit(s) || s.equals("null")).collect(Collectors.toList()));
            cd.get_vals().stream().filter(s -> !s.equals("null") && !StringUtils.isDigit(s)).forEach(s -> Assert.illegalDefinitionException("the value '" + s + "' is not a digit in _val", cd.getLineNum(), cd.getDocName()));
            cd.set_vals(cd.get_vals().stream().filter(s -> StringUtils.isDigit(s) || s.equals("null")).collect(Collectors.toList()));
        }
        if (!cd.getCheckType().getSupportedOperators().contains(cd.getOperator()))
            runtimeException("unsupported operator:'" + cd.getOperator().getValue() + "' in check type " + cd.getCheckType().getName() + " ,at line " + cd.getLineNum());
        if (cd.get_vals().contains("null") && (cd.getOperator() != Operator.EQUAL && cd.getOperator() != Operator.NOT_EQUAL)) {
            Assert.illegalDefinitionException("unsupported operator:'" + cd.getOperator().getValue() + "' in null checking", cd.getLineNum(), cd.getDocName());
        }
    }

    private void setConditionProperty(Entity entity, ConditionDefinition cd, ValidationDefinition vd) {
        Map<String, String> property = entity.getProperty();
        String[] refIDs = property.get(PROPERTY_REF).split(SPLITTER);
        List<CheckDefinition> refs = new ArrayList<>();
        for (String s : refIDs) {
            if (vd.getConditionIdMap().get(s) == null)
                Assert.entityIDException("cannot find check with specified id.", s, entity.getLineNum(), entity.getDocName());
            refs.addAll(vd.getConditionIdMap().get(s).getRefChecks());
        }
        if (property.containsKey(PROPERTY_MSG))
            cd.setMsg(property.get(PROPERTY_MSG));
        else
            cd.setMsg(refs.get(0).getMsg());
        cd.setRefChecks(refs);
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

    /* 在String.split()中使用"."作为分隔符，必须写成"[.]"的形式 */
    private Map<Class, String[]> resolveFields(String fieldProperty, Map<String, Class> classMap) {
        Map<Class, String[]> fieldMap = new HashMap<>();

        if (StringUtils.isNotEmpty(fieldProperty)) {
            String[] fieldNames = fieldProperty.split(SPLITTER);
            for (String s : fieldNames) {
                Class clazz = null;
                String[] fields = null;
                // 单类校验可以不指定类名
                if (classMap.size() == 1) {
                    if (s.contains(DOT) && classMap.containsKey(s.substring(0, s.indexOf(DOT))))
                        fields = s.substring(s.indexOf(DOT) + 1, s.length()).split(SPLIT_DOT);
                    else
                        fields = s.split(SPLIT_DOT);
                    clazz = (Class) classMap.values().toArray()[0];
                }
                // 多类校验必须指定类名
                else {
                    if (s.contains(DOT)) {
                        String clazzName = s.substring(0, s.indexOf(DOT));
                        if (classMap.get(clazzName) == null) {
                            Assert.runtimeException("class not specified in multi Classes validation or" +
                                    " reference class:'" + s + "' does not exist, at line " + lineNum);
                        } else {
                            clazz = classMap.get(clazzName);
                            fields = s.substring(s.indexOf(DOT) + 1, s.length()).split(SPLIT_DOT);
                        }
                    } else {
                        Assert.runtimeException("class not specified in multi Classes validation, at line " + lineNum);
                    }
                }
                checkFieldAccessible(clazz, fields, lineNum);
                fieldMap.put(clazz, fields);
            }
        }

        return fieldMap;
    }

    private List<String> resolveValue(String value, Map<String, ConstantDefinition> map) {
        List<String> vals = new ArrayList<>();

        if (StringUtils.isNotEmpty(value)) {
            for (String s : value.split(SPLITTER)) {
                ConstantDefinition cd = map.get(s);
                if (cd != null)
                    vals.add(cd.getValue().toString());
                else
                    vals.add(s);
            }
        }

        return vals;
    }

    public MultivariateOperator resolve(String attr, Class clazz, CheckType type) {
        if (StringUtils.isEmpty(attr))
            return (AssociativeOperator) ReflectUtils.invokeStatic(clazz, "getDefault");
        AssociativeOperator operator = (AssociativeOperator) ReflectUtils.invokeStatic(clazz, "fromName", attr);
        if (operator == null)
            Assert.unsupportedOperator(attr, type, lineNum, docName);
        return operator;
    }

//    private NumberAssociativeOperator resolveCmpt(String s, int lineNum, String docName) {
//        if (StringUtils.isEmpty(s))
//            return NumberAssociativeOperator.UNKNOWN;
//        NumberAssociativeOperator operate = NumberAssociativeOperator.fromName(s);
//        if (operate.equals(NumberAssociativeOperator.UNKNOWN))
//            Assert.runtimeException("unknown compute operator:'" + s + "' at line " + lineNum + " in " + docName);
//        return operate;
//    }

//    private OperatorAttribute resolveOptr(String s) {
//        if (StringUtils.isEmpty(s))
//            return OperatorAttribute.getDefault();
//    }

//    private LogicAssociativeOperator resolveLogic(String s, int lineNum, String docName) {
//        if (StringUtils.isEmpty(s))
//            return LogicAssociativeOperator.AND;
//        LogicAssociativeOperator logic = LogicAssociativeOperator.fromName(s);
//        if (logic.equals(LogicAssociativeOperator.UNKNOWN))
//            Assert.runtimeException("unknown logic operator:'" + s + "' at line " + lineNum + " in " + docName);
//        return logic;
//    }

    private Operator resolveOperator(String s, int lineNum, String docName) {
        if (StringUtils.isEmpty(s))
            Assert.runtimeException("operator not specified at line " + lineNum + " in " + docName);
        Operator operator = Operator.fromName(s);
        if (operator.equals(Operator.UNKNOWN))
            Assert.runtimeException("unknown operator '" + s + "' at line " + lineNum + " in " + docName);
        return operator;
    }

    private void checkFieldAccessible(Class clazz, String[] fieldName, int line) {
        for (int i = 0; i < fieldName.length; i++) {
            try {
                Field field = clazz.getDeclaredField(fieldName[i]);
                if (ReflectUtils.isMapClass(field.getType()))
                    break;
                if (!Modifier.isPublic(field.getModifiers()))
                    clazz.getDeclaredMethod(ReflectUtils.genGetMethod(fieldName[i]));
                clazz = field.getType();
            } catch (NoSuchFieldException e) {
                Assert.runtimeException("field '" + fieldName[i] + "' in Class " + clazz.toString() + " does not exist, at line " + line);
            } catch (NoSuchMethodException e) {
                Assert.runtimeException("cannot access field '" + fieldName[i] + "' in Class " + clazz.toString() + ", the field is neither public or without " +
                        "a get method " + ReflectUtils.genGetMethod(fieldName[i]) + "may be set, at line " + line);
            }
        }
    }

}
