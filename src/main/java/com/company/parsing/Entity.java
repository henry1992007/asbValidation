package com.company.parsing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 15/11/16.
 */
public class Entity {
    private String name;
    private int lineNum;
    private String docName;
    private Map<String, String> property = new HashMap<>();
    private List<Entity> subs;

    public Entity(String name, int lineNum, String docName) {
        this.name = name;
        this.lineNum = lineNum;
        this.docName = docName;
    }

    public String getName() {
        return name;
    }

    public int getLineNum() {
        return lineNum;
    }

    public String getDocName() {
        return docName;
    }

    public Map<String, String> getProperty() {
        return property;
    }

    public void setProperty(String key, String value) {
        property.put(key, value);
    }

    public List<Entity> getSubs() {
        return subs;
    }

    public void setSubs(List<Entity> subs) {
        this.subs = subs;
    }

    @Override
    public String toString() {
        return "name:" + name + ", line:" + lineNum + ", docName:" + docName + "\n";
    }
}
