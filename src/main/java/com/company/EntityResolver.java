package com.company;

import com.company.parsing.Entity;

/**
 * 解析tag
 *
 * @author henry
 */
public interface EntityResolver {
    void resolve(Entity entity, ConfigContext context);
}
