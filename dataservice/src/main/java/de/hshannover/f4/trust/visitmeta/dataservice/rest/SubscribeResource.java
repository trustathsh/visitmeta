package de.hshannover.f4.trust.visitmeta.dataservice.rest;

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

import java.util.Iterator;

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

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeUpdate;
import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.dataservice.util.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.ifmap.ConnectionManager;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionException;
import de.hshannover.f4.trust.visitmeta.util.PropertiesReaderWriter;

/**
 * For each request a new object of this class will be created. The
 * resource is accessible under the path <tt>/subscribe</tt>.
 * About this interface can be update and delete subscriptions. Untder the
 * path /subscribe/active return a list for all active subscriptions.
 *
 * @author Marcel Reichenbach
 *
 */

@Path("{connectionName}/subscribe")
public class SubscribeResource {


	private static final Logger log = Logger.getLogger(SubscribeResource.class);


	private static final PropertiesReaderWriter config = Application.getIFMAPConfig();

	private static final int MAX_DEPTH = Integer.parseInt(config.getProperty(ConfigParameter.IFMAP_MAX_DEPTH));

	private static final int MAX_SIZE = Integer.parseInt(config.getProperty(ConfigParameter.IFMAP_MAX_SIZE));

	public static final String JSON_KEY_SUBSCRIBE_NAME = "subscribeName";

	public static final String JSON_KEY_IDENTIFIER = "identifier";

	public static final String JSON_KEY_IDENTIFIER_TYPE = "identifierType";

	public static final String JSON_KEY_MAX_DEPTH = "maxDepth";

	public static final String JSON_KEY_MAX_SIZE = "maxSize";

	public static final String JSON_KEY_LINKS_FILTER = "linksFilter";

	public static final String JSON_KEY_RESULT_FILTER = "resultFilter";

	public static final String JSON_KEY_TERMINAL_IDENTIFIER_TYPES = "terminalIdentifierTypes";


	@QueryParam("deleteAll")
	@DefaultValue("false")
	private boolean mDeleteAll = false;


	/**
	 * Returns a JSONArray with the active subscriptions for the dataservice about the default connection.
	 * 
	 * Example-URL: <tt>http://example.com:8000/default/subscribe</tt>
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Object getActiveSubscriptions(@PathParam("connectionName") String connectionName) {

		JSONArray activeSubscriptions;

		try {

			activeSubscriptions = new JSONArray(ConnectionManager.getActiveSubscriptionsFromConnection(connectionName));

		} catch (ConnectionException e) {

			return "ERROR: " + e.getClass().getSimpleName();
		};

		return activeSubscriptions;
	}

	/**
	 * Send a subscribeUpdate to the MAP-Server with a JSONObject.
	 * Max-Depth and Max-Size have the defaultValue 1000 and 1000000000. To change this values
	 * use the params "maxDepth" and "maxSize", or set this in the JSONObject.
	 * 
	 * On identifier type:
	 * ip-address:     "[type],[value]" e.g. "IPv4,10.1.1.1"
	 * device:         "[name]"
	 * access-request: "[name]"
	 * mac-address:    "[value]"
	 * 
	 * Use the valid JSON-Keys for subscriptions:
	 * SUBSCRIBE NAME = "subscribeName"
	 * IDENTIFIER = "identifier"
	 * IDENTIFIER-TYPE = "identifierType"
	 * MAX-DEPTH = "maxDepth"
	 * MAX-SIZE = "maxSize"
	 * LINKS-FILTER = "linksFilter"
	 * RESULT-FILTER = "resultFilter"
	 * TERMINAL-IDENTIFIER-TYPES = "terminalIdentifierTypes"
	 * 
	 * Example-URL: <tt>http://example.com:8000/default/subscribe/update</tt>
	 * 
	 * Example-JSONObject:
	 * {
	 * 	subscribeName:exampleSub,
	 * 	identifierType:device,
	 * 	identifier:device12
	 * }
	 */
	@PUT
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	public String update(@PathParam("connectionName") String name, JSONObject jObj) {

		try {

			Iterator<String> i = jObj.keys();

			while(i.hasNext()){
				String jKey = i.next();

				JSONObject moreSubscribes = jObj.getJSONObject(jKey);

				try{

					subscribeUpdate(name, moreSubscribes);

				} catch (ConnectionException e) {	//TODO HTTP Status Code von 200 OK auf nin Fehler setzen

					return "ERROR: subscribe update was not send: " + e.getClass().getSimpleName();
				};
			}

		} catch (JSONException e) {

			try{

				subscribeUpdate(name, jObj);

			} catch (ConnectionException ee) {	//TODO HTTP Status Code von 200 OK auf nin Fehler setzen
				return "ERROR: subscribe update was not send: " + ee.getClass().getSimpleName();
			}

		}
		return "INFO: subscribe successfully";
	}

	private void subscribeUpdate(String connectionName, JSONObject jObj) throws ConnectionException {
		String subscribeName = null;
		String identifierType = null;
		String identifier = null;
		int maxDepth = MAX_DEPTH;
		int maxSize = MAX_SIZE;

		String linksFilter = null;
		String resultFilter = null;
		String terminalIdentifierTypes = null;

		try {

			Iterator<String> i = jObj.keys();

			while (i.hasNext()) {
				String jKey = i.next();

				switch (jKey) {
				case JSON_KEY_SUBSCRIBE_NAME:
					subscribeName = jObj.getString(jKey);
					break;

				case JSON_KEY_IDENTIFIER_TYPE:
					identifierType = jObj.getString(jKey);
					break;

				case JSON_KEY_IDENTIFIER:
					identifier = jObj.getString(jKey);
					break;

				case JSON_KEY_MAX_DEPTH:
					maxDepth = jObj.getInt(jKey);
					break;

				case JSON_KEY_MAX_SIZE:
					maxSize = jObj.getInt(jKey);
					break;

				case JSON_KEY_LINKS_FILTER:
					linksFilter = jObj.getString(jKey);
					break;

				case JSON_KEY_RESULT_FILTER:
					resultFilter = jObj.getString(jKey);
					break;

				case JSON_KEY_TERMINAL_IDENTIFIER_TYPES:
					terminalIdentifierTypes = jObj.getString(jKey);
					break;

				default:
					log.warn("The key: \"" + jKey + "\" is not a valide JSON-Key for subscriptions.");
					break;
				}
			}
		} catch (JSONException e) {

			log.error(e.getMessage(), e);
		}

		SubscribeRequest request = Requests.createSubscribeReq();
		SubscribeUpdate subscribe = Requests.createSubscribeUpdate();

		subscribe.setName(subscribeName);
		subscribe.setMaxDepth(maxDepth);
		subscribe.setMaxSize(maxSize);
		subscribe.setMatchLinksFilter(linksFilter);
		subscribe.setResultFilter(resultFilter);
		subscribe.setTerminalIdentifierTypes(terminalIdentifierTypes);
		subscribe.setStartIdentifier(createStartIdentifier(identifierType, identifier));

		request.addSubscribeElement(subscribe);

		ConnectionManager.subscribeFromConnection(connectionName, request);
	}

	private Identifier createStartIdentifier(String sIdentifierType, String sIdentifier) {
		switch (sIdentifierType) {
		case "device":
			return Identifiers.createDev(sIdentifier);
		case "access-request":
			return Identifiers.createAr(sIdentifier);
		case "ip-address":
			String[] split = sIdentifier.split(",");
			switch (split[0]) {
			case "IPv4":
				return Identifiers.createIp4(split[1]);
			case "IPv6":
				return Identifiers.createIp6(split[1]);
			default:
				throw new RuntimeException("unknown IP address type '"+split[0]+"'");
			}
		case "mac-address":
			return Identifiers.createMac(sIdentifier);

			// TODO identity and extended identifiers

		default:
			throw new RuntimeException("unknown identifier type '"+sIdentifierType+"'");
		}
	}

	/**
	 * Send a subscribeDelete for all active subscriptions to the MAP-Server.
	 * You must set the deleteAll value of true.
	 * 
	 * Example-URL: <tt>http://example.com:8000/default/subscribe/delete?deleteAll=true</tt>
	 */
	@DELETE
	@Path("delete")
	public String deleteAll(@PathParam("connectionName") String name) {
		if(mDeleteAll){
			try{

				ConnectionManager.deleteSubscriptionsFromConnection(name);

			} catch (ConnectionException e) {	//TODO HTTP Status Code von 200 OK auf nin Fehler setzen

				return "ERROR: subscriptions not deleted: " + e.getClass().getSimpleName();
			};

			return "INFO: delete all active subscriptions successfully";

		}else {

			return "INFO: deleteAll value is not true, nothing were deleted";
		}
	}

	/**
	 * Send a subscribeDelete for the active subscription to the MAP-Server.
	 * 
	 * Example-URL: <tt>http://example.com:8000/default/subscribe/delete/{subscriptionName}</tt>
	 */
	@DELETE
	@Path("delete/{subscriptionName}")
	public String delete(@PathParam("connectionName") String name, @PathParam("subscriptionName") String subscriptionName) {

		try{

			ConnectionManager.deleteSubscribeFromConnection(name, subscriptionName);

		} catch (ConnectionException e) {	//TODO HTTP Status Code von 200 OK auf nin Fehler setzen

			return "ERROR: subscription not deleted: " + e.getClass().getSimpleName();
		};

		return "INFO: delete subscription successfully";
	}
}
