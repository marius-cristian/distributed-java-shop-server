package com.acertainsupermarket.utils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import com.acertainsupermarket.client.SupermarketClientConstants;
import com.acertainsupermarket.interfaces.SupermarketSerializer;

public final class SupermarketUtility {
	/** The Constant XmlStreams. */
	// We use pooling because creating an XStream object is expensive.
	public static final ThreadLocal<XStream> XML_STREAMS = new ThreadLocal<XStream>() {

		@Override
		protected XStream initialValue() {
			return new XStream(new StaxDriver());
		}
	};

	private SupermarketUtility() {
		
	};
	
	/**
	 * Converts a string to a float if possible else it returns the signal value
	 * for failure passed as parameter.
	 *
	 * @param str
	 *            the string
	 * @param failureSignal
	 *            the failure signal
	 * @return the float
	 */
	public static float convertStringToFloat(String str, float failureSignal) {
		float returnValue = failureSignal;

		try {
			returnValue = Float.parseFloat(str);

		} catch (NumberFormatException | NullPointerException ex) {
			System.err.println(ex.getStackTrace());
		}

		return returnValue;
	}
	

	/**
	 * Converts a string to an integer if possible else it returns the signal
	 * value for failure passed as parameter.
	 *
	 * @param str
	 *            the string
	 * @return the integer
	 * @throws SupermarketException
	 *             the book store exception
	 */
	public static int convertStringToInt(String str) throws SupermarketException {
		int returnValue = 0;

		try {
			returnValue = Integer.parseInt(str);
		} catch (Exception ex) {
			throw new SupermarketException(ex);
		}

		return returnValue;
	}
	
	/**
	 * Convert a request URI to the message tags supported in CertainSupermarket.
	 *
	 * @param requestURI
	 *            the request URI
	 * @return the book store message tag
	 */
	public static SupermarketMessageTag convertURItoMessageTag(String requestURI) {

		try {
			return SupermarketMessageTag.valueOf(requestURI.substring(1).toUpperCase());
		} catch (IllegalArgumentException | NullPointerException ex) {
			// Enumeration type matching failed so non supported message or the
			// request URI was empty.
			System.err.println(ex.getStackTrace());
		}

		return null;
	}
	
	
	/**
	 * Perform HTTP exchange.
	 *
	 * @param client
	 *            the client
	 * @param SupermarketRequest
	 *            the book store request
	 * @param serializer
	 *            the serializer
	 * @return the book store response
	 * @throws SupermarketException
	 *             the book store exception
	 */
	public static SupermarketResponse performHttpExchange(HttpClient client, SupermarketRequest SupermarketRequest,
			SupermarketSerializer serializer) throws SupermarketException {
		Request request;

		switch (SupermarketRequest.getMethod()) {
		case GET:
			request = client.newRequest(SupermarketRequest.getURLString()).method(HttpMethod.GET);
			break;

		case POST:
			try {
				byte[] serializedValue = serializer.serialize(SupermarketRequest.getInputValue());
				ContentProvider contentProvider = new BytesContentProvider(serializedValue);
				request = client.POST(SupermarketRequest.getURLString()).content(contentProvider);
			} catch (IOException ex) {
				throw new SupermarketException("Serialization error", ex);
			}

			break;

		default:
			throw new IllegalArgumentException("HTTP Method not supported.");
		}

		ContentResponse response;

		try {
			response = request.send();
		} catch (InterruptedException ex) {
			//System.out.println(ex);
			throw new SupermarketException(SupermarketClientConstants.STR_ERR_CLIENT_REQUEST_SENDING, ex);
		} catch (TimeoutException ex) {
			throw new SupermarketException(SupermarketClientConstants.STR_ERR_CLIENT_REQUEST_TIMEOUT, ex);
		} catch (ExecutionException ex) {
			throw new SupermarketException(SupermarketClientConstants.STR_ERR_CLIENT_REQUEST_EXCEPTION, ex);
		}

		SupermarketResponse SupermarketResponse;

		try {
			SupermarketResponse = (SupermarketResponse) serializer.deserialize(response.getContent());
		} catch (IOException ex) {
			throw new SupermarketException("Deserialization error", ex);
		}

		SupermarketException exception = SupermarketResponse.getException();

		if (exception != null) {
			throw exception;
		}

		return SupermarketResponse;
	}
	
	
}
