package de.hshannover.f4.trust.visitmeta.gui.util;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.WebResource;

import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.visitmeta.connections.MapServerConnectionDataImpl;
import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.exceptions.RESTException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.gui.ConnectionTab;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.network.ProxyGraphService;

public class MapServerRestConnectionImpl extends MapServerConnectionDataImpl implements MapServerConnection {

	private static final Logger LOGGER = Logger.getLogger(MapServerRestConnectionImpl.class);

	private GraphService mGraphService;

	private GraphContainer mGraphContainer;

	private ConnectionTab mConnectionTab;

	private boolean mGraphStarted;

	private DataserviceConnection mDataserviceConnection;

	private boolean mNotPersised;

	private MapServerConnectionData mOldData;

	public MapServerRestConnectionImpl(DataserviceConnection dataserviceConnection,
			MapServerConnectionData connectionData) {
		super(connectionData.getConnectionName());

		super.setUrl(connectionData.getUrl());
		super.setUserName(connectionData.getUserName());
		super.setUserPassword(connectionData.getUserPassword());
		super.setTruststorePath(connectionData.getTruststorePath());
		super.setTruststorePassword(connectionData.getTruststorePassword());
		super.setMaxPollResultSize(connectionData.getMaxPollResultSize());
		super.setConnected(connectionData.isConnected());
		super.setStartupConnect(connectionData.doesConnectOnStartup());
		super.setAuthenticationBasic(connectionData.isAuthenticationBasic());

		for (Data subscriptonData : connectionData.getSubscriptions()) {
			if (!(subscriptonData instanceof RestSubscriptionImpl) && subscriptonData instanceof SubscriptionData) {
				super.addSubscription(new RestSubscriptionImpl(this, (SubscriptionData) subscriptonData));
			}
		}

		mDataserviceConnection = dataserviceConnection;
	}

	public MapServerRestConnectionImpl(DataserviceConnection dataserviceConnection, String name) {
		super(name);

		mDataserviceConnection = dataserviceConnection;
	}

	public MapServerRestConnectionImpl(DataserviceConnection dataserviceConnection, String name, String url,
			String userName, String userPassword) {
		super(name, url, userName, userPassword);

		mDataserviceConnection = dataserviceConnection;
	}

	@Override
	public MapServerRestConnectionImpl copy() {
		MapServerConnectionData dataCopy = super.copy();
		MapServerRestConnectionImpl tmpCopy = new MapServerRestConnectionImpl(mDataserviceConnection, dataCopy);
		tmpCopy.setNotPersised(true);
		return tmpCopy;
	}

	@Override
	public MapServerRestConnectionImpl clone() {
		return (MapServerRestConnectionImpl) super.clone();
	}

	/**
	 * Start the ProxyGraphService and init the GraphContainer & ConnectionTab.
	 */
	public void initGraph() {
		if (!isGraphStarted()) {
			mGraphService = new ProxyGraphService(getGraphResource(), mDataserviceConnection.isRawXml());
			mGraphContainer = new GraphContainer(this, mGraphService);
			mConnectionTab = new ConnectionTab(mGraphContainer, null);

			mGraphStarted = true;
		}
	}

	public WebResource getGraphResource() {
		return RestHelper.getGraphResource(mDataserviceConnection, super.getConnectionName());
	}

	@Override
	public void connect() {
		try {

			RestHelper.connectMapServer(mDataserviceConnection, super.getConnectionName());
			super.setConnected(true);

		} catch (RESTException e) {
			LOGGER.error(e.toString());
		}
	}

	@Override
	public void disconnect() {
		try {

			RestHelper.disconnectMapServer(mDataserviceConnection, super.getConnectionName());
			super.setConnected(false);

			for (Data subscriptionData : super.getSubscriptions()) {
				if (subscriptionData instanceof Subscription) {
					Subscription subscription = (Subscription) subscriptionData;
					subscription.setActive(false);
				}
			}

		} catch (RESTException e) {
			LOGGER.error(e.toString());
		}
	}

	@Override
	public void startSubscription(String subscriptionName) throws ConnectionException {
		for (Data d : super.getSubscriptions()) {
			Subscription subscription = (Subscription) d;
			if (subscription.getName().equals(subscriptionName)) {
				subscription.startSubscription();
				break;
			}
		}
	}

	@Override
	public void stopSubscription(String subscriptionName) throws ConnectionException {
		for (Data d : super.getSubscriptions()) {
			Subscription subscription = (Subscription) d;
			if (subscription.getName().equals(subscriptionName)) {
				subscription.stopSubscription();
				break;
			}
		}
	}

	@Override
	public void stopAllSubscriptions() throws ConnectionException {
		for (Data d : super.getSubscriptions()) {
			Subscription subscription = (Subscription) d;
			subscription.stopSubscription();
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

	public void resetData(){
		if(mOldData != null){
			super.changeData(mOldData);
			mOldData = null;
		}
	}

	public ConnectionTab getConnectionTab() {
		return mConnectionTab;
	}

	public DataserviceConnection getDataserviceConnection() {
		return mDataserviceConnection;
	}

	public boolean isGraphStarted() {
		return mGraphStarted;
	}

	public boolean isNotPersised() {
		return mNotPersised;
	}

	public void setNotPersised(boolean b) {
		mNotPersised = b;
	}

	public void setOldData(MapServerConnectionData oldData) {
		mOldData = oldData;
	}
}
