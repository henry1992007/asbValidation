package com.company;

import com.company.parsing.Entity;

/**
 * Created by henry on 16/1/5.
 */
public class AbstractEntityResolver implements EntityResolver {

    protected int lineNum;
    protected String docPath;

    @Override
    public void resolve(Entity entity, ConfigContext context) {
        lineNum = entity.getLineNum();
        docPath = context.getDocPath();
    }

}
