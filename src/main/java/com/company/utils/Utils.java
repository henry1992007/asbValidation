package com.company.utils;

/**
 * Created by henry on 15/11/10.
 */
public abstract class Utils {
    static boolean debug = true;

    public Utils() {
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
