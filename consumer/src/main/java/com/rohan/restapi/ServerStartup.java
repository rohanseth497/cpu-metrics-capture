package com.rohan.restapi;

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

public class ServerStartup {

	private static final Logger LOGGER = Logger.getLogger(ServerStartup.class.getName());

	public static HttpServer startServer() throws IOException {

		String port = Optional.ofNullable(System.getenv("PORT")).orElse("8080");

		URI baseUri = UriBuilder.fromUri("http://localhost").port(Integer.parseInt(port)).build();

		ResourceConfig config = new ResourceConfig(ApiResource.class).register(MoxyJsonFeature.class);

		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
		server.start();

		LOGGER.info("Application accessible at " + baseUri.toString());

		return server;
	}
}
