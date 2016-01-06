package com.company;

import com.company.parsing.Entity;

/**
 * Created by henry on 16/1/5.
 */
public interface EntityResolver {
    void resolve(Entity entity, ConfigContext context);
}
