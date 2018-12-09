package org.smar4j.security.exception;

/**
 * 非法访问异常
 *
 * @author david
 * @since created by on 18/12/9 19:58
 */
public class AuthcException extends RuntimeException {

	public AuthcException() {
		super();
	}

	public AuthcException(String message) {
		super(message);
	}

	public AuthcException(Throwable cause) {
		super(cause);
	}
}
