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
 * This file is part of visitmeta-dataservice, version 0.2.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeDelete;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeElement;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InMemoryIdentifierFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InMemoryMetadataFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionCloseException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionEstablishedException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.IfmapConnectionException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.NotConnectedException;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JDatabase;
import de.hshannover.f4.trust.visitmeta.util.yaml.ConnectionsProperties;

/**
 * An implementation of the {@link Connection} interface.
 *
 * @author Bastian Hellmann
 * @author Marcel Reichenbach
 */
public class ConnectionImpl implements Connection {

	private static final Logger log = Logger.getLogger(Connection.class);

	private Neo4JDatabase mNeo4JDb;
	private UpdateService mUpdateService;

	private Thread mUpdateThread;

	private SSRC mSsrc;

	private boolean connected = false;
	private Set<String> activeSubscriptions;

	// connection data
	private String mConnectionName;
	private String mUrl;
	private String mUserName;
	private String mUserPass;
	// TODO the dataservice supports only basic authentication this value is not in use
	private boolean mAuthenticationBasic;
	private String mTruststorePath;
	private String mTruststorePass;
	private int mMaxPollResultSize;
	private boolean mStartupConnect;
	private List<Subscription> mSubscribeList;

	/**
	 * Represents an IF-MAP connection to a MAP server. If not changed by
	 * yourself, following default values are set:
	 * <ul>
	 * <li>AuthenticationBasic = true</li>
	 * <li>TruststorePath = see config.property(ifmap.truststore.path)</li>
	 * <li>TruststorePass = see config.property(ifmap.truststore.pw)</li>
	 * <li>MaxPollResultSize = see config.property(ifmap.maxsize)</li>
	 * <li>StartupConnect = false</li>
	 * </ul>
	 *
	 * @throws ConnectionException
	 */
	public ConnectionImpl(String name, String url, String userName,
			String userPass) throws ConnectionException {
		log.trace("new Connection() ...");

		setConnectionName(name);
		setUrl(url);
		setUserName(userName);
		setUserPass(userPass);
		setAuthenticationBasic(ConnectionsProperties.DEFAULT_AUTHENTICATION_BASIC);
		setTruststorePath(ConnectionsProperties.DEFAULT_TRUSTSTORE_PATH);
		setTruststorePass(ConnectionsProperties.DEFAULT_TRUSTSTORE_PASS);
		setMaxPollResultSize(ConnectionsProperties.DEFAULT_MAX_POLL_RESULT_SIZE);
		setStartupConnect(ConnectionsProperties.DEFAULT_STARTUP_CONNECT);

		mSubscribeList = new ArrayList<Subscription>();

		mNeo4JDb = new Neo4JDatabase(mConnectionName);
		activeSubscriptions = new HashSet<String>();

		log.trace("... new Connection() OK");
	}

	@Override
	public void connect() throws ConnectionException {
		checkIsConnectionDisconnected();

		initSsrc(getIfmapServerUrl(), getUserName(), getUserPassword(), mTruststorePath,
				mTruststorePass);
		initSession();
	}

	private void initSsrc(String url, String user, String userPass,
			String truststore, String truststorePass)
					throws IfmapConnectionException {
		log.trace("init SSRC ...");

		try {
			BasicAuthConfig config = new BasicAuthConfig(url, user, userPass,
					truststore, truststorePass, true, 120 * 1000);
			mSsrc = IfmapJ.createSsrc(config);
		} catch (InitializationException e) {
			resetConnection();
			throw new IfmapConnectionException(e);
		}

		log.debug("init SSRC OK");
	}

	private void initSession() throws IfmapConnectionException {
		log.trace("creating new SSRC session ...");

		try {
			mSsrc.newSession(mMaxPollResultSize);
			setConnected(true);
		} catch (IfmapErrorResult e) {
			resetConnection();
			throw new IfmapConnectionException(e);
		} catch (IfmapException e) {
			resetConnection();
			throw new IfmapConnectionException(e);
		}

		log.debug("new SSRC session OK");
	}

	@Override
	public void disconnect() throws ConnectionException {
		checkIsConnectionEstablished();

		try {
			log.trace("endSession() ...");
			mSsrc.endSession();
			log.debug("endSession() OK");

			log.trace("closeTcpConnection() ...");
			mSsrc.closeTcpConnection();
			log.debug("closeTcpConnection() OK");
		} catch (IfmapErrorResult e) {
			throw new IfmapConnectionException(e);
		} catch (CommunicationException e) {
			throw new IfmapConnectionException(e);
		} catch (IfmapException e) {
			throw new IfmapConnectionException(e);
		} finally {
			resetConnection();
		}
	}

	private void startUpdateService() throws ConnectionException {
		if ((mUpdateThread == null) || !mUpdateThread.isAlive()) {
			log.trace("start UpdateService from connection " + mConnectionName
					+ " ...");

			checkIsConnectionEstablished();

			if (mUpdateService == null) {
				mUpdateService = new UpdateService(this, mNeo4JDb.getWriter(),
						new InMemoryIdentifierFactory(),
						new InMemoryMetadataFactory());
			}

			mUpdateThread = new Thread(mUpdateService, "UpdateThread-"
					+ mConnectionName);
			mUpdateThread.start();

			log.debug("UpdateService for connection " + mConnectionName
					+ " started");
		}
	}

	@Override
	public void subscribe(SubscribeRequest request) throws ConnectionException {
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

			for (SubscribeElement r : request.getSubscribeElements()) {
				activeSubscriptions.add(r.getName());
			}
		} else {
			log.warn("Subscribe-Request was null or empty.");
		}
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
	public void deleteSubscription(String sName) throws ConnectionException {
		SubscribeRequest request = Requests.createSubscribeReq();
		SubscribeDelete subscribe = Requests.createSubscribeDelete(sName);

		request.addSubscribeElement(subscribe);
		subscribe(request);

		activeSubscriptions.remove(sName);
	}

	@Override
	public void deleteAllSubscriptions() throws ConnectionException {
		SubscribeRequest request = Requests.createSubscribeReq();

		for (String sKey : activeSubscriptions) {
			SubscribeDelete subscribe = Requests.createSubscribeDelete(sKey);
			request.addSubscribeElement(subscribe);
		}

		subscribe(request);

		activeSubscriptions.clear();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Connection: ").append(mConnectionName).append(" | URL: ");
		sb.append(getIfmapServerUrl()).append(" | User: ").append(getUserName());

		return sb.toString();
	}

	@Override
	public String getSessionId() throws ConnectionException {
		checkIsConnectionEstablished();
		return mSsrc.getSessionId();
	}

	@Override
	public String getPublisherId() throws ConnectionException {
		checkIsConnectionEstablished();
		return mSsrc.getPublisherId();
	}

	private void checkIsConnectionEstablished() throws NotConnectedException {
		if ((mSsrc == null) || !isConnected() || (mSsrc.getSessionId() == null)) {
			NotConnectedException e = new NotConnectedException();
			log.error(e.toString());
			resetConnection();
			throw e;
		}
	}

	private void checkIsConnectionDisconnected()
			throws ConnectionEstablishedException {
		if ((mSsrc != null) && isConnected() && (mSsrc.getSessionId() != null)) {
			throw new ConnectionEstablishedException();
		}
	}

	@Override
	public Set<String> getActiveSubscriptions() throws ConnectionException {
		checkIsConnectionEstablished();
		HashSet<String> temp = new HashSet<String>(activeSubscriptions);

		log.trace("getActiveSubscriptions(): " + temp);
		return temp;
	}

	private void resetConnection() {
		log.trace("resetConnection() ...");

		setConnected(false);
		mSsrc = null;
		activeSubscriptions.clear();

		log.trace("... resetConnection() OK");
	}

	private void setConnected(boolean b) {
		connected = b;
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public SimpleGraphService getGraphService() {
		return mNeo4JDb.getGraphService();
	}

	@Override
	public String getIfmapServerUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	@Override
	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String userName) {
		mUserName = userName;
	}

	@Override
	public String getUserPassword() {
		return mUserPass;
	}

	public void setUserPass(String userPass) {
		mUserPass = userPass;
	}

	@Override
	public String getConnectionName() {
		return mConnectionName;
	}

	public void setConnectionName(String connectionName) {
		mConnectionName = connectionName;
	}

	@Override
	public String getTruststorePath() {
		return mTruststorePath;
	}

	@Override
	public void setTruststorePath(String truststorePath) {
		mTruststorePath = truststorePath;
	}

	@Override
	public String getTruststorePassword() {
		return mTruststorePass;
	}

	@Override
	public void setTruststorePass(String truststorePass) {
		mTruststorePass = truststorePass;
	}

	@Override
	public int getMaxPollResultSize() {
		return mMaxPollResultSize;
	}

	@Override
	public void setMaxPollResultSize(int maxPollResultSize) {
		mMaxPollResultSize = maxPollResultSize;
	}

	@Override
	public void setAuthenticationBasic(boolean authenticationBasic) {
		mAuthenticationBasic = authenticationBasic;
	}

	@Override
	public boolean isAuthenticationBasic() {
		return mAuthenticationBasic;
	}

	@Override
	public void setStartupConnect(boolean startupConnect) {
		mStartupConnect = startupConnect;
	}

	@Override
	public boolean doesConnectOnStartup() {
		return mStartupConnect;
	}

	@Override
	public void addSubscription(Subscription subscription) {
		mSubscribeList.add(subscription);
	}

	public void setSubscribeList(List<Subscription> subscribtionList) {
		mSubscribeList = subscribtionList;
	}

	@Override
	public List<Subscription> getSubscriptions() {
		return mSubscribeList;
	}
}
