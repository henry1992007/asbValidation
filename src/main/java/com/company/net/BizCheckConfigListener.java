package com.company.net;

import com.company.parsing.ConfigLoader;
import com.company.test.Mock;
import com.company.validations.ValidationChecker;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Arrays;

/**
 * Created by henry on 15/12/9.
 */
public class BizCheckConfigListener implements ServletContextListener {

    private ConfigLoader configLoader;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        configLoader = new ConfigLoader();
        configLoader.load(event.getServletContext());
        ValidationChecker checker = ValidationChecker.get("couponFieldsCheck");
        System.out.println(Arrays.toString(checker.check(Mock.mock()).toArray()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (configLoader != null)
            configLoader.destroy(event.getServletContext());
    }

}