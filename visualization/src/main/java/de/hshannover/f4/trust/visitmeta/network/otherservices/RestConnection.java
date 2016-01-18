package de.hshannover.f4.trust.visitmeta.network.otherservices;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public abstract class RestConnection {

	protected Client mClient;
	
	public RestConnection() {
		ClientConfig config = new DefaultClientConfig();
		mClient = Client.create(config);
	}

	public WebResource buildWebResource(String url) {
		URI uri_connect = UriBuilder.fromUri(url).build();
		return mClient.resource(uri_connect);
	}
}
