package com.company.parsing;

import com.company.ConfigContext;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.StringUtils;
import com.company.validations.ConfigReader;
import com.google.common.collect.Lists;
import org.dom4j.*;

import javax.servlet.ServletContext;
import java.util.*;

/**
 * Created by henry on 15/11/4.
 */
public class ConfigLoader {

    private final static String CONFIG_LOCATION_PARAM = "bizCheckConfigLocation";

    private String[] locations;
    List<ConfigContext> configContexts;
    ConfigReader configReader;

    private XMLConfigParser parser;

    public ConfigLoader() {
        parser = new XMLConfigParser();
        configReader = new ConfigReader();
    }

    public void load(ServletContext context) {
        configContexts = Lists.newArrayList();
        String location = context.getInitParameter(CONFIG_LOCATION_PARAM);
        if (StringUtils.isEmpty(location))
            Assert.runtimeException("biz check config location undefined");
        locations = StringUtils.splitContainRegex(location, ".xml");
        for (int i = 0; i < locations.length; i++) {
            String loc = locations[i].trim();
            locations[i] = loc;
            if (loc.contains("classpath")) {
                try {
                    String classPath = loc.substring(loc.indexOf(":") + 1, loc.length());
                    configContexts.add(parser.parse(getClass().getClassLoader().getResourceAsStream(classPath), classPath));
                } catch (DocumentException e) {
                    Assert.runtimeException("failed to load validation config file:" + loc, e);
                }
            }
        }

        configReader.read(configContexts.toArray(new ConfigContext[configContexts.size()]));
    }

    public void destroy(ServletContext context) {

    }

}
