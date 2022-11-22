package com.acertainsupermarket.client.tests;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.acertainsupermarket.business.CertainCart;
import com.acertainsupermarket.business.CertainSupermarket;
import com.acertainsupermarket.business.ImmutableItem;
import com.acertainsupermarket.business.ItemDelta;
import com.acertainsupermarket.client.CartClientHTTPProxy;
import com.acertainsupermarket.client.SupermarketClientHTTPProxy;
import com.acertainsupermarket.interfaces.Cart;
import com.acertainsupermarket.interfaces.CartItem;
import com.acertainsupermarket.interfaces.Item;
import com.acertainsupermarket.interfaces.Supermarket;
import com.acertainsupermarket.utils.InexistentCartException;
import com.acertainsupermarket.utils.InexistentItemException;
import com.acertainsupermarket.utils.NegativeIdentifierException;
import com.acertainsupermarket.utils.SupermarketConstants;
import com.acertainsupermarket.utils.SupermarketException;


public class SupermarketTest {
	private static boolean localTest = false;
	private static Supermarket market = null;
	private static Cart cart = null;
	
	/**
	 * Sets the up before class.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		try {
			String localTestProperty = System.getProperty(SupermarketConstants.PROPERTY_KEY_LOCAL_TEST);
			localTest = (localTestProperty != null) ? Boolean.parseBoolean(localTestProperty) : localTest;

			if (localTest) {
				market = new CertainSupermarket();
				cart = new CertainCart(market);
			}
			else {
				market = new SupermarketClientHTTPProxy("http://localhost:8081");
				cart = new CartClientHTTPProxy("http://localhost:8081");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Stops server
	 * @throws SupermarketException 
	 */
	@AfterClass
	public static void AfterClass() throws SupermarketException {
		//market.resetSupermarket();
		//cart.resetCart();
		if(!localTest) {
			((SupermarketClientHTTPProxy) market).stop();
			((CartClientHTTPProxy)cart).stop();
		}
	}
	
	 /**
	  * Test invalid cart item id
	  */
	 @Test
	 public void invalidCartId() {
		 try {
			 cart.add(-1, 1);
			 fail();
		 }catch(Exception e) {
			 assertTrue(true);
		 }
	 }
	 
	 /**
	  * Tries to checkout 
	 * @throws SupermarketException 
	 * @throws InexistentItemException 
	 * @throws NegativeIdentifierException 
	  */
	 @Test
	 public void simpleCheckout() throws NegativeIdentifierException, InexistentItemException, SupermarketException {
		 int cart1Id=21;
		 Set<Integer> itemsForCart1=new HashSet<Integer>();
		 itemsForCart1.add(220);
		 itemsForCart1.add(222);
		 itemsForCart1.add(224);
		 itemsForCart1.add(226);
		 itemsForCart1.add(228);
		 itemsForCart1.add(232);
		 List<Item> cart1= market.getItems(itemsForCart1);
		 //fill up the shopping carts with more items that there exists
		 for(Item i : cart1) {
			 for(int n=0;n<i.getStock();n++){
				 cart.add(cart1Id, i.getItemId());
			 	}
		 }
		 cart.checkout(cart1Id);
		 
		 cart1=market.getItems(itemsForCart1);
		 for(Item i : cart1) {
			 if(i.getStock()!=0) {
				 fail();
			 }
		 }
		 assertTrue(true);
		 
	 }
	 
	 /** 
	  * Test cart checkout function concurent
	 * @throws SupermarketException 
	 * @throws InexistentItemException 
	 * @throws NegativeIdentifierException 
	  */
	 @Test
	 public void checkoutAllOfTheItems() throws NegativeIdentifierException, InexistentItemException, SupermarketException {
		 //use cart 1 as cart id 21
		 //cart 2 as cart id 22
		 int cart1Id=21;
		 int cart2Id=22;
		 Set<Integer> itemsForCart1=new HashSet<Integer>();
		 itemsForCart1.add(120);
		 itemsForCart1.add(122);
		 itemsForCart1.add(124);
		 itemsForCart1.add(126);
		 itemsForCart1.add(128);
		 itemsForCart1.add(132);
		 Set<Integer> itemsForCart2=new HashSet<Integer>();
		 itemsForCart2.add(128);
		 itemsForCart2.add(130);
		 itemsForCart2.add(127);
		 itemsForCart2.add(123);
		 itemsForCart2.add(121);
		 itemsForCart2.add(120);
		 List<Item> cart1= market.getItems(itemsForCart1);
		 List<Item> cart2= market.getItems(itemsForCart1);
		 //fill up the shopping carts with more items that there exists
		 
		 Thread c1 = new Thread("c1") {
			 public void run() {
				 try {
					 for(Item i : cart1) {
						 for(int n=0;n<i.getStock();n++){
							 cart.add(cart1Id, i.getItemId());
						 	}
					 }					 					 
					cart.checkout(cart1Id);
					System.out.println("did this C1 succeed?");
				} catch (NegativeIdentifierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InexistentCartException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SupermarketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		 };
		 
		 Thread c2 = new Thread("c2") {
			 public void run() {
				 try {
						for(Item i : cart2) {
							for(int n=0;n<i.getStock();n++){
								cart.add(cart2Id, i.getItemId());
							 }
						 }					 
					cart.checkout(cart2Id);
					System.out.println("did this C2 succeed?");
				} catch (NegativeIdentifierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InexistentCartException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SupermarketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		 };
		 c1.start();
		 c2.start();
		 try {
			c1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 try {
			c2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 //one of the carts should fail; and one should succeed and get cleared
		 List<CartItem> l1 = cart.getCartItems(cart1Id);
		 List<CartItem> l2 = cart.getCartItems(cart2Id);
		 System.out.println("C1");
		 for(CartItem c : l1) {
			 System.out.println("id: "+c.getItemId()+", qty: "+c.getQuantity());
		 }
		 System.out.println("C2");
		 for(CartItem c : l2) {
			 System.out.println("id: "+c.getItemId()+", qty: "+c.getQuantity());
		 }		 
		 if (l1.isEmpty() && l2.isEmpty()) {
			 fail();		 
		 }
		 if((l1.isEmpty() && !l2.isEmpty())||(!l1.isEmpty() && l2.isEmpty()) ) {
			 assertTrue(true);
		 }else {			 
			 fail();
		 }
	 }
	 
	 
	
	
	/**
	 * Adds some items in the cart, then checks to see if they cool; 
	 *
	 */
	 @Test
	 public void testGetCartItems() throws SupermarketException{
		 int cartId=1;
		 try {
			 //add 10 times item with id 1
			 for(int i=0; i<10; i++) {
				 cart.add(cartId, 1);			 
			 }
			 //add one time, items with id 2 to 9
			 for(int i=2; i<10;i++) {
				 cart .add(cartId, i);
			 }
			 //get cart items and check if items 
			 List<CartItem> res=cart.getCartItems(cartId);
			 //should be 10 items;
			 //and the first item should have quantity 10
			 System.out.println(res);
			 Boolean flag=true;
			 if(res.size()!=9) {
				 System.out.println(res.size());
				 flag=false;
			 }
			 System.out.println(res.get(0).getQuantity());
			 if(res.get(0).getQuantity()!=10) {
				 System.out.println("quant");
				 System.out.println(res.get(0).getQuantity());
				 flag=false;
			 }
			 int i=1;
			 for(CartItem itm : res) {
				 if(itm.getItemId()!=i) {
					 flag=false;
				 }
				 i++;
			 }
			 assertTrue(flag);
		 }catch(SupermarketException e) {
			 //if error, fail
			 System.out.println(e.getMessage());
			 fail();
		 }
	 }
	 
		/**
		 * Tests the method get items in CertainSupermarket on valid input.
		 * @throws SupermarketException 
		 * @throws InexistentItemException 
		 * @throws NegativeIdentifierException 
		 *
		 */
		 @Test
		 public void testSupermarketGetItems() throws NegativeIdentifierException, InexistentItemException, SupermarketException {
			 Set<Integer> itemId=new HashSet<Integer>();
			 for(Integer i=0; i<100;i++) {
				 itemId.add(i);
			 }
			 List<Item> res = market.getItems(itemId);
			 for(Integer i=0; i<100;i++) {
				 if(res.get(i).getItemId()!=i) {
					 fail();
				 }
			 }
			 assertTrue(true);
		 }
		 
		 /**
		  * Tests Update Stock on valid input both negative and positive
		 * @throws SupermarketException 
		 * @throws InexistentItemException 
		 * @throws NegativeIdentifierException 
		  * 
		  * 
		  */
		 @Test
		 public void testUpdateStock() throws NegativeIdentifierException, InexistentItemException, SupermarketException {
			 long timeStamp=System.currentTimeMillis() / 1000L;
			 Set<Integer> itemId=new HashSet<Integer>();
			 for(Integer i=0; i<100;i++) {
				 itemId.add(i);
			 }			 
			 //stock state before updates
			 List<Item> beforeModification = market.getItems(itemId);
			 
			 List<ItemDelta> positiveDeltas=new ArrayList<ItemDelta>();
			 List<ItemDelta> negativeDeltas=new ArrayList<ItemDelta>();
			 for(int i =0 ; i <100;i++) {
				 ItemDelta toBuy=new ItemDelta(i,(-1*(10+i)));
				 negativeDeltas.add(toBuy);
				 ItemDelta toRestock=new ItemDelta(i,(10+i));
				 positiveDeltas.add(toRestock);
			 }
			 
			 //first removes items and checks the stocks
			 market.updateStocks(negativeDeltas);
			 List<Item> afterModification = market.getItems(itemId);
			 for(int i=0; i< 100; i++) {
				 //fail if stock did not get modified
				 if(beforeModification.get(i).getStock()!= (afterModification.get(i).getStock()+(10+i))) {
					 fail();
				 }
			 }
			 market.updateStocks(positiveDeltas);
			 afterModification=market.getItems(itemId);
			 for(int i=0; i< 100; i++) {
				 //fail if stock did not get modified
				 if(beforeModification.get(i).getStock()!= afterModification.get(i).getStock()) {
					 fail();
				 }
				 //check if restocking timestamp got updated
				 //because unix time conversion precision disregards miliseconds; 
				 //we just check to see if the time stamps are in chronological order 
				 if(timeStamp > afterModification.get(i).getLastRestockingTimestamp()) {
					 fail();
				 }
			 }
			 //tests succeeded
			 assertTrue(true);
		 }
		 
		 /**
		  * Tests cart checkout and supermarket update stock concurently on the same item set
		 * @throws SupermarketException 
		 * @throws InexistentItemException 
		 * @throws NegativeIdentifierException 
		  * 
		  * 
		  */		 
		 @Test
		 public void replenishStock() throws NegativeIdentifierException, InexistentItemException, SupermarketException{
			 int cart1Id=51;
			 Set<Integer> itemsForCart1=new HashSet<Integer>();
			 itemsForCart1.add(420);
			 itemsForCart1.add(422);
			 itemsForCart1.add(424);
			 itemsForCart1.add(426);
			 itemsForCart1.add(428);
			 itemsForCart1.add(432);
			 List<Item> cart1= market.getItems(itemsForCart1);
			 //fill up the shopping carts with more items that there exists
			 List<ItemDelta> deltas=new ArrayList<ItemDelta>();
			 deltas.add(new ItemDelta(420, 100));
			 deltas.add(new ItemDelta(422, 100));
			 deltas.add(new ItemDelta(424, 100));
			 deltas.add(new ItemDelta(426, 100));
			 deltas.add(new ItemDelta(428, 100));
			 deltas.add(new ItemDelta(432, 100));
			 int repeat=100;
			 
			 cart.checkout(cart1Id);
			 Thread c1 = new Thread("c1") {
				 public void run() {
					 for(int r=0;r<repeat;r++) {
						 for(int i : itemsForCart1) {
							 for(int n=0;n<100;n++){
								 try {
									cart.add(cart1Id, i);
								}catch (SupermarketException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							 }
						 }
						 try {
							cart.checkout(cart1Id);
						} catch (SupermarketException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							fail();
						}
					 }	 
				 }
			 };
			 
			 Thread c2 = new Thread("c2") {
				 public void run() {
					 for(int r=0; r<repeat;r++) {
						 try {
						 market.updateStocks(deltas);
						 }catch(SupermarketException ex) {
							 ex.printStackTrace();
							 fail();
						 }
					 }
					 
				 }
			 };
			 c1.start();
			 c2.start();
			 try {
				c1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 try {
				c2.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 cart1= market.getItems(itemsForCart1);
			 for(Item i : cart1) {
				 if(i.getStock()!= 1000) {
					 fail();
				 }
			 }
			 assertTrue(true);
			 			 
		 }

		 
		 
		 /**
		  * Test if there are deadlocks
		 * @throws SupermarketException 
		 * @throws InexistentItemException 
		 * @throws NegativeIdentifierException 
		  * 
		  * 
		  */		 
		 @Test
		 public void testDeadlocks() throws NegativeIdentifierException, InexistentItemException, SupermarketException{
			 int cart1Id=300;
			 Set<Integer> itemsForCart1=new HashSet<Integer>();
			 for(Integer i=500; i<550;i++) {
				 itemsForCart1.add(i);
			 }
			 List<Item> cart1= market.getItems(itemsForCart1);
			 //fill up the shopping carts with more items that there exists
			 List<ItemDelta> deltas=new ArrayList<ItemDelta>();
			 for(Integer i=549;i>=500;i--) {
				 deltas.add(new ItemDelta(i, 100));
			 }
			 int repeat=100;
			 
			 cart.checkout(cart1Id);
			 Thread c1 = new Thread("c1") {
				 public void run() {
					 for(int r=0;r<repeat;r++) {
						 for(int i : itemsForCart1) {
							 for(int n=0;n<100;n++){
								 try {
									cart.add(cart1Id, i);
								}catch (SupermarketException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							 }
						 }
						 try {
							cart.checkout(cart1Id);
						} catch (SupermarketException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							fail();
						}
					 }	 
				 }
			 };
			 
			 Thread c2 = new Thread("c2") {
				 public void run() {
					 for(int r=0; r<repeat;r++) {
						 try {
						 market.updateStocks(deltas);
						 }catch(SupermarketException ex) {
							 ex.printStackTrace();
							 fail();
						 }
					 }
					 
				 }
			 };
			 c1.start();
			 c2.start();
			 try {
				c1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 try {
				c2.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 cart1= market.getItems(itemsForCart1);
			 for(Item i : cart1) {
				 if(i.getStock()!= 1000) {
					 fail();
				 }
			 }
			 assertTrue(true);
			 			 
		 }
		 		 
}	
