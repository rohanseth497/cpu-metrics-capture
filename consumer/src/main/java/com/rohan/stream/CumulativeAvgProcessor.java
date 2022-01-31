package com.rohan.stream;

import java.util.logging.Logger;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import com.rohan.Constants;

public class CumulativeAvgProcessor implements Processor<String, String> {

	private static final Logger LOGGER = Logger.getLogger(CumulativeAvgProcessor.class.getName());

	ProcessorContext pc = null;
	private KeyValueStore<String, Double> machineToAvgCPUUsageStore;
	private KeyValueStore<String, Integer> machineToNumberOfRecordsReadStore;

	@Override
	public void init(ProcessorContext context) {
		this.pc = context;
		this.pc.schedule(12000); // punctuate after this interval

		this.machineToAvgCPUUsageStore = (KeyValueStore<String, Double>) pc.getStateStore(Constants.AVG_STORE_NAME);
		this.machineToNumberOfRecordsReadStore = (KeyValueStore<String, Integer>) pc
				.getStateStore(Constants.NUM_RECORDS_STORE_NAME);

		LOGGER.info("Initializing the processor");
	}

	@Override
	public void process(String machineId, String currentCPUUsage) {
		LOGGER.info("Current machineId: " + machineId + " and usage is: " + currentCPUUsage);

		Double currentCPUUsageOfMachine = Double.parseDouble(currentCPUUsage);
		Integer recordsReadForMachine = machineToNumberOfRecordsReadStore.get(machineId);
		Double latestCumulativeAvg = null;

		if (recordsReadForMachine == null) {
			machineToNumberOfRecordsReadStore.put(machineId, 1);
			latestCumulativeAvg = currentCPUUsageOfMachine;
		} else {

			latestCumulativeAvg = (currentCPUUsageOfMachine + (recordsReadForMachine * currentCPUUsageOfMachine))
					/ (recordsReadForMachine + 1);

			LOGGER.info("Average for machine: " + machineId + " is: " + latestCumulativeAvg);

			machineToNumberOfRecordsReadStore.put(machineId, recordsReadForMachine + 1);
		}

		machineToAvgCPUUsageStore.put(machineId, latestCumulativeAvg);
	}

	@Override
	public void punctuate(long timestamp) {
		pc.commit();
	}

	@Override
	public void close() {
		machineToAvgCPUUsageStore.close();
		machineToNumberOfRecordsReadStore.close();
	}
}
