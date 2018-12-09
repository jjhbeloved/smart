package org.smart4j.framework.proxy;

/**
 * @author: YANGXUAN223
 * @date: 2018/12/6.
 */
public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Throwable;

}
