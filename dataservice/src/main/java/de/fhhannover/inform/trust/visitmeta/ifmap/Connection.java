
package de.fhhannover.inform.trust.visitmeta.ifmap;

import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.ifmapj.IfmapJHelper;
import de.fhhannover.inform.trust.ifmapj.exception.CommunicationException;
import de.fhhannover.inform.trust.ifmapj.exception.EndSessionException;
import de.fhhannover.inform.trust.ifmapj.exception.IfmapErrorResult;
import de.fhhannover.inform.trust.ifmapj.exception.IfmapException;
import de.fhhannover.inform.trust.ifmapj.exception.InitializationException;
import de.fhhannover.inform.trust.ifmapj.messages.PollResult;
import de.fhhannover.inform.trust.ifmapj.messages.Requests;
import de.fhhannover.inform.trust.ifmapj.messages.SubscribeDelete;
import de.fhhannover.inform.trust.ifmapj.messages.SubscribeElement;
import de.fhhannover.inform.trust.ifmapj.messages.SubscribeRequest;
import de.fhhannover.inform.trust.visitmeta.dataservice.factories.InMemoryIdentifierFactory;
import de.fhhannover.inform.trust.visitmeta.dataservice.factories.InMemoryMetadataFactory;
import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.fhhannover.inform.trust.visitmeta.ifmap.exception.ActiveDumpingException;
import de.fhhannover.inform.trust.visitmeta.ifmap.exception.ConnectionCloseException;
import de.fhhannover.inform.trust.visitmeta.ifmap.exception.ConnectionEstablishedException;
import de.fhhannover.inform.trust.visitmeta.ifmap.exception.ConnectionException;
import de.fhhannover.inform.trust.visitmeta.ifmap.exception.IfmapConnectionException;
import de.fhhannover.inform.trust.visitmeta.ifmap.exception.NoActiveDumpingException;
import de.fhhannover.inform.trust.visitmeta.ifmap.exception.NotConnectedException;
import de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JDatabase;

public class Connection {


	private Logger log = Logger.getLogger(Connection.class);


	private Neo4JDatabase mNeo4JDb;

	private UpdateService mUpdateService;

	private DumpingService mDumpingService;


	private Thread mUpdateThread;

	private Thread mDumpingThread;


	private ThreadSafeSsrc mSsrc;

	private boolean connected = false;


	private String mConnectionName;

	private String mUrl;

	private String mUser;

	private String mUserPass;

	private String mTruststore;

	private String mTruststorePass;


	private Set<String> activeSubscriptions;


	public Connection(String name, String url, String user, String userPass, String truststore, String truststorePass) {
		log.trace("new Connection() ...");

		mConnectionName = name;
		mUrl = url;
		mUser = user;
		mUserPass = userPass;
		mTruststore = truststore;
		mTruststorePass = truststorePass;

		mNeo4JDb = new Neo4JDatabase(name);
		activeSubscriptions = new HashSet<String>();

		log.trace("... new Connection() OK");
	}

	/**
	 * Connect to the MAP-Server.
	 * 
	 * @throws ConnectionException
	 */
	public void connect() throws ConnectionException {

		isConnectionDisconnected();

		initSsrc(mUrl, mUser, mUserPass, mTruststore, mTruststorePass);
		initSession();

	}

	private void initSsrc(String url, String user, String userPass, String truststore, String truststorePass) throws IfmapConnectionException {
		log.debug("init SSRC ...");

		try {

			TrustManager[] tms = IfmapJHelper.getTrustManagers(UpdateService.class.getResourceAsStream(truststore), truststorePass);
			mSsrc = new ThreadSafeSsrc(url, user, userPass, tms);

		} catch (InitializationException e) {

			log.error("Description: " + e.getDescription() + " | ErrorMessage: " + e.getMessage(), e);

			resetConnection();

			throw new IfmapConnectionException();
		}

		log.debug("... init SSRC OK");
	}

	private void initSession() throws IfmapConnectionException {
		log.debug("creating new SSRC session ...");

		try {

			mSsrc.newSession();

			setConnected(true);

		} catch (IfmapErrorResult e) {

			log.error("ErrorCode: " + e.getErrorCode() + " | ErrorString: " + e.getErrorString(), e);

			resetConnection();

			throw new IfmapConnectionException();

		} catch (IfmapException e) {

			log.error("Description: " + e.getDescription() + " | ErrorMessage: " + e.getMessage(), e);

			resetConnection();

			throw new IfmapConnectionException();
		}

		log.debug("... new SSRC session OK");
	}

	/**
	 * Close a connection to the MAP-Server.
	 * 
	 * @throws ConnectionException
	 */
	public void disconnect() throws ConnectionException {

		isConnectionEstablished();

		try {

			log.debug("endSession() ...");
			mSsrc.endSession();
			log.debug("... endSession() OK");

			log.debug("closeTcpConnection() ...");
			mSsrc.closeTcpConnection();
			log.debug("... closeTcpConnection() OK");

		} catch (IfmapErrorResult e) {

			log.error("ErrorCode: " + e.getErrorCode() + " | ErrorString: " + e.getErrorString(), e);

			throw new IfmapConnectionException();

		} catch (CommunicationException e) {

			log.error("ErrorMessage: " + e.getMessage(), e);

			throw new IfmapConnectionException();

		} catch (IfmapException e) {

			log.error("Description: " + e.getDescription() + " | ErrorMessage: " + e.getMessage(), e);

			throw new IfmapConnectionException();

		}finally{

			resetConnection();
		}
	}

	private void startUpdateService() throws ConnectionException{

		if(mUpdateThread == null || !mUpdateThread.isAlive()){

			log.trace("start UpdateService from connection " + mConnectionName + " ...");

			isConnectionEstablished();

			if(mUpdateService == null){
				mUpdateService = new UpdateService(this, mNeo4JDb.getWriter(), new InMemoryIdentifierFactory(), new InMemoryMetadataFactory());
			}

			mUpdateThread = new Thread(mUpdateService, "UpdateThread-" + mConnectionName);

			mUpdateThread.start();

			log.debug("UpdateService for connection " + mConnectionName + " started");
		}
	}

	/**
	 * Start a Dumping-Service.
	 * 
	 * @throws ConnectionException
	 */
	public void startDumpingService() throws ConnectionException{

		isConnectionEstablished();

		isDumpingStopped();

		startUpdateService();

		if(mDumpingService == null){
			mDumpingService = new DumpingService(this, mSsrc);
		}

		if(mDumpingThread == null || !mDumpingThread.isAlive()){

			mDumpingThread = new Thread(mDumpingService, "DumpingThread-" + mConnectionName);

			mDumpingThread.start();
		}
	}

	/**
	 * Stopped a active Dumping-Service.
	 * 
	 * @throws ConnectionException
	 */
	public void stopDumpingService() throws ConnectionException{

		isConnectionEstablished();

		isDumpingActive();

		mDumpingThread.interrupt();
		mDumpingThread = null;
	}

	/**
	 * Send a SubscribeRequest to the map server.
	 * 
	 * @param request
	 * @throws ConnectionException
	 */
	public void subscribe(SubscribeRequest request) throws ConnectionException {

		isConnectionEstablished();

		isDumpingStopped();

		startUpdateService();

		if(request == null || !request.getSubscribeElements().isEmpty()){

			try {

				mSsrc.subscribe(request);

			} catch (IfmapErrorResult e) {

				log.error("ErrorCode: " + e.getErrorCode() + " | ErrorString: " + e.getErrorString(), e);

				throw new IfmapConnectionException();

			} catch (IfmapException e) {

				log.error("Description: " + e.getDescription()+ " | ErrorMessage: " + e.getMessage(), e);

				throw new IfmapConnectionException();
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

		isConnectionEstablished();

		try {

			return mSsrc.getArc().poll();

		} catch (IfmapErrorResult e) {

			log.error("ErrorCode: " + e.getErrorCode() + " | ErrorString: " + e.getErrorString(), e);

			throw new IfmapConnectionException();

		} catch (EndSessionException e) {

			//	Nothing to log while disconnecting from MAP-Server

			throw new ConnectionCloseException();

		} catch (IfmapException e) {

			log.error("Description: " + e.getDescription()+ " | ErrorMessage: " + e.getMessage(), e);

			throw new IfmapConnectionException();
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
		sb.append(mUrl).append(" | User: ").append(mUser);

		return sb.toString();
	}

	/**
	 * @return The session-id of the SSRC
	 * @throws ConnectionException
	 */
	public String getSessionId() throws ConnectionException {

		isConnectionEstablished();

		return mSsrc.getSessionId();
	}

	/**
	 * @return The publisher-id of the SSRC
	 * @throws ConnectionException
	 */
	public String getPublisherId() throws ConnectionException{

		isConnectionEstablished();

		return mSsrc.getPublisherId();
	}

	public boolean isConnectionEstablished() throws NotConnectedException{

		if(mSsrc == null || !isConnected() || mSsrc.getSessionId() == null){

			NotConnectedException e = new NotConnectedException();

			log.error(e.getClass().getSimpleName());

			resetConnection();

			throw e;
		}

		return true;
	}

	private boolean isConnectionDisconnected() throws ConnectionEstablishedException {

		if(mSsrc != null && isConnected() && mSsrc.getSessionId() != null){

			ConnectionEstablishedException e = new ConnectionEstablishedException();

			log.error(e.getClass().getSimpleName());

			throw e;
		}

		return true;
	}

	private boolean isDumpingActive() throws NoActiveDumpingException {

		if(mDumpingThread == null || !mDumpingThread.isAlive()){

			NoActiveDumpingException e = new NoActiveDumpingException();

			log.error(e.getClass().getSimpleName());

			throw e;
		}

		return true;
	}

	private boolean isDumpingStopped() throws ActiveDumpingException {

		if(mDumpingThread != null && mDumpingThread.isAlive()){

			ActiveDumpingException e = new ActiveDumpingException();

			log.error(e.getClass().getSimpleName());

			throw e;
		}

		return true;
	}

	/**
	 * @return A Set<String> with the active subscriptions.
	 * @throws ConnectionException
	 */
	public Set<String> getActiveSubscriptions() throws ConnectionException {

		isConnectionEstablished();

		isDumpingStopped();

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
}
