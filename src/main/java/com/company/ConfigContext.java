package com.company;


import com.company.element.ClassDefinition;
import com.company.element.ConstantDefinition;
import com.company.parsing.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置上下文 {@link ConfigContext} 代表一个xml配置文档中的所有内容
 * 包括:
 * {@link com.company.element.ClassDefinition},
 * {@link com.company.element.ConstantDefinition},
 * {@link com.company.element.ValidationDefinition},
 * {@link com.company.element.CheckDefinition},
 * {@link com.company.element.ConditionDefinition}
 * 等,用于{@link com.company.element.ValidationDefinition}的解析
 *
 * @author jianheng.he
 * @since 1.0
 */
public class ConfigContext {

    /**
     * 该上下文所在文档的完整路径
     */
    private String docPath;

    /**
     *
     */
    private List<Entity> entities;
    Map<String, ConstantDefinition> constants = new HashMap<>();
    Map<String, ClassDefinition> classes = new HashMap<>();


    public ConfigContext(String docPath, List<Entity> entities) {
        this.docPath = docPath;
        this.entities = entities;
    }

    public Map<String, ConstantDefinition> getConstants() {
        return constants;
    }

    public Map<String, ClassDefinition> getClasses() {
        return classes;
    }

    public void setClasses(Map<String, ClassDefinition> classes) {
        this.classes = classes;
    }

    public List<Entity> getEntities() {
        return entities;
    }


    public String getDocPath() {
        return docPath;
    }
}
