package org.smar4j.security;


import org.apache.shiro.web.env.EnvironmentLoader;
import org.apache.shiro.web.env.EnvironmentLoaderListener;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

/**
 * ServletContainerInitializer 提供了初始化配置
 * any required programmatic registration of servlets, filters, and listeners
 *
 * @author david
 * @since created by on 18/12/9 20:06
 */
public class SmartSecurityPlugin implements ServletContainerInitializer {

	@Override
	public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
		servletContext.setInitParameter(EnvironmentLoader.CONFIG_LOCATIONS_PARAM, "classpath:smart-security.ini");
		servletContext.addListener(EnvironmentLoaderListener.class);

	}
}
