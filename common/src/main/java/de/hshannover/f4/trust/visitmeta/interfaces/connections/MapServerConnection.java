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
 * This file is part of visitmeta-common, version 0.4.2,
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
package de.hshannover.f4.trust.visitmeta.interfaces.connections;

import de.hshannover.f4.trust.ifmapj.channel.SSRC;
import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.data.MapServerData;

/**
 * A interface for Connections within the dataservice.
 *
 * @author Bastian Hellmann
 * @author Marcel Reichenbach
 */
public interface MapServerConnection extends Connection, MapServerData {

	/**
	 * Start a subscription.
	 *
	 * @param subscriptionName the subscription name.
	 * 
	 * @throws ConnectionException
	 */
	public void startSubscription(String subscriptionName) throws ConnectionException;

	/**
	 * Stop a subscription.
	 *
	 * @param subscriptionName the subscription name.
	 * 
	 * @throws ConnectionException
	 */
	public void stopSubscription(String subscriptionName) throws ConnectionException;

	/**
	 * Stop all subscriptions.
	 *
	 * @throws ConnectionException
	 */
	public void stopAllSubscriptions() throws ConnectionException;

	/**
	 * Send a {@link SubscribeRequest} to the MAP server.
	 *
	 * @param subscription
	 * 
	 * @throws ConnectionException
	 */
	public void subscribe(Subscription subscription, boolean update) throws ConnectionException;

	/**
	 * Returns the {@link GraphService} associated with this {@link MapServerConnection}.
	 *
	 * @return a {@link GraphService} instance
	 */
	public GraphService getGraphService();
	
	/**
	 * Returns the publisher id of the {@link SSRC} associated with this {@link MapServerConnection}.
	 *
	 * @return The publisher id of the {@link SSRC}
	 * 
	 * @throws ConnectionException
	 */
	public String getPublisherId() throws ConnectionException;

	/**
	 * Returns the session id of the {@link SSRC} associated with this {@link MapServerConnection}.
	 *
	 * @return The session id of the SSRC
	 * 
	 * @throws ConnectionException
	 */
	public String getSessionId() throws ConnectionException;
	
	/**
	 * Sends a poll-request over the ARC and waits for the {@link PollResult}.
	 *
	 * @return PollResult the ifmapj {@link PollResult} to the poll-call
	 * 
	 * @throws ConnectionException
	 */
	public PollResult poll() throws ConnectionException;

	@Override
	public MapServerConnection copy();

	@Override
	public MapServerConnection clone();
}
