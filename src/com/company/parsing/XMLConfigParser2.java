package com.company.parsing;

import com.company.ConfigContext;
import com.company.enums.ElementType;
import com.company.parsing.extended.LocatingDocumentFactory;
import com.company.parsing.extended.LocatingElement;
import com.company.parsing.extended.LocatingSaxReader;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.StringUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

import java.io.File;
import java.util.*;

/**
 * Created by henry on 15/11/4.
 */
public class XMLConfigParser2 {

    public static ConfigContext parseFromSystemFile(String path) throws DocumentException {
        return parse(new File(path));
    }

    public static ConfigContext parse(File file) throws DocumentException {
        Locator locator = new LocatorImpl();
        DocumentFactory documentFactory = new LocatingDocumentFactory(locator);
        SAXReader saxReader = new LocatingSaxReader(documentFactory, locator);
        saxReader.setEncoding("UTF-8");
        Document document = saxReader.read(file);


        List<Entity> entities = getEntities((LocatingElement) document.getRootElement(),document.getName());

        return new ConfigContext(entities);
    }

    public static List<Entity> getEntities(LocatingElement rootElement, String docName) {
        List<Entity> entities = new ArrayList<>();
        for (Iterator<LocatingElement> i = rootElement.elementIterator(); i.hasNext(); ) {
            LocatingElement element = i.next();
            if (ElementType.fromString(element.getName().toLowerCase()) == null)
                Assert.runtimeException("unknown element '" + element.getName() + "', at line " + element.getLineNum());
            Entity entity = new Entity(element.getName(), element.getLineNum(), docName);
            for (Attribute attribute : (List<Attribute>) element.attributes())
                entity.getProperty().put(attribute.getName(), attribute.getValue());
            entity.setSubs(getEntities(element,docName));
            entities.add(entity);
        }

        return entities;
    }

}
