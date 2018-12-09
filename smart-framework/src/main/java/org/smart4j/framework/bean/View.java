package org.smart4j.framework.bean;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author: YANGXUAN223
 * @date: 2018/12/7.
 */
public class View {

    // 视图路径
    private String path;

    // 数据模型
    private Map<String, Object> models;

    public View(String path) {
        this.path = path;
        models = Maps.newLinkedHashMap();
    }

    public View addModel(String key, Object value) {
        models.put(key, value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModels() {
        return models;
    }
}
