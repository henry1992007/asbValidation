package com.company;

import com.company.element.*;
import com.company.enums.CheckType;
import com.company.enums.DefinitionType;
import com.company.enums.Operator;
import com.company.parsing.Entity;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.ReflectUtils;
import com.company.utils.StringUtils;
import com.company.validations.AggregateOperator;
import com.company.validations.MultivariateOperator;
import com.company.validations.MultivariateOperators;
import com.company.validations.ValidationChecker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.company.EntityAttributes.*;
import static com.company.utils.Assert.runtimeException;

/**
 * Created by henry on 16/1/5.
 */
public class ValidationResolver extends AbstractEntityResolver {

    public final static String SPLITTER = ",";
    public final static String DOT = ".";
    public final static String SPLIT_DOT = "[.]";

    private ValidationDefinition vd;
    private ConfigContext context;

    @Override
    public void resolve(Entity entity, ConfigContext context) {
        super.resolve(entity, context);

        Map<String, String> property = entity.getProperty();

        ValidationDefinition vd = new ValidationDefinition(property.get(ID), entity.getLineNum(), docPath);
        this.vd = vd;
        this.context = context;

        if (ValidationChecker.validations.containsKey(vd.getId()))
            Assert.entityIDException(Assert.DUPLICATED_VALIDATION_ID, vd.getId(), vd.getLineNum(), docPath);

        Map<String, Class> classMap = new HashMap<>();
        if (StringUtils.isEmpty(property.get(CLASS)))
            Assert.illegalDefinitionException("class not specified", vd.getLineNum(), docPath);
        String[] classNames = property.get(CLASS).split(",");
        for (int i = 0; i < classNames.length; i++) {
            String name = classNames[i];
            try {
                Class clazz = Class.forName(name);
                classMap.put(ReflectUtils.getRefClassName(name), clazz);
                if (i == 0)
                    vd.setMainClass(clazz);
            } catch (ClassNotFoundException e) {
                ClassDefinition clazz = context.getClasses().get(name);
                if (clazz == null)
                    Assert.runtimeException("class name:'" + name + "' defined in validation id:" + vd.getId() + " does not refer to an class definition or existing Class, at line " + entity.getLineNum() + " in " + docPath);
                classMap.put(name, clazz.getClazz());
                if (i == 0)
                    vd.setMainClass(clazz.getClazz());
            }
        }

        vd.setClasses(classMap);
        resolveDefinition(entity.getChildEntities(), vd, vd);
        ValidationChecker.validations.put(vd.getId(), vd);
    }

    private void resolveDefinition(List<Entity> entities, ParentElement pe, ValidationDefinition vd) {
        next:
        for (Entity entity : entities) {
            Map<String, String> property = entity.getProperty();
            lineNum = entity.getLineNum();
            switch (DefinitionType.fromName(entity.getName())) {
                case CHECK:
                    CheckDefinition check = new CheckDefinition(lineNum);
                    setCheckProperty(entity, check, vd.getClasses());
                    pe.addCheck(check);
                    break;
                case CONDITION:
                    ConditionDefinition condition = new ConditionDefinition(lineNum);
                    if (StringUtils.isNotEmpty(property.get(REF))) {
                        setConditionProperty(entity, condition, vd);
                    }
                    if (StringUtils.isNotEmpty(property.get(ID))) {
                        String id = property.get(ID);
                        if (vd.getConditionIdMap().containsKey(id))
                            Assert.entityIDException("duplicated check id defined", id, entity.getLineNum(), docPath);
                        vd.getConditionIdMap().put(id, condition);
                    }
                    //todo:change to
                    if (property.containsKey(FIELD)) {
                        CheckDefinition subCd = new CheckDefinition(entity.getLineNum());
                        setCheckProperty(entity, subCd, vd.getClasses());
                        condition.addRefCheck(subCd);
                    }
                    if (CollectionUtils.isNotEmpty(entity.getChildEntities()))
                        resolveDefinition(entity.getChildEntities(), condition, vd);
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

        if (StringUtils.isEmpty(property.get(TYPE)))
            Assert.illegalDefinitionException("check type undefined", lineNum, docPath);
        String s = property.get(TYPE);
        CheckType type = CheckType.fromName(s);
        if (type == null)
            Assert.illegalDefinitionException("unknown check type:'" + s + "'", lineNum, docPath);
        cd.setCheckType(type);


        cd.setFields(resolveFields(property.get(FIELD), classMap));
        cd.set_fields(resolveFields(property.get(_FIELD), classMap));
        //todo:type compatible with checkDefinition type
        cd.setVals(resolveValue(property.get(VALUE), context.getConstants()));
        cd.set_vals(resolveValue(property.get(_VALUE), context.getConstants()));

        cd.setCmpt(resolve(property.get(CMPT), type));
        cd.set_cmpt(resolve(property.get(_CMPT), type));
        cd.setLogic((AggregateOperator<Boolean>) resolve(property.get(LOGIC)));
        cd.set_logic((AggregateOperator<Boolean>) resolve(property.get(_LOGIC)));

        cd.setOperator(resolveOperator(property.get(OPERATOR), lineNum, docPath));
        cd.setMsg(property.get("msg"));

        checkParamsLegal(cd);
    }

    private void checkDefinitionLegal(Map<String, String> map) {
        if ((StringUtils.isEmpty(map.get(FIELD)) && StringUtils.isEmpty(map.get(VALUE))) ||
                (StringUtils.isEmpty(map.get(_FIELD)) && StringUtils.isEmpty(map.get(_VALUE)))) {
            Assert.illegalDefinitionException("check definition illegal, compare value expected for the operator", lineNum, docPath);
        }
    }

    private void checkParamsLegal(CheckDefinition cd) {
        if (!vd.getClasses().values().containsAll(cd.getFields().stream().map(FieldPath::getClazz).collect(Collectors.toList())) ||
                !vd.getClasses().values().containsAll(cd.get_fields().stream().map(FieldPath::getClazz).collect(Collectors.toList()))) {
            Assert.illegalDefinitionException("class definition error", vd.getLineNum(), docPath);
        }
        if (cd.getCheckType().equals(CheckType.NUMBER)) {
            cd.getVals().stream().filter(s -> (!s.equals("null")) && (!StringUtils.isDigit(s))).forEach(s -> Assert.illegalDefinitionException("the value '" + s + "' is not a digit in val", cd.getLineNum(), docPath));
            cd.setVals(cd.getVals().stream().filter(s -> StringUtils.isDigit(s) || s.equals("null")).collect(Collectors.toList()));
            cd.get_vals().stream().filter(s -> !s.equals("null") && !StringUtils.isDigit(s)).forEach(s -> Assert.illegalDefinitionException("the value '" + s + "' is not a digit in _val", cd.getLineNum(), docPath));
            cd.set_vals(cd.get_vals().stream().filter(s -> StringUtils.isDigit(s) || s.equals("null")).collect(Collectors.toList()));
        }
        if (!cd.getCheckType().getSupportedOperators().contains(cd.getOperator()))
            runtimeException("unsupported operator:'" + cd.getOperator().getValue() + "' in check type " + cd.getCheckType().getName() + " ,at line " + cd.getLineNum());
        if (cd.get_vals().contains("null") && (cd.getOperator() != Operator.EQUAL && cd.getOperator() != Operator.NOT_EQUAL)) {
            Assert.illegalDefinitionException("unsupported operator:'" + cd.getOperator().getValue() + "' in null checking", cd.getLineNum(), docPath);
        }
    }

    private void setConditionProperty(Entity entity, ConditionDefinition cd, ValidationDefinition vd) {
        Map<String, String> property = entity.getProperty();
        String[] refIDs = property.get(REF).split(SPLITTER);
        List<CheckDefinition> refs = new ArrayList<>();
        for (String s : refIDs) {
            if (vd.getConditionIdMap().get(s) == null)
                Assert.entityIDException("cannot find check with specified id.", s, entity.getLineNum(), docPath);
            refs.addAll(vd.getConditionIdMap().get(s).getRefChecks());
        }
        if (property.containsKey(MSG))
            cd.setMsg(property.get(MSG));
        else
            cd.setMsg(refs.get(0).getMsg());
        cd.setRefChecks(refs);
    }

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
                FieldPath fieldPath = new FieldPath(clazz, fields);
                if (clazz.equals(vd.getMainClass()))
                    fieldPath.setMain(true);
                checkFieldAccessible(clazz, fields);
                fieldPaths.add(fieldPath);
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
            Assert.unsupportedOperator(attr, type, lineNum, docPath);
        return MultivariateOperators.get(attr);
    }

    public MultivariateOperator resolve(String attr) {
        if (StringUtils.isEmpty(attr))
            return MultivariateOperators.get("and");
        if (!MultivariateOperators.contains(attr))
            Assert.runtimeException("unsupported logic operator:'" + attr + "'");
        return MultivariateOperators.get(attr);
    }

    private Operator resolveOperator(String s, int lineNum, String docName) {
        if (StringUtils.isEmpty(s))
            Assert.runtimeException("operator not specified at line " + lineNum + " in " + docName);
        Operator operator = Operator.fromName(s);
        if (operator.equals(Operator.UNKNOWN))
            Assert.runtimeException("unknown operator '" + s + "' at line " + lineNum + " in " + docName);
        return operator;
    }

    /**
     * 该属性可以被访问必须满足以下条件之一:
     * 1.被public修饰
     * 2.存在该属性的getter
     * <p>
     * 该方法遵循以下算法
     * 假设类C是当前类
     * 1.如果该属性是C或者C父类的public属性,则该属性可以访问,用这个属性的类代替C并重1开始,否则执行2
     * 2.如果C或者C的父类拥有该属性的public getter,则该属性可以访问,用这个属性的类代替C并重1开始,否则抛出异常
     *
     * @param clazz      需要访问的类链的第一个类
     * @param fieldNames 需要访问属性的路径
     */
    private void checkFieldAccessible(Class clazz, String[] fieldNames) {
        for (String fieldName : fieldNames)
            try {
                if (ReflectUtils.isMapClass(clazz)) {
                    break;
                } else {
                    Field field = clazz.getField(fieldName);
                    clazz = field.getType();
                }
            } catch (NoSuchFieldException e) {
                try {
                    clazz = clazz.getMethod(ReflectUtils.genGetMethod(fieldName)).getReturnType();
                } catch (NoSuchMethodException e1) {
                    Assert.runtimeException("cannot access field '" + fieldName + "' in Class " + clazz.toString() + ", the field might do not exist, " +
                            "be public or have a accessible public getter, at line " + lineNum + ", " + docPath);
                }
            }
    }

}
