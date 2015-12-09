package com.company.utils;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by henry on 15/12/3.
 */
public abstract class ReflectUtils {

    public ReflectUtils() {
    }

    public static String genGetMethod(String s) {
        return "get" + StringUtils.firstToCapital(s);
    }

    public static String genSetMethod(String s) {
        return "set" + StringUtils.firstToCapital(s);
    }

    public static boolean isNumberClass(Class clazz) {
        while (!clazz.equals(Object.class)) {
            clazz = clazz.getSuperclass();
            if (clazz.equals(Number.class))
                return true;
        }
        return false;
    }

    public static boolean isMapClass(Class clazz) {
        while (!clazz.equals(Object.class)) {
            if (Arrays.asList(clazz.getInterfaces()).contains(Map.class))
                return true;
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    public static String getRefClassName(String s) {
        return StringUtils.firstToLowerCase(s.substring(s.lastIndexOf(".") + 1, s.length()));
    }

}
