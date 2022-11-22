package com.acertainsupermarket.client.workloads;

import java.util.TreeMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.acertainsupermarket.business.ItemDelta;
import com.acertainsupermarket.utils.SupermarketConstants;

public class ItemSetGenerator {
	
	public ItemSetGenerator() {
	}
	/**
	 * 
	 * @param diversity, how many different items
	 * @param quant, how many of each item
	 * @return set of integer, itemId, quantity
	 */
	public Set<ItemDelta> itemQuantities(int diversity, int quant){
		Set<ItemDelta> result=new HashSet<ItemDelta>();
		diversity=(diversity>SupermarketConstants.NUMBER_OF_ITEMS-1)?SupermarketConstants.NUMBER_OF_ITEMS-1:diversity;
		//at most 25 items of the same type
		quant=quant%25;
		Random rnd=new Random();
		for(int i=0;i<diversity;i++) {
			Integer key=rnd.nextInt()%SupermarketConstants.NUMBER_OF_ITEMS;
			key = (key<0)? (-1*key) : key;
			int val=rnd.nextInt(quant+1)+1;
			ItemDelta x=new ItemDelta(key,(-1*val));
			if(result.contains(x)) {
				continue;
			}
			//exclude the possibility of 0, generate at most between 0 and 25; + 1
			
			result.add(x);
		}
		return result;
	}
	
	/**
	 * 
	 * @param diversity, how many different items
	 * @param quant, how many of each item
	 * @return set of integer, itemId, quantity
	 */
	public Set<ItemDelta> restockQuantities(int diversity, int quant){
		Set<ItemDelta> result=new HashSet<ItemDelta>();
		diversity=(diversity>SupermarketConstants.NUMBER_OF_ITEMS-1)?SupermarketConstants.NUMBER_OF_ITEMS-1:diversity;
		//at most 25 items of the same type
		Random rnd=new Random();
		for(int i=0;i<diversity;i++) {
			Integer key=rnd.nextInt()%SupermarketConstants.NUMBER_OF_ITEMS;
			key = (key<0)? (-1*key) : key;
			int val=rnd.nextInt(quant)+25; //at least 25 items
			ItemDelta x=new ItemDelta(key,val);
			if(result.contains(x)) {
				continue;
			}
			result.add(x);
		}
		return result;
	}
	
	public int cartId() {
		Random rnd=new Random();
		int val=rnd.nextInt(SupermarketConstants.NUMBER_OF_CARTS);
		return (val<0)?(-1*val):val;
	}
}
