package com.acertainsupermarket.utils;

/**
 * This class represents an application-level exception that is raised whenever
 * a parameter is supposed to be a nonnegative integer, but a negative integer
 * is given by the calling code instead.
 * 
 * @author vmarcos
 */
public class NegativeIdentifierException extends SupermarketException {

	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 6451467008223050457L;

	/**
	 * Instantiates a {@link NegativeIdentifierException}
	 */
	public NegativeIdentifierException() {
	}

	/**
	 * Instantiates a {@link NegativeIdentifierException} with a given error
	 * message.
	 *
	 * @param message
	 */
	public NegativeIdentifierException(String message) {
		super(message);
	}

	/**
	 * Instantiates a {@link NegativeIdentifierException} with a given root
	 * cause.
	 *
	 * @param cause
	 */
	public NegativeIdentifierException(Throwable cause) {
		super(cause);
	}

	/**
	 * Instantiates a {@link NegativeIdentifierException} with a given error
	 * message and root cause.
	 *
	 * @param message
	 * @param cause
	 */
	public NegativeIdentifierException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a {@link NegativeIdentifierException} with a given error
	 * message, root cause, suppression modifier, and writable stack trace
	 * modifier.
	 *
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NegativeIdentifierException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
