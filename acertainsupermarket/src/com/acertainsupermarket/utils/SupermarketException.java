package com.acertainsupermarket.utils;

/**
 * This class is the root exception type for the acertainsupermarket service.
 * The exception can be thrown directly in case of system-level errors, as
 * opposed to specialized exception classes for application-level errors.
 * 
 * @author vmarcos
 */
public class SupermarketException extends Exception {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -2763883513983981027L;

	/**
	 * Instantiates an {@link SupermarketException}
	 */
	public SupermarketException() {
	}

	/**
	 * Instantiates an {@link SupermarketException} with a given error message.
	 *
	 * @param message
	 */
	public SupermarketException(String message) {
		super(message);
	}

	/**
	 * Instantiates an {@link SupermarketException} with a given root cause.
	 *
	 * @param cause
	 */
	public SupermarketException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates an {@link SupermarketException} with a given error message
	 * and root cause.
	 *
	 * @param message
	 * @param cause
	 */
	public SupermarketException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates an {@link SupermarketException} with a given error message,
	 * root cause, suppression modifier, and writable stack trace modifier.
	 *
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public SupermarketException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
