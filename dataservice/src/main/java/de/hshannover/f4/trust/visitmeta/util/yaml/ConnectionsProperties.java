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
package de.hshannover.f4.trust.visitmeta.util.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.ironcommon.util.ObjectChecks;
import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.SubscriptionImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.ifmap.ConnectionManager;
import de.hshannover.f4.trust.visitmeta.util.ConnectionKey;
import de.hshannover.f4.trust.visitmeta.util.SubscriptionKey;

public class ConnectionsProperties extends Properties {

	private static final Properties mCONFIG = Application.getConfig();

	// TODO the dataservice supports only basic authentication this value is not
	// in use
	public static final boolean DEFAULT_AUTHENTICATION_BASIC = true;

	public static final boolean DEFAULT_STARTUP_CONNECT = mCONFIG.getBoolean(
			"ifmap.defaultConnectionSettings.useConnectionAsStartup", false);

	public static final boolean DEFAULT_STARTUP_SUBSCRIPTION = mCONFIG
			.getBoolean(
					"ifmap.defaultConnectionSettings.useSubscriptionAsStartup",
					false);

	public static final String DEFAULT_TRUSTSTORE_PATH = mCONFIG.getString(
			"ifmap.defaultConnectionSettings.truststorePath", "/visitmeta.jks");

	public static final String DEFAULT_TRUSTSTORE_PASSWORD = mCONFIG.getString(
			"ifmap.defaultConnectionSettings.truststorePassword", "visitmeta");

	public static final int DEFAULT_MAX_POLL_RESULT_SIZE = mCONFIG.getInt(
			"ifmap.defaultConnectionSettings.maxPollResultSize", 1000000000);

	public static final int DEFAULT_SUBSCRIPTION_MAX_DEPTH = mCONFIG.getInt(
			"ifmap.defaultConnectionSettings.subscriptionMaxDepth", 1000);

	private ConnectionManager mManager;

	public ConnectionsProperties(ConnectionManager manager, String fileName)
			throws PropertyException {
		super(fileName);
		mManager = manager;
	}

	public MapServerConnection buildConnection(String connectionName)
			throws ConnectionException, PropertyException {
		// read values from property
		String ifmapServerUrl = getPropertyIfmapServerUrl(connectionName);
		String userName = getPropertyUserName(connectionName);
		String userPassword = getPropertyUserPassword(connectionName);

		String truststorePath = getPropertyTruststorePath(connectionName);
		String truststorePassword = getPropertyTruststorePassword(connectionName);
		boolean authenticationBasic = isPropertyAuthenticationBasic(connectionName);
		boolean startupConnect = isPropertyStartupConnect(connectionName);
		int maxPollResultSize = getPropertyMaxPollResultSize(connectionName);

		// build connection
		MapServerConnection newConnection = mManager.createConnection(connectionName, ifmapServerUrl, userName,
				userPassword);

		// build/set subscription list, if exists
		List<Subscription> subscribtionList = buildSubscribtion(newConnection);
		if (subscribtionList != null) {
			for (Subscription s : subscribtionList) {
				newConnection.addSubscription(s);
			}
		}

		// set other values
		newConnection.setTruststorePath(truststorePath);
		newConnection.setTruststorePassword(truststorePassword);
		newConnection.setAuthenticationBasic(authenticationBasic);
		newConnection.setStartupConnect(startupConnect);
		newConnection.setMaxPollResultSize(maxPollResultSize);

		return newConnection;
	}

	private List<Subscription> buildSubscribtion(MapServerConnection mapServerConnection) throws PropertyException {
		String connectionName = mapServerConnection.getConnectionName();

		// try to read SubscribeList from Connection, if not exists return null
		Map<String, String> propertySubscribeList;
		try {
			propertySubscribeList = (Map<String, String>) super.get(connectionName).getValue(
					ConnectionKey.SUBSCRIPTIONS);
		} catch (PropertyException e) {
			return null;
		}

		List<Subscription> subscribtionList = new ArrayList<Subscription>();

		// for all Subscriptions
		for (String subscribeName : propertySubscribeList.keySet()) {
			// build new Subscription
			Subscription subscribtion = new SubscriptionImpl(subscribeName, mapServerConnection);
			// set required values
			subscribtion.setStartIdentifier(getPropertySubscriptionStartIdentifier(
							connectionName, subscribeName));
			subscribtion
					.setIdentifierType(getPropertySubscriptionIdentifierType(
							connectionName, subscribeName));
			// set optional values
			subscribtion
					.setMatchLinksFilter(getPropertySubscriptionMatchLinksFilter(
							connectionName, subscribeName));
			subscribtion.setResultFilter(getPropertySubscriptionResultFilter(
					connectionName, subscribeName));
			subscribtion
					.setTerminalIdentifierTypes(getPropertySubscriptionTerminalIdentifierTypes(
							connectionName, subscribeName));
			subscribtion.setMaxDepth(getPropertySubscriptionMaxDepth(
					connectionName, subscribeName));
			subscribtion.setMaxSize(getPropertySubscriptionMaxSize(
					connectionName, subscribeName));
			subscribtion.setStartupSubscribe(getPropertySubscriptionStartup(
					connectionName, subscribeName));

			// add new Subscription
			subscribtionList.add(subscribtion);
		}

		return subscribtionList;
	}

	private void resetProperties() throws PropertyException {
		super.save(new HashMap<String, Object>());
	}

	public void persistConnections() throws PropertyException {
		resetProperties();

		Map<String, MapServerConnection> connectionMap = mManager.getSavedConnections();

		for (MapServerConnection connection : connectionMap.values()) {
			persistConnection(connection);
		}
	}

	private void persistConnection(MapServerConnection connection) throws PropertyException {
		// read values from Connection
		String name = connection.getConnectionName();
		String ifmapServerUrl = connection.getUrl();
		String userName = connection.getUserName();
		String userPassword = connection.getUserPassword();

		String truststorePath = connection.getTruststorePath();
		String truststorePassword = connection.getTruststorePassword();
		boolean authenticationBasic = connection.isAuthenticationBasic();
		boolean startupConnect = connection.doesConnectOnStartup();
		int maxPollResultSize = connection.getMaxPollResultSize();

		// set required values
		setPropertyUserName(name, userName);
		setPropertyuserPassword(name, userPassword);
		setPropertyIfmapServerUrl(name, ifmapServerUrl);

		// set Optional default values to connection, if value == default
		// nothing to set
		if (!ObjectChecks.equalsWithNullReferenceAllowed(truststorePath,
				DEFAULT_TRUSTSTORE_PATH)) {
			setPropertyTruststorePath(name, truststorePath);
		}
		if (!ObjectChecks.equalsWithNullReferenceAllowed(truststorePassword,
				DEFAULT_TRUSTSTORE_PASSWORD)) {
			setPropertyTruststorePassword(name, truststorePassword);
		}
		if (!ObjectChecks.equalsWithNullReferenceAllowed(authenticationBasic,
				DEFAULT_AUTHENTICATION_BASIC)) {
			setPropertyAuthenticationBasic(name, authenticationBasic);
		}
		if (!ObjectChecks.equalsWithNullReferenceAllowed(startupConnect,
				DEFAULT_STARTUP_CONNECT)) {
			setPropertyStartupConnect(name, startupConnect);
		}
		if (!ObjectChecks.equalsWithNullReferenceAllowed(maxPollResultSize,
				DEFAULT_MAX_POLL_RESULT_SIZE)) {
			setPropertyMaxPollResultSize(name, maxPollResultSize);
		}

		// set subscription
		persistSubscriptions(connection);
	}

	private void persistSubscriptions(MapServerConnection connection) throws PropertyException {
		String connectionName = connection.getConnectionName();

		// read Subscriptions from Connection
		List<SubscriptionData> subscribeList = connection.getSubscriptions();

		for (SubscriptionData subscriptionData : subscribeList) {
			Subscription subscribtion = (Subscription) subscriptionData;
			// read values from Subscription
			String subscriptionName = subscribtion.getName();
			String startIdentifier = subscribtion.getStartIdentifier();
			String identifierType = subscribtion.getIdentifierType();
			String matchLinksFilter = subscribtion.getMatchLinksFilter();
			String resultFilter = subscribtion.getResultFilter();
			String terminalIdentifierTypes = subscribtion.getTerminalIdentifierTypes();
			boolean startupSubscribe = subscribtion.isStartupSubscribe();
			int maxDepth = subscribtion.getMaxDepth();
			int maxSize = subscribtion.getMaxSize();

			setPropertySubscriptionStartIdentifier(connectionName, subscriptionName, startIdentifier);
			setPropertySubscriptionIdentifierType(connectionName, subscriptionName, identifierType);

			if (matchLinksFilter != null && !matchLinksFilter.isEmpty()) {
				setPropertySubscriptionMatchLinksFilter(connectionName, subscriptionName, matchLinksFilter);
			}
			if (resultFilter != null && !resultFilter.isEmpty()) {
				setPropertySubscriptionResultFilter(connectionName, subscriptionName, resultFilter);
			}
			if (terminalIdentifierTypes != null && !terminalIdentifierTypes.isEmpty()) {
				setPropertySubscriptionTerminalIdentifierTypes(connectionName, subscriptionName,
						terminalIdentifierTypes);
			}
			if (!ObjectChecks.equalsWithNullReferenceAllowed(startupSubscribe, DEFAULT_STARTUP_SUBSCRIPTION)) {
				setPropertySubscriptionStartup(connectionName, subscriptionName, startupSubscribe);
			}
			if (!ObjectChecks.equalsWithNullReferenceAllowed(maxDepth, DEFAULT_SUBSCRIPTION_MAX_DEPTH)) {
				setPropertySubscriptionMaxDepth(connectionName, subscriptionName, maxDepth);
			}
			if (!ObjectChecks.equalsWithNullReferenceAllowed(maxSize, DEFAULT_MAX_POLL_RESULT_SIZE)) {
				setPropertySubscriptionMaxSize(connectionName, subscriptionName, maxSize);
			}
		}
	}

	/**
	 * Get the sub-Properties(subscriptions) for connectionName.
	 *
	 * @param connectionName
	 *            property key
	 * @return Properties
	 * @throws PropertyException
	 */
	private Properties getPropertySubscribeList(String connectionName) throws PropertyException {
		return super.get(connectionName).get(ConnectionKey.SUBSCRIPTIONS);
	}

	private String getPropertyIfmapServerUrl(String connectionName) throws PropertyException {
		return super.get(connectionName).getString(ConnectionKey.IFMAP_SERVER_URL);
	}

	private void setPropertyIfmapServerUrl(String connectionName, String ifmapServerUrl) throws PropertyException {
		super.set(connectionName + "." + ConnectionKey.IFMAP_SERVER_URL, ifmapServerUrl);
	}

	private String getPropertyUserName(String connectionName) throws PropertyException {
		return super.get(connectionName).getString(ConnectionKey.USER_NAME);
	}

	private void setPropertyUserName(String connectionName, String userName) throws PropertyException {
		super.set(connectionName + "." + ConnectionKey.USER_NAME, userName);
	}

	private String getPropertyUserPassword(String connectionName) throws PropertyException {
		return super.get(connectionName).getString(ConnectionKey.USER_PASSWORD);
	}

	private void setPropertyuserPassword(String connectionName, String userPassword) throws PropertyException {
		super.set(connectionName + "." + ConnectionKey.USER_PASSWORD,
				userPassword);
	}

	private String getPropertyTruststorePath(String connectionName) throws PropertyException {
		return super.get(connectionName).getString(
				ConnectionKey.TRUSTSTORE_PATH, DEFAULT_TRUSTSTORE_PATH);
	}

	private void setPropertyTruststorePath(String connectionName, String truststorePath) throws PropertyException {
		super.set(connectionName + "." + ConnectionKey.TRUSTSTORE_PATH,
				truststorePath);
	}

	private String getPropertyTruststorePassword(String connectionName) throws PropertyException {
		return super.get(connectionName).getString(
				ConnectionKey.TRUSTSTORE_PASSWORD, DEFAULT_TRUSTSTORE_PASSWORD);
	}

	private void setPropertyTruststorePassword(String connectionName, String truststorePassword)
			throws PropertyException {
		super.set(connectionName + "." + ConnectionKey.TRUSTSTORE_PASSWORD,
				truststorePassword);
	}

	private boolean isPropertyAuthenticationBasic(String connectionName) throws PropertyException {
		return super.get(connectionName).getBoolean(
				ConnectionKey.AUTHENTICATION_BASIC,
				DEFAULT_AUTHENTICATION_BASIC);
	}

	private void setPropertyAuthenticationBasic(String connectionName, boolean authenticationBasic)
			throws PropertyException {
		super.set(connectionName + "." + ConnectionKey.AUTHENTICATION_BASIC,
				authenticationBasic);
	}

	private boolean isPropertyStartupConnect(String connectionName) throws PropertyException {
		return super.get(connectionName).getBoolean(
				ConnectionKey.USE_CONNECTION_AS_STARTUP,
				DEFAULT_STARTUP_CONNECT);
	}

	private void setPropertyStartupConnect(String connectionName, boolean startupConnect) throws PropertyException {
		super.set(connectionName + "."
				+ ConnectionKey.USE_CONNECTION_AS_STARTUP, startupConnect);
	}

	private int getPropertyMaxPollResultSize(String connectionName) throws PropertyException {
		return super.get(connectionName).getInt(
				ConnectionKey.MAX_POLL_RESULT_SIZE,
				DEFAULT_MAX_POLL_RESULT_SIZE);
	}

	private void setPropertyMaxPollResultSize(String connectionName, int maxPollResultSize) throws PropertyException {
		super.set(connectionName + "." + ConnectionKey.MAX_POLL_RESULT_SIZE,
				maxPollResultSize);
	}

	private String getPropertySubscriptionStartIdentifier(String connectionName, String subscriptionName)
			throws PropertyException {
		return getPropertySubscribeList(connectionName).get(subscriptionName).getString(
				SubscriptionKey.START_IDENTIFIER);
	}

	private void setPropertySubscriptionStartIdentifier(String connectionName, String subscriptionName,
			String startIdentifier) throws PropertyException {
		set(connectionName + "." + ConnectionKey.SUBSCRIPTIONS + "." + subscriptionName + "."
				+ SubscriptionKey.START_IDENTIFIER, startIdentifier);
	}

	private String getPropertySubscriptionIdentifierType(String connectionName, String subscriptionName)
			throws PropertyException {
		return getPropertySubscribeList(connectionName).get(subscriptionName)
				.getString(SubscriptionKey.IDENTIFIER_TYPE);
	}

	private void setPropertySubscriptionIdentifierType(String connectionName, String subscriptionName,
			String identifierType) throws PropertyException {
		set(connectionName + "." + ConnectionKey.SUBSCRIPTIONS + "." + subscriptionName + "."
				+ SubscriptionKey.IDENTIFIER_TYPE, identifierType);
	}

	private String getPropertySubscriptionMatchLinksFilter(String connectionName, String subscriptionName)
			throws PropertyException {
		return getPropertySubscribeList(connectionName).get(subscriptionName).getString(
				SubscriptionKey.MATCH_LINKS_FILTER, null);
	}

	private void setPropertySubscriptionMatchLinksFilter(String connectionName, String subscriptionName,
			String matchLinksFilter) throws PropertyException {
		set(connectionName + "." + ConnectionKey.SUBSCRIPTIONS + "." + subscriptionName + "."
				+ SubscriptionKey.MATCH_LINKS_FILTER, matchLinksFilter);
	}

	private String getPropertySubscriptionResultFilter(String connectionName,
			String subscriptionName) throws PropertyException {
		return getPropertySubscribeList(connectionName).get(subscriptionName)
				.getString(SubscriptionKey.RESULT_FILTER, null);
	}

	private void setPropertySubscriptionResultFilter(String connectionName,
			String subscriptionName, String resultFilter)
			throws PropertyException {
		set(connectionName + "." + ConnectionKey.SUBSCRIPTIONS + "."
				+ subscriptionName + "." + SubscriptionKey.RESULT_FILTER,
				resultFilter);
	}

	private String getPropertySubscriptionTerminalIdentifierTypes(
			String connectionName, String subscriptionName)
			throws PropertyException {
		return getPropertySubscribeList(connectionName).get(subscriptionName)
				.getString(SubscriptionKey.TERMINAL_IDENTIFIER_TYPES, null);
	}

	private void setPropertySubscriptionTerminalIdentifierTypes(
			String connectionName, String subscriptionName,
			String terminalIdentifierTypes) throws PropertyException {
		set(connectionName + "." + ConnectionKey.SUBSCRIPTIONS + "."
				+ subscriptionName + "."
				+ SubscriptionKey.TERMINAL_IDENTIFIER_TYPES,
				terminalIdentifierTypes);
	}

	private int getPropertySubscriptionMaxDepth(String connectionName,
			String subscriptionName) throws PropertyException {
		return getPropertySubscribeList(connectionName).get(subscriptionName)
				.getInt(SubscriptionKey.MAX_DEPTH,
						DEFAULT_SUBSCRIPTION_MAX_DEPTH);
	}

	private void setPropertySubscriptionMaxDepth(String connectionName,
			String subscriptionName, int maxDepth) throws PropertyException {
		set(connectionName + "." + ConnectionKey.SUBSCRIPTIONS + "."
				+ subscriptionName + "." + SubscriptionKey.MAX_DEPTH, maxDepth);
	}

	private int getPropertySubscriptionMaxSize(String connectionName,
			String subscriptionName) throws PropertyException {
		return getPropertySubscribeList(connectionName).get(subscriptionName)
				.getInt(SubscriptionKey.MAX_SIZE, DEFAULT_MAX_POLL_RESULT_SIZE);
	}

	private void setPropertySubscriptionMaxSize(String connectionName,
			String subscriptionName, int maxSize) throws PropertyException {
		set(connectionName + "." + ConnectionKey.SUBSCRIPTIONS + "."
				+ subscriptionName + "." + SubscriptionKey.MAX_SIZE, maxSize);
	}

	private boolean getPropertySubscriptionStartup(String connectionName,
			String subscriptionName) throws PropertyException {
		return getPropertySubscribeList(connectionName).get(subscriptionName)
				.getBoolean(SubscriptionKey.USE_SUBSCRIPTION_AS_STARTUP,
						DEFAULT_STARTUP_SUBSCRIPTION);
	}

	private void setPropertySubscriptionStartup(String connectionName,
			String subscriptionName, boolean useSubscriptionAsStartup)
			throws PropertyException {
		set(connectionName + "." + ConnectionKey.SUBSCRIPTIONS + "."
				+ subscriptionName + "."
				+ SubscriptionKey.USE_SUBSCRIPTION_AS_STARTUP,
				useSubscriptionAsStartup);
	}
}
