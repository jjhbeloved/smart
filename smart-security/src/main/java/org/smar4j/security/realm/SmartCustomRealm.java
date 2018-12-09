package org.smar4j.security.realm;

import com.google.common.collect.Sets;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.smar4j.security.SecurityConstant;
import org.smar4j.security.SmartSecurity;
import org.smar4j.security.password.Md5CredentialsMatcher;

import java.util.Set;

/**
 * @author david
 * @since created by on 18/12/9 20:33
 */
public class SmartCustomRealm extends AuthorizingRealm {

	private final SmartSecurity smartSecurity;

	public SmartCustomRealm(SmartSecurity smartSecurity) {
		this.smartSecurity = smartSecurity;
		super.setName(SecurityConstant.REALMS_CUSTOM);
		super.setCredentialsMatcher(new Md5CredentialsMatcher());
	}

	/**
	 * 权限校验
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		if (principalCollection == null) {
			throw new AuthorizationException("principal is null");
		}
		// 获取已经认证的用户名
		String username = String.valueOf(super.getAvailablePrincipal(principalCollection));
		// 获取权限集
		Set<String> roles = smartSecurity.getRolenNameSet(username);
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo(roles);
		Set<String> permissions = Sets.newLinkedHashSet();
		for (String role : roles) {
			permissions.addAll(smartSecurity.getPermissionNameSet(role));
		}
		simpleAuthorizationInfo.setStringPermissions(permissions);
		return simpleAuthorizationInfo;
	}

	/**
	 * 认证校验(此方法没有做校验, 通过 macher 做密码校验)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		if (authenticationToken == null) {
			throw new AuthenticationException("token is null");
		}
		String username = ((UsernamePasswordToken) authenticationToken).getUsername();
		String password = smartSecurity.getPassword(username);
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, super.getName(), password);
		return simpleAuthenticationInfo;
	}
}
