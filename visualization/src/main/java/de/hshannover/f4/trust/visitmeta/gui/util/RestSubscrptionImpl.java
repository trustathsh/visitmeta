package de.hshannover.f4.trust.visitmeta.gui.util;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.SubscriptionDataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;

public class RestSubscrptionImpl extends SubscriptionDataImpl implements Subscription {

	private static final Logger LOGGER = Logger.getLogger(MapServerRestConnectionImpl.class);

	private DataserviceConnection mDataserviceConnection;

	public RestSubscrptionImpl(DataserviceConnection dataserviceConnection) {
		super();

		init(dataserviceConnection);
	}

	private void init(DataserviceConnection dataserviceConnection) {
		mDataserviceConnection = dataserviceConnection;
	}

	@Override
	public void stopSubscription(MapServerConnection mapServerConnection) throws ConnectionException {
		LOGGER.trace("Method stopSubscription() called.");
		throw new UnsupportedOperationException();
	}

	@Override
	public void startSubscription(MapServerConnection mapServerConnection) throws ConnectionException {
		LOGGER.trace("Method startSubscription() called.");
		throw new UnsupportedOperationException();
	}

}
