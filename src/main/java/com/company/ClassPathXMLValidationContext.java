package com.company;

/**
 * Created by henry on 15/12/8.
 */
public class ClassPathXMLValidationContext {

    String location;

    public ClassPathXMLValidationContext(String location) {
        setLocation(location);
    }

    private void setLocation(String location) {
        this.getClass().getResourceAsStream(location);
    }
}
