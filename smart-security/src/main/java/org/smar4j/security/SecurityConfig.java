package org.smar4j.security;

import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.util.ReflectionUtil;

/**
 * @author david
 * @since created by on 18/12/9 20:22
 */
public class SecurityConfig {

	public static String getRealms() {
		return ConfigHelper.getString(SecurityConstant.REALMS);
	}

	public static SmartSecurity getSmartSecurity() {
		String clazz = ConfigHelper.getString(SecurityConstant.SMART_SECURITY_CLASS);
		return (SmartSecurity) ReflectionUtil.newInstance(clazz);
	}

	public static boolean isCache() {
		return ConfigHelper.getBoolean(SecurityConstant.SMART_CACHE);
	}
}
