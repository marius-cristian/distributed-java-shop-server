package com.acertainsupermarket.business;

import com.acertainsupermarket.interfaces.Item;
import com.acertainsupermarket.interfaces.Supermarket;

/**
 * A mutable {@link Item} implementation to be used in the {@link Supermarket}
 * implementation.
 * 
 * @author vmarcos
 */
public class MutableItem implements Item {

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
	private double price;

	/**
	 * The quantity of the item in stock.
	 */
	private int stock;

	/**
	 * The timestamp of the last time the item stock was replenished.
	 */
	private long lastRestockingTimestamp;

	/**
	 * Instantiates a new {@link MutableItem} with given information.
	 * 
	 * @param id
	 * @param name
	 */
	public MutableItem(int id, String name) {
		this.id = id;
		this.name = name;
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

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @param stock
	 *            the stock quantity to set
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}

	/**
	 * @param lastRestockingTimestamp
	 *            the lastRestockingTimestamp to set
	 */
	public void setLastRestockingTimestamp(long lastRestockingTimestamp) {
		this.lastRestockingTimestamp = lastRestockingTimestamp;
	}

	public ImmutableItem immutableItem() {
		return new ImmutableItem(this.id, this.name, this.price, this.stock, this.lastRestockingTimestamp);
	}
	// TODO: include here code for hashCode and equals as required by your
	// implementation. Note that an item is identified by its ID.

}
