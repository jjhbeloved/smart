package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author: YANGXUAN223
 * @date: 2018/11/29.
 */
public final class ReflectionUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

	public static Object newInstance(String clazzName) {
		try {
			return Class.forName(clazzName).newInstance();
		} catch (Exception e) {
			LOGGER.error("new instance failure.exception:", e);
			throw new RuntimeException(e);
		}
	}

	public static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			LOGGER.error("new instance failure.exception:", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 执行方法
	 */
	public static Object invokeMethod(Object object, Method method, Object... params) {
		try {
			method.setAccessible(true);
			return method.invoke(object, params);
		} catch (Exception e) {
			LOGGER.error("invoke method failure.exception:", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 设置成员变量
	 */
	public static void setField(Object object, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(object, value);
		} catch (Exception e) {
			LOGGER.error("set field failure.exception:", e);
			throw new RuntimeException(e);
		}
	}
}
