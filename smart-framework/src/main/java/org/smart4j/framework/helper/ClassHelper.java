package org.smart4j.framework.helper;

import com.google.common.collect.Sets;
import org.checkerframework.checker.units.qual.C;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: YANGXUAN223
 * @date: 2018/11/28.
 */
public final class ClassHelper {

    private static final Set<Class<?>> CLASS_SET;

    static {
        CLASS_SET = ClassUtil.getClassSet(ConfigHelper.getAppBasePackage());
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    public static Set<Class<?>> getServiceClassSet() {
        return CLASS_SET.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Service.class))
                .collect(Collectors.toSet());
    }

    public static Set<Class<?>> getControllerClassSet() {
        return CLASS_SET.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Controller.class))
                .collect(Collectors.toSet());
    }

    public static Set<Class<?>> getBeanClassSet() {
        Set<Class<?>> classes = Sets.newLinkedHashSet();
        classes.addAll(getServiceClassSet());
        classes.addAll(getControllerClassSet());
        return classes;
    }

    /**
     * 获取应用包名类下[指定]父类(或接口)的所有子类(或者实现类)
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        return CLASS_SET.stream()
                // 父类继承
                .filter(superClass::isAssignableFrom)
                // 不相等
                .filter(cls -> !cls.equals(superClass))
                .collect(Collectors.toSet());
    }

    /**
     * 获取应用包名类下[带有]注解的所有类
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotation) {
        return CLASS_SET.stream()
                // [带有]注解
                .filter(cls -> cls.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }
}
