package org.smar4j.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.util.JsonUtil;

/**
 * @author: YANGXUAN223
 * @date: 2018/12/9.
 */
public class HelloShiro {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloShiro.class);

	public static void main(String[] args) {
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager sm = factory.getInstance();
		SecurityUtils.setSecurityManager(sm);
		// 获取当前用户
		Subject subject = SecurityUtils.getSubject();
		// login in
		UsernamePasswordToken upt = new UsernamePasswordToken("shiro", "201812");
		try {
			LOGGER.info("{}", subject.getPrincipal());
			LOGGER.info("{}", subject.isAuthenticated());
			LOGGER.info("{}", JsonUtil.toJson(subject.getSession()));
			subject.login(upt);
			LOGGER.info("{}", subject.getPrincipals());
			LOGGER.info("{}", subject.isAuthenticated());
			LOGGER.info("{}", JsonUtil.toJson(subject.getSession()));
		} catch (AuthenticationException e) {
			LOGGER.error("login failure", e);
			return;
		}
		LOGGER.info("login success");
		// login out
		subject.logout();
	}
}
