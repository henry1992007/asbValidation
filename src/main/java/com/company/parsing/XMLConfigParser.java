package com.company.parsing;

import com.company.ConfigContext;
import com.company.enums.ElementType;
import com.company.parsing.extended.LocatingDocumentFactory;
import com.company.parsing.extended.LocatingElement;
import com.company.parsing.extended.LocatingSaxReader;
import com.company.utils.Assert;
import com.company.utils.CollectionUtils;
import com.company.utils.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;
import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import static com.company.utils.Utils.info;

/**
 * Created by henry on 15/11/4.
 */
public class XMLConfigParser {

    private SAXReader saxReader;

    public XMLConfigParser() {
        Locator locator = new LocatorImpl();
        DocumentFactory documentFactory = new LocatingDocumentFactory(locator);
        saxReader = new LocatingSaxReader(documentFactory, locator);
        saxReader.setEncoding("UTF-8");
    }

    public ConfigContext parse(InputStream inputStream, String docName) throws DocumentException {
        Document document = saxReader.read(inputStream);
        ConfigContext configContext = new ConfigContext(docName, getEntities((LocatingElement) document.getRootElement(), docName));
        info("在" + docName + "中读取到" + configContext.getEntities().size() + "条entities:\n"+ Arrays.toString(configContext.getEntities().toArray()));
        return configContext;
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

}
