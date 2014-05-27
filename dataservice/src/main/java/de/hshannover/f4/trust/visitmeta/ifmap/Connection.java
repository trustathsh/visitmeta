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
 * This file is part of visitmeta dataservice, version 0.0.3,
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

import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.ifmapj.IfmapJHelper;
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
import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InMemoryIdentifierFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.factories.InMemoryMetadataFactory;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.hshannover.f4.trust.visitmeta.dataservice.util.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionCloseException;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionEstablishedException;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.IfmapConnectionException;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.NotConnectedException;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JDatabase;
import de.hshannover.f4.trust.visitmeta.util.yaml.YamlPersist.OPTIONAL;
import de.hshannover.f4.trust.visitmeta.util.yaml.YamlPersist.REQUIRED;

public class Connection {

	public static final boolean DEFAULT_AUTHENTICATION_BASIC = true;

	public static final String DEFAULT_TRUSTSTORE_PATH = Application.getIFMAPConfig().getProperty(ConfigParameter.IFMAP_TRUSTSTORE_PATH);

	public static final String DEFAULT_TRUSTSTORE_PASS = Application.getIFMAPConfig().getProperty(ConfigParameter.IFMAP_TRUSTSTORE_PASS);

	public static final int DEFAULT_MAX_POLL_RESULT_SIZE = Integer.parseInt(Application.getIFMAPConfig().getProperty(ConfigParameter.IFMAP_MAX_SIZE));

	public static final boolean DEFAULT_STARTUP_CONNECT = false;

	private static final Logger log = Logger.getLogger(Connection.class);


	private Neo4JDatabase mNeo4JDb;
	private UpdateService mUpdateService;

	private Thread mUpdateThread;

	private ThreadSafeSsrc mSsrc;

	private boolean connected = false;
	private Set<String> activeSubscriptions;

	// connection data
	@REQUIRED
	private String mConnectionName;

	@REQUIRED
	private String mUrl;

	@REQUIRED
	private String mUserName;

	@REQUIRED
	private String mUserPass;

	@OPTIONAL("DEFAULT_AUTHENTICATION_BASIC")
	private boolean mAuthenticationBasic;

	@OPTIONAL("DEFAULT_TRUSTSTORE_PATH")
	private String mTruststorePath;

	@OPTIONAL("DEFAULT_TRUSTSTORE_PASS")
	private String mTruststorePass;

	@OPTIONAL("DEFAULT_MAX_POLL_RESULT_SIZE")
	private int mMaxPollResultSize;

	@OPTIONAL("DEFAULT_STARTUP_CONNECT")
	private boolean mStartupConnect;

	private List<JSONObject> mSubscribeList;

	/**
	 * Represents an IF-MAP connection to a MAP server.
	 * If not changed by yourself, following default values ​​are set:
	 * <ul>
	 * 	<li>AuthenticationBasic = true</li>
	 * 	<li>TruststorePath = see config.property(ifmap.truststore.path)</li>
	 * 	<li>TruststorePass = see config.property(ifmap.truststore.pw)</li>
	 * 	<li>MaxPollResultSize = see config.property(ifmap.maxsize)</li>
	 * 	<li>StartupConnect = false</li>
	 * </ul>
	 * @throws ConnectionException
	 */
	public Connection(String name, String url, String userName, String userPass) throws ConnectionException {
		log.trace("new Connection() ...");

		if(ConnectionManager.existsConnectionName(name)){
			throw new ConnectionException(name + " connection already exists");
		}

		setConnectionName(name);
		setUrl(url);
		setUserName(userName);
		setUserPass(userPass);
		setAuthenticationBasic(DEFAULT_AUTHENTICATION_BASIC);
		setTruststorePath(DEFAULT_TRUSTSTORE_PATH);
		setTruststorePass(DEFAULT_TRUSTSTORE_PASS);
		setMaxPollResultSize(DEFAULT_MAX_POLL_RESULT_SIZE);
		setStartupConnect(DEFAULT_STARTUP_CONNECT);
		mSubscribeList = new ArrayList<JSONObject>();

		mNeo4JDb = new Neo4JDatabase(mConnectionName);
		activeSubscriptions = new HashSet<String>();

		log.trace("... new Connection() OK");
	}

	/**
	 * Connect to the MAP-Server.
	 * 
	 * 
	 * @throws ConnectionException
	 */
	public void connect() throws ConnectionException {
		checkIsConnectionDisconnected();

		initSsrc(getUrl(), getUserName(), getUserPass(), mTruststorePath, mTruststorePass);
		initSession();
	}

	private void initSsrc(String url, String user, String userPass, String truststore, String truststorePass) throws IfmapConnectionException {
		log.trace("init SSRC ...");

		try {

			TrustManager[] tms = IfmapJHelper.getTrustManagers(UpdateService.class.getResourceAsStream(truststore), truststorePass);
			mSsrc = new ThreadSafeSsrc(url, user, userPass, tms);

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

	/**
	 * Close a connection to the MAP-Server.
	 * 
	 * @throws ConnectionException
	 */
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

		}finally{
			resetConnection();
		}
	}

	private void startUpdateService() throws ConnectionException{
		if(mUpdateThread == null || !mUpdateThread.isAlive()){
			log.trace("start UpdateService from connection " + mConnectionName + " ...");

			checkIsConnectionEstablished();

			if(mUpdateService == null){
				mUpdateService = new UpdateService(this, mNeo4JDb.getWriter(), new InMemoryIdentifierFactory(), new InMemoryMetadataFactory());
			}

			mUpdateThread = new Thread(mUpdateService, "UpdateThread-" + mConnectionName);
			mUpdateThread.start();

			log.debug("UpdateService for connection " + mConnectionName + " started");
		}
	}

	/**
	 * Send a SubscribeRequest to the map server.
	 * 
	 * @param request
	 * @throws ConnectionException
	 */
	public void subscribe(SubscribeRequest request) throws ConnectionException {
		checkIsConnectionEstablished();
		startUpdateService();

		if(request == null || !request.getSubscribeElements().isEmpty()){
			try {

				mSsrc.subscribe(request);

			} catch (IfmapErrorResult e) {
				throw new IfmapConnectionException(e);

			} catch (IfmapException e) {
				throw new IfmapConnectionException(e);
			}

			for(SubscribeElement r: request.getSubscribeElements()){
				activeSubscriptions.add(r.getName());
			}
		}else{
			log.warn("Subscribe-Request was null or empty.");
		}
	}

	/**
	 * A poll-request over the ARC
	 * 
	 * @return PollResult
	 * @throws ConnectionException
	 */
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

	/**
	 * Delete a subscription with his name.
	 * 
	 * @param sName The subscription name.
	 * @throws ConnectionException
	 */
	public void deleteSubscribe(String sName) throws ConnectionException {
		SubscribeRequest request = Requests.createSubscribeReq();
		SubscribeDelete subscribe = Requests.createSubscribeDelete(sName);

		request.addSubscribeElement(subscribe);
		subscribe(request);

		activeSubscriptions.remove(sName);
	}

	/**
	 * Delete all active subscriptions.
	 * 
	 * @throws ConnectionException
	 */
	public void deleteSubscriptions() throws ConnectionException{
		SubscribeRequest request = Requests.createSubscribeReq();

		for(String sKey: activeSubscriptions){
			SubscribeDelete subscribe = Requests.createSubscribeDelete(sKey);
			request.addSubscribeElement(subscribe);
		}

		subscribe(request);

		activeSubscriptions.clear();
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();

		sb.append("Connection: ").append(mConnectionName).append(" | URL: ");
		sb.append(getUrl()).append(" | User: ").append(getUserName());

		return sb.toString();
	}

	/**
	 * @return The session-id of the SSRC
	 * @throws ConnectionException
	 */
	public String getSessionId() throws ConnectionException {
		checkIsConnectionEstablished();
		return mSsrc.getSessionId();
	}

	/**
	 * @return The publisher-id of the SSRC
	 * @throws ConnectionException
	 */
	public String getPublisherId() throws ConnectionException{
		checkIsConnectionEstablished();
		return mSsrc.getPublisherId();
	}

	public void checkIsConnectionEstablished() throws NotConnectedException{
		if(mSsrc == null || !isConnected() || mSsrc.getSessionId() == null){
			NotConnectedException e = new NotConnectedException();
			log.error(e.toString());
			resetConnection();
			throw e;
		}
	}

	private void checkIsConnectionDisconnected() throws ConnectionEstablishedException {
		if(mSsrc != null && isConnected() && mSsrc.getSessionId() != null){
			ConnectionEstablishedException e = new ConnectionEstablishedException();
			log.error(e.toString());
			throw e;
		}
	}

	/**
	 * @return A Set<String> with the active subscriptions.
	 * @throws ConnectionException
	 */
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

	private void setConnected(boolean b){
		connected = b;
	}

	/**
	 * @return True when the connection to a MAP-Server is active.
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * @return A SimpleGraphService from the Neo4J-Database.
	 */
	public SimpleGraphService getGraphService() {
		return mNeo4JDb.getGraphService();
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public String getUserName() {
		return mUserName;
	}

	public void setUserName(String userName) {
		mUserName = userName;
	}

	public String getUserPass() {
		return mUserPass;
	}

	public void setUserPass(String userPass) {
		mUserPass = userPass;
	}

	public String getConnectionName() {
		return mConnectionName;
	}

	public void setConnectionName(String connectionName) {
		mConnectionName = connectionName;
	}

	public String getTruststorePath() {
		return mTruststorePath;
	}

	public void setTruststorePath(String truststorePath) {
		mTruststorePath = truststorePath;
	}

	public String getTruststorePass() {
		return mTruststorePass;
	}

	public void setTruststorePass(String truststorePass) {
		mTruststorePass = truststorePass;
	}

	public int getMaxPollResultSize() {
		return mMaxPollResultSize;
	}

	public void setMaxPollResultSize(int maxPollResultSize) {
		mMaxPollResultSize = maxPollResultSize;
	}

	public void setAuthenticationBasic(boolean authenticationBasic) {
		mAuthenticationBasic = authenticationBasic;
	}

	public boolean isAuthenticationBasic() {
		return mAuthenticationBasic;
	}

	public void setStartupConnect(boolean startupConnect) {
		mStartupConnect = startupConnect;
	}

	public boolean isStartupConnect() {
		return mStartupConnect;
	}

	public List<JSONObject> getSubscribeList() {
		return mSubscribeList;
	}

	public void addSubscribe(JSONObject jObj) {
		mSubscribeList.add(jObj);
	}
}

