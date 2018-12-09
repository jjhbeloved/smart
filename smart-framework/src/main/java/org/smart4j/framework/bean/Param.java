package org.smart4j.framework.bean;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Map;

/**
 * @author: YANGXUAN223
 * @date: 2018/12/7.
 */
public class Param {

    private Map<String, Object> params;

    public Param(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public boolean isEmpty() {
        return CollectionUtils.sizeIsEmpty(params);
    }
}
