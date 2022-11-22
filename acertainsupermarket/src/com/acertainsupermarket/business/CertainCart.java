/**
 * 
 */
package com.acertainsupermarket.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.acertainsupermarket.interfaces.Cart;
import com.acertainsupermarket.interfaces.CartItem;
import com.acertainsupermarket.interfaces.Item;
import com.acertainsupermarket.interfaces.Supermarket;
import com.acertainsupermarket.utils.InexistentCartException;
import com.acertainsupermarket.utils.InexistentItemException;
import com.acertainsupermarket.utils.NegativeIdentifierException;
import com.acertainsupermarket.utils.SupermarketConstants;
import com.acertainsupermarket.utils.SupermarketException;

/**
 * @author marius
 *
 */
public class CertainCart implements Cart {
	Supermarket supermarket=null;
	Map<Integer, Map<Integer, MutableCartItem>> cartMap=null;
	
	final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);
	private Map<Integer, ReentrantReadWriteLock> lockMap = null;	
	
	public CertainCart(Supermarket market){
		this.supermarket=market;
		lockMap=new HashMap<>();
		
		cartMap = new HashMap<Integer, Map<Integer, MutableCartItem>>();
		for(Integer i=0; i<SupermarketConstants.NUMBER_OF_CARTS;i++) {
			cartMap.put(i, new HashMap<Integer, MutableCartItem>());
			lockMap.put(i, new ReentrantReadWriteLock(true));
		}		
	}
	/**
	 * Reset carts
	 */
	@Override
	public void resetCart() {
		lockMap=new HashMap<>();
		
		cartMap = new HashMap<Integer, Map<Integer, MutableCartItem>>();
		for(Integer i=0; i<SupermarketConstants.NUMBER_OF_CARTS;i++) {
			cartMap.put(i, new HashMap<Integer, MutableCartItem>());
			lockMap.put(i, new ReentrantReadWriteLock());
		}		
	}
	/* (non-Javadoc)
	 * @see com.acertainsupermarket.interfaces.Cart#add(int, int)
	 */
	@Override
	public void add(int cartId, int itemId)
			throws NegativeIdentifierException, InexistentCartException, InexistentItemException, SupermarketException {
		if(itemId<0) {
			throw new NegativeIdentifierException(SupermarketConstants.NEGATIVE_ID + " item: "+itemId);
		}
		if(cartId<0) {
			throw new NegativeIdentifierException(SupermarketConstants.NEGATIVE_ID + " cart: "+cartId);
		}
		if(!cartMap.containsKey(cartId)) {
			throw new InexistentCartException();
		}
		rwl.readLock().lock();
		try {
			//get cart item list
			Map<Integer, MutableCartItem> cart=cartMap.get(cartId);
			//check if item is in cart
			if(cart.containsKey(itemId)) {
				//write lock for item increment in cart
				lockMap.get(cartId).writeLock().lock();
				//increment item quantity
				int quantity = cart.get(itemId).getQuantity()+1;
				cart.get(itemId).setQuantity(quantity);
			}else{
				Set<Integer> getItem = new TreeSet<Integer>();
				getItem.add(itemId);
				//get supermarket list
				List<Item> items = supermarket.getItems(getItem);
				if(items==null) {
					throw new InexistentItemException(SupermarketConstants.INEXISTENT_ITEM);
				}
				lockMap.get(cartId).writeLock().lock();
				//add item to cart
				for(Item i : items) {
					MutableCartItem aux=new MutableCartItem(i.getItemId(), i.getItemName(), i.getPrice());
					aux.setQuantity(1);
					cart.put(i.getItemId(), aux);
				}				
			}
		}finally {
			lockMap.get(cartId).writeLock().unlock();
			rwl.readLock().unlock();
		}

	}

	/* (non-Javadoc)
	 * @see com.acertainsupermarket.interfaces.Cart#remove(int, int)
	 */
	@Override
	public void remove(int cartId, int itemId)
			throws NegativeIdentifierException, InexistentCartException, InexistentItemException, SupermarketException {
		if(itemId<0) {
			throw new NegativeIdentifierException(SupermarketConstants.NEGATIVE_ID + " item: "+itemId);
		}
		if(cartId<0) {
			throw new NegativeIdentifierException(SupermarketConstants.NEGATIVE_ID + " cart: "+cartId);
		}
		if(!cartMap.containsKey(cartId)) {
			throw new InexistentCartException();
		}
		//rwl.readLock().lock();
		try {
			Map<Integer, MutableCartItem> cart=cartMap.get(cartId);
			if(!cart.containsKey(itemId)) {
				throw new InexistentItemException(SupermarketConstants.INEXISTENT_ITEM + itemId);
			}
			lockMap.get(cartId).writeLock().lock();
			int quantity=cart.get(itemId).getQuantity() -1;
			if(quantity<=0) {
				cart.remove(itemId);
			}else {
				cart.get(itemId).setQuantity(quantity);
			}
		}finally {
			lockMap.get(cartId).writeLock().unlock();
			//rwl.readLock().unlock();
		}
	}

	/* (non-Javadoc)
	 * @see com.acertainsupermarket.interfaces.Cart#getCartItems(int)
	 */
	@Override
	public List<CartItem> getCartItems(int cartId)
			throws NegativeIdentifierException, InexistentCartException, SupermarketException {
		if(cartId<0) {
			throw new NegativeIdentifierException(SupermarketConstants.NEGATIVE_ID + " cart: "+cartId);
		}
		if(!cartMap.containsKey(cartId)) {
			throw new InexistentCartException();
		}
		lockMap.get(cartId).readLock().lock();
		Map<Integer, MutableCartItem> cart=cartMap.get(cartId);
		List<CartItem> result=new ArrayList<CartItem>(cart.values());
		lockMap.get(cartId).readLock().unlock();
		return result;
	}

	/* (non-Javadoc)
	 * @see com.acertainsupermarket.interfaces.Cart#checkout(int)
	 */
	@Override
	public void checkout(int cartId) throws NegativeIdentifierException, InexistentCartException, SupermarketException {
		if(cartId<0) {
			throw new NegativeIdentifierException(SupermarketConstants.NEGATIVE_ID + " cart: "+cartId);
		}
		if(!cartMap.containsKey(cartId)) {
			throw new InexistentCartException();
		}
		rwl.readLock().lock();
		try{
			List<ItemDelta> deltas=new ArrayList<ItemDelta>();
			lockMap.get(cartId).writeLock().lock();
			Map<Integer, MutableCartItem> cart=cartMap.get(cartId);
			List<CartItem> items=new ArrayList<CartItem>(cart.values());
			for(CartItem item : items) {
				//because we buy, delta is negative
				ItemDelta i=new ItemDelta(item.getItemId(),(-1*item.getQuantity()));
				deltas.add(i);
			}
			//try to set stock in supermarket
			supermarket.updateStocks(deltas);
			//if it throws error, execution stops when we reach this comment, everything went fine
			//everything went fine, time to clear basket
			cartMap.remove(cartId);
			cartMap.put(cartId, new HashMap<Integer, MutableCartItem>());
			
		}catch(SupermarketException ex){ex.printStackTrace();}finally {
			lockMap.get(cartId).writeLock().unlock();
			rwl.readLock().unlock();
		}
	}

}
