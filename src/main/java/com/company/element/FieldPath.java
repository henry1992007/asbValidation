package com.company.element;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by henry on 15/12/30.
 */
@Getter
@Setter
public class FieldPath {
    private Class clazz;
    private String[] path;
    private boolean main;

    public FieldPath(Class clazz, String[] path) {
        this.clazz = clazz;
        this.path = path;
    }
}
