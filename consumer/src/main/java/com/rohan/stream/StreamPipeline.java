package com.rohan.stream;

import java.util.Properties;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.TopologyBuilder;

public class StreamPipeline {
	
	static Properties props;
	
	static {
		props = new Properties();
		
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, props);
		
		props.put(StreamsConfig.APPLICATION_SERVER_CONFIG, props);
		
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, props);
		
		props.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, props);
		
		props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, props);
		
		props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, props);
		
		props.put(StreamsConfig.STATE_DIR_CONFIG, props);
	}
	
	public static KafkaStreams start() {
		StreamsConfig config = new StreamsConfig(props);
		
		TopologyBuilder builder = null;
		
		KafkaStreams stream = null;
		
		try {
			stream = new KafkaStreams(builder, config);
		} catch (Exception e) {
			
		}
		
		stream.start();
		
		return stream;
	}
}
