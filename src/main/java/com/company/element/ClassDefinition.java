package com.company.element;

import com.company.utils.Assert;
import com.company.utils.StringUtils;

/**
 * Created by henry on 15/11/16.
 */
public class ClassDefinition extends AbstractElementDefinition {

    private Class clazz;

    public ClassDefinition(String id, int lineNum, String docName) {
        super(lineNum, docName);
        if (StringUtils.isEmpty(id))
            Assert.illegalDefinitionException(Assert.CLASS_ID_UNSPECIFIED, lineNum, docName);
        setId(id);
    }


    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }
}
