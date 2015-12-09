package com.company.net;

import com.company.parsing.ConfigLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by henry on 15/12/9.
 */
public class BizCheckConfigListener implements ServletContextListener {

    private ConfigLoader configLoader;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        configLoader = new ConfigLoader();
        configLoader.load(event.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (configLoader != null)
            configLoader.destroy(event.getServletContext());
    }

}