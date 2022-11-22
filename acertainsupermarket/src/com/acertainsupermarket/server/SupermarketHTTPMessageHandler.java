package com.acertainsupermarket.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.acertainsupermarket.business.ItemDelta;
import com.acertainsupermarket.interfaces.Cart;
import com.acertainsupermarket.interfaces.Supermarket;
import com.acertainsupermarket.interfaces.SupermarketSerializer;
import com.acertainsupermarket.utils.CartItemTuple;
import com.acertainsupermarket.utils.SupermarketConstants;
import com.acertainsupermarket.utils.SupermarketException;
import com.acertainsupermarket.utils.SupermarketKryoSerializer;
import com.acertainsupermarket.utils.SupermarketMessageTag;
import com.acertainsupermarket.utils.SupermarketResponse;
import com.acertainsupermarket.utils.SupermarketUtility;
import com.acertainsupermarket.utils.SupermarketXStreamSerializer;
import com.esotericsoftware.kryo.io.Input;


public class SupermarketHTTPMessageHandler extends AbstractHandler  {
	/** The supermarket. */
	private Supermarket supermarket = null;
	
	/** The carts. */
	private Cart carts = null;

	/** The serializer. */
	private static ThreadLocal<SupermarketSerializer> serializer;

	/**
	 * Instantiates a new {@link SupermarketHTTPMessageHandler}.
	 *
	 * @param bookStore
	 *            the book store
	 */
	public SupermarketHTTPMessageHandler(Supermarket supermarket, Cart carts) {
		this.supermarket = supermarket;
		this.carts = carts;

		// Setup the type of serializer.
		if (SupermarketConstants.BINARY_SERIALIZATION) {
			serializer = ThreadLocal.withInitial(SupermarketKryoSerializer::new);
		} else {
			serializer = ThreadLocal.withInitial(SupermarketXStreamSerializer::new);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jetty.server.Handler#handle(java.lang.String,
	 * org.eclipse.jetty.server.Request, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		SupermarketMessageTag messageTag;
		String requestURI;

		response.setStatus(HttpServletResponse.SC_OK);
		requestURI = request.getRequestURI();
		
		messageTag = SupermarketUtility.convertURItoMessageTag(requestURI);
		if(messageTag==null) {
			System.err.println("No message tag.");
		}else {
			switch(messageTag) {
			case UPDATESTOCK: updateStock(request,response); break;
			case GETITEMS: getItems(request, response); break;
			case ADD: add(request, response); break;
			case REMOVE: remove(request, response); break;
			case GETCARTITEMS: getCartItems(request,response); break;
			case CHECKOUT: checkout(request,response); break;
			case CLEANCART: cleanCart(request,response); break;
			case CLEANSUPERMARKET: cleanSupermarket(request,response); break;
			default:
				System.err.println("Unsupported message tag.");
				break;
			}
		}
		// Mark the request as handled so that the HTTP response can be sent
		baseRequest.setHandled(true);
	}
	
	private void cleanCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SupermarketResponse supermarketResponse = new SupermarketResponse();
		try{
			carts.resetCart();
		}catch(SupermarketException ex) {
			supermarketResponse.setException(ex);
		}

		byte[] serializedResponseContent = serializer.get().serialize(supermarketResponse);
		response.getOutputStream().write(serializedResponseContent);		
		
	}
	private void cleanSupermarket(HttpServletRequest request, HttpServletResponse response) {
		SupermarketResponse supermarketResponse = new SupermarketResponse();
		try {
			supermarket.resetSupermarket();
		} catch(SupermarketException ex) {
			;
		}
		
	}
	
	private void checkout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		byte[] serializedRequestContent = getSerializedRequestContent(request);

		Integer cartId = (Integer) serializer.get().deserialize(serializedRequestContent);
		SupermarketResponse supermarketResponse = new SupermarketResponse();

		try {
			carts.checkout(cartId);
		} catch (SupermarketException ex) {
			supermarketResponse.setException(ex);
		}

		byte[] serializedResponseContent = serializer.get().serialize(supermarketResponse);
		response.getOutputStream().write(serializedResponseContent);
		
	}
	private void getCartItems(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String cartId = URLDecoder.decode(request.getParameter(SupermarketConstants.CART_ID), "UTF-8");
		SupermarketResponse supermarketResponse = new SupermarketResponse();

		try {
			int id = SupermarketUtility.convertStringToInt(cartId);
			supermarketResponse.setList(carts.getCartItems(id));
		} catch (SupermarketException ex) {
			supermarketResponse.setException(ex);
		}

		byte[] serializedResponseContent = serializer.get().serialize(supermarketResponse);
		response.getOutputStream().write(serializedResponseContent);
		
	}
	private void remove(HttpServletRequest request, HttpServletResponse response) throws IOException {
		byte[] serializedRequestContent = getSerializedRequestContent(request);

		CartItemTuple tuple = (CartItemTuple) serializer.get().deserialize(serializedRequestContent);
		SupermarketResponse supermarketResponse = new SupermarketResponse();

		try {
			carts.remove(tuple.cartId,tuple.itemId );
		} catch (SupermarketException ex) {
			supermarketResponse.setException(ex);
		}

		byte[] serializedResponseContent = serializer.get().serialize(supermarketResponse);
		response.getOutputStream().write(serializedResponseContent);
		
	}
	private void add(HttpServletRequest request, HttpServletResponse response) throws IOException {
		byte[] serializedRequestContent = getSerializedRequestContent(request);

		CartItemTuple tuple = (CartItemTuple) serializer.get().deserialize(serializedRequestContent);
		SupermarketResponse supermarketResponse = new SupermarketResponse();

		try {
			carts.add(tuple.cartId,tuple.itemId );
		} catch (SupermarketException ex) {
			supermarketResponse.setException(ex);
		}

		byte[] serializedResponseContent = serializer.get().serialize(supermarketResponse);
		response.getOutputStream().write(serializedResponseContent);
		
	}
	/**
	 * Updates the stock by item deltas.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	private void updateStock(HttpServletRequest request, HttpServletResponse response) throws IOException {
		byte[] serializedRequestContent = getSerializedRequestContent(request);

		List<ItemDelta> deltaSet = (List<ItemDelta>) serializer.get().deserialize(serializedRequestContent);
		SupermarketResponse supermarketResponse = new SupermarketResponse();

		try {
			supermarket.updateStocks(deltaSet);
		} catch (SupermarketException ex) {
			supermarketResponse.setException(ex);
		}

		byte[] serializedResponseContent = serializer.get().serialize(supermarketResponse);
		response.getOutputStream().write(serializedResponseContent);
	}

	/**
	 * Updates the stock by item deltas.
	 *
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	private void getItems(HttpServletRequest request, HttpServletResponse response) throws IOException {
		byte[] serializedRequestContent = getSerializedRequestContent(request);

		Set<Integer> itemIds = (Set<Integer>) serializer.get().deserialize(serializedRequestContent);
		SupermarketResponse supermarketResponse = new SupermarketResponse();

		try {
			supermarketResponse.setList(supermarket.getItems(itemIds));
		} catch (SupermarketException ex) {
			supermarketResponse.setException(ex);
		}

		byte[] serializedResponseContent = serializer.get().serialize(supermarketResponse);
		response.getOutputStream().write(serializedResponseContent);
	}	

	/**
	 * Gets the serialized request content.
	 *
	 * @param request the request
	 * @return the serialized request content
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private byte[] getSerializedRequestContent(HttpServletRequest request) throws IOException {
		Input in = new Input(request.getInputStream());
		byte[] serializedRequestContent = in.readBytes(request.getContentLength());
		in.close();
		return serializedRequestContent;
	}	
}
