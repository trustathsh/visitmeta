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
 * This file is part of visitmeta dataservice, version 0.0.5,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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
package de.hshannover.f4.trust.visitmeta.dataservice.rest;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpException;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.ifmap.ConnectionManager;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionEstablishedException;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.NotConnectedException;
import de.hshannover.f4.trust.visitmeta.util.ConnectionKey;


@Path("/")
public class ConnectionResource {

	private static final Logger log = Logger.getLogger(ConnectionResource.class);


	@QueryParam("onlyActive")
	@DefaultValue("false")
	private boolean mOnlyActive = false;

	@QueryParam("allValues")
	@DefaultValue("false")
	private boolean mWithAllValues = false;

	/**
	 * Delete a saved connection.
	 * 
	 * Example-URL: <tt>http://example.com:8000/default</tt>
	 */
	@DELETE
	@Path("{connectionName}")
	public Response deleteConnection(@PathParam("connectionName") String name) {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Not implemented").build();
	}

	/**
	 * <br>
	 * <ul>
	 * 	<li><b><i>saveConnection</i></b><br>
	 * 	<br>
	 * 	Persist a new connection to a MAP-Server.<br>
	 * 	<br>
	 * 	Example-URL: <tt>http://example.com:8000/default</tt><br>
	 * 	<br>
	 * 	Example-JSONObject:<br>
	 * 	{<br>
	 * 	url:"https://localhost:8443",<br>
	 *  user.name:visitmeta,<br>
	 *  user.password:visitmeta,<br>
	 * 	}<br>
	 * 	<br>
	 * 	<b>required values:</b>
	 *  <ul>
	 *  	<li>url</li>
	 *  	<li>user.name</li>
	 *  	<li>user.password</li>
	 *  </ul>
	 * 	<br>
	 * 	<b>optional values:</b><br>
	 * 	<ul>
	 * 		<li>authentication.basic</li>
	 * 		<li>authentication.cert</li> <b>(!is not implemented yet!)</b>
	 * 		<li>truststore.path</li>
	 * 		<li>truststore.pw</li>
	 * 		<li>startup.connect</li>
	 * 		<li>maxPollResultSize</li>
	 * 	</ul>
	 * 	</li>
	 * </ul>
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveConnection(JSONObject jObj) {
		log.trace("new rest PUT save Connection...");
		String connectionName;
		String url;
		String userName;
		String userPassword;

		try {

			// get required values
			log.trace("get required values");

			connectionName = jObj.getString(ConnectionKey.NAME);
			url = jObj.getString(ConnectionKey.URL);
			userName = jObj.getString(ConnectionKey.USER_NAME);
			userPassword = jObj.getString(ConnectionKey.USER_PASSWORD);

		} catch (JSONException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}

		// get optional values
		log.trace(connectionName + ": get optional values");

		boolean authenticationBasic = jObj.optBoolean(ConnectionKey.AUTHENTICATION_BASIC);
		String truststorePath = jObj.optString(ConnectionKey.TRUSTSTORE_PATH);
		String truststorePass = jObj.optString(ConnectionKey.TRUSTSTORE_PASS);
		boolean startupConnect = jObj.optBoolean(ConnectionKey.STARTUP_CONNECT);
		int maxPollResultSize = jObj.optInt(ConnectionKey.MAX_POLL_RESULT_SIZE);

		// build new Connection
		log.trace(connectionName + ": build new Connection");

		Connection newConnection = null;
		try {
			newConnection = new Connection(connectionName, url, userName, userPassword);
		} catch (ConnectionException e) {
			String msg = "error while new Connection()";
			log.error(msg, e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}

		// set optional values
		log.trace(connectionName + ": set optional values");
		Iterator<String> i = jObj.keys();

		while (i.hasNext()) {
			String jKey = i.next();

			switch (jKey) {
			case ConnectionKey.AUTHENTICATION_BASIC: newConnection.setAuthenticationBasic(authenticationBasic);
			break;

			case ConnectionKey.TRUSTSTORE_PATH: newConnection.setTruststorePath(truststorePath);
			break;

			case ConnectionKey.TRUSTSTORE_PASS: newConnection.setTruststorePass(truststorePass);
			break;

			case ConnectionKey.STARTUP_CONNECT: newConnection.setStartupConnect(startupConnect);
			break;

			case ConnectionKey.MAX_POLL_RESULT_SIZE: newConnection.setMaxPollResultSize(maxPollResultSize);
			break;

			case ConnectionKey.AUTHENTICATION_CERT: return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(ConnectionKey.AUTHENTICATION_CERT + " is not implemented yet").build();
			}
		}

		// add to connection pool
		try {
			ConnectionManager.add(newConnection);
		} catch (ConnectionException e) {
			String msg = "error while adding connection to the connection pool";
			log.error(msg, e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg + " | Exception: " + e.toString()).build();
		}

		// persist connection in property
		try {
			Application.getConnectionPersister().persistConnections();
		} catch (FileNotFoundException e) {
			log.error("error while connection persist", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("error while connection persist -> " + e.toString()).build();
		}

		log.trace("... new rest PUT saveConnection " + connectionName + " success");
		return Response.ok().entity(connectionName + " was saved").build();
	}

	/**
	 * Conneced the dataservice with connection default to a MAP-Server.
	 * 
	 * Example-URL: <tt>http://example.com:8000/default/connect</tt>
	 * @throws HttpException
	 * 
	 **/
	@PUT
	@Path("{connectionName}/connect")
	public Response connect(@PathParam("connectionName") String name) {
		try {

			ConnectionManager.connectTo(name);

		} catch (ConnectionEstablishedException e){
			log.warn(e.toString());
			return Response.ok().entity("INFO: connection allready aktive").build();
		} catch (ConnectionException e) {
			log.error("error while connecting to " + name, e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}

		return Response.ok("INFO: connecting successfully").build();
	}

	/**
	 * Disconnected the default connection from a MAP-Server.
	 * 
	 * Example-URL: <tt>http://example.com:8000/default/disconnect</tt>
	 * 
	 **/
	@PUT
	@Path("{connectionName}/disconnect")
	public Response disconnect(@PathParam("connectionName") String name) {
		try {

			ConnectionManager.disconnectFrom(name);

		} catch (NotConnectedException e){
			log.error("error while disconnect from " + name, e);
			return Response.ok().entity("INFO: connection allready disconnected").build();
		} catch (ConnectionException e) {
			log.error("error while disconnect from " + name, e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}

		return Response.ok().entity("INFO: disconnection successfully").build();
	}

	/**
	 * Returns a JSONObject with saved connections to a MAP-Server.
	 * Example-URL: <tt>http://example.com:8000/</tt>
	 * 
	 * You can set the onlyActive value of true.
	 * Example-URL: <tt>http://example.com:8000/?onlyActive=true</tt>
	 **/
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Object getConnections() {
		if(mWithAllValues){
			JSONObject jsonO = new JSONObject();
			for(Connection c: ConnectionManager.getConnectionPool().values()){
				Map<String,String> connectionMap = new HashMap<String,String>();

				connectionMap.put(ConnectionKey.URL, c.getUrl());
				connectionMap.put(ConnectionKey.USER_NAME, c.getUserName());
				connectionMap.put(ConnectionKey.USER_PASSWORD, c.getUserPass());
				connectionMap.put(ConnectionKey.AUTHENTICATION_BASIC, String.valueOf(c.isAuthenticationBasic()));
				connectionMap.put(ConnectionKey.TRUSTSTORE_PATH, c.getTruststorePath());
				connectionMap.put(ConnectionKey.TRUSTSTORE_PASS, c.getTruststorePass());
				connectionMap.put(ConnectionKey.STARTUP_CONNECT, String.valueOf(c.isStartupConnect()));
				connectionMap.put(ConnectionKey.MAX_POLL_RESULT_SIZE, String.valueOf(c.getMaxPollResultSize()));

				JSONObject jsonConnection = new JSONObject(connectionMap);

				try {
					jsonO.put(c.getConnectionName(), jsonConnection);
				} catch (JSONException e) {
					log.error("error while put to JSONObject", e);
					return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
				}
			}
			return jsonO;


		}else if(mOnlyActive){
			JSONArray jsonA = new JSONArray();
			for(Connection c: ConnectionManager.getConnectionPool().values()){
				if(c.isConnected()){
					jsonA.put(c.getConnectionName());
				}
			}
			return jsonA;

		}else {
			JSONArray jsonA = new JSONArray();
			for(Connection c: ConnectionManager.getConnectionPool().values()){
				jsonA.put(c.getConnectionName());
			}
			return jsonA;
		}
	}
}
