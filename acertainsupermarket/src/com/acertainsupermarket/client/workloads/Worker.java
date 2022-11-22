package com.acertainsupermarket.client.workloads;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

import com.acertainsupermarket.business.ItemDelta;
import com.acertainsupermarket.utils.InexistentItemException;
import com.acertainsupermarket.utils.NegativeIdentifierException;
import com.acertainsupermarket.utils.SupermarketException;

public class Worker implements Callable<WorkerRunResult> {

	private int numTotalFrequentInteraction=0;
	private int successfulFrequentInteractionRuns=0;

	private WorkloadConfiguration configuration = null;
	
	public Worker(WorkloadConfiguration c) {
		configuration=c;
	}
	@Override
	public WorkerRunResult call() throws Exception {
			int count = 1;
			long startTimeInNanoSecs = 0;
			long endTimeInNanoSecs = 0;
			int successfulInteractions = 0;
			long timeForRunsInNanoSecs = 0;

			Random rand = new Random();
			float chooseInteraction;

			// Perform the warm-up runs
			while (count++ <= 100) {
			    chooseInteraction = rand.nextFloat() * 100f;
			    runInteraction(chooseInteraction);
			}

			count = 1;
			numTotalFrequentInteraction = 0;
			successfulFrequentInteractionRuns = 0;

			// Perform the actual runs
			startTimeInNanoSecs = System.nanoTime();
			while (count++ <= 500) {
			    chooseInteraction = rand.nextFloat() * 100f;
			    if (runInteraction(chooseInteraction)) {
				successfulInteractions++;
			    }
			}
			endTimeInNanoSecs = System.nanoTime();
			timeForRunsInNanoSecs += (endTimeInNanoSecs - startTimeInNanoSecs);
			//System.out.println("successfulInteractions: " + successfulInteractions);
			//System.out.println("timeForRunsInNanoSecs: "+ timeForRunsInNanoSecs);
			//System.out.println("succesfullFrequentInteractionRuns: "+successfulFrequentInteractionRuns);
			//System.out.println("numTotalFrequentInteraction: "+numTotalFrequentInteraction);
			return new WorkerRunResult(successfulInteractions, timeForRunsInNanoSecs, 500, 
					successfulFrequentInteractionRuns, numTotalFrequentInteraction);
	}


    /**
     * Run the appropriate interaction while trying to maintain the configured
     * distributions
     * 
     * Updates the counts of total runs and successful runs for customer
     * interaction
     * 
     * @param chooseInteraction
     * @return
     */
    private boolean runInteraction(float chooseInteraction) {
	try {
	    float percentRareResuplyInteraction = configuration.percentRareResuplyInteraction;
	    float percentFrequentResuplyInteraction = configuration.percentFrequentResuplyInteraction;

	    if (chooseInteraction < percentRareResuplyInteraction) {
		runRareResuplyInteraction();
	    } else if (chooseInteraction < percentRareResuplyInteraction
		    + percentFrequentResuplyInteraction) {
		runFrequentResuplyInteraction();
	    } else {
	    numTotalFrequentInteraction++;
		runFrequentCartInteractions();
		successfulFrequentInteractionRuns++;
	    }
	} catch (SupermarketException ex) {
		//System.out.println(ex);
	    return false;
	}
	return true;
    }
	
	private void runFrequentCartInteractions() throws NegativeIdentifierException, InexistentItemException, SupermarketException {
		Set<ItemDelta> itemsToBuy=configuration.generator.itemQuantities(100,25);
		List<ItemDelta> buy=new ArrayList<ItemDelta>(itemsToBuy);
		int cartId=configuration.generator.cartId();
		for (ItemDelta i:itemsToBuy) {
			for(int j=0;j<i.getQuantityDifference();j++) {
				configuration.carts.add(cartId, i.getItemId());
			}
		}
		configuration.carts.checkout(cartId);
		
	}
	
	private void runFrequentResuplyInteraction() throws NegativeIdentifierException, InexistentItemException, SupermarketException {
		Set<ItemDelta> itemsToBuy=configuration.generator.restockQuantities(100, 25);
		List<ItemDelta> buy=new ArrayList<ItemDelta>(itemsToBuy);
		configuration.supermarket.updateStocks(buy);		
	}
	
	private void runRareResuplyInteraction() throws NegativeIdentifierException, InexistentItemException, SupermarketException {
		Set<ItemDelta> itemsToBuy=configuration.generator.restockQuantities(25, 100);
		List<ItemDelta> buy=new ArrayList<ItemDelta>(itemsToBuy);
		configuration.supermarket.updateStocks(buy);
	}
	
	

}
