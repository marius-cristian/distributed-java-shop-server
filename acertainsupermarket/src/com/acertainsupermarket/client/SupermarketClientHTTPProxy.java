package com.acertainsupermarket.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.acertainsupermarket.business.ItemDelta;
import com.acertainsupermarket.interfaces.CartItem;
import com.acertainsupermarket.interfaces.Item;
import com.acertainsupermarket.interfaces.Supermarket;
import com.acertainsupermarket.interfaces.SupermarketSerializer;
import com.acertainsupermarket.utils.InexistentItemException;
import com.acertainsupermarket.utils.NegativeIdentifierException;
import com.acertainsupermarket.utils.SupermarketConstants;
import com.acertainsupermarket.utils.SupermarketException;
import com.acertainsupermarket.utils.SupermarketKryoSerializer;
import com.acertainsupermarket.utils.SupermarketMessageTag;
import com.acertainsupermarket.utils.SupermarketRequest;
import com.acertainsupermarket.utils.SupermarketResponse;
import com.acertainsupermarket.utils.SupermarketUtility;
import com.acertainsupermarket.utils.SupermarketXStreamSerializer;

public class SupermarketClientHTTPProxy implements Supermarket{
	/** The client. */
	protected HttpClient client;

	/** The server address. */
	protected String serverAddress;

	/** The serializer. */
	private static ThreadLocal<SupermarketSerializer> serializer;
	
	/**
	 * Initializes a new {@link SupermarketClientHTTPProxy}.
	 *
	 * @param serverAddress
	 *            the server address
	 * @throws Exception
	 *             the exception
	 */
	public SupermarketClientHTTPProxy(String serverAddress) throws Exception {

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
	@Override
	public void updateStocks(List<ItemDelta> itemDeltas) throws SupermarketException {
		String urlString = serverAddress + "/" + SupermarketMessageTag.UPDATESTOCK;
		SupermarketRequest supermarketRequest = SupermarketRequest.newPostRequest(urlString, itemDeltas);
		SupermarketUtility.performHttpExchange(client, supermarketRequest, serializer.get());
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Item> getItems(Set<Integer> itemIds) throws SupermarketException{
		String urlString = serverAddress + "/" + SupermarketMessageTag.GETITEMS;
		SupermarketRequest supermarketRequest = SupermarketRequest.newPostRequest(urlString, itemIds);
		SupermarketResponse supermarketResponse = SupermarketUtility.performHttpExchange(client, supermarketRequest, serializer.get());
		return (List<Item>) supermarketResponse.getList();			
	}

	@Override
	public void resetSupermarket() throws SupermarketException {
		String urlString = serverAddress + "/" + SupermarketMessageTag.CLEANSUPERMARKET;
		SupermarketRequest supermarketRequest = SupermarketRequest.newPostRequest(urlString, 0);
		SupermarketUtility.performHttpExchange(client, supermarketRequest, serializer.get());
	}


}
