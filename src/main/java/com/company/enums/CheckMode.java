package com.company.enums;

/**
 * Created by henry on 15/12/29.
 */
public enum CheckMode {

    SINGLE(0),
    RELATED(1);


    private int mode;

    CheckMode(int mode) {
        this.mode = mode;
    }

    public static CheckMode get(int mode) {
        for (CheckMode e : values()) {
            if (e.getMode() == mode) {
                return e;
            }
        }
        return null;
    }

    public int getMode() {
        return mode;
    }

}
