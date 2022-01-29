package com.rohan.consumer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.streams.KafkaStreams;
import org.glassfish.grizzly.http.server.HttpServer;

import com.rohan.restapi.ApiResource;
import com.rohan.restapi.ServerStartup;

public class Bootstrap {

	private static final Logger LOGGER = Logger.getLogger(Bootstrap.class.getName());

	private static void startServerAndStream() throws IOException {
		
		// Start running the Kafka Stream
		KafkaStreams stream = null;
		
		// Starting the jersey server
		HttpServer server = ServerStartup.startServer();
		
		// Close all open streams with shutdown hook for JVM
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					stream.close();
					LOGGER.log(Level.INFO, "Kafka stream closed");
					
					server.shutdownNow();
					LOGGER.log(Level.INFO, "Jersey REST services stopped");
				} catch (Exception ex) {
					LOGGER.log(Level.SEVERE, ex, ex::getMessage);
				}
			}
		}));
	}

	public static void main(String[] args) {
		try {
			startServerAndStream();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}
}
