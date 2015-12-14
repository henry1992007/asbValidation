package com.company.utils;

import javax.sql.rowset.serial.SerialArray;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
        if (clazz.isInterface()) {
            return clazz.equals(Map.class) || searchInterface(clazz.getInterfaces());
        } else {
            return searchClass(clazz);
        }
    }

    public static boolean searchInterface(Class[] clazzes) {
        for (Class clazz1 : clazzes)
            if (clazz1.equals(Map.class) || searchInterface(clazz1.getInterfaces()))
                return true;
        return false;
    }

    public static boolean searchClass(Class clazz) {
        while (clazz != null) {
            if (searchInterface(clazz.getInterfaces()))
                return true;
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    public static String getRefClassName(String s) {
        return StringUtils.firstToLowerCase(s.substring(s.lastIndexOf(".") + 1, s.length()));
    }

    public static Object invokeStatic(Class clazz, String mName, Object... args) {
        return invoke(null, clazz, mName, args);
    }

    public static Object invoke(Object target, String mName, Object... args) {
        return invoke(target, target.getClass(), mName, args);
    }

    public static Object invoke(Object target, Class<?> clazz, String mName, Object... args) {
        try {
            Method method = clazz.getDeclaredMethod(mName, getParamClass(args));
            return method.invoke(target, args);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Class[] getParamClass(Object... args) {
        List<Class> classes = new ArrayList<>();
        for (Object o : args)
            classes.add(o.getClass());
        return classes.toArray(new Class[classes.size()]);
    }

}
