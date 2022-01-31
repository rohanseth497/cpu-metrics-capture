package com.rohan.stream;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.processor.StateStoreSupplier;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.Stores;

import com.rohan.Constants;

public class StreamPipeline {

	private static final Logger LOGGER = Logger.getLogger(StreamPipeline.class.getName());

	static Properties props;

	static {
		props = new Properties();

		props.put(StreamsConfig.APPLICATION_ID_CONFIG, Constants.APPLICATION_ID);

		String defaultKafkaCluster = "localhost:9092";
		String kafkaCluster = System.getProperty(Constants.BOOTSTRAP_SERVER_CONFIG_PARAM, defaultKafkaCluster);
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCluster);

		props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		props.put(StreamsConfig.STATE_DIR_CONFIG, Constants.AVG_STORE_NAME);
	}

	public static KafkaStreams start() {
		StreamsConfig config = new StreamsConfig(props);

		TopologyBuilder builder = getStoreSuppliedTopology();

		KafkaStreams stream = null;

		try {
			stream = new KafkaStreams(builder, config);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Kafka Stream not able to start");
		}

		stream.start();

		return stream;
	}

	static TopologyBuilder getStoreSuppliedTopology() {

		StateStoreSupplier machineToAvgCPUUsageStore = Stores.create(Constants.AVG_STORE_NAME).withStringKeys()
				.withDoubleValues().inMemory().build();

		StateStoreSupplier machineToNumberOfRecordsReadStore = Stores.create(Constants.NUM_RECORDS_STORE_NAME)
				.withStringKeys().withIntegerValues().inMemory().build();

		TopologyBuilder builder = new TopologyBuilder();

		builder.addSource(Constants.SOURCE_NAME, Constants.TOPIC_NAME)
				.addProcessor(Constants.PROCESSOR_NAME, new ProcessorSupplier<String, String>() {
					@Override
					public Processor<String, String> get() {
						return new CumulativeAvgProcessor();
					}
				}, Constants.SOURCE_NAME).addStateStore(machineToAvgCPUUsageStore, Constants.PROCESSOR_NAME)
				.addStateStore(machineToNumberOfRecordsReadStore, Constants.PROCESSOR_NAME);

		return builder;
	}
}
