package de.hshannover.f4.trust.visitmeta.dataservice.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * Class that defines a filter that allows cross-origin resource sharing. Based
 * on: http://stackoverflow.com/questions/19920119/problems
 * -trying-to-add-a-filter-to-a-grizzlyjersey-app
 *
 * @author Soeren Grzanna
 * @author Bastian Hellmann
 *
 */
public class CrossOriginResourceSharingFilter implements
		ContainerResponseFilter {
	@Override
	public ContainerResponse filter(ContainerRequest request,
			ContainerResponse response) {

		ResponseBuilder responseBuilder = Response.fromResponse(response
				.getResponse());
		responseBuilder.header("Access-Control-Allow-Origin", "*").header(
				"Access-Control-Allow-Methods",
				"GET, POST, PUT, DELETE, OPTIONS");

		String requestHead = request
				.getHeaderValue("Access-Control-Request-Headers");

		if (null != requestHead && !requestHead.equals(null)) {
			responseBuilder.header("Access-Control-Allow-Headers", requestHead);
		}

		response.setResponse(responseBuilder.build());
		return response;
	}
}
