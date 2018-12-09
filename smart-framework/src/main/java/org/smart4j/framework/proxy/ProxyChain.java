package org.smart4j.framework.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: YANGXUAN223
 * @date: 2018/12/6.
 */
public class ProxyChain {

    private final Class<?> targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final Object[] methodParams;
    private final MethodProxy methodProxy;

    private final List<Proxy> proxies;
    // 代理初始值
    private int proxyIndex = 0;

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, Object[] methodParams,
                      MethodProxy methodProxy,
                      List<Proxy> proxies) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodParams = methodParams;

        this.methodProxy = methodProxy;
        this.proxies = proxies;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    /**
     * 执行代理获取结果
     */
    public Object doProxyChain() throws Throwable {
        if (proxyIndex < proxies.size()) {
            // 实现横切
            return proxies.get(proxyIndex++).doProxy(this);
        } else {
            // TODO invokerSuper 和 invoke 分析
            return methodProxy.invokeSuper(targetObject, methodParams);
        }
    }
}
