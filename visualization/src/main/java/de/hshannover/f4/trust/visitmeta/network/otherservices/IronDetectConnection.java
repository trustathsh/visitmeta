package de.hshannover.f4.trust.visitmeta.network.otherservices;

import javax.ws.rs.core.MediaType;

public class IronDetectConnection extends RestConnection {

	public String send(String json) {
		return buildWebResource("http://localhost:8001").path("livechecker")
.accept(MediaType.APPLICATION_JSON)
				.entity(json, MediaType.APPLICATION_JSON_TYPE)
				.put(String.class);
	}
	
}
