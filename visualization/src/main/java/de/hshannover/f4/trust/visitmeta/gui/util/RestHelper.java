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
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.visitmeta.data.DataManager;
import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class RestHelper {

	private static final Logger LOGGER = Logger.getLogger(RestHelper.class);

	public static List<Data> loadMapServerConnections(DataserviceConnection dataserviceConnection)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, JSONHandlerException,
			JSONException {
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
			JSONHandlerException {
		List<Data> connections = new ArrayList<Data>();

		JSONArray jsonResponse = buildWebResource(dataserviceConnection).path("/").accept(MediaType.APPLICATION_JSON)
				.get(JSONArray.class);

		for (int i = 0; i < jsonResponse.length(); i++) {
			JSONObject jsonConnectionData = jsonResponse.getJSONObject(i);

			Data connectionData = DataManager.transformJSONObject(jsonConnectionData, MapServerConnectionData.class);

			connections.add(connectionData);
		}
		
		return connections;
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
