package org.smart4j.framework.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.util.List;

/**
 * @author: YANGXUAN223
 * @date: 2018/12/6.
 */
public class ProxyManager {

    /**
     * 创建对象代理(并且输入切面对象)
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final Class<T> targetClass, final List<Proxy> proxies) {
        return (T) Enhancer.create(targetClass, (MethodInterceptor)
                (targetObject, targetMethod, methodParams, methodProxy) ->
                        new ProxyChain(targetClass, targetObject, targetMethod, methodParams, methodProxy, proxies)
                                .doProxyChain());
    }
}
