package com.company.utils;

import java.util.function.Function;

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

    public static String firstToLowerCase(String s) {
        if (isEmpty(s))
            return s;
        return s.substring(0, 1).toLowerCase().concat(s.substring(1, s.length()));
    }

    public static String firstToCapital(String s) {
        if (isEmpty(s))
            return s;
        return s.substring(0, 1).toUpperCase().concat(s.substring(1, s.length()));
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

    public static String[] splitContainRegex(String s, String regex) {
        String[] strings = s.split(regex);
        for (int i = 0; i < strings.length; i++)
            strings[i] = strings[i].concat(regex);
        return strings;
    }

}
