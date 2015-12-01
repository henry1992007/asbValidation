package com.company.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 15/11/16.
 */
public class Entity {
    private String name;
    private String id;
    private int lineNum;
    private Map<String, String> property = new HashMap<>();
    private List<Entity> subs = new ArrayList<>();

    public Entity() {
    }

    public Entity(String name, int lineNum) {
        this.name = name;
        this.lineNum = lineNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public Map<String, String> getProperty() {
        return property;
    }

    public void setProperty(Map<String, String> property) {
        this.property = property;
    }

    public List<Entity> getSubs() {
        return subs;
    }

    public void setSubs(List<Entity> subs) {
        this.subs = subs;
    }
}
