package de.fhhannover.inform.trust.visitmeta.dataservice.rest;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * This file is part of VisITMeta, version 0.0.1, implemented by the Trust@FHH
 * research group at the Hochschule Hannover.
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

import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.fhhannover.inform.trust.visitmeta.ifmap.ConnectionManager;
import de.fhhannover.inform.trust.visitmeta.ifmap.exception.ConnectionException;


@Path("/")
public class ConnectionResource {


	private static final Logger log = Logger.getLogger(ConnectionResource.class);


	public static final String JSON_KEY_CONNECTION_NAME = "connectionName";

	public static final String JSON_KEY_CONNECTION_URL = "url";

	public static final String JSON_KEY_CONNECTION_USER = "user";

	public static final String JSON_KEY_CONNECTION_USER_PASS = "userPass";

	public static final String JSON_KEY_CONNECTION_TRUSTSTORE = "truststore";

	public static final String JSON_KEY_CONNECTION_TRUSTSTORE_PASS = "truststorePass";


	/**
	 * Delete a a saved connection.
	 * 
	 * Example-URL: <tt>http://example.com:8000/default/delete</tt>
	 */
	@DELETE
	@Path("{connectionName}/delete")
	public String deleteConnection(@PathParam("connectionName") String name) {
		return "INFO: TODO"; // TODO
	}

	/**
	 * Saved a new connection to a MAP-Server with a JSONObject.
	 * 
	 * Example-URL: <tt>http://example.com:8000/save</tt>
	 * 
	 * Example-JSONObject:
	 * {
	 *  connectionName:con1,
	 *  url:"https://localhost:8443",
	 *  user:visitmeta,
	 *  userPass:visitmeta,
	 *  truststore:"/visitmeta.jks",
	 *  truststorePass:visitmeta
	 * }
	 */
	@PUT
	@Path("save")
	@Consumes(MediaType.APPLICATION_JSON)
	public String saveConnection(JSONObject jObj) {

		try {

			saveNewConnection(jObj);

		} catch (JSONException e) {

			return e.getMessage();
		}

		return "INFO: connection was saved";
	}


	/**
	 * Conneced the dataservice with connection default to a MAP-Server.
	 * 
	 * Example-URL: <tt>http://example.com:8000/default/connect</tt>
	 * 
	 **/
	@PUT
	@Path("{connectionName}/connect")
	public String connect(@PathParam("connectionName") String name) {

		try {

			ConnectionManager.connectTo(name);

		} catch (ConnectionException e) {

			return "ERROR: " + e.getClass().getSimpleName() +", connecting fail.";
		};

		return "INFO: connection successfully";
	}

	/**
	 * Disconnected the default connection from a MAP-Server.
	 * 
	 * Example-URL: <tt>http://example.com:8000/default/disconnect</tt>
	 * 
	 **/
	@PUT
	@Path("{connectionName}/disconnect")
	public String disconnect(@PathParam("connectionName") String name) {

		try {

			ConnectionManager.disconnectFrom(name);

		} catch (ConnectionException e) {

			return "ERROR: " + e.getClass().getSimpleName() + ", disconnecting fail.";
		};

		return "INFO: disconnection successfully";
	}

	/**
	 * Returns a JSONObject with saved connections to a MAP-Server.
	 * 
	 * Example-URL: <tt>http://example.com:8000/</tt>
	 * 
	 **/
	@GET
	public String getConnections() {
		return ConnectionManager.getSavedConnections().toString();
	}

	private void saveNewConnection(JSONObject jObj) throws JSONException {
		String connectionName = null;
		String url = null;
		String user = null;
		String userPass = null;
		String truststore = null;
		String truststorePass = null;

		Iterator<String> i = jObj.keys();

		while (i.hasNext()) {
			String jKey = i.next();

			switch (jKey) {

			case JSON_KEY_CONNECTION_NAME:

				connectionName = jObj.getString(jKey);
				break;

			case JSON_KEY_CONNECTION_URL:

				url = jObj.getString(jKey);
				break;

			case JSON_KEY_CONNECTION_USER:

				user = jObj.getString(jKey);
				break;

			case JSON_KEY_CONNECTION_USER_PASS:

				userPass = jObj.getString(jKey);
				break;

			case JSON_KEY_CONNECTION_TRUSTSTORE:

				truststore = jObj.getString(jKey);
				break;

			case JSON_KEY_CONNECTION_TRUSTSTORE_PASS:

				truststorePass = jObj.getString(jKey);
				break;

			default:
				log.warn("The key: \"" + jKey + "\" is not a valide JSON-Key for connections.");
				break;
			}
		}

		if(connectionName == null || url == null || user == null || userPass == null || truststore == null ||
				truststorePass == null){
			throw new JSONException("One or more keys was not a valide JSON-Key for connections.");
		}

		ConnectionManager.newConnection(connectionName, url, user, userPass, truststore, truststorePass);
	}
}