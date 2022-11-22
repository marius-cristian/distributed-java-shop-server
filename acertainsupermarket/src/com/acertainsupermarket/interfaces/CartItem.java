package com.acertainsupermarket.interfaces;

/**
 * This interface represents information about an item in a given cart. The
 * {@link CartItem} records item name, price, and quantities for an item
 * associated to a cart.
 * 
 * @author vmarcos
 */
public interface CartItem {

	/**
	 * @return the item ID.
	 */
	public int getItemId();

	/**
	 * @return the name of the item in the cart.
	 */
	public String getItemName();

	/**
	 * @return the price of the item when it was added to the cart.
	 */
	public double getPrice();

	/**
	 * @return the quantity of the item added to the cart.
	 */
	public int getQuantity();

}