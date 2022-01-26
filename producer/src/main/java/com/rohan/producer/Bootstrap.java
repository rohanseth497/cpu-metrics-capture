package com.rohan.producer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/*
 * Bootstrap program to start the run the producer
 * */
public class Bootstrap {
	
	private static final Logger LOG = Logger.getLogger(Bootstrap.class.getName());
	
	public static void main(String[] args) {
		
		LOG.info("Starting the producer thread");
		
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		try {
			executorService = Executors.newFixedThreadPool(1);
			Future<?> future = executorService.submit(new Producer());
			
			future.get();
		} catch (Exception e) {
			LOG.info("Exception occured " + e.getMessage());
		} finally {
			com.rohan.util.Utils.stop(executorService);
		}
		
		LOG.info("Producer thread is over now");
	}
}
