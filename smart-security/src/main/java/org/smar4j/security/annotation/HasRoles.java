package org.smar4j.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否拥有指定的角色
 *
 * @author david
 * @since created by on 18/12/9 22:45
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasRoles {

	// 角色按照 "," 分割多个
	String roles();

}
