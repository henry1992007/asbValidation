package com.company.enums;

/**
 * Created by henry on 15/11/10.
 */
public enum ConditionTypeEnum {

    REFERENCE(1),
    NORMAL(2),
    IF(3);

    private int code;

    ConditionTypeEnum(int code) {
        this.code = code;
    }

    public static ConditionTypeEnum fromCode(int code) {
        for (ConditionTypeEnum e : values()) {
            if (e.getCode() == code)
                return e;
        }

        return null;
    }

    public int getCode() {
        return code;
    }
}
