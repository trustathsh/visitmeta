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
 * This file is part of visitmeta dataservice, version 0.1.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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
import java.util.List;
import java.util.Map;

import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.SubscriptionImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.interfaces.ifmap.ConnectionManager;
import de.hshannover.f4.trust.visitmeta.util.Same;
import de.hshannover.f4.trust.visitmeta.util.properties.Properties;
import de.hshannover.f4.trust.visitmeta.util.properties.PropertyException;

public class ConnectionsProperties extends Properties{
	
	private static final Properties mCONFIG = Application.getConfig();

	// TODO the dataservice supports only basic authentication this value is not in use
	public static final boolean DEFAULT_AUTHENTICATION_BASIC = true;

	public static final boolean DEFAULT_STARTUP_CONNECT = mCONFIG.getBoolean("ifmap.connection.useConnectionAsStartup", false);

	public static final boolean DEFAULT_STARTUP_SUBSCRIPTION = mCONFIG.getBoolean("ifmap.connection.useSubscriptionAsStartup", false);

	public static final String DEFAULT_TRUSTSTORE_PATH = mCONFIG.getString("ifmap.connection.truststore.path", "/visitmeta.jks");

	public static final String DEFAULT_TRUSTSTORE_PASS = mCONFIG.getString("ifmap.connection.truststore.pw", "visitmeta");

	public static final int DEFAULT_MAX_POLL_RESULT_SIZE = mCONFIG.getInt("ifmap.connection.maxPollResultSize", 1000000000);

	public static final int DEFAULT_SUBSCRIPTION_MAX_DEPTH = mCONFIG.getInt("ifmap.connection.subscriptionMaxDepth", 1000);

	private ConnectionManager mManager;

	public ConnectionsProperties(ConnectionManager manager, String fileName) throws PropertyException {
		super(fileName);
		mManager = manager;
	}

	public Connection buildConnection(String connectionName) throws ConnectionException, PropertyException{
		// read values from property
		String ifmapServerUrl = getPropertyIfmapServerUrl(connectionName);
		String userName = getPropertyUserName(connectionName);
		String userPass = getPropertyUserPass(connectionName);

		String truststorePath = getPropertyTruststorePath(connectionName);
		String truststorePass = getPropertyTruststorePass(connectionName);
		boolean authenticationBasic = isPropertyAuthenticationBasic(connectionName);
		boolean startupConnect = isPropertyStartupConnect(connectionName);
		int maxPollResultSize = getPropertyMaxPollResultSize(connectionName);

		// build connection
		Connection newConnection = mManager.createConnection(connectionName, ifmapServerUrl, userName, userPass);

		// build/set subscription list, if exists
		List<Subscription> subscribtionList = buildSubscribtion(connectionName);
		if (subscribtionList != null) {
			for (Subscription s : subscribtionList) {
				newConnection.addSubscription(s);
			}
		}

		// set other values
		newConnection.setTruststorePath(truststorePath);
		newConnection.setTruststorePass(truststorePass);
		newConnection.setAuthenticationBasic(authenticationBasic);
		newConnection.setStartupConnect(startupConnect);
		newConnection.setMaxPollResultSize(maxPollResultSize);

		return newConnection;
	}

	private List<Subscription> buildSubscribtion(String connectionName) throws PropertyException {
		// try to read SubscribeList from Connection, if not exists return null
		Properties propertySubscribeList;
		try {
			propertySubscribeList = getPropertySubscribeList(connectionName);
		} catch (PropertyException e){
			return null;
		}

		List<Subscription> subscribtionList = new ArrayList<Subscription>();

		// for all Subscriptions
		for(String subscribeName: propertySubscribeList.getKeySet()){
			// build new Subscription
			Subscription subscribtion = new SubscriptionImpl();
			// set required values
			subscribtion.setName(subscribeName);
			subscribtion.setIdentifier(getPropertySubscriptionIdentifier(connectionName, subscribeName));
			subscribtion.setIdentifierType(getPropertySubscriptionIdentifierType(connectionName, subscribeName));
			// set optional values
			subscribtion.setFilterLinks(getPropertySubscriptionFilterLinks(connectionName, subscribeName));
			subscribtion.setFilterResult(getPropertySubscriptionFilterResult(connectionName, subscribeName));
			subscribtion.setTerminalIdentifierTypes(getPropertySubscriptionTerminalIdentifierTypes(connectionName, subscribeName));
			subscribtion.setMaxDepth(getPropertySubscriptionMaxDepth(connectionName, subscribeName));
			subscribtion.setMaxSize(getPropertySubscriptionMaxSize(connectionName, subscribeName));
			subscribtion.setStartupSubscribe(getPropertySubscriptionStartupSubscribe(connectionName, subscribeName));

			// add new Subscription
			subscribtionList.add(subscribtion);
		}

		return subscribtionList;
	}

	public void persistConnections() throws PropertyException {
		Map<String, Connection> connectionMap = mManager.getSavedConnections();

		for(Connection connection: connectionMap.values()){
			persistConnection(connection);
		}
	}

	private void persistConnection(Connection connection) throws PropertyException{
		// read values from Connection
		String name = connection.getConnectionName();
		String ifmapServerUrl = connection.getIfmapServerUrl();
		String userName = connection.getUserName();
		String userPass = connection.getUserPassword();

		String truststorePath = connection.getTruststorePath();
		String truststorePass = connection.getTruststorePassword();
		boolean authenticationBasic = connection.isAuthenticationBasic();
		boolean startupConnect = connection.doesConnectOnStartup();
		int maxPollResultSize = connection.getMaxPollResultSize();

		// set required values
		setPropertyUrl(name, ifmapServerUrl);
		setPropertyUserName(name, userName);
		setPropertyUserPass(name, userPass);

		// set Optional default values to connection, if value == default nothing to set
		if(!Same.check(truststorePath, DEFAULT_TRUSTSTORE_PATH)){
			setPropertyTruststorePath(name, truststorePath);
		}
		if(!Same.check(truststorePass, DEFAULT_TRUSTSTORE_PASS)){
			setPropertyTruststorePass(name, truststorePass);
		}
		if(!Same.check(authenticationBasic, DEFAULT_AUTHENTICATION_BASIC)){
			setPropertyAuthenticationBasic(name, authenticationBasic);
		}
		if(!Same.check(startupConnect, DEFAULT_STARTUP_CONNECT)){
			setPropertyStartupConnect(name, startupConnect);
		}
		if(!Same.check(maxPollResultSize, DEFAULT_MAX_POLL_RESULT_SIZE)){
			setPropertyMaxPollResultSize(name, maxPollResultSize);
		}

		// set subscription
		persistSubscriptions(connection);
	}

	private void persistSubscriptions(Connection connection) throws PropertyException{
		String connectionName = connection.getConnectionName();

		// read Subscriptions from Connection
		List<Subscription> subscribeList = connection.getSubscriptions();

		for(Subscription subscription: subscribeList){
			// read values from Subscription
			String subscriptionName = subscription.getName();
			String identifier = subscription.getIdentifier();
			String identifierType = subscription.getIdentifierType();
			String filterLinks = subscription.getFilterLinks();
			String filterResult = subscription.getFilterResult();
			String terminalIdentifierTypes = subscription.getTerminalIdentifierTypes();
			boolean startupSubscribe = subscription.isStartupSubscribe();
			int maxDepth = subscription.getMaxDepth();
			int maxSize = subscription.getMaxSize();

			setPropertySubscriptionIdentifier(connectionName, subscriptionName, identifier);
			setPropertySubscriptionIdentifierType(connectionName, subscriptionName, identifierType);

			if(filterLinks != null && !filterLinks.isEmpty()){
				setPropertySubscriptionFilterLinks(connectionName, subscriptionName, filterLinks);
			}
			if(filterResult != null){
				setPropertySubscriptionFilterResult(connectionName, subscriptionName, filterResult);
			}
			if(terminalIdentifierTypes != null){
				setPropertySubscriptionTerminalIdentifierTypes(connectionName, subscriptionName, terminalIdentifierTypes);
			}
			if(!Same.check(startupSubscribe, DEFAULT_STARTUP_SUBSCRIPTION)){
				setPropertySubscriptionStartupSubscribe(connectionName, subscriptionName, startupSubscribe);
			}
			if(!Same.check(maxDepth, DEFAULT_SUBSCRIPTION_MAX_DEPTH)){
				setPropertySubscriptionMaxDepth(connectionName, subscriptionName, maxDepth);
			}
			if(!Same.check(maxSize, DEFAULT_MAX_POLL_RESULT_SIZE)){
				setPropertySubscriptionMaxSize(connectionName, subscriptionName, maxSize);
			}
		}
	}
	
	/**
	 * Get the sub-Properties(subscrptions) for connectionName.
	 * @param connectionName property key
	 * @return Properties
	 * @throws PropertyException
	 */
	private Properties getPropertySubscribeList(String connectionName) throws PropertyException {
		return super.get(connectionName).get("subscrptions");
	}

	private String getPropertyIfmapServerUrl(String connectionName) throws PropertyException{
		return super.get(connectionName).getString("ifmapServerUrl");
	}

	private void setPropertyUrl(String connectionName, String ifmapServerUrl) throws PropertyException{
		super.set(connectionName + ".ifmapServerUrl", ifmapServerUrl);
	}

	private String getPropertyUserName(String connectionName) throws PropertyException{
		return super.get(connectionName).getString("userName");
	}

	private void setPropertyUserName(String connectionName, String userName) throws PropertyException{
		super.set(connectionName + ".userName", userName);
	}

	private String getPropertyUserPass(String connectionName) throws PropertyException{
		return super.get(connectionName).getString("userPass");
	}

	private void setPropertyUserPass(String connectionName, String userPass) throws PropertyException{
		super.set(connectionName + ".userPass", userPass);
	}

	private String getPropertyTruststorePath(String connectionName) throws PropertyException{
		return super.get(connectionName).getString("truststorePath", DEFAULT_TRUSTSTORE_PATH);
	}

	private void setPropertyTruststorePath(String connectionName, String truststorePath) throws PropertyException{
		super.set(connectionName + ".truststorePath", truststorePath);
	}

	private String getPropertyTruststorePass(String connectionName) throws PropertyException{
		return super.get(connectionName).getString("truststorePass", DEFAULT_TRUSTSTORE_PASS);
	}

	private void setPropertyTruststorePass(String connectionName, String truststorePass) throws PropertyException{
		super.set(connectionName + ".truststorePass", truststorePass);
	}

	private boolean isPropertyAuthenticationBasic(String connectionName) throws PropertyException{
		return super.get(connectionName).getBoolean("authenticationBasic", DEFAULT_AUTHENTICATION_BASIC);
	}

	private void setPropertyAuthenticationBasic(String connectionName, boolean authenticationBasic) throws PropertyException{
		super.set(connectionName + ".authenticationBasic", authenticationBasic);
	}

	private boolean isPropertyStartupConnect(String connectionName) throws PropertyException{
		return super.get(connectionName).getBoolean("useConnectionAsStartup", DEFAULT_STARTUP_CONNECT);
	}

	private void setPropertyStartupConnect(String connectionName, boolean startupConnect) throws PropertyException{
		super.set(connectionName + ".useConnectionAsStartup", startupConnect);
	}

	private int getPropertyMaxPollResultSize(String connectionName) throws PropertyException{
		return super.get(connectionName).getInt("maxPollResultSize", DEFAULT_MAX_POLL_RESULT_SIZE);
	}

	private void setPropertyMaxPollResultSize(String connectionName, int maxPollResultSize) throws PropertyException{
		super.set(connectionName + ".maxPollResultSize", maxPollResultSize);
	}

	private String getPropertySubscriptionIdentifier(String connectionName, String subscriptionName) throws PropertyException{
		return getPropertySubscribeList(connectionName).get(subscriptionName).getString("identifier");
	}

	private void setPropertySubscriptionIdentifier(String connectionName, String subscriptionName, String identifier) throws PropertyException{
		set(connectionName + ".subscrptions." + subscriptionName + ".identifier", identifier);
	}

	private String getPropertySubscriptionIdentifierType(String connectionName, String subscriptionName) throws PropertyException{
		return getPropertySubscribeList(connectionName).get(subscriptionName).getString("identifierType");
	}

	private void setPropertySubscriptionIdentifierType(String connectionName, String subscriptionName, String identifierType) throws PropertyException{
		set(connectionName + ".subscrptions." + subscriptionName + ".identifierType", identifierType);
	}

	private String getPropertySubscriptionFilterLinks(String connectionName, String subscriptionName) throws PropertyException{
		return getPropertySubscribeList(connectionName).get(subscriptionName).getString("filterLinks", null);
	}

	private void setPropertySubscriptionFilterLinks(String connectionName, String subscriptionName, String filterLinks) throws PropertyException{
		set(connectionName + ".subscrptions." + subscriptionName + ".filterLinks", filterLinks);
	}

	private String getPropertySubscriptionFilterResult(String connectionName, String subscriptionName) throws PropertyException{
		return getPropertySubscribeList(connectionName).get(subscriptionName).getString("filterResult", null);
	}

	private void setPropertySubscriptionFilterResult(String connectionName, String subscriptionName, String filterResult) throws PropertyException{
		set(connectionName + ".subscrptions." + subscriptionName + ".filterResult", filterResult);
	}

	private String getPropertySubscriptionTerminalIdentifierTypes(String connectionName, String subscriptionName) throws PropertyException{
		return getPropertySubscribeList(connectionName).get(subscriptionName).getString("terminalIdentifierTypes", null);
	}

	private void setPropertySubscriptionTerminalIdentifierTypes(String connectionName, String subscriptionName, String terminalIdentifierTypes) throws PropertyException{
		set(connectionName + ".subscrptions." + subscriptionName + ".terminalIdentifierTypes", terminalIdentifierTypes);
	}

	private int getPropertySubscriptionMaxDepth(String connectionName, String subscriptionName) throws PropertyException{
		return getPropertySubscribeList(connectionName).get(subscriptionName).getInt("maxDepth", DEFAULT_SUBSCRIPTION_MAX_DEPTH);
	}

	private void setPropertySubscriptionMaxDepth(String connectionName, String subscriptionName, int maxDepth) throws PropertyException{
		set(connectionName + ".subscrptions." + subscriptionName + ".maxDepth", maxDepth);
	}

	private int getPropertySubscriptionMaxSize(String connectionName, String subscriptionName) throws PropertyException{
		return getPropertySubscribeList(connectionName).get(subscriptionName).getInt("maxSize", DEFAULT_MAX_POLL_RESULT_SIZE);
	}

	private void setPropertySubscriptionMaxSize(String connectionName, String subscriptionName, int maxSize) throws PropertyException{
		set(connectionName + ".subscrptions." + subscriptionName + ".maxSize", maxSize);
	}

	private boolean getPropertySubscriptionStartupSubscribe(String connectionName, String subscriptionName) throws PropertyException{
		return getPropertySubscribeList(connectionName).get(subscriptionName).getBoolean("useSubscriptionAsStartup", DEFAULT_STARTUP_SUBSCRIPTION);
	}

	private void setPropertySubscriptionStartupSubscribe(String connectionName, String subscriptionName, boolean startupSubscribe) throws PropertyException{
		set(connectionName + ".subscrptions." + subscriptionName + ".startupSubscribe", startupSubscribe);
	}
}
