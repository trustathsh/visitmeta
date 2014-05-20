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



import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.dataservice.util.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.NoSavedConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;

public class ConnectionManager {


	private static Logger log = Logger.getLogger(ConnectionManager.class);

	private static final String TRUSTSTORE_PATH = Application.getIFMAPConfig().getProperty(ConfigParameter.IFMAP_TRUSTSTORE_PATH);
	private static final String TRUSTSTORE_PASS = Application.getIFMAPConfig().getProperty(ConfigParameter.IFMAP_TRUSTSTORE_PASS);
	private static final int MAX_SIZE = Integer.parseInt(Application.getIFMAPConfig().getProperty(ConfigParameter.IFMAP_MAX_SIZE));

	private static Map<String, Connection> conPool = new HashMap<String,Connection>();


	public static Connection newConnection(String name, String url, String user, String userPass){
		return newConnection(name, url, user, userPass, TRUSTSTORE_PATH, TRUSTSTORE_PASS, MAX_SIZE);
	}

	public static Connection newConnection(String name, String url, String user, String userPass, String truststore, String truststorePass, int maxSize) {
		log.trace("new connection " + name + "...");

		Connection newConnection = new Connection(name, url, user, userPass, truststore, truststorePass, maxSize);
		conPool.put(name, newConnection);

		log.info("connection " + name + " is saved");
		return newConnection;
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
	public static String getSavedConnections(){
		log.trace("getSavedConnections()...");

		StringBuilder sb = new StringBuilder();

		for(Connection c: conPool.values()){
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

		for(Connection c: conPool.values()){
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
		if(conPool.containsKey(connectionName)){
			return conPool.get(connectionName);
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
}
