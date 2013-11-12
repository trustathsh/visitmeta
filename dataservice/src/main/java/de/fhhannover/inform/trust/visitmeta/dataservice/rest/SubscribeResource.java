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

import de.fhhannover.inform.trust.visitmeta.dataservice.Application;
import de.fhhannover.inform.trust.visitmeta.ifmap.UpdateService;

/**
 * For each request a new object of this class will be created. The
 * resource is accessible under the path <tt>/subscribe</tt>.
 * About this interface can be update and delete subscriptions. Untder the
 * path /subscribe/active return a list for all active subscriptions.
 *
 * @author Marcel Reichenbach
 *
 */

@Path("/subscribe")
public class SubscribeResource {

	private static final Logger log = Logger.getLogger(SubscribeResource.class);

	private UpdateService mUpdateService;

	@QueryParam("maxDepth")
	@DefaultValue("1000")
	private int MAX_DEPTH;

	@QueryParam("maxSize")
	@DefaultValue("1000000000")					//TODO aus der config auslesen??
	private int MAX_SIZE;

	@QueryParam("deleteAll")
	@DefaultValue("false")
	private boolean mDeleteAll;

	public SubscribeResource() {
		mUpdateService = initUpdateService();
	}

	/**
	 * Returns a JSONArray with the active subscriptions for the dataservice.
	 * Example-URL: <tt>http://example.com:8000/subscribe</tt>
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray active() {
		JSONArray activeSubscriptions= new JSONArray(mUpdateService.getActiveSubscriptions());
		return activeSubscriptions;
	}

	/**
	 * Send a subscribeUpdate to the MAP-Server.
	 * Max-Depth and Max-Size have the defaultValue 1000 and 1000000000. To change this values
	 * use the params "maxDepth" and "maxSize".
	 * Example-URL: <tt>http://example.com:8000/subscribe/update/access-request/exampleIdentifier1</tt>
	 * Example-URL: <tt>http://example.com:8000/subscribe/update/device/exampleIdentifier2?maxDepth=5&maxSize=500</tt>
	 */
	@PUT
	@Path("update/{identifierType}/{identifier}")
	public String update(@PathParam("identifierType") String identifierType, @PathParam("identifier") String identifier) {
		String subscribeName = identifierType + "-" + identifier;
		mUpdateService.subscribeUpdate(subscribeName, identifierType, identifier, MAX_DEPTH, MAX_SIZE);
		return "OK";
	}

	/**
	 * Send a subscribeDelete for all active subscriptions to the MAP-Server.
	 * You must set the deleteAll value of true.
	 * Example-URL: <tt>http://example.com:8000/subscribe/delete?deleteAll=true</tt>
	 */
	@DELETE
	@Path("delete")
	public String deleteAll() {
		if(mDeleteAll){
			mUpdateService.subscribeDeleteAll();
		}
		return "OK";
	}

	/**
	 * Send a subscribeDelete for the active subscription to the MAP-Server.
	 * Example-URL: <tt>http://example.com:8000/subscribe/delete/(subscriptionName)</tt>
	 */
	@DELETE
	@Path("delete/{identifier}")
	public String delete(@PathParam("identifier") String identifier) {
		mUpdateService.subscribeDelete(identifier);
		return "OK";
	}

	private UpdateService initUpdateService() {
		return Application.getUpdateService();
	}
}
