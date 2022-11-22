package com.acertainsupermarket.business;

import com.acertainsupermarket.interfaces.Item;

/**
 * An immutable {@link Item} implementation to be used in query methods.
 * 
 * @author vmarcos
 */
public final class ImmutableItem implements Item {

	/**
	 * The ID of the item.
	 */
	private final int id;

	/**
	 * The name of the item.
	 */
	private final String name;

	/**
	 * The price of the item.
	 */
	private final double price;

	/**
	 * The quantity of the item in stock.
	 */
	private final int stock;

	/**
	 * The timestamp of the last time the item stock was replenished.
	 */
	private final long lastRestockingTimestamp;

	/**
	 * Instantiates a new {@link ImmutableItem} with given information.
	 * 
	 * @param id
	 * @param name
	 * @param price
	 * @param stockQuantity
	 * @param lastRestockingTimestamp
	 */
	public ImmutableItem(int id, String name, double price, int stock, long lastRestockingTimestamp) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.lastRestockingTimestamp = lastRestockingTimestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acertainsupermarket.interfaces.Item#getItemId()
	 */
	@Override
	public int getItemId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acertainsupermarket.interfaces.Item#getItemName()
	 */
	@Override
	public String getItemName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acertainsupermarket.interfaces.Item#getPrice()
	 */
	@Override
	public double getPrice() {
		return this.price;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acertainsupermarket.interfaces.Item#getStock()
	 */
	@Override
	public int getStock() {
		return this.stock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.acertainsupermarket.interfaces.Item#getLastRestockingTimestamp()
	 */
	@Override
	public long getLastRestockingTimestamp() {
		return this.lastRestockingTimestamp;
	}

	// TODO: include here code for hashCode and equals as required by your
	// implementation. Note that an item is identified by its ID.

}
