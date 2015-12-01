package com.company.element;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by henry on 15/11/19.
 */
public class ConditionField {
    private Class clazz;
    private Field[] fields;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append(clazz.toString()).append(".");
        for (Field field : fields)
            sb.append(".").append(field.getName());

        return sb.toString();
    }

}
