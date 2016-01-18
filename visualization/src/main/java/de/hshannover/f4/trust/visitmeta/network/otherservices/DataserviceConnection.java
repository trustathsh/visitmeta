package de.hshannover.f4.trust.visitmeta.network.otherservices;

import javax.ws.rs.core.MediaType;

public class DataserviceConnection extends RestConnection {
	
	public String getGraphAt(Long timestamp) {
		return buildWebResource("http://localhost:8000")
				.path("localMAPServer").path("graph").path("current")
				.queryParam("rawData", "true")
				.accept(MediaType.APPLICATION_JSON).get(String.class);
	}

}
