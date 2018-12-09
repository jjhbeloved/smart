package org.smart4j.framework.helper;

import com.google.common.collect.Maps;
import org.smart4j.framework.util.ReflectionUtil;

import java.util.Map;
import java.util.Set;

/**
 * @author: YANGXUAN223
 * @date: 2018/11/29.
 */
public final class BeanHelper {

    /**
     * 定义了 class类 和 object实例 的映射
     */
    private static final Map<Class<?>, Object> BEAN_MAP = Maps.newLinkedHashMap();

    static {
        // 初始化bean定义类
        Set<Class<?>> beanClasses = ClassHelper.getBeanClassSet();
        for (Class<?> clazz : beanClasses) {
            Object instance = ReflectionUtil.newInstance(clazz);
            BEAN_MAP.put(clazz, instance);
        }
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        if (!BEAN_MAP.containsKey(clazz)) {
            throw new RuntimeException("not exists class:" + clazz);
        }
        return (T) BEAN_MAP.get(clazz);
    }

    /**
     * 设置 bean
     */
    public static void setBean(Class<?> cls, Object obj) {
        BEAN_MAP.put(cls, obj);
    }
}
