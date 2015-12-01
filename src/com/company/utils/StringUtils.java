package com.company.utils;

/**
 * Created by henry on 15/11/12.
 */
public abstract class StringUtils {

    public StringUtils() {
    }

    public static boolean isEmpty(Object string) {
        return string == null || string.equals("") || ((String) string).isEmpty();
    }

    public static boolean isNotEmpty(Object string) {
        return !isEmpty(string);
    }

    public static boolean allEmpty(Object... string) {
        for (Object s : string)
            if (isNotEmpty(s))
                return false;
        return true;
    }

    public static boolean allNotEmpty(Object... string) {
        for (Object s : string)
            if (isEmpty(s))
                return false;
        return true;
    }

    public static String firstToCapital(String s) {
        if (isEmpty(s))
            return s;
        return s.substring(0, 1).toUpperCase().concat(s.substring(1, s.length()));
    }

    public static String genGetMethod(String s) {
        return "get" + firstToCapital(s);
    }

    public static String genSetMethod(String s) {
        return "set" + firstToCapital(s);
    }

    public static boolean isDigit(String s) {
        return s.matches("-?\\d*\\.?\\d+");
    }

    public static boolean isInteger(String s) {
        return s.matches("-?\\d*");
    }

    public static boolean isFlowPoint(String s) {
        return s.matches("-?\\d*\\.\\d+");
    }

    public static boolean notEquals(String var1, String var2) {
        return !var1.equals(var2);
    }

    public static boolean equals(String var1, String var2) {
        return var1.equals(var2);
    }
}
