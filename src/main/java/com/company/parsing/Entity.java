package com.company.parsing;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by henry on 15/11/16.
 */
public class Entity {
    private String name;
    private int lineNum;
    private String docName;
    private Map<String, String> property = new HashMap<>();
    private Entity[] subs;

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

    public Entity[] getSubs() {
        return subs;
    }

    public void setSubs(Entity[] subs) {
        this.subs = subs;
    }
}
