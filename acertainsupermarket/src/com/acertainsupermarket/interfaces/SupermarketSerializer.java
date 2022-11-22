package com.acertainsupermarket.interfaces;

import java.io.IOException;

public interface SupermarketSerializer {
	/**
	 * Serializes an object into a sequence of bytes.
	 *
	 * @param object
	 *            the object
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	byte[] serialize(Object object) throws IOException;

	/**
	 * De-serializes a sequence of bytes into an object.
	 *
	 * @param bytes
	 *            the bytes
	 * @return the object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	Object deserialize(byte[] bytes) throws IOException;
}
