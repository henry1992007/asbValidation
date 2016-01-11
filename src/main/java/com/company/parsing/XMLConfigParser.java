package com.company.parsing;

import com.company.ConfigContext;
import com.company.enums.ElementType;
import com.company.parsing.extended.LocatingDocumentFactory;
import com.company.parsing.extended.LocatingElement;
import com.company.parsing.extended.LocatingSaxReader;
import com.company.utils.Assert;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.company.utils.Utils.info;

/**
 * Created by henry on 15/11/4.
 */
public class XMLConfigParser {

    private LocatingSaxReader saxReader;

    public XMLConfigParser() {
        Locator locator = new LocatorImpl();
        DocumentFactory documentFactory = new LocatingDocumentFactory(locator);
        saxReader = new LocatingSaxReader(documentFactory, locator);
        saxReader.setEncoding("UTF-8");
    }

    public ConfigContext parse(InputStream inputStream, String docName) throws DocumentException {
        Document document = saxReader.read(inputStream);
        ConfigContext configContext = new ConfigContext(docName, getEntities((LocatingElement) document.getRootElement(), docName));
        info("在" + docName + "中读取到" + configContext.getEntities().size() + "条entities:\n" + Arrays.toString(configContext.getEntities().toArray()));
        return configContext;
    }

    public static List<Entity> getEntities(LocatingElement rootElement, String docName) {
        List<Entity> entities = new ArrayList<>();
        for (Iterator<LocatingElement> i = rootElement.elementIterator(); i.hasNext(); ) {
            LocatingElement element = i.next();
            if (ElementType.fromString(element.getName().toLowerCase()) == null)
                Assert.unknownElementException(element.getName(), element.getLineNum(), docName);
            Entity entity = new Entity(element.getName(), element.getLineNum());
            ((List<Attribute>) element.attributes()).stream().forEach(
                    a -> entity.putProperty(a.getName(), a.getValue()));
            entity.setChildEntities(getEntities(element, docName));
            entities.add(entity);
        }

        return entities;
    }

}
