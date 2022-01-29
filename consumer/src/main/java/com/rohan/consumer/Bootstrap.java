package com.rohan.consumer;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.rohan.restapi.ApiResource;

public class Bootstrap {

	private static final Logger LOGGER = Logger.getLogger(Bootstrap.class.getName());

	private static void startServerAndStream() throws IOException {

		String port = Optional.ofNullable(System.getenv("PORT")).orElse("8080");

		URI baseUri = UriBuilder.fromUri("http://localhost").port(Integer.parseInt(port)).build();

		ResourceConfig config = new ResourceConfig(ApiResource.class).register(MoxyJsonFeature.class);

		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
		server.start();

		LOGGER.info("Application accessible at " + baseUri.toString());

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
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
