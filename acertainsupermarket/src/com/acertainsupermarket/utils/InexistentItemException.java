package com.acertainsupermarket.utils;

/**
 * This class represents an application-level exception to be raised whenever an
 * item is to be found by identifier, but the calling code provides an
 * identifier that does not match any item recorded in the service.
 * 
 * @author vmarcos
 */
public class InexistentItemException extends SupermarketException {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 6244608863341579929L;

	/**
	 * Instantiates an {@link InexistentItemException}
	 */
	public InexistentItemException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates an {@link InexistentItemException} with a given error
	 * message.
	 *
	 * @param message
	 */
	public InexistentItemException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates an {@link InexistentItemException} with a given root cause.
	 *
	 * @param cause
	 */
	public InexistentItemException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates an {@link InexistentItemException} with a given error
	 * message and root cause.
	 *
	 * @param message
	 * @param cause
	 */
	public InexistentItemException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates an {@link InexistentItemException} with a given error
	 * message, root cause, suppression modifier, and writable stack trace
	 * modifier.
	 *
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public InexistentItemException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
