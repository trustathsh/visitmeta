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
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnection;
import de.hshannover.f4.trust.visitmeta.util.ConnectionKey;

public class RestConnectionFactory {

	private static final Logger log = Logger.getLogger(RestConnectionFactory.class);

	public static List<RESTConnection> getAllConnectionsFrom(DataserviceConnection dataConnection){
		List<RESTConnection> connections = new ArrayList<RESTConnection>();

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_connect = UriBuilder.fromUri(dataConnection.getUrl()).build();
		WebResource resource = client.resource(uri_connect);
		JSONObject jsonResponse = null;

		try{
			jsonResponse = resource.path("/").queryParam("connectionData", String.valueOf(true)).accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
		} catch (ClientHandlerException e){
			log.error("getAllConnectionsFrom error", e);
			return connections;
		} catch (UniformInterfaceException e){
			log.error("getAllConnectionsFrom error", e);
			return connections;
		}
		Iterator<String> ii = jsonResponse.keys();

		while(ii.hasNext()){
			String connectionName = ii.next();
			JSONObject jsonConnection;

			try {

				jsonConnection = jsonResponse.getJSONObject(connectionName);
				RESTConnection restConn = new RESTConnection(dataConnection, connectionName);

				restConn.setUrl(jsonConnection.getString(ConnectionKey.URL));
				restConn.setUsername(jsonConnection.getString(ConnectionKey.USER_NAME));
				restConn.setPassword(jsonConnection.getString(ConnectionKey.USER_PASSWORD));
				restConn.setAuthenticationBasic(jsonConnection.getBoolean(ConnectionKey.AUTHENTICATION_BASIC));
				restConn.setTruststorePath(jsonConnection.getString(ConnectionKey.TRUSTSTORE_PATH));
				restConn.setTruststorePass(jsonConnection.getString(ConnectionKey.TRUSTSTORE_PASS));
				restConn.setStartupConnect(jsonConnection.getBoolean(ConnectionKey.STARTUP_CONNECT));
				restConn.setStartupDump(jsonConnection.getBoolean(ConnectionKey.STARTUP_DUMP));
				restConn.setMaxPollResultSize(jsonConnection.getString(ConnectionKey.MAX_POLL_RESULT_SIZE));

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
		System.out.println("Dataservice: " + temp2.path(restConnection.getConnectionName()).path("dump/start").put(String.class));
	}

	public static void stopDump(RESTConnection restConnection){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_start_dump = UriBuilder.fromUri(restConnection.getDataserviceConnection().getUrl()).build();
		WebResource temp2 = client.resource(uri_start_dump);
		System.out.println("Dataservice: " + temp2.path(restConnection.getConnectionName()).path("dump/stop").put(String.class));
	}

	public static void connect(RESTConnection restConnection){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_start_dump = UriBuilder.fromUri(restConnection.getDataserviceConnection().getUrl()).build();
		WebResource temp2 = client.resource(uri_start_dump);
		System.out.println("Dataservice: " + temp2.path(restConnection.getConnectionName()).path("connect").put(String.class));
	}

	public static void disconnect(RESTConnection restConnection){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_start_dump = UriBuilder.fromUri(restConnection.getDataserviceConnection().getUrl()).build();
		WebResource temp2 = client.resource(uri_start_dump);
		System.out.println("Dataservice: " + temp2.path(restConnection.getConnectionName()).path("disconnect").put(String.class));
	}

	public static WebResource getGraphResource(RESTConnection restConnection){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri = UriBuilder.fromUri(restConnection.getDataserviceConnection().getUrl() + "/" + restConnection.getConnectionName() + "/graph").build();
		return client.resource(uri);
	}

	public static void saveInDataservice(RESTConnection restConnection){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_connect = UriBuilder.fromUri(restConnection.getDataserviceConnection().getUrl()).build();
		WebResource resource = client.resource(uri_connect);

		JSONObject jObj = new JSONObject();
		try {

			jObj.put(ConnectionKey.NAME, restConnection.getConnectionName());
			jObj.put(ConnectionKey.URL, restConnection.getUrl());
			jObj.put(ConnectionKey.USER_NAME, restConnection.getUsername());
			jObj.put(ConnectionKey.USER_PASSWORD, restConnection.getPassword());
			jObj.put(ConnectionKey.AUTHENTICATION_BASIC, restConnection.isAuthenticationBasic());
			jObj.put(ConnectionKey.TRUSTSTORE_PATH, restConnection.getTruststorePath());
			jObj.put(ConnectionKey.TRUSTSTORE_PASS, restConnection.getTruststorePass());
			jObj.put(ConnectionKey.STARTUP_CONNECT, restConnection.isStartupConnect());
			jObj.put(ConnectionKey.STARTUP_DUMP, restConnection.isStartupDump());
			jObj.put(ConnectionKey.MAX_POLL_RESULT_SIZE, restConnection.getMaxPollResultSize());

		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try{
			resource.type(MediaType.APPLICATION_JSON).put(jObj);
		}catch (UniformInterfaceException e){
			log.error("error while saveInDataservice()", e);
		}
	}

}
