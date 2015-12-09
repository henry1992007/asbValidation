package com.company.enums;

/**
 * Created by henry on 15/12/4.
 */
public enum DefinitionType {
    UNKNOWN(0, "unknown"),
    CHECK(1, "check"),
    CONDITION(2, "condition");

    private int code;
    private String name;

    DefinitionType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static DefinitionType fromCode(int code) {
        for (DefinitionType e : values())
            if (e.getCode() == code)
                return e;
        return null;
    }

    public static DefinitionType fromName(String name) {
        for (DefinitionType e : values())
            if (e.getName().equals(name.toLowerCase()))
                return e;
        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
