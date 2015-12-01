package com.company;

import com.company.parsing.XMLConfigParser;
import org.dom4j.DocumentException;

/**
 * Created by henry on 15/11/10.
 */
public class Config {
    public static ConfigContext validationConfig;

    static {
        try {
            validationConfig = XMLConfigParser.parseFromSystemFile("src/Conditions.xml");
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

}
