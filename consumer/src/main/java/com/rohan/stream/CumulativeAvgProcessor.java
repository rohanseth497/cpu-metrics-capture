package com.rohan.stream;

import java.util.logging.Logger;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

public class CumulativeAvgProcessor implements Processor<String, Double> {

	private static final Logger LOGGER = Logger.getLogger(CumulativeAvgProcessor.class.getName());
	private static final String AVG_STORE_NAME = "in_memory_avg_store";
	
	ProcessorContext pc = null;
	private KeyValueStore<String, Double> machineToAvgCPUUsageStore;

	@Override
	public void init(ProcessorContext context) {
		pc = context;
		machineToAvgCPUUsageStore = (KeyValueStore<String, Double>) pc.getStateStore(AVG_STORE_NAME);
		LOGGER.info("Initializing the processor");
	}

	@Override
	public void process(String machineId, Double currentCPUUsage) {
		LOGGER.info("Calculating score: " + currentCPUUsage);
	}

	@Override
	public void punctuate(long timestamp) {

	}

	@Override
	public void close() {

	}
}
