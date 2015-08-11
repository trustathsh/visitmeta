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
 * This file is part of visitmeta-dataservice, version 0.5.1,
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

import de.hshannover.f4.trust.visitmeta.data.SubscriptionDataImpl;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.util.yaml.ConnectionsProperties;

public class SubscriptionImpl extends SubscriptionDataImpl implements Subscription {

	private MapServerConnection mMapServerConnection;

	public SubscriptionImpl(String subscriptionName, MapServerConnection mapServerConnection) {
		super(subscriptionName);
		mMapServerConnection = mapServerConnection;

		init();
	}

	public SubscriptionImpl(MapServerConnection mapServerConnection, SubscriptionData subscriptionData) {
		this(subscriptionData.getName(), mapServerConnection);

		super.setStartIdentifier(subscriptionData.getStartIdentifier());
		super.setIdentifierType(subscriptionData.getIdentifierType());
		super.setMatchLinksFilter(subscriptionData.getMatchLinksFilter());
		super.setResultFilter(subscriptionData.getResultFilter());
		super.setTerminalIdentifierTypes(subscriptionData.getTerminalIdentifierTypes());
		super.setStartupSubscribe(subscriptionData.isStartupSubscribe());
		super.setActive(subscriptionData.isActive());

		if (subscriptionData.getMaxSize() >= 0) {
			super.setMaxSize(subscriptionData.getMaxSize());
		}
		if (subscriptionData.getMaxDepth() >= 0) {
			super.setMaxDepth(subscriptionData.getMaxDepth());
		}
	}

	private void init() {
		// set default data
		super.setMaxDepth(ConnectionsProperties.DEFAULT_SUBSCRIPTION_MAX_DEPTH);
		super.setMaxSize(ConnectionsProperties.DEFAULT_MAX_POLL_RESULT_SIZE);
	}

	@Override
	public SubscriptionImpl copy() {
		SubscriptionData dataCopy = super.copy();
		SubscriptionImpl tmpCopy = new SubscriptionImpl(mMapServerConnection, dataCopy);
		return tmpCopy;
	}

	@Override
	public SubscriptionImpl clone() {
		return (SubscriptionImpl) super.clone();
	}

	@Override
	public void stopSubscription() throws ConnectionException {
		mMapServerConnection.subscribe(this, false);

		super.setActive(false);
	}

	@Override
	public void startSubscription() throws ConnectionException {
		mMapServerConnection.subscribe(this, true);
		super.setActive(true);
	}

}
