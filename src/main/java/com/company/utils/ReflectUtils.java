package com.company.utils;

import javax.sql.rowset.serial.SerialArray;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

    public static Object getObjectValue(Class clazz, Object checkObj, String[] fieldPath) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Object target = checkObj;
        Class objClass = clazz;

        for (int i = 0; i < fieldPath.length; i++) {
            if (ReflectUtils.isMapClass(objClass)) {
                target = ((Map) target).get(fieldPath[i]);
                //todo : handle null object
            } else {
                Field field = objClass.getDeclaredField(fieldPath[i]);
                if (Modifier.isPublic(field.getModifiers())) {
                    target = objClass.getField(fieldPath[i]).get(target);
                } else {
                    String getMethodName = ReflectUtils.genGetMethod(fieldPath[i]);
                    target = objClass.getDeclaredMethod(getMethodName).invoke(target);
                }
            }
            objClass = target.getClass();
        }

        return target;
    }

    public static Object getFieldValue(Object checkObj, String[] fieldPath) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
        return getObjectValue(checkObj.getClass(), checkObj, fieldPath);
    }

    public static Object getConstantValue(Class clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        int modifiers = field.getModifiers();
        if (!(Modifier.isPublic(modifiers) && Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers))) {

        }
        return field.get(null);
    }


}
