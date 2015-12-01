package com.company.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by henry on 15/11/12.
 */
public abstract class CollectionUtils {

    public CollectionUtils() {
    }

    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c);
    }

    public static <T> boolean ArrayEmpty(T[] a) {
        return a == null || a.length == 0;
    }

    public static <T> boolean ArrayNotEmpty(T[] a) {
        return !ArrayEmpty(a);
    }

    public static boolean[] toPrimitiveBooleanArray(List<Boolean> list) {
        boolean[] res = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++)
            res[i] = list.get(i);
        return res;
    }

    public static <T> T[] listsToArray(List<T>... lists) {
        List<T> res = new ArrayList<>();
        for (List<T> list : lists)
            res.addAll(list);
        return (T[]) res.toArray();
    }

}
