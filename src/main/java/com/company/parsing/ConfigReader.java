package com.company.parsing;

import com.company.*;
import com.company.utils.Assert;
import com.company.utils.MapUtils;
import com.company.utils.StringUtils;
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

    static Map<String, EntityResolver> fistClass = MapUtils.<String, EntityResolver>newHashMap(
            "class", new ClassResolver(),
            "constant", new ConstantResolver(),
            "validation", new ValidationResolver()
    );

    private String[] locations;
    List<ConfigContext> configContexts;

    private XMLConfigParser parser;

    public ConfigReader() {
        parser = new XMLConfigParser();
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


        for (ConfigContext ctx : configContexts) {
            for (String eName : fistClass.keySet())
                ctx.getEntities().stream().filter(
                        e -> e.getName().equals(eName)).forEach(
                        e -> fistClass.get(eName).resolve(e, ctx));
        }
    }

    public void destroy(ServletContext context) {

    }

}
