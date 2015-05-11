package de.hshannover.f4.trust.visitmeta.gui.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.util.ConnectionKey;

public class RestHelper {

	private static final Logger LOGGER = Logger.getLogger(RestHelper.class);

	public static List<String> loadMapServerConnectionNames(DataserviceConnection dataserviceConnection)
			throws JSONException,
			ClientHandlerException,
			UniformInterfaceException {
		List<String> connections = new ArrayList<String>();

		JSONArray jsonResponse = null;
		jsonResponse = buildWebResource(dataserviceConnection).path("/").accept(MediaType.APPLICATION_JSON)
				.get(JSONArray.class);
		for (int i = 0; i < jsonResponse.length(); i++) {
			connections.add(jsonResponse.getString(i));
		}
		return connections;
	}

	public static List<String> loadActiveMapServerConnectionNames(DataserviceConnection dataserviceConnection)
			throws JSONException, ClientHandlerException,
			UniformInterfaceException {
		List<String> connections = new ArrayList<String>();

		JSONArray jsonResponse = null;
		jsonResponse = buildWebResource(dataserviceConnection).path("/").queryParam("onlyActive", String.valueOf(true))
				.accept(MediaType.APPLICATION_JSON).get(JSONArray.class);

		for (int i = 0; i < jsonResponse.length(); i++) {
			connections.add(jsonResponse.getString(i));
		}
		return connections;
	}

	public static List<Data> loadMapServerConnections(DataserviceConnection dataserviceConnection) {
		List<Data> connections = new ArrayList<Data>();

		JSONObject jsonResponse = null;

		jsonResponse = buildWebResource(dataserviceConnection).path("/").queryParam("allValues", String.valueOf(true))
				.accept(MediaType.APPLICATION_JSON).get(JSONObject.class);

		@SuppressWarnings("unchecked")
		Iterator<String> i = jsonResponse.keys();
		while (i.hasNext()) {
			String connectionName = i.next();
			JSONObject jsonConnection;

			try {

				jsonConnection = jsonResponse.getJSONObject(connectionName);
				MapServerRestConnectionImpl restConnection = new MapServerRestConnectionImpl(dataserviceConnection,
						connectionName);

				restConnection.setUrl(jsonConnection.getString(ConnectionKey.IFMAP_SERVER_URL));
				restConnection.setUserName(jsonConnection.getString(ConnectionKey.USER_NAME));
				restConnection.setUserPassword(jsonConnection.getString(ConnectionKey.USER_PASSWORD));
				restConnection.setAuthenticationBasic(jsonConnection.getBoolean(ConnectionKey.AUTHENTICATION_BASIC));
				restConnection.setTruststorePath(jsonConnection.getString(ConnectionKey.TRUSTSTORE_PATH));
				restConnection.setTruststorePassword(jsonConnection.getString(ConnectionKey.TRUSTSTORE_PASSWORD));
				restConnection.setStartupConnect(jsonConnection.getBoolean(ConnectionKey.USE_CONNECTION_AS_STARTUP));
				restConnection.setMaxPollResultSize(Integer.valueOf(jsonConnection
						.getString(ConnectionKey.MAX_POLL_RESULT_SIZE)));
				restConnection.setConnected(jsonConnection.getBoolean(ConnectionKey.IS_CONNECTED));
				restConnection.setSubscriptionData(loadRestSubscriptions(dataserviceConnection, connectionName));

				connections.add(restConnection);

			} catch (JSONException e) {
				LOGGER.error("error while loadRestConnections()", e);
			}
		}
		return connections;
	}

	public static List<Data> loadRestSubscriptions(DataserviceConnection dataserviceConnection, String connectionName)
			throws ClientHandlerException, UniformInterfaceException {
		List<Data> subscriptions = new ArrayList<Data>();

		JSONArray jsonResponse = null;

		jsonResponse = buildWebResource(dataserviceConnection).path(connectionName).path("subscribe")
				.accept(MediaType.APPLICATION_JSON).get(JSONArray.class);

		for (int i = 0; i < jsonResponse.length(); i++) {

			try {

				String subscriptionName = jsonResponse.getString(i);
				Subscription subscription = new RestSubscrptionImpl(dataserviceConnection);
				subscription.setName(subscriptionName);

				subscriptions.add(subscription);
			} catch (JSONException e) {
				LOGGER.error("error while loadRestSubscriptions()", e);
			}
		}
		return subscriptions;
	}

	public static void connectMapServer(DataserviceConnection dataserviceConnection, String restConnectionName) {
		LOGGER.trace("send connect request...");
		String response = buildWebResource(dataserviceConnection).path(restConnectionName).path("connect")
				.put(String.class);
		LOGGER.info("connect response: " + response);
	}

	public static void disconnectMapServer(DataserviceConnection dataserviceConnection, String restConnectionName) {
		LOGGER.trace("send disconnect request...");
		String response = buildWebResource(dataserviceConnection).path(restConnectionName).path("disconnect")
				.put(String.class);
		LOGGER.info("disconnect response: " + response);
	}

	public static WebResource getGraphResource(DataserviceConnection dataserviceConnection, String restConnectionName) {
		return buildWebResource(dataserviceConnection).path(restConnectionName).path("graph");
	}

	private static WebResource buildWebResource(DataserviceConnection dataserviceConnection) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_connect = UriBuilder.fromUri(dataserviceConnection.getUrl()).build();
		WebResource resource = client.resource(uri_connect);
		return resource;
	}

}
