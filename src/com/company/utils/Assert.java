package com.company.utils;

/**
 * Created by henry on 15/11/16.
 */
public abstract class Assert {
    public Assert() {
    }

//    public void assertAttributeNotEmpty(String... strings) {
//        if (!StringUtils.allNotEmpty(strings))
//            throw
//    }

    public static void runtimeException(String msg) {
        throw new RuntimeException(msg);
    }

    public static void runtimeException(String msg, Exception e) {
        throw new RuntimeException(msg, e);
    }

}
