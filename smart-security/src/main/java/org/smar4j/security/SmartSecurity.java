package org.smar4j.security;

import java.util.Set;

/**
 * @author david
 * @since created by on 18/12/9 19:39
 */
public interface SmartSecurity {

	String getPassword(String username);

	Set<String> getRolenNmeSet(String username);

	Set<String> getPermissionNameSet(String roleName);
}
