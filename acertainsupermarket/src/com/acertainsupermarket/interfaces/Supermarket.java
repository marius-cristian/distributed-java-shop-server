package com.acertainsupermarket.interfaces;

import java.util.List;
import java.util.Set;

import com.acertainsupermarket.business.ItemDelta;
import com.acertainsupermarket.utils.InexistentItemException;
import com.acertainsupermarket.utils.NegativeIdentifierException;
import com.acertainsupermarket.utils.SupermarketException;

/**
 * This interface represents the supermarket service. The {@link Supermarket}
 * instance handles the back-end stock and item management for the supermarket.
 * 
 * For more details on the methods of this interface, please refer to the
 * programming task description.
 * 
 * @author vmarcos
 */
public interface Supermarket {

	/**
	 * Updates the stocks of given items identified by ID with differences
	 * (positive or negative) to be applied to their quantities.
	 * 
	 * @param itemDeltas
	 *            - the list of item IDs and differences to be applied to the
	 *            item quantities in stock.
	 * @throws {@link
	 *             NegativeIdentifierException}
	 * @throws {@link
	 *             InexistentItemException}
	 * @throws {@link
	 *             SupermarketException}
	 */
	public void updateStocks(List<ItemDelta> itemDeltas)
			throws NegativeIdentifierException, InexistentItemException, SupermarketException;

	/**
	 * Retrieves a set of items by ID.
	 * 
	 * @param itemIds
	 *            - the IDs of items to be retrieved.
	 * @return the list of (immutable) items requested.
	 * @throws {@link
	 *             NegativeIdentifierException}
	 * @throws {@link
	 *             InexistentItemException}
	 * @throws {@link
	 *             SupermarketException}
	 */
	public List<Item> getItems(Set<Integer> itemIds)
			throws NegativeIdentifierException, InexistentItemException, SupermarketException;

	public void resetSupermarket() throws SupermarketException;

}
