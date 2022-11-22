package com.acertainsupermarket.business;

/**
 * Instances of this class represent differences in quantity of a given item.
 * These differences are used to update stocks for the item.
 * 
 * @author vmarcos
 */
public final class ItemDelta {

	/**
	 * The item ID.
	 */
	private final int itemId;

	/**
	 * The difference (either positive or negative) in quantity to be applied to
	 * the item stock.
	 */
	private final int quantityDifference;

	/**
	 * Instantiates a new {@link ItemDelta}.
	 * 
	 * @param itemId
	 * @param quantityDifference
	 */
	public ItemDelta(int itemId, int quantityDifference) {
		this.itemId = itemId;
		this.quantityDifference = quantityDifference;
	}

	/**
	 * @return the item ID.
	 */
	public int getItemId() {
		return this.itemId;
	}

	/**
	 * @return the quantity difference for the item.
	 */
	public int getQuantityDifference() {
		return this.quantityDifference;
	}

}
