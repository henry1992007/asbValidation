package com.company.utils;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by henry on 15/11/10.
 */
public abstract class Utils {
    static boolean debug = true;

    public Utils() {
    }


    public static boolean CollectionEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean CollectionNotEmpty(Collection<?> c) {
        return !CollectionEmpty(c);
    }

    public static <T> boolean ArrayEmpty(T[] a) {
        return a == null || a.length == 0;
    }

    public static <T> boolean ArrayNotEmpty(T[] a) {
        return !ArrayEmpty(a);
    }

    public static <T> T getMax(List<T> list) {
        T res = list.get(0);
        for (T t : list)
            if (((Comparable) t).compareTo(res) > 0)
                res = t;
        return res;
    }

    public static Comparable getMin(Comparable[] var1) {
        Comparable c = var1[0];
        for (Comparable var2 : var1)
            if (var2.compareTo(c) < 0)
                c = var2;
        return c;
    }

    public static <T> T getFirstNotNull(T... list) {
        for (T var : list)
            if (var != null)
                return var;
        return null;
    }

    public static void info(String s) {
        if (debug)
            System.out.println(s);
    }

    public static void err(String s) {
        if (debug)
            System.err.println(s);
    }

}
