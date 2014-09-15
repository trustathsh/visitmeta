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
 * This file is part of visitmeta-common, version 0.1.2,
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
package de.hshannover.f4.trust.visitmeta.interfaces.ifmap;

import java.util.List;
import java.util.Set;

import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.ifmapj.channel.SSRC;
import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.NotConnectedException;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;

/**
 * A interface for Connections within the dataservice.
 * 
 * @author Bastian Hellmann
 * @author Marcel Reichenbach
 */
public interface Connection {

	/**
	 * Adds a subscription to the internal list of subscriptions for this
	 * {@link Connection}.
	 * 
	 * @param subscription
	 *            a subscription
	 */
	public void addSubscription(JSONObject subscription);

	/**
	 * Connects this {@link Connection} to a MAP server.
	 * 
	 * @throws ConnectionException
	 */
	public void connect() throws ConnectionException;

	/**
	 * Delete a subscription.
	 * 
	 * @param subscriptionName
	 *            the subscription name.
	 * @throws ConnectionException
	 */
	public void deleteSubscription(String subscriptionName)
			throws ConnectionException;

	/**
	 * Delete all active subscriptions.
	 * 
	 * @throws ConnectionException
	 */
	public void deleteAllSubscriptions() throws ConnectionException;

	/**
	 * Disconnects the connection to a MAP server.
	 * 
	 * @throws ConnectionException
	 */
	public void disconnect() throws ConnectionException;

	/**
	 * Returns all active subscriptions.
	 * 
	 * @return a {@link Set} with all active subscriptions.
	 * @throws NotConnectedException
	 */
	public Set<String> getActiveSubscriptions() throws ConnectionException;

	/**
	 * Returns the name of this {@link Connection}.
	 * 
	 * @return name of this {@link Connection}
	 */
	public String getConnectionName();

	/**
	 * Returns the {@link GraphService} associated with this {@link Connection}.
	 * 
	 * @return a {@link GraphService} instance
	 */
	public GraphService getGraphService();

	/**
	 * Returns the maximum poll result size of this {@link Connection}.
	 * 
	 * @return the maximum poll result size
	 */
	public int getMaxPollResultSize();

	/**
	 * Returns the publisher id of the {@link SSRC} associated with this
	 * {@link Connection}.
	 * 
	 * @return The publisher id of the {@link SSRC}
	 * @throws ConnectionException
	 */
	String getPublisherId() throws ConnectionException;

	/**
	 * Returns the session id of the {@link SSRC} associated with this
	 * {@link Connection}.
	 * 
	 * @return The session id of the SSRC
	 * @throws ConnectionException
	 */
	String getSessionId() throws ConnectionException;

	/**
	 * Returns a {@link List} of {@link JSONObject} representing all
	 * subscriptions for this {@link Connection}.
	 * 
	 * @return all subscriptions as a {@link List} of {@link JSONObject}.
	 */
	public List<JSONObject> getSubscriptions();

	/**
	 * Returns the password to the truststore.
	 * 
	 * @return password to the truststore
	 */
	public String getTruststorePassword();

	/**
	 * Returns the path to the truststore.
	 * 
	 * @return path to the truststore
	 */
	public String getTruststorePath();

	/**
	 * Returns the URL of the MAP server.
	 * 
	 * @return the URL of the MAP server
	 */
	public String getUrl();

	/**
	 * Returns the username for this {@link Connection}.
	 * 
	 * @return the username for this {@link Connection}.
	 */
	public String getUserName();

	/**
	 * Returns the password of the user for this {@link Connection}.
	 * 
	 * @return the password of the user for this {@link Connection}.
	 */
	public String getUserPassword();

	/**
	 * Returns if this {@link Connection} uses IF-MAP basic authentication.
	 * 
	 * @return true, if this {@link Connection} uses IF-MAP basic authentication
	 */
	public boolean isAuthenticationBasic();

	/**
	 * Returns if this {@link Connection} is connected to a MAP server.
	 * 
	 * @return true, when the connection to a MAP server is establised and
	 *         active.
	 */
	public boolean isConnected();

	/**
	 * Returns if the flag for connecting on startup is set for this
	 * {@link Connection}.
	 * 
	 * @return true, if this {@link Connection} shall connect on startup
	 */
	public boolean doesConnectOnStartup();

	/**
	 * Sends a poll-request over the ARC and waits for the {@link PollResult}.
	 * 
	 * @return PollResult the ifmapj {@link PollResult} to the poll-call
	 * @throws ConnectionException
	 */
	public de.hshannover.f4.trust.ifmapj.messages.PollResult poll()
			throws ConnectionException;

	public void setAuthenticationBasic(boolean authenticationBasic);

	public void setMaxPollResultSize(int maxPollResultSize);

	public void setStartupConnect(boolean startupConnect);

	public void setTruststorePass(String truststorePass);

	public void setTruststorePath(String truststorePath);

	/**
	 * Send a {@link SubscribeRequest} to the MAP server.
	 * 
	 * @param request
	 * @throws ConnectionException
	 */
	public void subscribe(SubscribeRequest request) throws ConnectionException;

}
