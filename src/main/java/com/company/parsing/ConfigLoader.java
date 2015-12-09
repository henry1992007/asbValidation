package com.company.parsing;

import com.company.ConfigContext;
import com.company.enums.ElementType;
import com.company.parsing.extended.LocatingDocumentFactory;
import com.company.parsing.extended.LocatingElement;
import com.company.parsing.extended.LocatingSaxReader;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.StringUtils;
import com.google.common.collect.Lists;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * Created by henry on 15/11/4.
 */
public class ConfigLoader {

    private final static String CONFIG_LOCATION_PARAM = "bizCheckConfigLocation";

    private String[] locations;
    Locator locator;
    DocumentFactory documentFactory;
    SAXReader saxReader;
    List<ConfigContext> configContexts;

    ConfigReader configReader;

    public ConfigLoader() {
        locator = new LocatorImpl();
        documentFactory = new LocatingDocumentFactory(locator);
        saxReader = new LocatingSaxReader(documentFactory, locator);
        saxReader.setEncoding("UTF-8");
        configReader = new ConfigReader();
    }

    public void load(ServletContext context) {
        configContexts = Lists.newArrayList();
        locations = context.getInitParameter(CONFIG_LOCATION_PARAM).split(".xml");
        for (int i = 0; i < locations.length; i++) {
            String loc = locations[i].trim();
            locations[i] = loc;
            if (loc.contains("classpath")) {
                try {
                    String classPath = loc.substring(loc.indexOf(":") + 1, loc.length());
                    configContexts.add(parse(getClass().getResourceAsStream(classPath)));
                } catch (DocumentException e) {

                }
            }
        }

        configReader.read();
    }

    public ConfigContext parse(InputStream inputStream) throws DocumentException {
        Document document = saxReader.read(inputStream);
        return new ConfigContext(document.getName(), getEntities((LocatingElement) document.getRootElement(), document.getName()));
    }

    public static List<Entity> getEntities(LocatingElement rootElement, String docName) {
        List<Entity> entities = new ArrayList<>();
        for (Iterator<LocatingElement> i = rootElement.elementIterator(); i.hasNext(); ) {
            LocatingElement element = i.next();
            if (ElementType.fromString(element.getName().toLowerCase()) == null)
                Assert.unknownElementException(element.getName(), element.getLineNum(), docName);
            Entity entity = new Entity(element.getName(), element.getLineNum(), docName);
            for (Attribute attribute : (List<Attribute>) element.attributes())
                entity.getProperty().put(attribute.getName(), attribute.getValue());
            entity.setSubs(getEntities(element, docName));
            entities.add(entity);
        }
        return entities;
    }

    public void destroy(ServletContext context) {

    }

}
