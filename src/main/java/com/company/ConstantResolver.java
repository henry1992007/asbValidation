package com.company;

import com.company.element.ConstantDefinition;
import com.company.enums.CheckType;
import com.company.parsing.Entity;
import com.company.utils.Assert;
import com.company.utils.ReflectUtils;
import com.company.utils.StringUtils;

import java.util.Map;

import static com.company.EntityAttributes.*;

/**
 * Created by henry on 16/1/5.
 */
public class ConstantResolver extends AbstractEntityResolver {

    @Override
    public void resolve(Entity entity, ConfigContext context) {
        super.resolve(entity, context);
        Map<String, String> property = entity.getProperty();

        ConstantDefinition cd = new ConstantDefinition(entity.getLineNum());
        if (StringUtils.isEmpty(property.get(ID)))
            Assert.illegalDefinitionException(Assert.VALIDATION_ID_UNSPECIFIED, lineNum, docPath);
        cd.setId(property.get(ID));

        CheckType type = CheckType.fromName(property.get(TYPE));
        // todo:type compatible
//        if (type.equals())
        // 不能同时定义field和value
        if ((StringUtils.isNotEmpty(property.get(CLASS)) || StringUtils.isNotEmpty(property.get(FIELD))) && StringUtils.isNotEmpty(property.get(VALUE))) {
            Assert.illegalDefinitionException("only define either in constant definition", lineNum, docPath);
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
                    Assert.illegalDefinitionException("class '" + className + "' does not exist", lineNum, docPath);
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

}
