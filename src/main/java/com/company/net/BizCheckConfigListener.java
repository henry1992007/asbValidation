package com.company.net;

import com.company.parsing.ConfigReader;
import com.company.test.CouponOfferDTO;
import com.company.test.Mock;
import com.company.validations.ValidationChecker;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Arrays;

import static com.company.utils.Utils.info;

/**
 * Created by henry on 15/12/9.
 */
public class BizCheckConfigListener implements ServletContextListener {

    private ConfigReader configReader;
    Logger logger = Logger.getLogger(BizCheckConfigListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        configReader = new ConfigReader();
        configReader.load(event.getServletContext());
        ValidationChecker checker = ValidationChecker.get("couponFieldsCheck");
        info("获得checker:" + checker.getID());
//        Collection<ValidationDefinition> list = ValidationChecker.validations.values();
//        System.out.println(list);


        CouponOfferDTO[] mocks = Mock.mocks();

        long t1 = System.currentTimeMillis();
        System.out.println(Arrays.toString(checker.check(mocks).toArray()));
        System.out.println(System.currentTimeMillis() - t1);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (configReader != null)
            configReader.destroy(event.getServletContext());
    }


}