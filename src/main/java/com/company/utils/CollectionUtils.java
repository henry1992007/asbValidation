package com.company.utils;

import com.company.validations.FunctionXY;

import javax.security.auth.callback.Callback;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

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

    public static <T> boolean isNotEmpty(T[] a) {
        return !ArrayEmpty(a);
    }

    public static <T> List<T> map(List<T> a, Function<T, T> f) {
        for (int i = 0; i < a.size(); i++)
            a.set(i, f.apply(a.get(i)));
        return a;
    }

    public static <T> List<T> reduce(List<T> a, FunctionXY<T> f) {
        for (int i = 1; i < a.size(); i++)
            a.set(0, f.apply(a.get(0), a.get(i)));
        return Collections.singletonList(a.get(0));
    }

    public static <T> boolean equal(List<T> a) {
        for (int i = 1; i < a.size(); i++)
            if (!a.get(0).equals(a.get(i)))
                return false;
        return true;
    }

    public static <T, S, R> R anyMatchAndThen(List<T> list, Predicate<T> predicate, Function<S, R> andThen, S o) {
        if (list.stream().anyMatch(predicate) && andThen != null)
            return andThen.apply(o);
        return null;
    }

}
