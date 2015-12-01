package com.company.parsing;

import com.company.ConfigContext;
import com.company.MultiKeySetMap;
import com.company.element.ClassDefinition;
import com.company.element.ConditionDefinition;
import com.company.element.ConditionField;
import com.company.element.ValidationDefinition;
import com.company.enums.ConditionTypeEnum;
import com.company.enums.LogicComputeOperator;
import com.company.enums.Operator;
import com.company.utils.Assert;
import com.company.utils.StringUtils;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

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

    Map<String, ClassDefinition> classes = new HashMap<String, ClassDefinition>();
    Map<String, ConditionDefinition> refConditions = new HashMap<String, ConditionDefinition>();
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
        for (Entity entity : context.getEntities()) {
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

    private void readClasses() {
        for (String s : classEntities.keySet()) {
            ClassEntity entity = classEntities.get(s);
            ClassDefinition cd = new ClassDefinition(entity.getId(), entity.getLineNum());
            try {
                cd.setClazz(Class.forName(entity.getClassName()));
            } catch (ClassNotFoundException e) {
                Assert.runtimeException("a class definition should specify id and a class name of an existing Class. At line " + entity.getLineNum());
            }
            classes.put(s, cd);
        }
    }

    private void readRefConditions() {
        for (String s : refConditionEntities.keySet()) {
            ConditionEntity entity = refConditionEntities.get(s);
            ConditionDefinition ced = new ConditionDefinition();
            ced.setId(entity.getId());

            Operator operator = Operator.getOperator(entity.getOptr());
            if (operator.equals(Operator.UNKNOWN))
                Assert.runtimeException("unsupported operator type " + entity.getOptr() + " at line " + entity.getLineNum());
            ced.setOperator(operator);


        }
    }

    private void readValidations() {
        for (String s : validationEntities.keySet()) {
            ValidationEntity entity = validationEntities.get(s);
            ValidationDefinition vd = new ValidationDefinition(entity.getId(), entity.getLineNum());

            Map<String, Class> classMap = new HashMap<String, Class>();
            String[] classNames = entity.getClassName().split(",");
            for (String name : classNames) {
                try {
                    Class clazz = Class.forName(name);
                    classMap.put(name.substring(name.lastIndexOf(".") + 1, name.length()), clazz);
                } catch (ClassNotFoundException e) {
                    ClassDefinition clazz = classes.get(name);
                    if (clazz == null)
                        Assert.runtimeException("class name:'" + name + "' defined in validation id:" + entity.getId() + " does not refer to an class definition or existing Class, at line " + entity.getLineNum());
                    classMap.put(name, clazz.getClazz());
                }
            }
            vd.setClasses(classMap);
            vd.setConditions(resolveCondition(entity.getConditions(), vd));
            validations.put(vd.getId(), vd);
        }
    }

    private void checkTypeCompatible(ConditionDefinition ced) {
        List<Field> fields = new ArrayList<Field>();
        for (ConditionField cf : ced.getFields())
            fields.add(cf.getFields().get(cf.getFields().size() - 1));
        for (ConditionField cf : ced.getOtherFields())
            fields.add(cf.getFields().get(cf.getFields().size() - 1));

        Class clazz = parentClassMap.get(fields.get(0).getType());
        for (Field field : fields) {
            if (!clazz.equals(parentClassMap.get(field.getType())))
                Assert.runtimeException("incompatible type " + clazz + " and " + field.getType() + " in comparison, at line " + ced.getLineNum());
        }

        List<String> value = Arrays.asList(ced.getValue().split(","));
        for (String v : value)
            if (clazz.equals(Number.class) && !StringUtils.isDigit(v))
                Assert.runtimeException("incompatible type:'" + clazz + "' and value:'" + v + "' , at line " + ced.getLineNum());
    }

    private void checkOperatorCompatible(ConditionDefinition ced) {
        //todo
    }

    private ConditionDefinition[] resolveCondition(List<ConditionEntity> entities, ValidationDefinition ved) {
        List<ConditionDefinition> cds = new ArrayList<ConditionDefinition>();
        for (ConditionEntity entity : entities) {
            ConditionDefinition ced = new ConditionDefinition(entity.getId(), entity.getLineNum());

            if (StringUtils.isNotEmpty(entity.getRef())) {
                String[] refIDs = entity.getRef().split(",");
                for (String id : refIDs) {

                }
                ConditionEntity ref = refConditionEntities.get(entity.getRef());
                if (ref == null) {
                    Assert.runtimeException("reference condition id:'" + entity.getRef() + "' does not exist, at line " + entity.getLineNum());
                } else {
                    overrideAndExtendCondition(ref, entity);
                }
            }

            ced.setFields(resolveFields(entity.getFields(), ved.getClasses(), entity.getLineNum()));

            Operator operator = Operator.getOperator(entity.getOperator());
            if (operator == null || operator.equals(Operator.UNKNOWN))
                Assert.runtimeException("unsupported operator '" + entity.getOperator() + "' at line " + entity.getLineNum());
            ced.setOperator(operator);

            ced.setValue(entity.getValue());
            ced.setOtherFields(resolveFields(entity.getOtherFields(), ved.getClasses(), entity.getLineNum()));

            LogicComputeOperator logicOperator = LogicComputeOperator.AND;
            if (StringUtils.isNotEmpty(entity.getLogic())) {
                logicOperator = LogicComputeOperator.fromValue(entity.getLogic());
                if (logicOperator == null)
                    Assert.runtimeException("unsupported logic operator:'" + entity.getLogic() + "', at line " + entity.getLineNum());
            }
            ced.setLogic(logicOperator);

            ced.setMsg(entity.getMsg());
            ced.setConditionType(entity.getConditionType());
            ced.setLineNum(entity.getLineNum());
            cds.add(ced);
            if (ced.getConditionType().equals(ConditionTypeEnum.IF)) {
                ced.setSubConditions(resolveCondition(entity.getSubConditions(), ved));
            }
        }

        return cds.toArray(new ConditionDefinition[cds.size()]);
    }

    private List<ConditionField> resolveFields(String fieldName, Map<String, Class> classMap, int lineNum) {
        List<ConditionField> fieldList = new ArrayList<ConditionField>();
        List<String> fieldNames = Arrays.asList(fieldName.split(","));

        for (String s : fieldNames) {
            ConditionField conditionField = new ConditionField();
            //多类比较必须指定Class
            String clazzName = StringUtils.firstToCapital(s.substring(0, s.indexOf(".")));
            if (classMap.get(clazzName) == null) {
                if (classMap.size() > 1) {
                    Assert.runtimeException("class not specified in multi Classes validation or" +
                            "reference class:'" + s + "' does not exist, at line " + lineNum);
                } else {
                    conditionField.setClazz((Class) classMap.values().toArray()[0]);
                    conditionField.setFields(checkFieldExist((Class) classMap.values().toArray()[0], s.substring(s.indexOf(".") + 1, s.length()), lineNum));
                }
            } else {
                conditionField.setClazz(classMap.get(clazzName));
                conditionField.setFields(checkFieldExist(classMap.get(clazzName), s.substring(s.indexOf(".") + 1, s.length()), lineNum));
            }
            fieldList.add(conditionField);
        }

        return fieldList;
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

    private List<Field> checkFieldExist(Class clazz, String fieldNameStr, int line) {
        String[] fieldName = fieldNameStr.split("[.]");
        List<Field> fields = new ArrayList<Field>();

        for (int i = 0; i < fieldName.length; i++) {
            Field field = null;
            try {
                field = clazz.getField(fieldName[i]);
                fields.add(field);
                clazz.getMethod(StringUtils.genGetMethod(fieldName[i]));
            } catch (NoSuchFieldException e) {
                Assert.runtimeException("field '" + fieldName[i] + "' in Class " + clazz.toString() + " does not exist, at line " + line);
            } catch (NoSuchMethodException e) {
                Assert.runtimeException("field '" + fieldName[i] + "' in Class " + clazz.toString() + " is private or cannot be accessed, " +
                        "a get method " + StringUtils.genGetMethod(fieldName[i]) + "may be set, at line " + line);
            }
            clazz = field.getType();
        }

        return fields;
    }


}
