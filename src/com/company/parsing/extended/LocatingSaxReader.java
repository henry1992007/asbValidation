package com.company.parsing.extended;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXContentHandler;
import org.dom4j.io.SAXReader;
import org.xml.sax.*;

/**
 * Created by henry on 15/11/15.
 */
public class LocatingSaxReader extends SAXReader {
    DocumentFactory docFactory;
    Locator locator;

    public LocatingSaxReader(DocumentFactory docFactory) {
        super(docFactory);
        this.docFactory = docFactory;
    }

    public LocatingSaxReader(DocumentFactory docFactory, Locator locator) {
        super(docFactory);
        this.locator = locator;
        this.docFactory = docFactory;
    }

    @Override
    protected SAXContentHandler createContentHandler(XMLReader reader) {
        return new LocatingContentHandler(this.getDocumentFactory(), super.getDispatchHandler());
    }

    @Override
    public Document read(InputSource in) throws DocumentException {
        try {
            XMLReader reader = this.getXMLReader();

            reader = this.installXMLFilter(reader);

            EntityResolver thatEntityResolver = super.getEntityResolver();

            if (thatEntityResolver == null) {
                thatEntityResolver = this.createDefaultEntityResolver(in.getSystemId());
                super.setEntityResolver(thatEntityResolver);
            }

            reader.setEntityResolver(thatEntityResolver);

            SAXContentHandler contentHandler = this.createContentHandler(reader);
            contentHandler.setEntityResolver(thatEntityResolver);
            contentHandler.setInputSource(in);

            boolean internal = this.isIncludeInternalDTDDeclarations();
            boolean external = this.isIncludeExternalDTDDeclarations();

            contentHandler.setIncludeInternalDTDDeclarations(internal);
            contentHandler.setIncludeExternalDTDDeclarations(external);
            contentHandler.setMergeAdjacentText(this.isMergeAdjacentText());
            contentHandler.setStripWhitespaceText(this.isStripWhitespaceText());
            contentHandler.setIgnoreComments(this.isIgnoreComments());
            reader.setContentHandler(contentHandler);

            this.configureReader(reader, contentHandler);
            ((LocatingContentHandler) contentHandler).setDocFactory((LocatingDocumentFactory) this.docFactory);
            contentHandler.setDocumentLocator(this.locator);
            reader.parse(in);

            return contentHandler.getDocument();
        } catch (Exception e) {
            if (e instanceof SAXParseException) {
                SAXParseException parseException = (SAXParseException) e;
                String systemId = parseException.getSystemId();

                if (systemId == null) {
                    systemId = "";
                }

                String message = "Error on line " + parseException.getLineNumber() + " of document " + systemId + " : "
                        + parseException.getMessage();

                throw new DocumentException(message, e);
            } else {
                throw new DocumentException(e.getMessage(), e);
            }
        }
    }
}
