package com.acertainsupermarket.utils;

public final class SupermarketConstants {
	/**
	 * The Constant BINARY_SERIALIZATION decides whether we use Kryo or XStream.
	 */
	public static final boolean BINARY_SERIALIZATION = true;
	
	/** The Constant NULL_INPUT. */
	public static final String NULL_INPUT = "null input parameters";

	/** The Constant PROPERTY_KEY_LOCAL_TEST. */
	public static final String PROPERTY_KEY_LOCAL_TEST = "localtest";

	/** The Constant PROPERTY_KEY_SERVER_PORT. */
	public static final String PROPERTY_KEY_SERVER_PORT = "port";

	/** The Constant EPSILON used for floating point number comparison */
	public static final float EPSILON = 0.000001F;

	public static final String NEGATIVE_ID = "negative id ";

	public static final String INEXISTENT_ITEM = "item does not exists ";

	public static final String INVALID_STATE = "the operation is invalid ";
	
	public static final int NUMBER_OF_CARTS =500;
	
	public static final int NUMBER_OF_ITEMS =10000;
	
	public static final int STOCK_PER_ITEM = 20000;

	public static final String CART_ID = "cart_id";
	
	/** The Constant XMLSTRINGLEN_PARAM. */
	public static final String XMLSTRINGLEN_PARAM = "len";
	
	/**
	 * Prevents the instantiation of a new {@link BookStoreConstants}.
	 */
	private SupermarketConstants() {
		// Prevent instantiation.
	}

}
