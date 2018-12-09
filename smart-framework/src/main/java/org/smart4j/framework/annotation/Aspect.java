package org.smart4j.framework.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 暂时实现的是类注解
 *
 * @author: YANGXUAN223
 * @date: 2018/12/6.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    // 注解目标类(只能是注解)
    Class<? extends Annotation> value();
}
