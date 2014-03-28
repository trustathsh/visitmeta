package de.hshannover.f4.trust.visitmeta.network;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnection;

public class RestConnectionFactory {

	private static final Logger log = Logger.getLogger(RestConnectionFactory.class);

	public static List<RESTConnection> getAllConnectionsFrom(DataserviceConnection dataConnection){
		List<RESTConnection> connections = new ArrayList<RESTConnection>();

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_connect = UriBuilder.fromUri(dataConnection.getUrl()).build();
		WebResource resource = client.resource(uri_connect);
		JSONObject jsonResponse = resource.accept(MediaType.APPLICATION_JSON).get(JSONObject.class);

		Iterator<String> ii = jsonResponse.keys();

		while(ii.hasNext()){
			String connectionName = ii.next();
			JSONObject jsonConnection;

			try {

				jsonConnection = jsonResponse.getJSONObject(connectionName);
				RESTConnection restConn = new RESTConnection(dataConnection, connectionName);

				restConn.setUrl(jsonConnection.optString("URL"));
				restConn.setUsername(jsonConnection.optString("Username"));
				restConn.setPassword(jsonConnection.optString("Password"));
				restConn.setBasicAuthentication(jsonConnection.optBoolean("BasicAuthentication", true));
				restConn.setConnectAtStartUp(jsonConnection.optBoolean("ConnectAtStartUp", false));
				restConn.setDumping(jsonConnection.optBoolean("Dumping", false));
				restConn.setMaxPollResultSize(jsonConnection.optString("MaxPollResultSize"));

				connections.add(restConn);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return connections;
	}

	public static void startDump(RESTConnection restConnection){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_start_dump = UriBuilder.fromUri(restConnection.getDataserviceConnection().getUrl()).build();
		WebResource temp2 = client.resource(uri_start_dump);
		System.out.println("Dataservice: " + temp2.path(restConnection.getName()).path("dump/start").put(String.class));
	}

	public static void stopDump(RESTConnection restConnection){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_start_dump = UriBuilder.fromUri(restConnection.getDataserviceConnection().getUrl()).build();
		WebResource temp2 = client.resource(uri_start_dump);
		System.out.println("Dataservice: " + temp2.path(restConnection.getName()).path("dump/stop").put(String.class));
	}

	public static void connect(RESTConnection restConnection){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_start_dump = UriBuilder.fromUri(restConnection.getDataserviceConnection().getUrl()).build();
		WebResource temp2 = client.resource(uri_start_dump);
		System.out.println("Dataservice: " + temp2.path(restConnection.getName()).path("connect").put(String.class));
	}

	public static void disconnect(RESTConnection restConnection){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_start_dump = UriBuilder.fromUri(restConnection.getDataserviceConnection().getUrl()).build();
		WebResource temp2 = client.resource(uri_start_dump);
		System.out.println("Dataservice: " + temp2.path(restConnection.getName()).path("disconnect").put(String.class));
	}

	public static WebResource getGraphResource(RESTConnection restConnection){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri = UriBuilder.fromUri(restConnection.getDataserviceConnection().getUrl() + "/" + restConnection.getName() + "/graph").build();
		return client.resource(uri);
	}

	public static void saveInDataservice(RESTConnection restConnection){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_connect = UriBuilder.fromUri(restConnection.getDataserviceConnection().getUrl()).build();
		WebResource resource = client.resource(uri_connect);

		JSONObject jObj = new JSONObject();
		try {

			jObj.put("url", restConnection.getUrl());
			jObj.put("user", restConnection.getUsername());
			jObj.put("userPass", restConnection.getPassword());
			jObj.put("basicAuthentication", restConnection.isBasicAuthentication());
			jObj.put("connectAtStartUp", restConnection.isConnectAtStartUp());
			jObj.put("dumping", restConnection.isDumping());

		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		String response = resource.path(restConnection.getName()).type(MediaType.APPLICATION_JSON).put(String.class, jObj);
		System.out.println("response: " + response);
	}

}
