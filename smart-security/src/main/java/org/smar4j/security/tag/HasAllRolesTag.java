package org.smar4j.security.tag;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.tags.RoleTag;


/**
 * 判断当前用户是否 拥有所有角色
 *
 * @author david
 * @since created by on 18/12/9 21:47
 */
public class HasAllRolesTag extends RoleTag {

	private static final String ROLE_NAMES_DELIMITER = ",";

	/**
	 * @param roleName 当前系统所有的角色
	 */
	@Override
	protected boolean showTagBody(String roleName) {
		Subject user = getSubject();
		if (user == null) {
			return false;
		}
		return user.hasAllRoles(Lists.newArrayList(StringUtils.split(roleName, ROLE_NAMES_DELIMITER)));
	}
}
