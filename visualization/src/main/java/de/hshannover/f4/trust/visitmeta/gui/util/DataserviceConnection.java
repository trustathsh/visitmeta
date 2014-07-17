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
 * This file is part of visitmeta visualization, version 0.1.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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

import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.util.ConnectionKey;


public class DataserviceConnection {

	private static final Logger log = Logger.getLogger(Main.class);

	private String mName;
	private String mUrl;

	private boolean mRawXml;

	public DataserviceConnection(){}

	public DataserviceConnection(String name, String url, boolean rawXml){
		setName(name);
		setUrl(url);
		setRawXml(rawXml);
	}

	public List<String> loadRestConnectionNames() throws JSONException, ClientHandlerException, UniformInterfaceException{
		List<String> connections = new ArrayList<String>();

		JSONArray jsonResponse = null;
		jsonResponse = buildWebResource().path("/").accept(MediaType.APPLICATION_JSON).get(JSONArray.class);
		for(int i=0; i < jsonResponse.length(); i++){
			connections.add(jsonResponse.getString(i));
		}
		return connections;
	}

	public List<String> loadActiveRestConnectionNames() throws JSONException, ClientHandlerException, UniformInterfaceException{
		List<String> connections = new ArrayList<String>();

		JSONArray jsonResponse = null;
		jsonResponse = buildWebResource().path("/").queryParam("onlyActive", String.valueOf(true))
				.accept(MediaType.APPLICATION_JSON).get(JSONArray.class);

		for(int i=0; i < jsonResponse.length(); i++){
			connections.add(jsonResponse.getString(i));
		}
		return connections;
	}

	public List<RestConnection> loadRestConnections() throws ClientHandlerException, UniformInterfaceException{
		List<RestConnection> connections = new ArrayList<RestConnection>();

		JSONObject jsonResponse = null;

		jsonResponse = buildWebResource().path("/").queryParam("allValues", String.valueOf(true))
				.accept(MediaType.APPLICATION_JSON).get(JSONObject.class);

		Iterator<String> i = jsonResponse.keys();
		while(i.hasNext()){
			String connectionName = i.next();
			JSONObject jsonConnection;

			try {

				jsonConnection = jsonResponse.getJSONObject(connectionName);
				RestConnection restConn = new RestConnection(this, connectionName);

				restConn.setUrl(jsonConnection.getString(ConnectionKey.URL));
				restConn.setUsername(jsonConnection.getString(ConnectionKey.USER_NAME));
				restConn.setPassword(jsonConnection.getString(ConnectionKey.USER_PASSWORD));
				restConn.setAuthenticationBasic(jsonConnection.getBoolean(ConnectionKey.AUTHENTICATION_BASIC));
				restConn.setTruststorePath(jsonConnection.getString(ConnectionKey.TRUSTSTORE_PATH));
				restConn.setTruststorePass(jsonConnection.getString(ConnectionKey.TRUSTSTORE_PASS));
				restConn.setStartupConnect(jsonConnection.getBoolean(ConnectionKey.STARTUP_CONNECT));
				restConn.setMaxPollResultSize(jsonConnection.getString(ConnectionKey.MAX_POLL_RESULT_SIZE));

				connections.add(restConn);

			} catch (JSONException e) {
				log.error("error while loadRestConnections()", e);
			}
		}
		return connections;
	}

	private WebResource buildWebResource() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_connect = UriBuilder.fromUri(getUrl()).build();
		WebResource resource = client.resource(uri_connect);
		return resource;
	}

	public void connect(String restConnectionName){
		log.trace("send connect request...");
		String response = buildWebResource().path(restConnectionName).path("connect").put(String.class);
		log.info("connect response: " + response);
	}

	public void disconnect(String restConnectionName){
		log.trace("send disconnect request...");
		String response = buildWebResource().path(restConnectionName).path("disconnect").put(String.class);
		log.info("disconnect response: " + response);
	}

	public WebResource getGraphResource(String restConnectionName){
		return buildWebResource().path(restConnectionName).path("graph");
	}

	@Override
	public DataserviceConnection clone() {
		DataserviceConnection tmp = copy();
		String tmpName = tmp.getName();
		tmp.setName(tmpName + "(clone)");
		return tmp;
	}

	public DataserviceConnection copy() {
		return new DataserviceConnection(mName, mUrl, mRawXml);
	}

	public void update(DataserviceConnection dataservice) {
		mName = dataservice.getName();
		mUrl = dataservice.getUrl();
		mRawXml = dataservice.isRawXml();
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public boolean isRawXml() {
		return mRawXml;
	}

	public void setRawXml(boolean rawXml) {
		mRawXml = rawXml;
	}

	@Override
	public String toString(){
		return mName;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this){
			return true;
		}else if(o instanceof DataserviceConnection){
			if(((DataserviceConnection)o).getName().equals(getName())){
				return true;
			}
		}
		return false;
	}
}
