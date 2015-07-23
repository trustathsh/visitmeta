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
 * This file is part of visitmeta-dataservice, version 0.4.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.connections.MapServerConnectionImpl;
import de.hshannover.f4.trust.visitmeta.data.DataManager;
import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionEstablishedException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.NoSavedConnectionException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.NotConnectedException;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.MapServerData;

/**
 * For each request a new object of this class will be created. The resource is
 * accessible under the root-path <tt>/</tt>. About this
 * interface can be update and delete multiple map-server-connections. Under the root path
 * return a list for all saved connections.
 *
 * @author Marcel Reichenbach
 *
 */

@Path("/")
public class ConnectionResource {

	private static final Logger LOGGER = Logger.getLogger(ConnectionResource.class);

	/**
	 * Delete a saved connection.
	 *
	 * Example-URL: <tt>http://example.com:8000/default</tt>
	 */
	@DELETE
	@Path("{connectionName}")
	public Response deleteConnection(@PathParam("connectionName") String connectionName) {
		try {
			Application.getConnectionManager().removeConnection(connectionName);
		} catch (ConnectionException e) {
			return responseError("delete connection('" + connectionName + "')", e.toString());
		}

		// persist
		try {
			Application.getConnections().persistConnections();
		} catch (PropertyException e) {
			return responseError("[persist] delete connection('" + connectionName + "')", e.toString());
		}

		return Response.ok().entity("INFO: delete connection(" + connectionName + ") successfully").build();
	}

	/**
	 * <br>
	 * <ul>
	 * <li><b><i>saveConnection</i></b><br>
	 * <br>
	 * Persist a new connection to a MAP-Server with or without subscriptions<br>
	 * <br>
	 * Example-URL: <tt>http://example.com:8000</tt><br>
	 * <br>
	 * Example-JSONObject:<br>
	 * {<br>
	 * <tab>"conExample": {<br>
	 * <br>
	 * "ifmapServerUrl": "https://localhost:8443", <br>
	 * "userName": "visitmeta", <br>
	 * "userPassword": "visitmeta", <br>
	 * "subscriptions": [<br>
	 * {<br>
	 * "subExample": { <br>
	 * <br>
	 * "startIdentifier": "freeradius-pdp", <br>
	 * "identifierType": "device" <br>
	 * ]}} <br>
	 * 
	 * <b>required values:</b>
	 * <ul>
	 * <li>ifmapServerUrl</li>
	 * <li>userName</li>
	 * <li>userPassword</li>
	 * </ul>
	 * <br>
	 * <b>optional values:</b><br>
	 * <ul>
	 * <li>authenticationBasic</li>
	 * <li>authenticationCert</li> <b>(!is not implemented yet!)</b>
	 * <li>truststorePath</li>
	 * <li>truststorePassword</li>
	 * <li>useAsStartup</li>
	 * <li>maxPollResultSize</li>
	 * </ul>
	 * </li>
	 * </ul>
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putConnection(JSONObject jsonConnectionData) {
		// transform
		MapServerData newConnectionData = null;
		try {
			newConnectionData = (MapServerData) DataManager.transformJSONObject(jsonConnectionData,
					MapServerData.class);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONHandlerException
				| JSONException e) {
			return responseError("JSONObject transform", e.toString());
		}

		// update || save connection
		try {
			Application.getConnectionManager().updateConnection(newConnectionData);
		} catch (NoSavedConnectionException e) {
			// OK, then try to save
			MapServerConnection newConnection = new MapServerConnectionImpl(newConnectionData);

			try {
				Application.getConnectionManager().addConnection(newConnection);
			} catch (ConnectionException ee) {
				return responseError("connection store", ee.toString());
			}
		}

		// persist
		try {
			Application.getConnections().persistConnections();
		} catch (PropertyException e) {
			return responseError("connection persist", e.toString());
		}

		return Response.ok().entity(newConnectionData + " was saved or updated").build();
	}

	/**
	 * Conneced the dataservice with connection default to a MAP-Server.
	 *
	 * Example-URL: <tt>http://example.com:8000/default/connect</tt>
	 *
	 *
	 **/
	@PUT
	@Path("{connectionName}/connect")
	public Response connect(@PathParam("connectionName") String connectionName) {
		try {
			Application.getConnectionManager().connect(connectionName);
		} catch (ConnectionEstablishedException e) {
			LOGGER.warn(e.toString());
			return Response.ok().entity("INFO: connection allready aktive | " + e.toString()).build();
		} catch (ConnectionException e) {
			return responseError("connecting to " + connectionName, e.toString());
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
	public Response disconnect(@PathParam("connectionName") String connectionName) {
		try {
			Application.getConnectionManager().disconnect(connectionName);
		} catch (NotConnectedException e) {
			LOGGER.warn("INFO: connection allready disconnected | " + e.toString());
			return Response.ok().entity("INFO: connection allready disconnected | " + e.toString()).build();
		} catch (ConnectionException e) {
			return responseError("disconnecting to " + connectionName, e.toString());
		}

		return Response.ok().entity("INFO: disconnection successfully").build();
	}

	/**
	 * Returns a JSONArray with saved connections to a MAP-Server.<br>
	 * <br>
	 * Example-URL: <tt>http://example.com:8000/</tt> <br>
	 * <br>
	 * 
	 **/
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Object getConnections() {
		JSONArray jsonConnections = new JSONArray();
		for (MapServerConnection c : Application.getConnectionManager().getSavedConnections().values()) {

			JSONObject jsonConnectionData = null;
			try {
				jsonConnectionData = DataManager.transformData(c);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONException e) {
				return responseError("Data transform", e.toString());
			}

			jsonConnections.put(jsonConnectionData);

		}
		return jsonConnections;
	}

	private Response responseError(String errorWhile, String exception) {
		String msg = "error while " + errorWhile + " | Exception: " + exception;
		LOGGER.error(msg);
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg).build();
	}
}
