package com.acertainsupermarket.business;

import com.acertainsupermarket.interfaces.CartItem;

/**
 * An immutable {@link CartItem} implementation to be used in query methods.
 * 
 * @author vmarcos
 */
public final class ImmutableCartItem implements CartItem {

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
	private final int quantity;

	/**
	 * Instantiates a new {@link ImmutableCartItem} with given information.
	 * 
	 * @param id
	 * @param name
	 * @param price
	 * @param quantity
	 */
	public ImmutableCartItem(int id, String name, double price, int quantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
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

	// TODO: include here code for hashCode and equals as required by your
	// implementation. Note that an item is identified by its ID.
}
