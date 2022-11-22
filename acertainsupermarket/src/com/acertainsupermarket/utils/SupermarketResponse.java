package com.acertainsupermarket.utils;

import java.util.LinkedList;
import java.util.List;

import com.acertainsupermarket.business.ImmutableItem;

public class SupermarketResponse {
	
	/** The exception. */
	private SupermarketException exception;

	/** The list. */
	private List<?> list;

	/**
	 * Instantiates a new {@link SupermarketResponse}.
	 *
	 * @param exception
	 *            the exception
	 * @param list
	 *            the list
	 */
	public SupermarketResponse(SupermarketException exception, List<ImmutableItem> list) {
		this.setException(exception);
		this.setList(list);
	}

	/**
	 * Instantiates a new book store response.
	 */
	public SupermarketResponse() {
		this.setException(null);
		this.setList(new LinkedList<>());
	}

	/**
	 * Gets the list.
	 *
	 * @return the list
	 */
	public List<?> getList() {
		return list;
	}

	/**
	 * Sets the list.
	 *
	 * @param list
	 *            the new list
	 */
	public void setList(List<?> list) {
		this.list = list;
	}

	/**
	 * Gets the exception.
	 *
	 * @return the exception
	 */
	public SupermarketException getException() {
		return exception;
	}

	/**
	 * Sets the exception.
	 *
	 * @param exception
	 *            the new exception
	 */
	public void setException(SupermarketException exception) {
		this.exception = exception;
	}	

}
