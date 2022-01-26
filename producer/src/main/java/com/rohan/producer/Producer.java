package com.rohan.producer;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

/*
 * Runnable to produce records for Kafka topic and push them regularly
 * */
public class Producer implements Runnable {

	private static Logger LOGGER = Logger.getLogger(Producer.class.getName());
	private static final String BOOTSTRAP_SERVER_CONFIG_PARAM = "KAFKA_CLUSTER";

	private KafkaProducer<String, String> kafkaProducer = null;

	Producer() {
		LOGGER.log(Level.INFO, "Producer producing running in thread {0}", Thread.currentThread().getName());

		Properties kafkaProperties = new Properties();

		String defaultKafkaCluster = "localhost:9092";
		String kafkaCluster = System.getProperty(BOOTSTRAP_SERVER_CONFIG_PARAM, defaultKafkaCluster);

		kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCluster);
		kafkaProperties.put(ProducerConfig.ACKS_CONFIG, "0");
		kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");

		this.kafkaProducer = new KafkaProducer<>(kafkaProperties);
	}

	public void run() {

	}
}
