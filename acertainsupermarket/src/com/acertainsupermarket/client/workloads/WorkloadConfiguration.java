package com.acertainsupermarket.client.workloads;

import java.util.Map;

import com.acertainsupermarket.interfaces.Cart;
import com.acertainsupermarket.interfaces.Supermarket;

public class WorkloadConfiguration {
	public ItemSetGenerator generator=null;
	public Supermarket supermarket=null;
	public Cart carts=null;
	public float percentRareResuplyInteraction=10f;
	public float percentFrequentResuplyInteraction=30f;
	
	public WorkloadConfiguration(ItemSetGenerator g, Supermarket s, Cart c){
		generator=g;
		supermarket=s;
		carts=c;
	}	
}
