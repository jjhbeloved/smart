package org.smar4j.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 判断用户是否已经登录(登录会有用户信息保存)
 * 并且通过认证(通过认证可能还没有用户信息)
 *
 * @author david
 * @since created by on 18/12/9 22:03
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}
