package com.company;

import com.company.utils.Assert;
import com.company.utils.ReflectUtils;
import com.company.utils.StringUtils;

/**
 * Created by henry on 15/12/13.
 */
public abstract class AbstractOperatorAttributeResolver implements OperatorAttributeResolver {

    @Override
    public OperatorAttribute resolve(String attr, Class clazz, int lineNum, String docName) {
        if (StringUtils.isEmpty(attr))
            return (OperatorAttribute) ReflectUtils.invokeStatic(clazz, "getDefault");
        OperatorAttribute operator = (OperatorAttribute) ReflectUtils.invokeStatic(clazz, "fromName", attr);
        if (operator == null)
            Assert.runtimeException("unknown logic operator:'" + attr + "' at line " + lineNum + " in " + docName);
        return operator;
    }

}
