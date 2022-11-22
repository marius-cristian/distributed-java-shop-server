package com.acertainsupermarket.utils;

public enum SupermarketMessageTag {
	/** The tag for the update stock message. */
	UPDATESTOCK,

	/** The tag for the get stock message. */
	GETITEMS,

	/** The tag for the add item into cart message. */
	ADD,

	/** The tag for the remove item from cart message. */
	REMOVE,

	/** The tag for the get cart items message. */
	GETCARTITEMS,

	/** The tag for the checkout message. */
	CHECKOUT, 
	
	/** The tag for cleaning carts. */
	CLEANCART, 
	
	/** The tag for cleaning the supermarkets */
	CLEANSUPERMARKET;
}
