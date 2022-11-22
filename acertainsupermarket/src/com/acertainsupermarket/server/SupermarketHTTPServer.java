package com.acertainsupermarket.server;

import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.acertainsupermarket.business.CertainCart;
import com.acertainsupermarket.business.CertainSupermarket;
import com.acertainsupermarket.utils.SupermarketConstants;

public class SupermarketHTTPServer {
	/** The Constant defaultListenOnPort. */
	private static final int DEFAULT_PORT = 8081;
	private static final int MIN_THREADPOOL_SIZE = 10;
	private static final int MAX_THREADPOOL_SIZE = 100;
	
	/**
	 * Prevents the instantiation of a new {@link SupermarketHTTPServer}.
	 */
	private SupermarketHTTPServer() {
		// Prevent instances from being created.
	}
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		int listenOnPort = DEFAULT_PORT;
		
		SupermarketHTTPMessageHandler handler = null;

			CertainSupermarket supermarket = new CertainSupermarket();
			CertainCart carts = new CertainCart(supermarket);
			handler = new SupermarketHTTPMessageHandler(supermarket, carts);
	
		
		String serverPortString = System.getProperty(SupermarketConstants.PROPERTY_KEY_SERVER_PORT);

		if (serverPortString != null) {
			try {
				listenOnPort = Integer.parseInt(serverPortString);
			} catch (NumberFormatException ex) {
				System.err.println("Unsupported message tag");
			}
		}

		QueuedThreadPool threadpool = new QueuedThreadPool(MAX_THREADPOOL_SIZE, MIN_THREADPOOL_SIZE);
		SupermarketHTTPServerUtility.createServer(listenOnPort, handler, threadpool);
	}
}
