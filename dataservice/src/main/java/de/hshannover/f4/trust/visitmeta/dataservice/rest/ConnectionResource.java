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
 * This file is part of visitmeta dataservice, version 0.0.3,
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



import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.ifmap.ConnectionManager;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionEstablishedException;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.NotConnectedException;


@Path("/")
public class ConnectionResource {

	private static final Logger log = Logger.getLogger(ConnectionResource.class);

	public static final String JSON_KEY_CONNECTION_URL = "url";
	public static final String JSON_KEY_CONNECTION_USER = "user";
	public static final String JSON_KEY_CONNECTION_USER_PASS = "userPass";

	@QueryParam("onlyActive")
	@DefaultValue("false")
	private boolean mOnlyActive = false;


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
	 * Saved a new connection to a MAP-Server with a JSONObject.
	 * 
	 * Example-URL: <tt>http://example.com:8000/default</tt>
	 * 
	 * Example-JSONObject:
	 * {
	 *  url:"https://localhost:8443",
	 *  user:visitmeta,
	 *  userPass:visitmeta,
	 * }
	 */
	@PUT
	@Path("{connectionName}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response saveConnection(@PathParam("connectionName") String name, JSONObject jObj) {
		try {

			ConnectionManager.newConnection(name, jObj.getString(JSON_KEY_CONNECTION_URL), jObj.getString(JSON_KEY_CONNECTION_USER),
					jObj.getString(JSON_KEY_CONNECTION_USER_PASS));

		} catch (JSONException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}

		return Response.ok().entity("INFO: connection was saved").build();
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
			return Response.ok().entity("INFO: connection allready aktive").build();
		} catch (ConnectionException e) {
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
			return Response.ok().entity("INFO: connection allready disconnected").build();
		} catch (ConnectionException e) {
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


		JSONObject changes = new JSONObject();
		for(Entry<String,Connection> entry: ConnectionManager.getSavedConnectionMap().entrySet()){
			Map<String,String> connectionMap = new HashMap<String,String>();

			connectionMap.put("URL", entry.getValue().getUrl());
			connectionMap.put("Username", entry.getValue().getUser());
			connectionMap.put("Password", entry.getValue().getUserPass());

			JSONObject jsonConnection = new JSONObject(connectionMap);

			try {

				changes.put(entry.getKey(), jsonConnection);

			} catch (JSONException e) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
			}
		}

		return changes;

		//		if(mOnlyActive){
		//			return ConnectionManager.getActiveConnections().toString();
		//		}else{
		//			return ConnectionManager.getSavedConnections().toString();
		//		}
	}

}