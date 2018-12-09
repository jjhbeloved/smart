package org.smart4j.framework.bean;

import java.lang.reflect.Method;

/**
 * @author: YANGXUAN223
 * @date: 2018/12/7.
 */
public class Handler {

    private Class<?> controllerClass;

    private Method method;

    public Handler(Class<?> controllerClass, Method method) {
        this.controllerClass = controllerClass;
        this.method = method;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getMethod() {
        return method;
    }
}
