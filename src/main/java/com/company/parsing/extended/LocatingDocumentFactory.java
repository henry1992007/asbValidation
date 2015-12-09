package com.company.parsing.extended;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xml.sax.Locator;

/**
 * Created by henry on 15/11/15.
 */
public class LocatingDocumentFactory extends DocumentFactory {

    private Locator locator;

    public LocatingDocumentFactory(Locator locator) {
        super();
        this.locator = locator;
    }

    @Override
    public Element createElement(QName qname) {
        LocatingElement element = new LocatingElement(qname);
        element.setLineNum(this.locator.getLineNumber());
        element.setColNum(this.locator.getColumnNumber());
        return element;
    }

    @Override
    public Element createElement(String name) {
        LocatingElement element = new LocatingElement(name);
        element.setLineNum(this.locator.getLineNumber());
        element.setColNum(this.locator.getColumnNumber());
        return element;
    }

    public void setLocator(Locator locator) {
        this.locator = locator;
    }

}
