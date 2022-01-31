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

public class StreamPipeline {

	private static final Logger LOGGER = Logger.getLogger(StreamPipeline.class.getName());

	static Properties props;
	private static final String BOOTSTRAP_SERVER_CONFIG_PARAM = "KAFKA_CLUSTER";
	private static final String SOURCE_NAME = "cpu-metrics-topic-source";
	private static final String AVG_STORE_NAME = "in_memory_avg_store";
	private static final String PROCESSOR_NAME = "in_memory_avg_processor";
	private static final String APPLICATION_ID = "my-streams-application";
	private static final String TOPIC_NAME = "cpu-metrics";

	static {
		props = new Properties();

		props.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);

		String defaultKafkaCluster = "localhost:9092";
		String kafkaCluster = System.getProperty(BOOTSTRAP_SERVER_CONFIG_PARAM, defaultKafkaCluster);
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCluster);

		props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		props.put(StreamsConfig.STATE_DIR_CONFIG, AVG_STORE_NAME);
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

		StateStoreSupplier machineToAvgCPUUsageStore = Stores.create(AVG_STORE_NAME).withStringKeys().withDoubleValues()
				.inMemory().build();

		TopologyBuilder builder = new TopologyBuilder();

		builder.addSource(SOURCE_NAME, TOPIC_NAME)
				.addProcessor(PROCESSOR_NAME, new ProcessorSupplier<String, String>() {
					@Override
					public Processor<String, String> get() {
						return new CumulativeAvgProcessor();
					}
				}, SOURCE_NAME).addStateStore(machineToAvgCPUUsageStore, PROCESSOR_NAME);

		return builder;
	}
}
