package org.smar4j.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smar4j.security.exception.AuthcException;

import java.util.Optional;

/**
 * @author david
 * @since created by on 18/12/9 19:57
 */
public class SecurityHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityHelper.class);

	public static void login(String username, String password) throws AuthcException {
		Subject user = Optional.ofNullable(SecurityUtils.getSubject())
				.orElseThrow(() -> new AuthcException("login failure"));
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try {
			user.login(token);
		} catch (AuthenticationException e) {
			LOGGER.error("login failure", e);
			throw new AuthcException(e);
		}
	}

	public static void logout() {
		Subject user = SecurityUtils.getSubject();
		Optional.ofNullable(user)
				.ifPresent(Subject::logout);
	}
}
