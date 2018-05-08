/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of visitmeta-visualization, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.hshannover.f4.trust.visitmeta.gui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.data.DataManager;
import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.exceptions.RESTException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.MapServerData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

// secure
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;

import com.sun.jersey.client.urlconnection.HTTPSProperties;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.KeyManagerFactory;

public class RestHelper {

	private static final Logger LOGGER = Logger.getLogger(RestHelper.class);

	public static List<MapServerConnection> loadMapServerConnections(DataserviceConnection dataserviceConnection)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, JSONHandlerException,
			JSONException, ConnectionException {
		List<MapServerData> dataList = loadMapServerConnectionsData(dataserviceConnection);

		List<MapServerConnection> connectionsList = new ArrayList<MapServerConnection>();

		for (MapServerData data : dataList) {
			MapServerConnection connection = new MapServerRestConnectionImpl(dataserviceConnection, data);
				connectionsList.add(connection);
		}

		return connectionsList;
	}

	public static List<MapServerData> loadMapServerConnectionsData(DataserviceConnection dataserviceConnection)
			throws JSONException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			JSONHandlerException, ConnectionException {
		List<MapServerData> connections = new ArrayList<MapServerData>();

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

			MapServerData connectionData = (MapServerData) DataManager.transformJSONObject(jsonConnectionData,
					MapServerData.class);

			connections.add(connectionData);
		}

		return connections;
	}

	public static void saveMapServerConnection(DataserviceConnection dataserviceConnection,
			MapServerData connectionData) throws ClassNotFoundException, InstantiationException,
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
		Properties props = Main.getConfig();
		
		// secure REST-Interface, Alexander Kuzminykh
		
		if (dataserviceConnection.getUrl().substring(0, 5).equals("https")) {
			// enables "localhost" in url
			
			boolean verify = false;
			try {
				verify = props.getBoolean(VisualizationConfig.CONNECTION_DATASERVICESSL_VERIFY);
			} catch (PropertyException e) {
				LOGGER.error(e.getMessage(), e);
			}
			HostnameVerifier hostnameVerifier = null;
			if (verify) {
				hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
			} else {
				hostnameVerifier = new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession sslSession) {
						return true;
					}
				};
			}
			
			// disables ssl validation
			TrustManager[] trustAllCerts = getTrustAllKeystore();
			
			try {
				SSLContext ctx = SSLContext.getInstance("SSL");
								
				String defaultAlgo = KeyManagerFactory.getDefaultAlgorithm();
				KeyManagerFactory kmf = KeyManagerFactory.getInstance(defaultAlgo);
				KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
				ks.load(new FileInputStream(
						new File(props.getString(VisualizationConfig.KEY_CONNECTION_IFMAP_TRUSTSTORE_PATH)))
						, props.getString(VisualizationConfig.KEY_CONNECTION_IFMAP_TRUSTSTORE_PASSWORD).toCharArray());
				kmf.init(ks, props.getString(VisualizationConfig.KEY_CONNECTION_IFMAP_TRUSTSTORE_PASSWORD).toCharArray());
				
				TrustManagerFactory tmf = TrustManagerFactory.getInstance(defaultAlgo);
				tmf.init(ks);
				
				ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
				
				config.getProperties()
					.put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
						new HTTPSProperties(hostnameVerifier, ctx));
			} catch (NoSuchAlgorithmException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (KeyManagementException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (KeyStoreException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (CertificateException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (FileNotFoundException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (PropertyException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (UnrecoverableKeyException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		// end secure
		
		Client client = Client.create(config);
		
		URI uri_connect = UriBuilder.fromUri(dataserviceConnection.getUrl()).build();
		WebResource resource = client.resource(uri_connect);
		return resource;
	}
	
	// Für den Fall, dass Servercerts nicht überprüft werden sollen
	private static TrustManager[] getTrustAllKeystore() {
		return new TrustManager[] {new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
				// NOTHING
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
				// NOTHING
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		}};
	}
}
