package com.company.net;

import com.company.element.ValidationDefinition;
import com.company.parsing.ConfigLoader;
import com.company.test.Mock;
import com.company.validations.ValidationChecker;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.company.utils.Utils.info;

/**
 * Created by henry on 15/12/9.
 */
public class BizCheckConfigListener implements ServletContextListener {

    private ConfigLoader configLoader;
    Logger logger = Logger.getLogger(BizCheckConfigListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        configLoader = new ConfigLoader();
        configLoader.load(event.getServletContext());
        ValidationChecker checker = ValidationChecker.get("couponFieldsCheck");
        info("获得checker:" + checker.getID());
//        Collection<ValidationDefinition> list = ValidationChecker.validations.values();
//        System.out.println(list);

        System.out.println(Arrays.toString(checker.check(Mock.mocks1()).toArray()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (configLoader != null)
            configLoader.destroy(event.getServletContext());
    }


}