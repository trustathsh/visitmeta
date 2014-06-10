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
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.ifmap;



import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.NoSavedConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;

public class ConnectionManager {


	private static Logger log = Logger.getLogger(ConnectionManager.class);

	private static Map<String, Connection> mConnectionPool = new HashMap<String,Connection>();


	/**
	 * Add a new connection to the connection pool.
	 * 
	 * @param connection
	 * @throws ConnectionException
	 */
	public static void add(Connection connection) throws ConnectionException {
		if(!existsConnectionName(connection.getConnectionName())){
			mConnectionPool.put(connection.getConnectionName(), connection);
			log.debug(connection.getConnectionName() + " added to connection pool");
		}else{
			throw new ConnectionException(connection.getConnectionName() + " connection name already exists, adding canceled");
		}
	}

	/**
	 * Connected to the Map-Server of all connections at the connection pool.
	 * StartupConnect must be set true.
	 * 
	 * @throws ConnectionException
	 */
	public static void startupConnect() throws ConnectionException{
		for(Connection c: mConnectionPool.values()){
			if(c.isStartupConnect()){
				connectTo(c.getConnectionName());
				executeInitialSubscription(c);
			}
		}
	}

	/* FIXME: change this to something that gets all saved subscriptions from "connections.yml"
	 * and execute these subscriptions on startup.
	 * In addition: add an initial subscription which *always* subscribe for the IF-MAP 2.2 defined
	 * MAP server identifier.
	 */
	private static void executeInitialSubscription(Connection connection) throws ConnectionException {
		for(JSONObject json: connection.getSubscribeList()){
			// TODO [MR] NEXT RELEASE add startupSubscribe to SubscribeKey
			if(json.optBoolean("startupSubscribe")){
				// TODO [MR] NEXT RELEASE use SubscribeKey's
				log.debug("initial subscription("+ json.optString("subscribeName") +") for connection("+ connection.getConnectionName() +")...");
				SubscribeRequest request = SubscriptionHelper.buildRequest(json);
				subscribeFromConnection(connection.getConnectionName(), request);
			}
		}
	}

	/**
	 * Delete a saved connection.
	 * 
	 * @param name Is the connection name.
	 * @throws ConnectionException
	 */
	public static void removeConnection(String name) throws ConnectionException{
		log.info(" TODO "); //TODO removeConnection()
	}

	/**
	 * Connect the dataservice to a MAP-Server.
	 * 
	 * @param name Is the connection name.
	 * @throws ConnectionException
	 */
	public static void connectTo(String name) throws ConnectionException {
		log.trace("connection to " + name + " ...");

		getConnection(name).connect();

		log.info("connected to " + name);
	}

	/**
	 * Close a connection to the MAP-Server.
	 * 
	 * @param name Is the connection name.
	 * @throws ConnectionException
	 */
	public static void disconnectFrom(String name) throws ConnectionException {
		log.trace("disconnection from " + name + " ...");

		getConnection(name).disconnect();

		log.info("disconnected from " + name);
	}

	/**
	 * @return All saved connections.
	 */
	public static Map<String, Connection> getConnectionPool(){
		return mConnectionPool;
	}

	/**
	 * @return All saved connections.
	 */
	public static String getSavedConnections(){
		log.trace("getSavedConnections()...");

		StringBuilder sb = new StringBuilder();

		for(Connection c: mConnectionPool.values()){
			sb.append(c).append("\n");
		}

		log.trace("Saved-Connections: \n" + sb.toString());
		return sb.toString();
	}

	/**
	 * @return All active connections.
	 */
	public static String getActiveConnections(){
		log.trace("getActiveConnections()...");

		StringBuilder sb = new StringBuilder();

		for(Connection c: mConnectionPool.values()){
			if(c.isConnected()){
				sb.append(c).append("\n");
			}
		}

		log.trace("Active-Connections: \n" + sb.toString());
		return sb.toString();
	}

	/**
	 * @return A Set<String> with the active subscriptions.
	 * @throws ConnectionException
	 */
	public static Set<String> getActiveSubscriptionsFromConnection(String name) throws ConnectionException {
		return getConnection(name).getActiveSubscriptions();
	}

	/**
	 * Send a SubscribeRequest to the map server.
	 * 
	 * @param name Is the connection name.
	 * @param request Is the SubscribeRequest.
	 * @throws ConnectionException
	 */
	public static void subscribeFromConnection(String name, SubscribeRequest request) throws ConnectionException {
		log.trace("new subscribe for Connection " + name + " ...");

		getConnection(name).subscribe(request);

		log.info("subscribe received");
	}

	/**
	 * Delete a subscription with his subscriptionName.
	 * 
	 * @param name Is the connection name.
	 * @param subscriptionName Is the name of the Subscription.
	 * @throws ConnectionException
	 */
	public static void deleteSubscribeFromConnection(String name, String subscriptionName) throws ConnectionException {
		log.trace("delete subscription from connection " + name + "[Name="+ subscriptionName + "] ...");

		getConnection(name).deleteSubscribe(subscriptionName);

		log.info("subscription(" + name + "[Name=" + subscriptionName + "]" + ") were deleted");
	}

	/**
	 * Delete all active subscriptions.
	 * 
	 * @param name Is the connection name.
	 * @throws ConnectionException
	 */
	public static void deleteSubscriptionsFromConnection(String name) throws ConnectionException {
		log.trace("delete all subscriptions from connection " + name + " ...");

		getConnection(name).deleteSubscriptions();

		log.info("all subscriptions were deleted");
	}

	private static Connection getConnection(String connectionName) throws NoSavedConnectionException{
		if(mConnectionPool.containsKey(connectionName)){
			return mConnectionPool.get(connectionName);
		}

		NoSavedConnectionException e = new NoSavedConnectionException();
		log.error(e.toString());
		throw e;
	}

	/**
	 * @param name Is the connection name.
	 * @return A SimpleGraphService from the Neo4J-Database.
	 * @throws ConnectionException
	 */
	public static GraphService getGraphServiceFromConnection(String name) throws ConnectionException {
		return getConnection(name).getGraphService();
	}

	public static boolean existsConnectionName(String connectionName) {
		if(mConnectionPool.containsKey(connectionName)){
			return true;
		}
		return false;
	}

	public static void persistSubscribeToConnection(String connectionName, JSONObject jObj) throws FileNotFoundException {
		mConnectionPool.get(connectionName).addSubscribe(jObj);
		Application.getConnectionPersister().persistConnections();
	}
}
