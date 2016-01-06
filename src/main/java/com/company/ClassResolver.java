package com.company;

import com.company.element.ClassDefinition;
import com.company.parsing.Entity;
import com.company.utils.Assert;
import com.company.utils.StringUtils;

import java.util.Map;

import static com.company.EntityAttributes.*;

/**
 * Created by henry on 16/1/5.
 */
public class ClassResolver extends AbstractEntityResolver {

    @Override
    public void resolve(Entity entity, ConfigContext context) {
        super.resolve(entity, context);

        Map<String, String> property = entity.getProperty();

        ClassDefinition cd = new ClassDefinition(property.get(ID), entity.getLineNum(), entity.getDocName());
        if (context.getClasses().containsKey(cd.getId()))
            Assert.entityIDException(Assert.DUPLICATED_CLASS_ID, cd.getId(), lineNum, docName);

        String className = property.get(CLASS);
        try {
            if (StringUtils.isEmpty(className))
                Assert.illegalDefinitionException(Assert.CLASS_NAME_UNSPECIFIED, lineNum, docName);
            cd.setClazz(Class.forName(className));
        } catch (ClassNotFoundException e) {
            Assert.illegalDefinitionException(Assert.CLASS_NOT_FOUND + ":'" + className + "'", lineNum, docName);
        }

        context.getClasses().put(cd.getId(), cd);
    }

}
