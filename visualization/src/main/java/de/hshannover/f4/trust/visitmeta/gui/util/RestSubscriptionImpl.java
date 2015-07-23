package de.hshannover.f4.trust.visitmeta.gui.util;

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
 * This file is part of visitmeta-visualization, version 0.4.2,
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


import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.data.SubscriptionDataImpl;
import de.hshannover.f4.trust.visitmeta.exceptions.RESTException;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.data.SubscriptionData;

public class RestSubscriptionImpl extends SubscriptionDataImpl implements Subscription {

	private static final Logger LOGGER = Logger.getLogger(RestSubscriptionImpl.class);

	private MapServerRestConnectionImpl mMapServerConnection;

	private SubscriptionData mOldData;

	private boolean mNotPersised;

	public RestSubscriptionImpl(String subscriptionName, MapServerRestConnectionImpl mapServerConnection) {
		super(subscriptionName);
		mMapServerConnection = mapServerConnection;
	}

	public RestSubscriptionImpl(MapServerRestConnectionImpl mapServerConnection, SubscriptionData subscriptionData) {
		this(subscriptionData.getName(), mapServerConnection);

		super.setStartIdentifier(subscriptionData.getStartIdentifier());
		super.setIdentifierType(subscriptionData.getIdentifierType());
		super.setMatchLinksFilter(subscriptionData.getMatchLinksFilter());
		super.setResultFilter(subscriptionData.getResultFilter());
		super.setTerminalIdentifierTypes(subscriptionData.getTerminalIdentifierTypes());
		super.setStartupSubscribe(subscriptionData.isStartupSubscribe());
		super.setMaxDepth(subscriptionData.getMaxDepth());
		super.setMaxSize(subscriptionData.getMaxSize());
		super.setActive(subscriptionData.isActive());

	}

	@Override
	public RestSubscriptionImpl copy() {
		SubscriptionData dataCopy = super.copy();
		RestSubscriptionImpl tmpCopy = new RestSubscriptionImpl(mMapServerConnection, dataCopy);
		tmpCopy.setNotPersised(true);
		return tmpCopy;
	}

	@Override
	public RestSubscriptionImpl clone() {
		return (RestSubscriptionImpl) super.clone();
	}

	@Override
	public void stopSubscription() {
		LOGGER.trace("Method stopSubscription() called.");
		try {

			RestHelper.stopSubscription(mMapServerConnection.getDataserviceConnection(),
					mMapServerConnection.getConnectionName(), super.getName());
			super.setActive(false);

		} catch (RESTException e) {
			LOGGER.error(e.toString());
		}
	}

	@Override
	public void startSubscription() {
		LOGGER.trace("Method startSubscription() called.");
		try {

			RestHelper.startSubscription(mMapServerConnection.getDataserviceConnection(),
					mMapServerConnection.getConnectionName(), super.getName());
			super.setActive(true);

		} catch (RESTException e) {
			LOGGER.error(e.toString());
		}
	}

	public void resetData(){
		if(mOldData != null){
			super.changeData(mOldData);
			mOldData = null;
		}
	}

	public boolean isNotPersised() {
		return mNotPersised;
	}

	public void setNotPersised(boolean b) {
		mNotPersised = b;
	}

	public void setOldData(SubscriptionData oldData) {
		mOldData = oldData;
	}

	public SubscriptionData getOldData() {
		return mOldData;
	}
}
