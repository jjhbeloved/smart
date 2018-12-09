package org.smart4j.transit.security;

import org.smar4j.security.SmartSecurity;

import java.util.Set;

/**
 * @author david
 * @since created by on 18/12/9 19:43
 */
public class AppSecurity implements SmartSecurity {
	@Override
	public String getPassword(String username) {
		return null;
	}

	@Override
	public Set<String> getRolenNmeSet(String username) {
		return null;
	}

	@Override
	public Set<String> getPermissionNameSet(String roleName) {
		return null;
	}
}
