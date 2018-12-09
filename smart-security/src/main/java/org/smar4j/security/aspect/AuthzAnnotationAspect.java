package org.smar4j.security.aspect;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.smar4j.security.annotation.*;
import org.smar4j.security.exception.AuthzException;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.proxy.AbstractAspectProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * @author david
 * @since created by on 18/12/9 22:06
 */
@Aspect(Controller.class)
public class AuthzAnnotationAspect extends AbstractAspectProxy {

	private static final String DELIMITER = ",";

	private static final List<Class<? extends Annotation>> ANNOTATION_CLASSES = Lists.newArrayList(
			Login.class, Authenticated.class, Guest.class
	);

	@Override
	public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
		Annotation annotation = getAnnotation(cls, method);
		if (annotation == null) {
			return;
		}
		Class<? extends Annotation> clazz = annotation.annotationType();
		if (Login.class.equals(clazz)) {
			handleLogin();
		} else if (Authenticated.class.equals(clazz)) {
			handleAuthz();
		} else if (HasPermissions.class.equals(clazz)) {
			String permissions = ((HasPermissions) annotation).permissions();
			String[] ps = StringUtils.split(permissions, DELIMITER);
			handlePermission(ps);
		} else if (HasRoles.class.equals(clazz)) {
			String roles = ((HasRoles) annotation).roles();
			String[] rs = StringUtils.split(roles, DELIMITER);
			handleRole(Lists.newArrayList(rs));
		}
	}


	@Override
	public void after(Class<?> cls, Method method, Object[] params) throws Throwable {

	}

	@Override
	public void error(Class<?> cls, Method method, Object[] params, Throwable throwable) {

	}


	private Annotation getAnnotation(Class<?> cls, Method method) {
		for (Class<? extends Annotation> annotation : ANNOTATION_CLASSES) {
			// 1. 先 method
			if (method.isAnnotationPresent(annotation)) {
				return method.getAnnotation(annotation);
			}
			// 2. 再 cls
			if (cls.isAnnotationPresent(annotation)) {
				return cls.getAnnotation(annotation);
			}
		}
		return null;
	}

	private Subject getSubject() {
		return Optional.ofNullable(SecurityUtils.getSubject())
				.orElseThrow(() -> new AuthzException("user not login"));
	}

	private void handleLogin() {
		Subject subject = getSubject();
		PrincipalCollection principals = subject.getPrincipals();
		if (CollectionUtils.sizeIsEmpty(principals)) {
			throw new AuthzException("user not login");
		}
	}

	private void handleAuthz() {
		Subject subject = getSubject();
		if (!subject.isAuthenticated()) {
			throw new AuthzException("user not authz");
		}
	}

	private void handleGuest() {
		// 访客身份没有校验
	}

	private void handleRole(List<String> roles) {
		if (CollectionUtils.isEmpty(roles)) {
			return;
		}
		Subject subject = getSubject();
		if (!subject.hasAllRoles(roles)) {
			throw new AuthzException("user roles not matches");
		}
	}

	private void handlePermission(String... permissions) {
		if (ArrayUtils.isEmpty(permissions)) {
			return;
		}
		Subject subject = getSubject();
		try {
			subject.checkPermissions(permissions);
		} catch (AuthorizationException e) {
			throw new AuthzException("user permissions not matches");
		}
	}
}
