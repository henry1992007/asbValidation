package com.company;

/**
 * Created by henry on 15/12/13.
 */
public interface EntityAttribute {

    default EntityAttribute fromName(String name) {
        for (EntityAttribute e : getValues())
            if (e.getName().equals(name.toLowerCase()))
                return e;
        return getDefault();
    }

    EntityAttribute[] getValues();

    String getName();

    EntityAttribute getDefault();

}
