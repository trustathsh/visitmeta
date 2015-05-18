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

import org.apache.http.HttpException;
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
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.NotConnectedException;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;

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
	public Response deleteConnection(@PathParam("connectionName") String name) {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity("Not implemented").build();
	}

	/**
	 * <br>
	 * <ul>
	 * <li><b><i>saveConnection</i></b><br>
	 * <br>
	 * Persist a new connection to a MAP-Server.<br>
	 * <br>
	 * Example-URL: <tt>http://example.com:8000/default</tt><br>
	 * <br>
	 * Example-JSONObject:<br>
	 * {<br>
	 * <tab> [connectionName] : {<br>
	 * <tab>ifmapServerUrl:"https://localhost:8443",<br>
	 * <tab>userName:visitmeta,<br>
	 * <tab>userPassword:visitmeta,<br>
	 * <tab>}<br>
	 * }<br>
	 * <br>
	 * <b>required values:</b>
	 * <ul>
	 * <li>ifmapServerUrl</li>
	 * <li>userName</li>
	 * <li>userPassword</li>
	 * </ul>
	 * <br>
	 * <b>optional values:</b><br>
	 * <ul>
	 * <li>authentication.basic</li>
	 * <li>authentication.cert</li> <b>(!is not implemented yet!)</b>
	 * <li>truststore.path</li>
	 * <li>truststore.pw</li>
	 * <li>startup.connect</li>
	 * <li>maxPollResultSize</li>
	 * </ul>
	 * </li>
	 * </ul>
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveConnection(JSONObject jsonConnectionData) {
		LOGGER.trace("new rest PUT save Connection...");

		MapServerConnectionData newConnectionData = null;
		try {
			newConnectionData = (MapServerConnectionData) DataManager.transformJSONObject(jsonConnectionData,
					MapServerConnectionData.class);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONHandlerException
				| JSONException e) {
			String msg = "error while transform JSONObject";
			LOGGER.error(msg + " | " + e.toString());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg + " | Exception: " + e.toString())
					.build();
		}

		MapServerConnection newConnection = new MapServerConnectionImpl(newConnectionData);

		// add to connection pool
		try {
			Application.getConnectionManager().addConnection(newConnection);
		} catch (ConnectionException e) {
			String msg = "error while adding connection to the connection pool";
			LOGGER.error(msg + " | " + e.toString());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg + " | Exception: " + e.toString())
					.build();
		}

		// persist connection in property
		try {
			Application.getConnections().persistConnections();
		} catch (PropertyException e) {
			LOGGER.error("error while connection persist", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("error while connection persist -> " + e.toString()).build();
		}

		LOGGER.trace("... new rest PUT saveConnection " + newConnection + " success");
		return Response.ok().entity(newConnection + " was saved").build();
	}

	/**
	 * Conneced the dataservice with connection default to a MAP-Server.
	 *
	 * Example-URL: <tt>http://example.com:8000/default/connect</tt>
	 *
	 * @throws HttpException
	 *
	 **/
	@PUT
	@Path("{connectionName}/connect")
	public Response connect(@PathParam("connectionName") String name) {
		try {
			Application.getConnectionManager().connect(name);
		} catch (ConnectionEstablishedException e) {
			LOGGER.warn(e.toString());
			return Response.ok().entity("INFO: connection allready aktive")
					.build();
		} catch (ConnectionException e) {
			LOGGER.error("error while connecting to " + name, e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e.toString()).build();
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
			Application.getConnectionManager().disconnect(name);
		} catch (NotConnectedException e) {
			LOGGER.error("error while disconnect from " + name, e);
			return Response.ok()
					.entity("INFO: connection allready disconnected").build();
		} catch (ConnectionException e) {
			LOGGER.error("error while disconnect from " + name, e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(e.toString()).build();
		}

		return Response.ok().entity("INFO: disconnection successfully").build();
	}

	/**
	 * Returns a JSONObject with saved connections to a MAP-Server. Example-URL:
	 * <tt>http://example.com:8000/</tt>
	 *
	 * You can set the onlyActive value of true. Example-URL:
	 * <tt>http://example.com:8000/?onlyActive=true</tt>
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
				LOGGER.error(e.toString(), e);
			}

			jsonConnections.put(jsonConnectionData);

		}
		return jsonConnections;
	}
}
