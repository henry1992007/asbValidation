package com.company.parsing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 15/11/16.
 */
public class Entity {

    /**
     * xml中tag的名称
     */
    private String name;

    /**
     * 该entity所在xml文档中的行数
     */
    private int lineNum;

    /**
     * tag属性和值
     */
    private Map<String, String> property;

    /**
     * 子tag
     */
    private List<Entity> childEntities;


    public Entity(String name, int lineNum) {
        this.name = name;
        this.lineNum = lineNum;
    }

    public String getName() {
        return name;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void putProperty(String k, String v) {
        if (property == null)
            property = new HashMap<>();
        property.put(k, v);
    }

    public Map<String, String> getProperty() {
        return property;
    }

    public List<Entity> getChildEntities() {
        return childEntities;
    }

    public void setChildEntities(List<Entity> childEntities) {
        this.childEntities = childEntities;
    }

    @Override
    public String toString() {
        return "name:" + name + ", line:" + lineNum;
    }
}
