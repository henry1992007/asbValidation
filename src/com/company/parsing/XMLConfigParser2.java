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

        List<Entity> entities = getEntities((LocatingElement) document.getRootElement());

        return new ConfigContext(entities);
    }

    public static List<Entity> getEntities(LocatingElement rootElement) {
        List<Entity> entities = new ArrayList<>();
        for (Iterator<LocatingElement> i = rootElement.elementIterator(); i.hasNext(); ) {
            LocatingElement element = i.next();
            if (ElementType.fromString(element.getName().toLowerCase()) == null)
                Assert.runtimeException("unknown element '" + element.getName() + "', at line " + element.getLineNum());
            Entity entity = new Entity(element.getName(), element.getLineNum());
            for (Attribute attribute : (List<Attribute>) element.attributes())
                entity.getProperty().put(attribute.getName(), attribute.getValue());
            entity.setSubs(getEntities(element));
            entities.add(entity);
        }

        return entities;
    }


    private static ConditionEntity getCondition(LocatingElement element) {
        ConditionEntity entity = new ConditionEntity();
        setEntityAttr(entity, element);
        entity.setRef(element.attributeValue("ref"));
        entity.setField(element.attributeValue("field"));
        entity.setVal(element.attributeValue("val"));
        entity.setCmpt(element.attributeValue("cmpt"));
        entity.setLogic(element.attributeValue("logic"));
        entity.setOptr(element.attributeValue("optr"));
        entity.set_field(element.attributeValue("_field"));
        entity.set_val(element.attributeValue("_val"));
        entity.set_cmpt(element.attributeValue("_cmpt"));
        entity.set_logic(element.attributeValue("_logic"));
        entity.setMsg(element.attributeValue("msg"));
        return entity;
    }

    private static ValidationEntity getValidation(LocatingElement element) {
        ValidationEntity entity = new ValidationEntity();
        setEntityAttr(entity, element);
        entity.setClassName(element.attributeValue("class"));
        entity.setConditions(resolveCondition(element));
        return entity;
    }

    private static void setEntityAttr(Entity entity, LocatingElement element) {
        entity.setId(element.attributeValue("id"));
        entity.setName(element.getName());
        entity.setLineNum(element.getLineNum());
    }

    private static List<ConditionEntity> resolveCondition(LocatingElement element) {
        List<ConditionEntity> conditionList = new ArrayList<ConditionEntity>();
        for (Iterator<LocatingElement> i = element.elementIterator(); i.hasNext(); ) {
            LocatingElement subElement = i.next();
            if (!StringUtils.firstToCapital(subElement.getName()).equals(ElementType.CONDITION.getValue()))
                continue;
            ConditionEntity condition = getCondition(subElement);
            condition.setSubConditions(resolveCondition(subElement));
            conditionList.add(condition);
        }

        return conditionList;
    }

}
