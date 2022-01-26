package com.rohan.producer;

import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/*
 * Runnable to produce records for Kafka topic and push them regularly
 * */
public class Producer implements Runnable {

	private static Logger LOGGER = Logger.getLogger(Producer.class.getName());
	private static final String BOOTSTRAP_SERVER_CONFIG_PARAM = "KAFKA_CLUSTER";
	private static final String TOPIC_NAME = "cpu-metrics";

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

	@Override
	public void run() {
		try {
			produce();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void produce() {
		ProducerRecord<String, String> producerRecord = null;

		try {
			Random rnd = new Random();
			while (true) {
				for (int i = 1; i <= 10; i++) {
					String key = "machine-" + i;
					String value = String.valueOf(rnd.nextInt(20));

					producerRecord = new ProducerRecord<>(TOPIC_NAME, key, value);

					kafkaProducer.send(producerRecord, new Callback() {
						@Override
						public void onCompletion(RecordMetadata metadata, Exception exception) {
							if (exception == null) {
								LOGGER.log(Level.INFO, "Partition for key-value {0}::{1} is {2}",
										new Object[] { key, value, metadata.partition() });
							} else {
								LOGGER.log(Level.WARNING, "Error sending message with key {0}\n{1}",
										new Object[] { key, exception.getMessage() });
							}
						}
					});

					Thread.sleep(1000);
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Producer thread was interrupted");
		} finally {
			kafkaProducer.close();

			LOGGER.log(Level.INFO, "Producer closed");
		}
	}
}
