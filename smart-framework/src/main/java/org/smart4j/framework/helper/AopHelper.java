package org.smart4j.framework.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.annotation.Transaction;
import org.smart4j.framework.proxy.AbstractAspectProxy;
import org.smart4j.framework.proxy.Proxy;
import org.smart4j.framework.proxy.ProxyManager;
import org.smart4j.framework.proxy.TransactionProxy;
import org.smart4j.framework.util.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Aop 是实现面向切面, 增强类
 *
 * @author: YANGXUAN223
 * @date: 2018/12/6.
 */
public final class AopHelper {

    //
    //给切面对象生成增强代理类
    //会覆盖原有的class定义
    //
    static {
        Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
        Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
        targetMap.forEach((cls, proxies) -> {
            Object proxy = ProxyManager.createProxy(cls, proxies);
            BeanHelper.setBean(cls, proxy);
        });
    }

    /**
     * 获取切面增强的所有类
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) {
        Class<? extends Annotation> annotation = aspect.value();
        if (!Aspect.class.equals(annotation)) {
            return ClassHelper.getClassSetByAnnotation(annotation);
        }
        return Sets.newLinkedHashSet();
    }

    /**
     * 获取所有的 普通代理增强类&&指定了增强目标 -> 并且关联使用了这个增强类的所有对象
     * key->代理类, value->被代理的目标对象
     */
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxies) {
        ClassHelper.getClassSetBySuper(AbstractAspectProxy.class)
                .stream()
                .filter(cls -> cls.isAnnotationPresent(Aspect.class))
                .forEach(cls -> {
                    Set<Class<?>> targetClassSet = createTargetClassSet(cls.getAnnotation(Aspect.class));
                    proxies.put(cls, targetClassSet);
                });
    }

    /**
     * 增加 @Service 事务代理
     */
    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxies) {
        proxies.put(TransactionProxy.class, ClassHelper.getClassSetByAnnotation(Service.class));
    }

    /**
     * 获取每个目标对象 对应的代理列表
     * key->目标对象, value->代理列表
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxies) {
        Map<Class<?>, List<Proxy>> targetMap = Maps.newLinkedHashMap();
        for (Map.Entry<Class<?>, Set<Class<?>>> entry : proxies.entrySet()) {
            Class<?> cls = entry.getKey();
            Set<Class<?>> classes = entry.getValue();
            for (Class<?> targetClass : classes) {
                Proxy proxy = (Proxy) ReflectionUtil.newInstance(cls);
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    targetMap.put(targetClass, Lists.newArrayList(proxy));
                }
            }
        }
        return targetMap;
    }

    /**
     * 创建全部代理
     */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() {
        Map<Class<?>, Set<Class<?>>> proxies = Maps.newLinkedHashMap();
        addAspectProxy(proxies);
        addTransactionProxy(proxies);
        return proxies;
    }
}
