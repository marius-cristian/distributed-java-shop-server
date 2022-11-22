package com.acertainsupermarket.client.workloads;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.acertainsupermarket.business.CertainCart;
import com.acertainsupermarket.business.CertainSupermarket;
import com.acertainsupermarket.client.CartClientHTTPProxy;
import com.acertainsupermarket.client.SupermarketClientHTTPProxy;
import com.acertainsupermarket.interfaces.Cart;
import com.acertainsupermarket.interfaces.Supermarket;
import com.acertainsupermarket.server.SupermarketHTTPMessageHandler;
import com.acertainsupermarket.server.SupermarketHTTPServerUtility;
import com.acertainsupermarket.utils.SupermarketException;

public class CertainWorkload {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		int numConcurrentWorkloadThreads = 10;
		String serverAddress = "http://localhost:8081";
		boolean localTest = true;
		List<WorkerRunResult> workerRunResults = new ArrayList<WorkerRunResult>();
		List<Future<WorkerRunResult>> runResults = new ArrayList<Future<WorkerRunResult>>();

		// Initialize the RPC interfaces if its not a localTest, the variable is
		// overriden if the property is set

		localTest = false;

		Supermarket market = null;
		Cart cart = null;

		for (int j=1; j<100; j+=15) {
            numConcurrentWorkloadThreads = j;
            if (localTest) {
                market=new CertainSupermarket();
                cart=new CertainCart(market);
            } else {
                market = new SupermarketClientHTTPProxy(serverAddress);
                cart = new CartClientHTTPProxy(serverAddress);
            }
            ItemSetGenerator generator=new ItemSetGenerator();
            //WorkloadConfiguration wc=new WorkloadConfiguration(generator,market,cart);
            ExecutorService exec = Executors
                    .newFixedThreadPool(numConcurrentWorkloadThreads);
            for (int i = 0; i < numConcurrentWorkloadThreads; i++) {
                WorkloadConfiguration wc=new WorkloadConfiguration(generator,market,cart);
            	Worker workerTask = new Worker(wc);
                // Keep the futures to wait for the result from the thread
                runResults.add(exec.submit(workerTask));
            }

            // Get the results from the threads using the futures returned
            for (Future<WorkerRunResult> futureRunResult : runResults) {
                WorkerRunResult runResult = futureRunResult.get(); // blocking call
                workerRunResults.add(runResult);
            }

            exec.shutdownNow(); // shutdown the executor

            // Finished initialization, stop the clients if not localTest
            if (!localTest) {
                ((SupermarketClientHTTPProxy) market).stop();
                ((CartClientHTTPProxy) cart).stop();
            }
            //		System.out.println("calling reportMetric");
            reportMetric(workerRunResults);
        }
	}

	/**
	 * Computes the metrics and prints them
	 * 
	 * @param workerRunResults
	 */
	public static void reportMetric(List<WorkerRunResult> workerRunResults) {
	    double totalSuccessRate;
	    double customerRate;
	    double throughput = 0;
	    double latency;
        double time = 0;
        int sInteractions = 0;
        int sRunsBookStore = 0;
        int runsBookStore = 0;
        int runs = 0;


        for (WorkerRunResult workerRunResult: workerRunResults) {
            runs += workerRunResult.getTotalRuns();
            time += workerRunResult.getElapsedTimeInNanoSecs();
            sInteractions += workerRunResult.getSuccessfulInteractions();
            runsBookStore += workerRunResult.getTotalFrequentInteractionRuns();
            sRunsBookStore += workerRunResult.getSuccessfulFrequentSupermarketInteractionRuns();
            // throughput in seconds (requests/time)
            throughput += workerRunResult.getSuccessfulFrequentSupermarketInteractionRuns()/
                    (double)workerRunResult.getElapsedTimeInNanoSecs()*1e9;
        }

//      latency in ms (avg round-trip time)
        latency = time/(double)sRunsBookStore/1e6;

//      not clear which should we use
//      latency2 = 1e6 * runsBookStore/time;

        totalSuccessRate = (double)sInteractions/runs;
        customerRate = (double)runsBookStore/runs;

        System.out.println("Throughput: "+ throughput + ", Latency: " + latency + ", Success Rate: " + totalSuccessRate + ", CustomerRate: " + customerRate);

        FileWriter pw = null;
        try {
            pw = new FileWriter("dataRPC_binary.csv",true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter bufferedWriter = new BufferedWriter(pw);
        try {
            bufferedWriter.write(throughput + "," + latency + "," + totalSuccessRate + "," + customerRate + "\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
