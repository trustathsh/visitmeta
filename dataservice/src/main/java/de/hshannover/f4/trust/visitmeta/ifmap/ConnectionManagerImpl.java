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
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.connections.MapServerConnectionImpl;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.NoSavedConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.Connection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.ifmap.ConnectionManager;

/**
 * An implementation of the {@link ConnectionManager} interface.
 *
 * @author Bastian Hellmann
 * @author Marcel Reichenbach
 */
public class ConnectionManagerImpl implements ConnectionManager {

	private static Logger log = Logger.getLogger(ConnectionManagerImpl.class);

	private static Map<String, MapServerConnection> mConnectionPool = new HashMap<String, MapServerConnection>();

	@Override
	public void addConnection(MapServerConnection connection) throws ConnectionException {
		if (!doesConnectionExist(connection.getConnectionName())) {
			mConnectionPool.put(connection.getConnectionName(), connection);
			log.debug(connection.getConnectionName() + " added to connection pool");
		} else {
			throw new ConnectionException(connection.getConnectionName()
					+ " connection name already exists, adding canceled");
		}
	}

	@Override
	public void updateConnection(MapServerConnectionData newData) throws NoSavedConnectionException {
		MapServerConnection savedConnection = getConnection(newData.getConnectionName());
		savedConnection.changeData(newData);
	}

	@Override
	public void executeStartupConnections() throws ConnectionException, InterruptedException {
		for (MapServerConnection c : mConnectionPool.values()) {
			if (c.doesConnectOnStartup()) {
				retryConnect(c, 3);
				executeInitialSubscription(c);
			}
		}
	}

	private void executeInitialSubscription(MapServerConnection connection) throws ConnectionException {
		for (Data subscription : connection.getSubscriptions()) {
			if (((SubscriptionData) subscription).isStartupSubscribe()) {
				log.debug("initial subscription(" + subscription.getName()
						+ ") for connection(" + connection.getConnectionName() + ")...");
				startSubscription(connection.getConnectionName(), subscription.getName());
			}
		}
	}

	@Override
	public void removeConnection(String connectionName) throws ConnectionException {
		MapServerConnection connection = mConnectionPool.get(connectionName);
		if (connection instanceof MapServerConnectionImpl) {
			MapServerConnectionImpl connectionImpl = (MapServerConnectionImpl) connection;

			if (connectionImpl.isConnected()) {
				connectionImpl.disconnect();
				log.info("Disconnected from Map-Server");
			}

			connectionImpl.disconnectFromDB();
			log.info("Disconnected from DB");

			mConnectionPool.remove(connectionName);
			log.info("connection '" + connectionName + "' was deleted");
		} else {
			log.fatal("While removeConnection('" + connectionName
					+ "'), this connection is not right for the dataservice.");
		}
	}

	@Override
	public void connect(String connectionName) throws ConnectionException {
		log.trace("connection to " + connectionName + " ...");

		getConnection(connectionName).connect();

		log.info("connected to " + connectionName);
	}

	private void retryConnect(Connection connection, int retry) throws ConnectionException, InterruptedException {
		for (int i = 0; i < retry; i++) {
			try {
				connection.connect();
				break;
			} catch (ConnectionException e) {
				if (i + 1 < retry) {
					log.warn(e.toString() + " -> Try it again (" + (i + 2) + "/" + retry + ") in 3 seconds...");
					Thread.sleep(3000);
				} else {
					throw e;
				}
			}
		}
	}

	@Override
	public void disconnect(String connectionName) throws ConnectionException {
		log.trace("disconnect from " + connectionName + " ...");

		getConnection(connectionName).disconnect();

		log.info("disconnected from " + connectionName);
	}

	@Override
	public Map<String, MapServerConnection> getSavedConnections() {
		return mConnectionPool;
	}

	@Override
	public List<Data> getActiveSubscriptions(String connectionName) throws ConnectionException {
		return getConnection(connectionName).getActiveSubscriptions();
	}

	@Override
	public List<Data> getSubscriptions(String connectionName) throws ConnectionException {
		return getConnection(connectionName).getSubscriptions();
	}

	@Override
	public void startSubscription(String connectionName, String subscriptionName) throws ConnectionException {
		log.trace("start " + subscriptionName + " for Connection " + connectionName + " ...");

		getConnection(connectionName).startSubscription(subscriptionName);

		log.info("subscribtion received");
	}

	@Override
	public void stopSubscription(String connectionName, String subscriptionName) throws ConnectionException {
		log.trace("stop " + subscriptionName + " for Connection " + connectionName + " ...");

		getConnection(connectionName).stopSubscription(subscriptionName);

		log.info("subscribtion received");
	}

	@Override
	public void deleteSubscription(String connectionName, String subscriptionName) throws ConnectionException {
		log.trace("delete subscription '" + subscriptionName + "' from connection '" + connectionName + "' ...");

		getConnection(connectionName).deleteSubscription(subscriptionName);

		log.info("subscription '" + subscriptionName + "' from connection '" + connectionName + "' was deleted");
	}

	@Override
	public void deleteAllSubscriptions(String connectionName) throws ConnectionException {
		log.trace("delete all subscriptions from connection " + connectionName + " ...");

		getConnection(connectionName).deleteAllSubscriptions();

		log.info("all subscriptions were deleted");
	}

	private MapServerConnection getConnection(String connectionName) throws NoSavedConnectionException {
		if (mConnectionPool.containsKey(connectionName)) {
			return mConnectionPool.get(connectionName);
		} else {
			throw new NoSavedConnectionException();
		}
	}

	@Override
	public GraphService getGraphService(String connectionName) throws ConnectionException {
		return getConnection(connectionName).getGraphService();
	}

	@Override
	public boolean doesConnectionExist(String connectionName) {
		return mConnectionPool.containsKey(connectionName);
	}

	@Override
	public void storeSubscription(String connectionName, SubscriptionData subscriptionData) throws IOException,
			PropertyException {
		MapServerConnection mapServerConnection = mConnectionPool.get(connectionName);
		Subscription subscription = new SubscriptionImpl(mapServerConnection, subscriptionData);
		mapServerConnection.addSubscription(subscription);
	}

	@Override
	public void updateSubscription(String connectionName, SubscriptionData newData) {
		mConnectionPool.get(connectionName).updateSubscription(newData);
	}

	@Override
	public MapServerConnection createConnection(String connectionName, String url, String userName, String userPassword)
			throws ConnectionException {
		if (doesConnectionExist(connectionName)) {
			throw new ConnectionException(connectionName + " connection already exists");
		} else {
			return new MapServerConnectionImpl(connectionName, url, userName, userPassword);
		}
	}

}
