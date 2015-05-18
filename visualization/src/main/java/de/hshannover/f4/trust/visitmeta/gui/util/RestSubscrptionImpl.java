package de.hshannover.f4.trust.visitmeta.gui.util;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.exceptions.RESTException;
import de.hshannover.f4.trust.visitmeta.ifmap.SubscriptionDataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;

public class RestSubscrptionImpl extends SubscriptionDataImpl implements Subscription {

	private static final Logger LOGGER = Logger.getLogger(RestSubscrptionImpl.class);

	private MapServerRestConnectionImpl mMapServerConnection;

	public RestSubscrptionImpl(MapServerRestConnectionImpl mapServerConnection) {
		mMapServerConnection = mapServerConnection;
	}

	public RestSubscrptionImpl(MapServerRestConnectionImpl mapServerConnection, SubscriptionData subscriptionData) {

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

		mMapServerConnection = mapServerConnection;
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

}
