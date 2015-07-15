package de.hshannover.f4.trust.visitmeta.gui.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.visitmeta.data.DataManager;
import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.exceptions.RESTException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class RestHelper {

	private static final Logger LOGGER = Logger.getLogger(RestHelper.class);

	public static List<Data> loadMapServerConnections(DataserviceConnection dataserviceConnection)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, JSONHandlerException,
			JSONException, ConnectionException {
		List<Data> dataList = loadMapServerConnectionsData(dataserviceConnection);

		List<Data> connectionsList = new ArrayList<Data>();

		for(Data data: dataList){
			if (data instanceof MapServerConnectionData) {
				MapServerConnectionData connectionData = (MapServerConnectionData) data;
				MapServerConnection connection = new MapServerRestConnectionImpl(dataserviceConnection, connectionData);
				connectionsList.add(connection);
			} else {
				LOGGER.fatal("Loaded connection data ist not a MapServerConnectionData");
			}
		}

		return connectionsList;
	}

	public static List<Data> loadMapServerConnectionsData(DataserviceConnection dataserviceConnection)
			throws JSONException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			JSONHandlerException, ConnectionException {
		List<Data> connections = new ArrayList<Data>();

		JSONArray jsonResponse = null;
		try {
			jsonResponse = buildWebResource(dataserviceConnection).path("/").accept(MediaType.APPLICATION_JSON)
				.get(JSONArray.class);
		} catch (ClientHandlerException e) {
			// if not available
			throw new ConnectionException(e.getMessage());
		}
		
		for (int i = 0; i < jsonResponse.length(); i++) {
			JSONObject jsonConnectionData = jsonResponse.getJSONObject(i);

			Data connectionData = DataManager.transformJSONObject(jsonConnectionData, MapServerConnectionData.class);

			connections.add(connectionData);
		}

		return connections;
	}

	public static void saveMapServerConnection(DataserviceConnection dataserviceConnection,
			MapServerConnectionData connectionData) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, JSONException {

		JSONObject jsonMapServerConnectionData = DataManager.transformData(connectionData);

		buildWebResource(dataserviceConnection).type(MediaType.APPLICATION_JSON).put(jsonMapServerConnectionData);
	}

	public static void deleteMapServerConnection(DataserviceConnection dataserviceConnection, String restConnectionName)
			throws RESTException {
		LOGGER.trace("send delete request...");
		ClientResponse response = buildWebResource(dataserviceConnection).path(restConnectionName).delete(
				ClientResponse.class);

		if (response.getClientResponseStatus() != ClientResponse.Status.OK) {
			throw new RESTException("Status Code(" + response.getClientResponseStatus() + ") Entity("
					+ response.getEntity(String.class) + ")");
		}

		LOGGER.info("connection '" + restConnectionName + "' was deleted : Status Code("
				+ response.getClientResponseStatus() + ")");
	}

	public static void saveSubscription(DataserviceConnection dataserviceConnection,
			String mapServerConnectionName, SubscriptionData subscriptionData) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, JSONException, ConnectionException {

		JSONObject jsonSubscriptionData = DataManager.transformData(subscriptionData);

		try {
			buildWebResource(dataserviceConnection).path(mapServerConnectionName).path("subscribe")
					.type(MediaType.APPLICATION_JSON).put(jsonSubscriptionData);
		} catch (UniformInterfaceException e) {
			throw new ConnectionException(e.getMessage());
		}
	}

	public static void startSubscription(DataserviceConnection dataserviceConnection, String restConnectionName,
			String subscriptionName) throws RESTException {
		LOGGER.trace("send subscribe update request...");
		ClientResponse response = buildWebResource(dataserviceConnection).path(restConnectionName).path("subscribe")
				.path("start").path(subscriptionName).put(ClientResponse.class);

		if (response.getClientResponseStatus() != ClientResponse.Status.OK) {
			throw new RESTException("Status Code(" + response.getClientResponseStatus() + ") Entity("
					+ response.getEntity(String.class) + ")");
		}

		LOGGER.info("subscribe update response: Status Code(" + response.getClientResponseStatus() + ")");
	}

	public static void deleteSubscription(DataserviceConnection dataserviceConnection, String restConnectionName,
			String subscriptionName) throws RESTException {
		LOGGER.trace("send delete request...");
		ClientResponse response = buildWebResource(dataserviceConnection).path(restConnectionName).path("subscribe")
				.path(subscriptionName).delete(ClientResponse.class);

		if (response.getClientResponseStatus() != ClientResponse.Status.OK) {
			throw new RESTException("Status Code(" + response.getClientResponseStatus() + ") Entity("
					+ response.getEntity(String.class) + ")");
		}

		LOGGER.info("subscription '" + subscriptionName + "' from connection '" + restConnectionName
				+ "' was deleted : Status Code(" + response.getClientResponseStatus() + ")");
	}

	public static void stopSubscription(DataserviceConnection dataserviceConnection, String restConnectionName,
			String subscriptionName) throws RESTException {
		LOGGER.trace("send subscribe delete request...");
		ClientResponse response = buildWebResource(dataserviceConnection).path(restConnectionName).path("subscribe")
				.path("stop").path(subscriptionName).put(ClientResponse.class);

		if (response.getClientResponseStatus() != ClientResponse.Status.OK) {
			throw new RESTException("Status Code(" + response.getClientResponseStatus() + ") Entity("
					+ response.getEntity(String.class) + ")");
		}

		LOGGER.info("subscribe delete response: Status Code(" + response.getClientResponseStatus() + ") Entity("
				+ response.getEntity(String.class) + ")");
	}

	public static void connectMapServer(DataserviceConnection dataserviceConnection, String restConnectionName)
			throws RESTException {
		LOGGER.trace("send connect request...");
		ClientResponse response = buildWebResource(dataserviceConnection).path(restConnectionName).path("connect")
				.put(ClientResponse.class);

		if (response.getClientResponseStatus() != ClientResponse.Status.OK) {
			throw new RESTException("Status Code(" + response.getClientResponseStatus() + ") Entity("
					+ response.getEntity(String.class) + ")");
		}

		LOGGER.info("connect response: Status Code(" + response.getClientResponseStatus() + ") Entity("
				+ response.getEntity(String.class) + ")");
	}

	public static void disconnectMapServer(DataserviceConnection dataserviceConnection, String restConnectionName)
			throws RESTException {
		LOGGER.trace("send disconnect request...");
		ClientResponse response = buildWebResource(dataserviceConnection).path(restConnectionName).path("disconnect")
				.put(ClientResponse.class);

		if (response.getClientResponseStatus() != ClientResponse.Status.OK) {
			throw new RESTException("Status Code(" + response.getClientResponseStatus() + ") Entity("
					+ response.getEntity(String.class) + ")");
		}

		LOGGER.info("disconnect response: Status Code(" + response.getClientResponseStatus() + ") Entity("
				+ response.getEntity(String.class) + ")");
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
