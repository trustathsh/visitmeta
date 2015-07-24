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
 * This file is part of visitmeta-common, version 0.5.0,
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
package de.hshannover.f4.trust.visitmeta.interfaces.data;

import java.util.List;

import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;

public interface MapServerData extends Data {

	public String getConnectionName();

	public void setConnectionName(String connectionName);

	public String getUrl();

	public void setUrl(String url);

	public String getUserName();

	public void setUserName(String userName);

	public String getUserPassword();

	public void setUserPassword(String userPassword);

	public String getTruststorePath();

	public void setTruststorePath(String truststorePath);

	public String getTruststorePassword();

	public void setTruststorePassword(String truststorePass);

	/**
	 * Adds a {@link SubscriptionData} to the internal list of subscriptions-datas for this {@link MapServerData}.
	 *
	 * @param subscriptionData
	 */
	public void addSubscription(SubscriptionData subscriptionData);

	/**
	 * Delete a subscription.
	 *
	 * @param subscriptionName the subscription name.
	 */
	public void deleteSubscription(String subscriptionName);

	/**
	 * Delete all subscriptions.
	 */
	public void deleteAllSubscriptions();

	/**
	 * Returns all active subscriptions.
	 *
	 * @return a {@link List} with all active subscriptions.
	 */
	public List<SubscriptionData> getActiveSubscriptions();

	/**
	 * Returns a {@link List} of {@link Subscription} representing all subscriptions for this
	 * {@link MapServerData}.
	 *
	 * @return all subscriptions as a {@link List} of {@link Subscription}.
	 */
	public List<SubscriptionData> getSubscriptions();

	public void setSubscriptionData(List<SubscriptionData> connection);

	public int getMaxPollResultSize();

	public void setMaxPollResultSize(int maxPollResultSize);

	public void setConnected(boolean connected);

	public boolean isConnected();

	/**
	 * Set the flag for connecting on startup for this {@link MapServerData}.
	 *
	 * @param startupConnect connect on startup boolean
	 */
	public void setStartupConnect(boolean startupConnect);

	/**
	 * Returns if the flag for connecting on startup is set for this {@link MapServerData}.
	 *
	 * @return true, if this {@link MapServerData} shall connect on startup
	 */
	public boolean doesConnectOnStartup();

	public void setAuthenticationBasic(boolean authenticationBasic);

	public boolean isAuthenticationBasic();

	/**
	 * Only the connection properties. For Subscriptions use updateSubscription().
	 * 
	 * @param newData
	 */
	public void changeData(MapServerData newData);

	public void updateSubscription(SubscriptionData newData);

	@Override
	public MapServerData copy();

	@Override
	public MapServerData clone();
}
