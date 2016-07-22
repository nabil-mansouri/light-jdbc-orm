package com.nm.orm.utils;
/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class JdbcOrmException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JdbcOrmException() {
		super();
	
	}

	public JdbcOrmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	
	}

	public JdbcOrmException(String message, Throwable cause) {
		super(message, cause);
	
	}

	public JdbcOrmException(String message) {
		super(message);
	
	}

	public JdbcOrmException(Throwable cause) {
		super(cause);
	
	}

}
