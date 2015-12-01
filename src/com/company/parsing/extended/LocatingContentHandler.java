package com.company.parsing.extended;

import org.dom4j.DocumentFactory;
import org.dom4j.ElementHandler;
import org.dom4j.io.SAXContentHandler;
import org.xml.sax.Locator;

/**
 * Created by henry on 15/11/15.
 */
public class LocatingContentHandler extends SAXContentHandler {

    private LocatingDocumentFactory documentFactory = null;

    public LocatingContentHandler(DocumentFactory documentFactory2, ElementHandler dispatchHandler) {
        super(documentFactory2, dispatchHandler);
    }

    public void setDocFactory(LocatingDocumentFactory fac) {
        this.documentFactory = fac;
    }

    @Override
    public void setDocumentLocator(Locator documentLocator) {
        super.setDocumentLocator(documentLocator);
        if (this.documentFactory != null)
            this.documentFactory.setLocator(documentLocator);
    }

}
