package de.hshannover.f4.trust.visitmeta.gui.util;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.exceptions.RESTException;
import de.hshannover.f4.trust.visitmeta.ifmap.SubscriptionDataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;

public class RestSubscriptionImpl extends SubscriptionDataImpl implements Subscription {

	private static final Logger LOGGER = Logger.getLogger(RestSubscriptionImpl.class);

	private MapServerRestConnectionImpl mMapServerConnection;

	private SubscriptionData mOldData;

	private boolean mNotPersised;

	public RestSubscriptionImpl(MapServerRestConnectionImpl mapServerConnection) {
		mMapServerConnection = mapServerConnection;
	}

	public RestSubscriptionImpl(MapServerRestConnectionImpl mapServerConnection, String subscriptionName) {
		this(mapServerConnection);
		super.setName(subscriptionName);
	}

	public RestSubscriptionImpl(MapServerRestConnectionImpl mapServerConnection, SubscriptionData subscriptionData) {
		this(mapServerConnection);

		super.setName(subscriptionData.getName());
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
