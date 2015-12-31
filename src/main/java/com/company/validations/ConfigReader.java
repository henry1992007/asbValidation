package com.company.validations;

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
import static com.company.EntityAttributes.*;

/**
 * Created by henry on 15/11/16.
 */
public class ConfigReader {

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

        ClassDefinition cd = new ClassDefinition(property.get(ID), entity.getLineNum(), entity.getDocName());
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
        if (StringUtils.isEmpty(property.get(ID)))
            Assert.illegalDefinitionException(Assert.VALIDATION_ID_UNSPECIFIED, lineNum, docName);
        cd.setId(property.get(ID));

        CheckType type = CheckType.fromName(property.get(TYPE));
        // todo:type compatible
//        if (type.equals())
        // 不能同时定义field和value
        if ((StringUtils.isNotEmpty(property.get(CLASS)) || StringUtils.isNotEmpty(property.get(FIELD))) && StringUtils.isNotEmpty(property.get(VALUE))) {
            Assert.illegalDefinitionException("only define either in constant definition", lineNum, docName);
        }
        String className = property.get(CLASS);
        String fieldName = property.get(FIELD);
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

        ValidationDefinition vd = new ValidationDefinition(property.get(ID), entity.getLineNum(), entity.getDocName());
        this.vd = vd;

        if (ValidationChecker.validations.containsKey(vd.getId()))
            Assert.entityIDException(Assert.DUPLICATED_VALIDATION_ID, vd.getId(), vd.getLineNum(), vd.getDocName());

        Map<String, Class> classMap = new HashMap<>();
        if (StringUtils.isEmpty(property.get(CLASS)))
            Assert.illegalDefinitionException("class not specified", vd.getLineNum(), vd.getDocName());
        String[] classNames = property.get(CLASS).split(",");
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
                    if (StringUtils.isNotEmpty(property.get(REF))) {
                        setConditionProperty(entity, condition, vd);
                    }
                    if (StringUtils.isNotEmpty(property.get(ID))) {
                        String id = property.get(ID);
                        if (vd.getConditionIdMap().containsKey(id))
                            Assert.entityIDException("duplicated check id defined", id, entity.getLineNum(), entity.getDocName());
                        vd.getConditionIdMap().put(id, condition);
                    }
                    //todo:change to
                    if (property.containsKey(FIELD)) {
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

        if (StringUtils.isEmpty(property.get(TYPE)))
            Assert.illegalDefinitionException("check type undefined", lineNum, docName);
        String s = property.get(TYPE);
        CheckType type = CheckType.fromName(s);
        if (type == null)
            Assert.illegalDefinitionException("unknown check type:'" + s + "'", lineNum, docName);
        cd.setCheckType(type);


        cd.setFields(resolveFields(property.get(FIELD), classMap));
        cd.set_fields(resolveFields(property.get(_FIELD), classMap));
        //todo:type compatible with checkDefinition type
        cd.setVals(resolveValue(property.get(VALUE), context.getConstants()));
        cd.set_vals(resolveValue(property.get(_VALUE), context.getConstants()));

        cd.setCmpt(resolve(property.get(CMPT), type));
        cd.set_cmpt(resolve(property.get(_CMPT), type));
        cd.setLogic((AssociativeOperator<Boolean>) resolve(property.get(LOGIC), type));
        cd.set_logic((AssociativeOperator<Boolean>) resolve(property.get(_LOGIC), type));

        cd.setOperator(resolveOperator(property.get(OPERATOR), lineNum, docName));
        cd.setMsg(property.get("msg"));

        checkParamsLegal(cd);
    }

    private void checkDefinitionLegal(Map<String, String> map) {
        if ((StringUtils.isEmpty(map.get(FIELD)) && StringUtils.isEmpty(map.get(VALUE))) ||
                (StringUtils.isEmpty(map.get(_FIELD)) && StringUtils.isEmpty(map.get(_VALUE)))) {
            Assert.illegalDefinitionException("check definition illegal, compare value expected for the operator", lineNum, docName);
        }
    }

    private void checkParamsLegal(CheckDefinition cd) {
        if (!vd.getClasses().values().containsAll(cd.getFields().stream().map(FieldPath::getClazz).collect(Collectors.toList())) ||
                !vd.getClasses().values().containsAll(cd.get_fields().stream().map(FieldPath::getClazz).collect(Collectors.toList()))) {
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
        String[] refIDs = property.get(REF).split(SPLITTER);
        List<CheckDefinition> refs = new ArrayList<>();
        for (String s : refIDs) {
            if (vd.getConditionIdMap().get(s) == null)
                Assert.entityIDException("cannot find check with specified id.", s, entity.getLineNum(), entity.getDocName());
            refs.addAll(vd.getConditionIdMap().get(s).getRefChecks());
        }
        if (property.containsKey(MSG))
            cd.setMsg(property.get(MSG));
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
    private List<FieldPath> resolveFields(String fieldProperty, Map<String, Class> classMap) {
        List<FieldPath> fieldPaths = new ArrayList<>();

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
                checkFieldAccessible(clazz, fields);
                fieldPaths.add(new FieldPath(clazz, fields));
            }
        }

        return fieldPaths;
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

    public MultivariateOperator resolve(String attr, CheckType type) {
        if (StringUtils.isEmpty(attr))
            return MultivariateOperators.getDefault();
        if (!type.getCmpt().contains(attr))
            Assert.unsupportedOperator(attr, type, lineNum, docName);
        return MultivariateOperators.get(attr);
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

    private void checkFieldAccessible(Class clazz, String[] fieldName) {
        for (int i = 0; i < fieldName.length; i++) {
            try {
                Field field = clazz.getDeclaredField(fieldName[i]);
                if (ReflectUtils.isMapClass(field.getType()))
                    break;
                if (!Modifier.isPublic(field.getModifiers()))
                    clazz.getDeclaredMethod(ReflectUtils.genGetMethod(fieldName[i]));
                clazz = field.getType();
            } catch (NoSuchFieldException e) {
                Assert.runtimeException("field '" + fieldName[i] + "' in Class " + clazz.toString() + " does not exist, at line " + lineNum);
            } catch (NoSuchMethodException e) {
                Assert.runtimeException("cannot access field '" + fieldName[i] + "' in Class " + clazz.toString() + ", the field is neither public or without " +
                        "a get method " + ReflectUtils.genGetMethod(fieldName[i]) + "may be set, at line " + lineNum);
            }
        }
    }

}
