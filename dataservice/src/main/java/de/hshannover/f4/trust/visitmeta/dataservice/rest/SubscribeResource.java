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

import java.io.IOException;
import java.util.List;

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

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.data.DataManager;
import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.ifmap.ConnectionManager;
import de.hshannover.f4.trust.visitmeta.util.JSONDataKey;

/**
 * For each request a new object of this class will be created. The resource is
 * accessible under the path <tt>{connectionName}/subscribe</tt>. About this
 * interface can be update and delete subscriptions. Untder the path
 * {connectionName}/subscribe/active return a list for all active subscriptions.
 *
 * @author Marcel Reichenbach
 *
 */

@Path("{connectionName}/subscribe")
public class SubscribeResource {

	private static final Logger log = Logger.getLogger(SubscribeResource.class);

	@QueryParam("deleteAll")
	@DefaultValue("false")
	private boolean mDeleteAll = false;

	@QueryParam("onlyActive")
	@DefaultValue("false")
	private boolean mOnlyActive = false;

	/**
	 * Returns a JSONArray with the subscriptions for the dataservice about the default connection.
	 *
	 * Example-URL: <tt>http://example.com:8000/default/subscribe</tt>
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Object getSubscriptions(@PathParam("connectionName") String name) {
		JSONArray jaSubscriptions;
		if (!mOnlyActive) {
			try {
				jaSubscriptions = new JSONArray(Application.getConnectionManager().getSubscriptions(name));
			} catch (ConnectionException e) {
				log.error("error at getSubscriptions from " + name + " | " + e.toString());
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
			}

		} else {

			try {
				jaSubscriptions = new JSONArray(Application.getConnectionManager().getActiveSubscriptions(name));
			} catch (ConnectionException e) {
				log.error("error at getActiveSubscriptions from " + name + " | " + e.toString());
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
			}

		}

		return jaSubscriptions;
	}

	/**
	 * Send a subscribeUpdate to the MAP-Server with a JSONObject. Max-Depth and
	 * Max-Size have the defaultValue 1000 and 1000000000. To change this values
	 * set "maxDepth" and "maxSize" in the JSONObject.
	 *
	 * On identifier type: ip-address: "[type],[value]" e.g. "IPv4,10.1.1.1"
	 * device: "[name]" access-request: "[name]" mac-address: "[value]"
	 *
	 * Use the valid JSON-Keys for subscriptions: START IDENTIFIER = "startIdentifier" IDENTIFIER-TYPE =
	 * "identifierType" MAX-DEPTH = "maxDepth" MAX-SIZE = "maxSize" MATCH-LINKS-FILTER
	 * = "matchLinksFilter" RESULT-FILTER = "resultFilter" TERMINAL-IDENTIFIER-TYPES
	 * = "terminalIdentifierTypes" see also {@link JSONDataKey}.
	 *
	 * Example-URL: <tt>http://example.com:8000/default/subscribe/update</tt>
	 *
	 * Example-JSONObject:
	 * {
	 * "subExample":
	 * {
	 * "startIdentifier": "freeradius-pdp",
	 * "identifierType": "device"
	 * }
	 * }
	 */
	@PUT
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("connectionName") String name, JSONObject jsonData) {
		ConnectionManager manager = Application.getConnectionManager();

		SubscriptionData subscriptionData;
		try {

			subscriptionData = (SubscriptionData) DataManager.transformJSONObject(jsonData, SubscriptionData.class);
			manager.storeSubscription(name, subscriptionData);
			manager.startSubscription(name, subscriptionData.getName());

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONHandlerException
				| JSONException | IOException | PropertyException | ConnectionException e) {
			String msg = "error while transform JSONObject";
			log.error(msg + " | " + e.toString());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg + " | Exception: " + e.toString())
					.build();
		}

		try {
			Application.getConnections().persistConnections();
		} catch (PropertyException e) {
			log.error("error while connection persist | " + e.toString());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("error while connection persist -> " + e.toString()).build();
		}

		return Response.ok().entity("INFO: subscribe successfully").build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putSubscription(@PathParam("connectionName") String connectionName, JSONObject jsonData) {

		SubscriptionData subscriptionData;
		try {

			subscriptionData = (SubscriptionData) DataManager.transformJSONObject(jsonData, SubscriptionData.class);

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONHandlerException
				| JSONException e) {
			String msg = "error while transform JSONObject";
			log.error(msg + " | " + e.toString());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msg + " | Exception: " + e.toString())
					.build();
		}

		List<Data> subscriptionList;
		try {
			subscriptionList = Application.getConnectionManager().getSubscriptions(connectionName);
		} catch (ConnectionException e) {
			log.error("error while subscription PUT | " + e.toString());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("error while connection PUT -> " + e.toString()).build();
		}

		if (subscriptionList.contains(subscriptionData)) {
			Application.getConnectionManager().updateSubscription(connectionName, subscriptionData);
		} else {
			try {
				Application.getConnectionManager().storeSubscription(connectionName, subscriptionData);
			} catch (IOException | PropertyException e) {
				log.error("error while subscription PUT | " + e.toString());
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("error while connection PUT -> " + e.toString()).build();
			}
		}

		try {
			Application.getConnections().persistConnections();
		} catch (PropertyException e) {
			log.error("error while connection persist | " + e.toString());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("error while connection persist -> " + e.toString()).build();
		}

		return Response.ok().entity("INFO: subscribe put successfully").build();
	}

	@PUT
	@Path("start/{subscriptionName}")
	public Response start(@PathParam("connectionName") String connectName,
			@PathParam("subscriptionName") String subscriptionName) {
		try {
			Application.getConnectionManager().startSubscription(connectName, subscriptionName);
		} catch (ConnectionException e) {
			log.error("error while start " + subscriptionName + " from " + connectName + " | " + e.toString());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}

		return Response.ok().entity("INFO: subscription(" + subscriptionName + ") enabled").build();
	}

	@PUT
	@Path("stop/{subscriptionName}")
	public Response stop(@PathParam("connectionName") String connectName,
			@PathParam("subscriptionName") String subscriptionName) {
		try {
			Application.getConnectionManager().stopSubscription(connectName, subscriptionName);
		} catch (ConnectionException e) {
			log.error("error while stop " + subscriptionName + " from " + connectName + " | " + e.toString());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}

		return Response.ok().entity("INFO: subscription(" + subscriptionName + ") disabled").build();
	}

	/**
	 * Send a subscribeDelete for all active subscriptions to the MAP-Server.
	 * You must set the deleteAll value of true.
	 *
	 * Example-URL: <tt>http://example.com:8000/default/subscribe/delete?deleteAll=true</tt>
	 */
	@DELETE
	public Response delete(@PathParam("connectionName") String name) {
		if (mDeleteAll) {
			try {
				Application.getConnectionManager().deleteAllSubscriptions(name);
			} catch (ConnectionException e) {
				log.error("error while delete all subscriptions from " + name + " | " + e.toString());
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
			}
			return Response.ok().entity("INFO: delete all active subscriptions successfully").build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED)
					.entity("INFO: deleteAll value is not true, nothing were deleted").build();
		}
	}

	/**
	 * Send a subscribeDelete for the active subscription to the MAP-Server.
	 *
	 * Example-URL: <tt>http://example.com:8000/default/subscribe/delete/{subscriptionName}</tt>
	 */
	@DELETE
	@Path("{subscriptionName}")
	public Response delete(@PathParam("connectionName") String name,
			@PathParam("subscriptionName") String subscriptionName) {
		try {
			Application.getConnectionManager().deleteSubscription(name, subscriptionName);
		} catch (ConnectionException e) {
			log.error("error while delete " + subscriptionName + " from " + name + " | " + e.toString());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}

		// persist connection in property
		try {
			Application.getConnections().persistConnections();
		} catch (PropertyException e) {
			log.error("error while delete subscription | " + e.toString());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("error while delete connection -> " + e.toString()).build();
		}

		return Response.ok().entity("INFO: delete subscription(" + subscriptionName + ") successfully").build();
	}
}
