package com.acertainsupermarket.interfaces;

/**
 * This interface represents the information of an item sold by the
 * {@link Supermarket}.
 * 
 * @author vmarcos
 */
public interface Item {

	/**
	 * @return the item ID.
	 */
	public int getItemId();

	/**
	 * @return the name of the item.
	 */
	public String getItemName();

	/**
	 * @return the price of the item.
	 */
	public double getPrice();

	/**
	 * @return the quantity of the item in stock.
	 */
	public int getStock();

	/**
	 * @return the timestamp (see @link System.currentTimeMillis) of the last
	 *         time the stock was replenished.
	 */
	public long getLastRestockingTimestamp();

}