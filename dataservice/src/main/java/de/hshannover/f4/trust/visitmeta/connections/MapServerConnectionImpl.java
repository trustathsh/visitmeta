package de.hshannover.f4.trust.visitmeta.connections;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ifmapj.IfmapJ;
import de.hshannover.f4.trust.ifmapj.channel.SSRC;
import de.hshannover.f4.trust.ifmapj.config.BasicAuthConfig;
import de.hshannover.f4.trust.ifmapj.exception.CommunicationException;
import de.hshannover.f4.trust.ifmapj.exception.EndSessionException;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ifmapj.exception.InitializationException;
import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InMemoryIdentifierFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InMemoryMetadataFactory;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionCloseException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionEstablishedException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.IfmapConnectionException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.NotConnectedException;
import de.hshannover.f4.trust.visitmeta.ifmap.SubscriptionHelper;
import de.hshannover.f4.trust.visitmeta.ifmap.UpdateService;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JDatabase;
import de.hshannover.f4.trust.visitmeta.util.yaml.ConnectionsProperties;

/**
 * Represents an IF-MAP connection to a MAP server. If not changed by
 * yourself, following default values are set:
 * <ul>
 * <li>AuthenticationBasic = true</li>
 * <li>TruststorePath = see config.property(ifmap.defaultConnectionSettings.truststorePath)</li>
 * <li>TruststorePassword = see config.property(ifmap.defaultConnectionSettings.truststorePassword)</li>
 * <li>MaxPollResultSize = see config.property(ifmap.defaultConnectionSettings.maxPollResultSize)</li>
 * <li>StartupConnect = see config.property(ifmap.defaultConnectionSettings.useConnectionAsStartup)</li>
 * </ul>
 *
 * @author Marcel Reichenbach
 */
public class MapServerConnectionImpl extends MapServerConnectionDataImpl implements MapServerConnection {

	private static final Logger LOGGER = Logger.getLogger(MapServerConnectionImpl.class);

	private Neo4JDatabase mNeo4JDb;

	private UpdateService mUpdateService;

	private Thread mUpdateThread;

	private SSRC mSsrc;


	public MapServerConnectionImpl(String name) {
		super(name);

		init();
	}

	public MapServerConnectionImpl(String name, String url, String userName, String userPassword) {
		super(name, url, userName, userPassword);

		init();
	}

	public MapServerConnectionImpl(MapServerConnectionData connectionData) {
		super(connectionData.getConnectionName(), connectionData.getUrl(), connectionData.getUserName(), connectionData
				.getUserPassword());

		init();

		super.setTruststorePath(connectionData.getTruststorePath());
		super.setTruststorePassword(connectionData.getTruststorePassword());
		super.setSubscriptionData(connectionData.getSubscriptions());
		super.setMaxPollResultSize(connectionData.getMaxPollResultSize());
		super.setConnected(connectionData.isConnected());
		super.setStartupConnect(connectionData.doesConnectOnStartup());
		super.setAuthenticationBasic(connectionData.isAuthenticationBasic());
	}

	private void init() {
		// set default data
		super.setAuthenticationBasic(ConnectionsProperties.DEFAULT_AUTHENTICATION_BASIC);
		super.setTruststorePath(ConnectionsProperties.DEFAULT_TRUSTSTORE_PATH);
		super.setTruststorePassword(ConnectionsProperties.DEFAULT_TRUSTSTORE_PASSWORD);
		super.setMaxPollResultSize(ConnectionsProperties.DEFAULT_MAX_POLL_RESULT_SIZE);
		super.setStartupConnect(ConnectionsProperties.DEFAULT_STARTUP_CONNECT);

		mNeo4JDb = new Neo4JDatabase(super.getName());
	}

	@Override
	public void connect() throws ConnectionException {
		checkIsConnectionDisconnected();

		String url = super.getUrl();
		String userName = super.getUserName();
		String userPassword = super.getUserPassword();
		String truststorePath = super.getTruststorePath();
		String truststorePassword = super.getTruststorePassword();
		int maxPollResultSize = super.getMaxPollResultSize();

		initSsrc(url, userName, userPassword, truststorePath, truststorePassword);
		initSession(maxPollResultSize);

		super.setConnected(true);
	}

	@Override
	public void disconnect() throws ConnectionException {
		checkIsConnectionEstablished();

		try {
			LOGGER.trace("endSession() ...");
			mSsrc.endSession();
			LOGGER.debug("endSession() OK");

			LOGGER.trace("closeTcpConnection() ...");
			mSsrc.closeTcpConnection();
			LOGGER.debug("closeTcpConnection() OK");

		} catch (IfmapErrorResult e) {
			throw new IfmapConnectionException(e);
		} catch (CommunicationException e) {
			throw new IfmapConnectionException(e);
		} catch (IfmapException e) {
			throw new IfmapConnectionException(e);
		} finally {
			resetConnection();
		}

		super.setConnected(false);
	}

	@Override
	public PollResult poll() throws ConnectionException {
		checkIsConnectionEstablished();

		try {
			return mSsrc.getArc().poll();
		} catch (IfmapErrorResult e) {
			throw new IfmapConnectionException(e);

		} catch (EndSessionException e) {
			throw new ConnectionCloseException();

		} catch (IfmapException e) {
			throw new IfmapConnectionException(e);
		}
	}

	@Override
	public void startSubscription(String subscriptionName) throws ConnectionException {
		for (Data subscription : getSubscriptions()) {
			if (subscription.getName().equals(subscriptionName)) {
				((Subscription) subscription).startSubscription(this);
				break;
			}
		}
	}

	@Override
	public void stopSubscription(String subscriptionName) throws ConnectionException {
		for (Data subscription : getSubscriptions()) {
			if (subscription.getName().equals(subscriptionName)) {
				((Subscription) subscription).stopSubscription(this);
				break;
			}
		}
	}

	@Override
	public void stopAllSubscriptions() throws ConnectionException {
		for (Data subData : getSubscriptions()) {
			((Subscription) subData).stopSubscription(this);
		}
	}

	private void subscribe(SubscribeRequest request) throws ConnectionException {
		checkIsConnectionEstablished();
		startUpdateService();

		if ((request == null) || !request.getSubscribeElements().isEmpty()) {
			try {
				mSsrc.subscribe(request);
			} catch (IfmapErrorResult e) {
				throw new IfmapConnectionException(e);

			} catch (IfmapException e) {
				throw new IfmapConnectionException(e);
			}

		} else {
			LOGGER.warn("Subscribe-Request was null or empty.");
		}
	}

	@Override
	public void subscribe(Subscription subscription, boolean update) throws ConnectionException {
		SubscribeRequest subscribeRequest;
		if (update) {
			subscribeRequest = SubscriptionHelper.buildUpdateRequest(subscription);
		} else {
			subscribeRequest = SubscriptionHelper.buildDeleteRequest(subscription);
		}

		subscribe(subscribeRequest);
	}

	@Override
	public String getConnectionName() {
		return super.getName();
	}

	@Override
	public GraphService getGraphService() {
		return mNeo4JDb.getGraphService();
	}

	@Override
	public String getPublisherId() throws ConnectionException {
		checkIsConnectionEstablished();
		return mSsrc.getPublisherId();
	}

	@Override
	public String getSessionId() throws ConnectionException {
		checkIsConnectionEstablished();
		return mSsrc.getSessionId();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Connection: ").append(getConnectionName()).append(" | URL: ");
		sb.append(getUrl()).append(" | User: ").append(getUserName());

		return sb.toString();
	}

	private void initSsrc(String url, String user, String userPass, String truststore, String truststorePassword)
			throws IfmapConnectionException {
		LOGGER.trace("init SSRC ...");

		try {

			BasicAuthConfig config = new BasicAuthConfig(url, user, userPass, truststore, truststorePassword, true,
					120 * 1000);
			mSsrc = IfmapJ.createSsrc(config);

		} catch (InitializationException e) {
			resetConnection();
			throw new IfmapConnectionException(e);
		}

		LOGGER.debug("init SSRC OK");
	}

	private void initSession(int maxPollResultSize) throws IfmapConnectionException {
		LOGGER.trace("creating new SSRC session ...");

		try {

			mSsrc.newSession(maxPollResultSize);

		} catch (IfmapErrorResult e) {
			resetConnection();
			throw new IfmapConnectionException(e);
		} catch (IfmapException e) {
			resetConnection();
			throw new IfmapConnectionException(e);
		}

		LOGGER.debug("new SSRC session OK");
	}

	private void checkIsConnectionEstablished() throws NotConnectedException {
		if ((mSsrc == null) || !super.isConnected() || (mSsrc.getSessionId() == null)) {
			NotConnectedException e = new NotConnectedException();
			LOGGER.error(e.toString());
			resetConnection();
			throw e;
		}
	}

	private void checkIsConnectionDisconnected() throws ConnectionEstablishedException {
		if ((mSsrc != null) && super.isConnected() && (mSsrc.getSessionId() != null)) {
			throw new ConnectionEstablishedException();
		}
	}

	private void resetConnection() {
		LOGGER.trace("resetConnection() ...");

		super.setConnected(false);
		mSsrc = null;

		LOGGER.trace("... resetConnection() OK");
	}

	private void startUpdateService() throws ConnectionException {
		String connectionName = getConnectionName();

		if ((mUpdateThread == null) || !mUpdateThread.isAlive()) {
			LOGGER.trace("start UpdateService from connection " + connectionName + " ...");

			checkIsConnectionEstablished();

			if (mUpdateService == null) {
				mUpdateService = new UpdateService(this, mNeo4JDb.getWriter(), new InMemoryIdentifierFactory(),
						new InMemoryMetadataFactory());
			}

			mUpdateThread = new Thread(mUpdateService, "UpdateThread-" + connectionName);
			mUpdateThread.start();

			LOGGER.debug("UpdateService for connection " + connectionName + " started");
		}
	}

}
