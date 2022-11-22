package com.acertainsupermarket.interfaces;

import java.util.List;

import com.acertainsupermarket.utils.InexistentCartException;
import com.acertainsupermarket.utils.InexistentItemException;
import com.acertainsupermarket.utils.NegativeIdentifierException;
import com.acertainsupermarket.utils.SupermarketException;

/**
 * The Cart service keeps track of the state of physical carts in the
 * self-checkout supermarket. This service is intended to be deployed across
 * several instances, each of which manages a disjoint set of carts from
 * separate supermarket locations.
 * 
 * For more details on the methods of this interface, please refer to the
 * programming task description.
 * 
 * @author vmarcos
 */
public interface Cart{

	/**
	 * Adds one unit of the given item to the provided cart.
	 * 
	 * @param cartId
	 *            - the identifier for the cart.
	 * @param itemId
	 *            - the identifier for the item.
	 * @throws {@link
	 *             NegativeIdentifierException}
	 * @throws {@link
	 *             InexistentCartException}
	 * @throws {@link
	 *             InexistentItemException}
	 * @throws {@link
	 *             SupermarketException}
	 */
	public void add(int cartId, int itemId)
			throws NegativeIdentifierException, InexistentCartException, InexistentItemException, SupermarketException;

	/**
	 * Removes one unit of the given item from the provided cart.
	 * 
	 * @param cartId
	 *            - the identifier for the cart.
	 * @param itemId
	 *            - the identifier for the item.
	 * @throws {@link
	 *             NegativeIdentifierException}
	 * @throws {@link
	 *             InexistentCartException}
	 * @throws {@link
	 *             InexistentItemException}
	 * @throws {@link
	 *             SupermarketException}
	 */
	public void remove(int cartId, int itemId)
			throws NegativeIdentifierException, InexistentCartException, InexistentItemException, SupermarketException;

	/**
	 * Obtains the information of all items currently in the given cart.
	 * 
	 * @param cartId
	 *            - the identifier for the cart.
	 * @return a list of immutable {@link CartItem} instances associated to the
	 *         cart.
	 * @throws {@link
	 *             NegativeIdentifierException}
	 * @throws {@link
	 *             InexistentCartException}
	 * @throws {@link
	 *             SupermarketException}
	 */
	public List<CartItem> getCartItems(int cartId)
			throws NegativeIdentifierException, InexistentCartException, SupermarketException;

	/**
	 * Checks out the contents of the cart, deducing the stocks from the
	 * {@link Supermarket} and finally emptying the cart.
	 * 
	 * @param cartId
	 *            - the identifier for the cart.
	 * @throws {@link
	 *             NegativeIdentifierException}
	 * @throws {@link
	 *             InexistentCartException}
	 * @throws {@link
	 *             SupermarketException}
	 */
	public void checkout(int cartId) throws NegativeIdentifierException, InexistentCartException, SupermarketException;
	
	public void resetCart() throws SupermarketException;

}
