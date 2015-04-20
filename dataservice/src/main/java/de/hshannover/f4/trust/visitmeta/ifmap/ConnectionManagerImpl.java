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
package de.hshannover.f4.trust.visitmeta.ifmap;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.NoSavedConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.interfaces.ifmap.ConnectionManager;

/**
 * An implementation of the {@link ConnectionManager} interface.
 *
 * @author Bastian Hellmann
 * @author Marcel Reichenbach
 */
public class ConnectionManagerImpl implements ConnectionManager {

	private static Logger log = Logger.getLogger(ConnectionManagerImpl.class);

	private static Map<String, Connection> mConnectionPool = new HashMap<String, Connection>();

	@Override
	public void addConnection(Connection connection) throws ConnectionException {
		if (!doesConnectionExist(connection.getConnectionName())) {
			mConnectionPool.put(connection.getConnectionName(), connection);
			log.debug(connection.getConnectionName()
					+ " added to connection pool");
		} else {
			throw new ConnectionException(connection.getConnectionName()
					+ " connection name already exists, adding canceled");
		}
	}

	@Override
	public void executeStartupConnections() throws ConnectionException {
		for (Connection c : mConnectionPool.values()) {
			if (c.doesConnectOnStartup()) {
				connect(c.getConnectionName());
				executeInitialSubscription(c);
			}
		}
	}

	/*
	 * FIXME: change this to something that gets all saved subscriptions from
	 * "connections.yml" and execute these subscriptions on startup. In
	 * addition: add an initial subscription which *always* subscribe for the
	 * IF-MAP 2.2 defined MAP server identifier.
	 */
	private void executeInitialSubscription(Connection connection)
			throws ConnectionException {
		for (Subscription s : connection.getSubscriptions()) {
			if (s.isStartupSubscribe()) {
				log.debug("initial subscription(" + s.getName()
						+ ") for connection(" + connection.getConnectionName()
						+ ")...");
				SubscribeRequest request = SubscriptionHelper.buildRequest(s);
				subscribe(connection.getConnectionName(), request);
			}
		}
	}

	@Override
	public void removeConnection(Connection c) throws ConnectionException {
		log.info("removeConnection not implemented yet.");
		// TODO remove Connection
	}

	@Override
	public void connect(String connectionName) throws ConnectionException {
		log.trace("connection to " + connectionName + " ...");

		getConnection(connectionName).connect();

		log.info("connected to " + connectionName);
	}

	@Override
	public void disconnect(String connectionName) throws ConnectionException {
		log.trace("disconnect from " + connectionName + " ...");

		getConnection(connectionName).disconnect();

		log.info("disconnected from " + connectionName);
	}

	@Override
	public Map<String, Connection> getSavedConnections() {
		return mConnectionPool;
	}

	@Override
	public Set<String> getActiveSubscriptions(String connectionName) throws ConnectionException {
		return getConnection(connectionName).getActiveSubscriptions();
	}

	@Override
	public Set<String> getSubscriptions(String connectionName) throws ConnectionException {
		Set<String> subscriptionNames = new HashSet<String>();
		List<Subscription> subscriptions = getConnection(connectionName).getSubscriptions();
		for (Subscription s : subscriptions) {
			subscriptionNames.add(s.getName());
		}
		return subscriptionNames;
	}

	@Override
	public void subscribe(String connectionName, SubscribeRequest request)
			throws ConnectionException {
		log.trace("new subscription for Connection " + connectionName + " ...");

		getConnection(connectionName).subscribe(request);

		log.info("subscribtion received");
	}

	@Override
	public void deleteSubscription(String connectionName,
			String subscriptionName) throws ConnectionException {
		log.trace("delete subscription '" + subscriptionName
				+ "' from connection '" + connectionName + "' ...");

		getConnection(connectionName).deleteSubscription(subscriptionName);

		log.info("subscription '" + subscriptionName + "' from connection '"
				+ connectionName + "' was deleted");
	}

	@Override
	public void deleteAllSubscriptions(String connectionName)
			throws ConnectionException {
		log.trace("delete all subscriptions from connection " + connectionName
				+ " ...");

		getConnection(connectionName).deleteAllSubscriptions();

		log.info("all subscriptions were deleted");
	}

	private Connection getConnection(String connectionName)
			throws NoSavedConnectionException {
		if (mConnectionPool.containsKey(connectionName)) {
			return mConnectionPool.get(connectionName);
		} else {
			throw new NoSavedConnectionException();
		}
	}

	@Override
	public GraphService getGraphService(String connectionName)
			throws ConnectionException {
		return getConnection(connectionName).getGraphService();
	}

	@Override
	public boolean doesConnectionExist(String connectionName) {
		return mConnectionPool.containsKey(connectionName);
	}

	@Override
	public void storeSubscription(String connectionName,
			Subscription subscription) throws IOException, PropertyException {
		mConnectionPool.get(connectionName).addSubscription(subscription);
		Application.getConnections().persistConnections();
	}

	@Override
	public Connection createConnection(String connectionName, String url,
			String userName, String userPassword) throws ConnectionException {
		if (doesConnectionExist(connectionName)) {
			throw new ConnectionException(connectionName
					+ " connection already exists");
		} else {
			return new ConnectionImpl(connectionName, url, userName,
					userPassword);
		}
	}

}
