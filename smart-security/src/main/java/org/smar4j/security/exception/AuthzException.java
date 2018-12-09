package org.smar4j.security.exception;

/**
 * 权限异常
 *
 * @author david
 * @since created by on 18/12/9 19:58
 */
public class AuthzException extends RuntimeException {


	public AuthzException() {
		super();
	}

	public AuthzException(String message) {
		super(message);
	}

	public AuthzException(Throwable cause) {
		super(cause);
	}
}
