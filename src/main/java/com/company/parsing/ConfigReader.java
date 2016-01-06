package com.company.parsing;

import com.company.*;
import com.company.utils.Assert;
import com.company.utils.MapUtils;
import com.company.utils.StringUtils;
import com.company.validations.ValidationChecker;
import com.google.common.collect.Lists;
import org.dom4j.DocumentException;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

/**
 * Created by henry on 15/11/4.
 */
public class ConfigReader {

    private final static String CONFIG_LOCATION_PARAM = "bizCheckConfigLocation";

    private final static String CLASSPATH_PREFIX = "classpath:";
    private final static String CLASSPATH_SUFFIX = ".xml";

    static Map<String, EntityResolver> fistClass = MapUtils.<String, EntityResolver>newHashMap(
            "class", new ClassResolver(),
            "constant", new ConstantResolver(),
            "validation", new ValidationResolver()
    );

    List<ConfigContext> configContexts;

    private XMLConfigParser parser;

    public ConfigReader() {
        parser = new XMLConfigParser();
    }

    public void load(ServletContext context) {
        configContexts = Lists.newArrayList();
        String locationParam = context.getInitParameter(CONFIG_LOCATION_PARAM);
        if (StringUtils.isEmpty(locationParam))
            Assert.runtimeException("biz check config location undefined");
        String[] locations = StringUtils.splitContainRegex(locationParam, ".xml");
        for (String loc : locations) {
            loc = loc.trim();
            if (loc.startsWith(CLASSPATH_PREFIX) && loc.endsWith(CLASSPATH_SUFFIX))
                try {
                    String classPath = loc.substring(CLASSPATH_PREFIX.length(), loc.length());
                    configContexts.add(parser.parse(getClass().getClassLoader().getResourceAsStream(classPath), classPath));
                } catch (DocumentException e) {
                    Assert.runtimeException("failed to load validation config file:" + loc, e);
                }
        }


        for (ConfigContext ctx : configContexts)
            for (String eName : fistClass.keySet())
                ctx.getEntities().stream().filter(
                        e -> e.getName().equals(eName)).forEach(
                        e -> fistClass.get(eName).resolve(e, ctx));
    }

    public void destroy(ServletContext context) {
        ValidationChecker.clearAll();
    }

}
