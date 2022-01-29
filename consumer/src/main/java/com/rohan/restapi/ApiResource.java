package com.rohan.restapi;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("metrics")
public final class ApiResource {
	
	public ApiResource() {
		
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response all_metrices() throws IOException {
		return Response.ok().build();
	}
	
	@GET
	@Path("/local")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response local_metrices() throws IOException {
		return Response.ok().build();
	}
	
	@GET
	@Path("/machine/{machineId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getMetricsForMachine(@PathParam("machineId") String machineId) throws IOException {
		return Response.ok().build();
	}

}
