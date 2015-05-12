package de.hshannover.f4.trust.visitmeta.gui.util;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.SubscriptionDataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;

public class RestSubscrptionImpl extends SubscriptionDataImpl implements Subscription {

	private static final Logger LOGGER = Logger.getLogger(MapServerRestConnectionImpl.class);

	private MapServerConnection mMapServerConnection;

	public RestSubscrptionImpl(MapServerConnection mapServerConnection) {
		mMapServerConnection = mapServerConnection;
	}

	@Override
	public void stopSubscription() throws ConnectionException {
		LOGGER.trace("Method stopSubscription() called.");
		// mMapServerConnection.subscribe(subscription, update);
		throw new UnsupportedOperationException();
	}

	@Override
	public void startSubscription() throws ConnectionException {
		LOGGER.trace("Method startSubscription() called.");
		throw new UnsupportedOperationException();
	}

}
