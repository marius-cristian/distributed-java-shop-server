package com.acertainsupermarket.utils;

/**
 * This class represents an application-level exception to be raised whenever a
 * cart is to be found by identifier, but the calling code provides an
 * identifier that does not match any cart recorded in the service.
 * 
 * @author vmarcos
 */
public class InexistentCartException extends SupermarketException {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -114985494153887272L;

	/**
	 * Instantiates an {@link InexistentCartException}
	 */
	public InexistentCartException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates an {@link InexistentCartException} with a given error
	 * message.
	 *
	 * @param message
	 */
	public InexistentCartException(String message) {
		super(message);
	}

	/**
	 * Instantiates an {@link InexistentCartException} with a given root
	 * cause.
	 *
	 * @param cause
	 */
	public InexistentCartException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates an {@link InexistentCartException} with a given error
	 * message and root cause.
	 *
	 * @param message
	 * @param cause
	 */
	public InexistentCartException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates an {@link InexistentCartException} with a given error
	 * message, root cause, suppression modifier, and writable stack trace
	 * modifier.
	 *
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public InexistentCartException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
