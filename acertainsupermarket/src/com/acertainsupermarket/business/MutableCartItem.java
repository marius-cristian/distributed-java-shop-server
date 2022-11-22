package com.acertainsupermarket.business;

import com.acertainsupermarket.interfaces.Cart;
import com.acertainsupermarket.interfaces.CartItem;

/**
 * A mutable {@link CartItem} implementation to be used in the {@link Cart}
 * implementation.
 * 
 * @author vmarcos
 */
public class MutableCartItem implements CartItem {

	/**
	 * The ID of the item.
	 */
	private final int id;

	/**
	 * The name of the item.
	 */
	private final String name;

	/**
	 * The price of the item when it was added to the cart.
	 */
	private final double price;

	/**
	 * The quantity of the item in the cart.
	 */
	private int quantity;

	/**
	 * Instantiates a new {@link MutableCartItem} with given information.
	 * 
	 * @param id
	 * @param name
	 */
	public MutableCartItem(int id, String name, double price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acertainsupermarket.interfaces.CartItem#getItemId()
	 */
	@Override
	public int getItemId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acertainsupermarket.interfaces.CartItem#getItemName()
	 */
	@Override
	public String getItemName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acertainsupermarket.interfaces.CartItem#getPrice()
	 */
	@Override
	public double getPrice() {
		return this.price;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acertainsupermarket.interfaces.CartItem#getQuantity()
	 */
	@Override
	public int getQuantity() {
		return this.quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	// TODO: include here code for hashCode and equals as required by your
	// implementation. Note that an item is identified by its ID.

}
