package com.rohan.topic;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;

import com.rohan.producer.Bootstrap;
import com.rohan.producer.Producer;

public class TopicCreation implements Runnable {
	
	private static Logger LOGGER = Logger.getLogger(TopicCreation.class.getName());
	
	private final Properties properties;
	private static final String BOOTSTRAP_SERVER_CONFIG_PARAM = "KAFKA_CLUSTER";
	
	public TopicCreation() {
		this.properties = new Properties();
		
		String defaultKafkaCluster = "localhost:9092";
		String kafkaCluster = System.getProperty(BOOTSTRAP_SERVER_CONFIG_PARAM, defaultKafkaCluster);

		this.properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCluster);
	}

	@Override
	public void run() {
		LOGGER.info("Started for checking and creating topic");
		
		checkAndCreateTopic(Bootstrap.TOPIC_NAME);
		
		LOGGER.info("End for checking and creating topic");
	}
	
	/*
	 * Function to check and create new topic
	 * */
	public void checkAndCreateTopic(String topicName) {
		AdminClient admin = AdminClient.create(properties);
		ListTopicsResult listTopics = admin.listTopics();
	    try {
			Set<String> topicNames = listTopics.names().get();
			boolean isTopicCreated = topicNames.contains(topicName);
			
			if (!isTopicCreated) {
				LOGGER.info("Going to create topic");
				createTopic(topicName);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Function to create topic in Kafka cluster with given topic name
	 * */
	public void createTopic(String topicName) throws Exception {
		int partitions = 1;
		short replicationFactor = 1;
		
		try (Admin admin = Admin.create(properties)) {
			NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
			
			CreateTopicsResult result = admin.createTopics(Collections.singleton(newTopic));
			
			KafkaFuture<Void> future = result.values().get(topicName);
			
			future.get();
		}
	}
}
