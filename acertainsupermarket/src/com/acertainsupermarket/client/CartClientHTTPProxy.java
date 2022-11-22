package com.acertainsupermarket.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.acertainsupermarket.interfaces.CartItem;
import com.acertainsupermarket.interfaces.Cart;
import com.acertainsupermarket.interfaces.SupermarketSerializer;
import com.acertainsupermarket.utils.CartItemTuple;
import com.acertainsupermarket.utils.SupermarketConstants;
import com.acertainsupermarket.utils.SupermarketException;
import com.acertainsupermarket.utils.SupermarketKryoSerializer;
import com.acertainsupermarket.utils.SupermarketMessageTag;
import com.acertainsupermarket.utils.SupermarketRequest;
import com.acertainsupermarket.utils.SupermarketResponse;
import com.acertainsupermarket.utils.SupermarketUtility;
import com.acertainsupermarket.utils.SupermarketXStreamSerializer;


public class CartClientHTTPProxy implements Cart {
	/** The client. */
	protected HttpClient client;

	/** The server address. */
	protected String serverAddress;

	/** The serializer. */
	private static ThreadLocal<SupermarketSerializer> serializer;
	
	/**
	 * Initializes a new {@link CartClientHTTPProxy}.
	 *
	 * @param serverAddress
	 *            the server address
	 * @throws Exception
	 *             the exception
	 */
	public CartClientHTTPProxy(String serverAddress) throws Exception {

		// Setup the type of serializer.
		if (SupermarketConstants.BINARY_SERIALIZATION) {
			serializer = ThreadLocal.withInitial(SupermarketKryoSerializer::new);
		} else {
			serializer = ThreadLocal.withInitial(SupermarketXStreamSerializer::new);
		}

		setServerAddress(serverAddress);
		client = new HttpClient();

		// Max concurrent connections to every address.
		client.setMaxConnectionsPerDestination(SupermarketClientConstants.CLIENT_MAX_CONNECTION_ADDRESS);

		// Max number of threads.
		client.setExecutor(new QueuedThreadPool(SupermarketClientConstants.CLIENT_MAX_THREADSPOOL_THREADS));

		// Seconds timeout; if no server reply, the request expires.
		client.setConnectTimeout(SupermarketClientConstants.CLIENT_MAX_TIMEOUT_MILLISECS);

		client.start();
	}
	
	/**
	 * Stops the proxy.
	 */
	public void stop() {
		try {
			client.stop();
		} catch (Exception ex) {
			System.err.println(ex.getStackTrace());
		}
	}	

	/**
	 * Gets the server address.
	 *
	 * @return the server address
	 */
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * Sets the server address.
	 *
	 * @param serverAddress
	 *            the new server address
	 */
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	
	public void add(int cartId, int itemId) throws SupermarketException {
		CartItemTuple obj=new CartItemTuple(cartId,itemId);
		String urlString = serverAddress + "/" + SupermarketMessageTag.ADD;
		SupermarketRequest supermarketRequest = SupermarketRequest.newPostRequest(urlString, obj);
		SupermarketUtility.performHttpExchange(client, supermarketRequest, serializer.get());		
	};
	
	public void remove(int cartId, int itemId) throws SupermarketException {
		CartItemTuple obj=new CartItemTuple(cartId,itemId);
		String urlString = serverAddress + "/" + SupermarketMessageTag.REMOVE;
		SupermarketRequest supermarketRequest = SupermarketRequest.newPostRequest(urlString, obj);
		SupermarketUtility.performHttpExchange(client, supermarketRequest, serializer.get());		
	};
	
	@SuppressWarnings("unchecked")
	public List<CartItem> getCartItems(int cartId) throws SupermarketException{
		String urlEncodedCartId = null;

		try {
			urlEncodedCartId = URLEncoder.encode(Integer.toString(cartId), "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			throw new SupermarketException("unsupported cartId", ex);
		}		
		

		String urlString = serverAddress + "/" + SupermarketMessageTag.GETCARTITEMS + "?"
				+ SupermarketConstants.CART_ID + "=" + urlEncodedCartId;
		
		SupermarketRequest supermarketRequest = SupermarketRequest.newGetRequest(urlString);
		SupermarketResponse supermarketResponse = SupermarketUtility.performHttpExchange(client, supermarketRequest,
				serializer.get());
		return (List<CartItem>) supermarketResponse.getList();		
	};
	
	public void checkout(int cartId) throws SupermarketException{
		String urlString = serverAddress + "/" + SupermarketMessageTag.CHECKOUT;
		SupermarketRequest supermarketRequest = SupermarketRequest.newPostRequest(urlString, cartId);
		SupermarketUtility.performHttpExchange(client, supermarketRequest, serializer.get());
	}

	@Override
	public void resetCart() throws SupermarketException {
		String urlString = serverAddress + "/" + SupermarketMessageTag.CLEANCART;
		SupermarketRequest supermarketRequest = SupermarketRequest.newPostRequest(urlString, 0);
		SupermarketUtility.performHttpExchange(client, supermarketRequest, serializer.get());
		
	};
	
	

}
