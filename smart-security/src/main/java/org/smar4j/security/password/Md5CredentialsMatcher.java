package org.smar4j.security.password;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.smart4j.framework.util.CodeUtil;

/**
 * 明文密码匹配器
 *
 * @author david
 * @since created by on 18/12/9 20:49
 */
public class Md5CredentialsMatcher implements CredentialsMatcher {

	/**
	 * 校验输入的 token 和 解析的 info 是否匹配
	 */
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		String password = String.valueOf(((UsernamePasswordToken) token).getPassword());
		String targetPassword = String.valueOf(info.getCredentials());
		return targetPassword.equals(CodeUtil.md5(password));
	}
}
