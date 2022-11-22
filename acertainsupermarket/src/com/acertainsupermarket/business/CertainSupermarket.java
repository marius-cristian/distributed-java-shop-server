package com.acertainsupermarket.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.acertainsupermarket.interfaces.Item;
import com.acertainsupermarket.interfaces.Supermarket;
import com.acertainsupermarket.utils.InexistentItemException;
import com.acertainsupermarket.utils.NegativeIdentifierException;
import com.acertainsupermarket.utils.SupermarketConstants;
import com.acertainsupermarket.utils.SupermarketException;


public class CertainSupermarket implements Supermarket {
	/** 
	 * 
	 */
	//static int debug=0;
	final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);
	private Map<Integer, MutableItem> itemMap = null;
	private Map<Integer, ReentrantReadWriteLock> lockMap = null;	
	
	/** Constructor
	 * Instantiates the stock with the given items 
	 */
	public CertainSupermarket(){
		itemMap = new HashMap<>();
		lockMap = new HashMap<>();
		for(Integer i=0; i<SupermarketConstants.NUMBER_OF_ITEMS; i++) {
			MutableItem item=new MutableItem(i, "Item "+i);
			item.setLastRestockingTimestamp(System.currentTimeMillis() / 1000L);
			item.setPrice(10+i);
			item.setStock(1000);
			itemMap.put(i,item);
			lockMap.put(i, new ReentrantReadWriteLock(true));
		}
	}
	
	/**
	 * Used to reset the supermarket
	 */
	@Override
	public void resetSupermarket(){
		itemMap = new HashMap<>();
		lockMap = new HashMap<>();
		for(Integer i=0; i<SupermarketConstants.NUMBER_OF_ITEMS; i++) {
			MutableItem item=new MutableItem(i, "Item "+i);
			item.setLastRestockingTimestamp(System.currentTimeMillis() / 1000L);
			item.setPrice(10+i);
			item.setStock(SupermarketConstants.STOCK_PER_ITEM);
			itemMap.put(i,item);
			lockMap.put(i, new ReentrantReadWriteLock());
		}		
	}
	
	@Override
	public void updateStocks(List<ItemDelta> itemDeltas)
			throws NegativeIdentifierException, InexistentItemException, SupermarketException {
		if(itemDeltas == null) {
			throw new SupermarketException(SupermarketConstants.NULL_INPUT);
		}
		itemDeltas.sort((ItemDelta a, ItemDelta b)-> {return a.getItemId()-b.getItemId();} );
		rwl.readLock().lock();
		Boolean restock=true;
		try{
			for(ItemDelta item : itemDeltas) {
			if(item.getItemId()<0) {
				throw new NegativeIdentifierException(SupermarketConstants.NEGATIVE_ID + item.getItemId());
			}
			if(!itemMap.containsKey(item.getItemId())) {
				throw new InexistentItemException(SupermarketConstants.INEXISTENT_ITEM + item.getItemId());
			}
			//check if delta forces item into invalid state ex: negative stock which is impossible in the real world.
			if(((item.getQuantityDifference()+itemMap.get(item.getItemId()).getStock()))<0) {
				throw new SupermarketException(SupermarketConstants.INVALID_STATE + item.getItemId() +" qty:" +item.getQuantityDifference());
			}
			if(item.getQuantityDifference()<0) {
				restock=false;
			}
		}
			}catch(Exception e) {
				rwl.readLock().unlock();
				throw e;
		}
		try {
			/* we assume that if the difference in deltas is not negative
			 * then the operation is a re-stock 
			 */
			//now accquire write locks for each item
			for(ItemDelta item : itemDeltas) {
				lockMap.get(item.getItemId()).writeLock().lock();
			}
			for(ItemDelta item : itemDeltas) {
				//debug++;
				//System.out.println(debug);
				MutableItem stockItem=itemMap.get(item.getItemId());
				int stock=stockItem.getStock() + item.getQuantityDifference();
				stockItem.setStock(stock);
				if(restock) {
					stockItem.setLastRestockingTimestamp(System.currentTimeMillis() / 1000L);
				}
			}
		}finally {
			itemDeltas.sort((ItemDelta a, ItemDelta b)-> {return b.getItemId()-a.getItemId();} );
			for(ItemDelta item : itemDeltas) {
				lockMap.get(item.getItemId()).writeLock().unlock();
			}
			rwl.readLock().unlock();
		}
		
	}

	@Override
	public List<Item> getItems(Set<Integer> itemIds)
			throws NegativeIdentifierException, InexistentItemException, SupermarketException {
		if (itemIds == null) {
			throw new SupermarketException(SupermarketConstants.NULL_INPUT);
		}
        List<Item> listItems = new ArrayList<>();
        try {
            rwl.readLock().lock();
            for (Integer id : itemIds) {
                if (id<0) {
                    throw new NegativeIdentifierException(SupermarketConstants.NEGATIVE_ID + id);
                }
                if (!itemMap.containsKey(id)) {
                    throw new InexistentItemException(SupermarketConstants.INEXISTENT_ITEM + id);
                }
            }
			for(Integer id : itemIds) {
				lockMap.get(id).readLock().lock();
			}            
            for (Integer id : itemIds) {
                listItems.add(itemMap.get(id).immutableItem());
                lockMap.get(id).readLock().unlock();
            }
        }
        finally {
            rwl.readLock().unlock();
        }
		return listItems;
	}
}
