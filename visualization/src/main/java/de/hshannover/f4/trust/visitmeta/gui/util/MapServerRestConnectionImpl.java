package de.hshannover.f4.trust.visitmeta.gui.util;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.WebResource;

import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.visitmeta.connections.MapServerConnectionDataImpl;
import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.gui.ConnectionTab;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.network.ProxyGraphService;

public class MapServerRestConnectionImpl extends MapServerConnectionDataImpl implements MapServerConnection {

	private static final Logger LOGGER = Logger.getLogger(MapServerRestConnectionImpl.class);

	private GraphService mGraphService;

	private GraphContainer mGraphContainer;

	private ConnectionTab mConnectionTab;

	private DataserviceConnection mDataserviceConnection;

	public MapServerRestConnectionImpl(DataserviceConnection dataserviceConnection, String name) {
		super(name);

		init(dataserviceConnection);
	}

	public MapServerRestConnectionImpl(DataserviceConnection dataserviceConnection, String name, String url,
			String userName, String userPassword) {
		super(name, url, userName, userPassword);

		init(dataserviceConnection);
	}

	private void init(DataserviceConnection dataserviceConnection) {
		mDataserviceConnection = dataserviceConnection;

		mGraphService = new ProxyGraphService(getGraphResource(), mDataserviceConnection.isRawXml());
		mGraphContainer = new GraphContainer(this, mGraphService);
		mConnectionTab = new ConnectionTab(mGraphContainer, null);

	}

	public WebResource getGraphResource() {
		return RestHelper.getGraphResource(mDataserviceConnection, super.getConnectionName());
	}

	@Override
	public void connect() {
		RestHelper.connectMapServer(mDataserviceConnection, super.getConnectionName());
		super.setConnected(true);
	}

	@Override
	public void disconnect() {
		RestHelper.disconnectMapServer(mDataserviceConnection, super.getConnectionName());
		super.setConnected(false);
	}

	@Override
	public void startSubscription(String subscriptionName) throws ConnectionException {
		for (Data d : super.getSubscriptions()) {
			Subscription subscription = (Subscription) d;
			if (subscription.getName().equals(subscriptionName)) {
				subscription.startSubscription(this);
				break;
			}
		}
	}

	@Override
	public void stopSubscription(String subscriptionName) throws ConnectionException {
		for (Data d : super.getSubscriptions()) {
			Subscription subscription = (Subscription) d;
			if (subscription.getName().equals(subscriptionName)) {
				subscription.stopSubscription(this);
				break;
			}
		}
	}

	@Override
	public void stopAllSubscriptions() throws ConnectionException {
		for (Data d : super.getSubscriptions()) {
			Subscription subscription = (Subscription) d;
			subscription.stopSubscription(this);
		}
	}

	@Override
	public void subscribe(Subscription subscription, boolean update) {
		LOGGER.trace("Method subscribe() called.");
		throw new UnsupportedOperationException();
	}

	@Override
	public GraphService getGraphService() {
		return mGraphService;
	}

	@Override
	public String getPublisherId() {
		LOGGER.trace("Method getPublisherId() called.");
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSessionId() {
		LOGGER.trace("Method getSessionId() called.");
		throw new UnsupportedOperationException();
	}

	@Override
	public PollResult poll() throws ConnectionException {
		LOGGER.trace("Method getSessionId() called.");
		throw new UnsupportedOperationException();
	}

	public ConnectionTab getConnectionTab() {
		return mConnectionTab;
	}

	public DataserviceConnection getDataserviceConnection() {
		return mDataserviceConnection;
	}
}
