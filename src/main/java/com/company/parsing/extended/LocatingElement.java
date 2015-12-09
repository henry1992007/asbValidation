package com.company.parsing.extended;

import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.DefaultElement;

/**
 * Created by henry on 15/11/15.
 */
public class LocatingElement extends DefaultElement {

    private int lineNum = 0;

    private int colNum = 0;

    public LocatingElement(QName qname) {
        super(qname);
    }

    public LocatingElement(QName qname, int attrCount) {
        super(qname, attrCount);
    }

    public LocatingElement(String name) {
        super(name);
    }

    public LocatingElement(String name, Namespace namespace) {
        super(name, namespace);
    }

    public int getLineNum() {
        return lineNum;
    }

    public int getColNum() {
        return colNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

}
