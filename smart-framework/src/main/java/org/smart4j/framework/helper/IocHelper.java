package org.smart4j.framework.helper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

/**
 * IoC 是实现依赖注入
 *
 * @author: YANGXUAN223
 * @date: 2018/11/29.
 */
public final class IocHelper {

    /**
     * 初始化Ioc, 给所有bean实例中的Inject注入对象
     */
    private static void init() {
        Map<Class<?>, Object> beanMaps = BeanHelper.getBeanMap();
        if (CollectionUtils.sizeIsEmpty(beanMaps)) {
            return;
        }
        beanMaps.forEach((clazz, inst) -> {
            Field[] beanFields = clazz.getDeclaredFields();
            if (ArrayUtils.isEmpty(beanFields)) {
                return;
            }
            // 遍历 field
            for (Field beanField : beanFields) {
                // 判断 field 是否带有 Inject 注解
                if (!beanField.isAnnotationPresent(Inject.class)) {
                    continue;
                }
                Class<?> beanFieldClass = beanField.getType();
                Object beanFieldInstance = beanMaps.get(beanFieldClass);
                // 初始化实例中的 Inject field
                Optional.ofNullable(beanFieldInstance)
                        .ifPresent(instance -> ReflectionUtil.setField(inst, beanField, beanFieldInstance));
            }
        });
    }

    static {
        init();
    }
}
