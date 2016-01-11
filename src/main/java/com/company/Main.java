package com.company;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) throws Exception {
        char x = 'y';
        System.out.println(true ? 121 : "o");
//        ApplicationContext context = new ClassPathXmlApplicationContext();
    }

    static class test {
        int u = 5;

        static {

        }

        strictfp public static void main(String[] args) {
            System.out.println(true ? 121 : "o");
        }
    }

}
