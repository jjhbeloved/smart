package org.smart4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.helper.ConfigHelper;

import java.lang.reflect.Method;

/**
 * 提供代理切面抽象模版
 *
 * @author: YANGXUAN223
 * @date: 2018/12/6.
 */
public abstract class AbstractAspectProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAspectProxy.class);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();
        // 解决针对 toString 和 hashCode 的切面控制...
        if ("toString".equals(method.getName()) || "hashCode".equals(method.getName())) {
            return proxyChain.doProxyChain();
        }
        Object result;
        begin();
        try {
            before(cls, method, params);
            result = proxyChain.doProxyChain();
            after(cls, method, params);
        } catch (Exception e) {
            LOGGER.error("proxy failure", e);
            error(cls, method, params, e);
            throw e;
        } finally {
            end();
        }
        return result;
    }

    public void begin() {

    }

    public void end() {

    }

    public abstract void before(Class<?> cls, Method method, Object[] params) throws Throwable;

    public abstract void after(Class<?> cls, Method method, Object[] params) throws Throwable;

    public abstract void error(Class<?> cls, Method method, Object[] params, Throwable throwable);
}
