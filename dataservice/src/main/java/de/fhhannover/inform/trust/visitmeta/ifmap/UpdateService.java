package de.fhhannover.inform.trust.visitmeta.ifmap;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * This file is part of VisITMeta, version 0.0.1, implemented by the Trust@FHH
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;
import de.fhhannover.inform.trust.ifmapj.IfmapJHelper;
import de.fhhannover.inform.trust.ifmapj.channel.ARC;
import de.fhhannover.inform.trust.ifmapj.exception.IfmapErrorResult;
import de.fhhannover.inform.trust.ifmapj.exception.IfmapException;
import de.fhhannover.inform.trust.ifmapj.exception.InitializationException;
import de.fhhannover.inform.trust.ifmapj.identifier.Identifier;
import de.fhhannover.inform.trust.ifmapj.identifier.Identifiers;
import de.fhhannover.inform.trust.ifmapj.messages.Requests;
import de.fhhannover.inform.trust.ifmapj.messages.Result;
import de.fhhannover.inform.trust.ifmapj.messages.SubscribeDelete;
import de.fhhannover.inform.trust.ifmapj.messages.SubscribeRequest;
import de.fhhannover.inform.trust.ifmapj.messages.SubscribeUpdate;
import de.fhhannover.inform.trust.visitmeta.dataservice.Application;
import de.fhhannover.inform.trust.visitmeta.dataservice.factories.InternalIdentifierFactory;
import de.fhhannover.inform.trust.visitmeta.dataservice.factories.InternalMetadataFactory;
import de.fhhannover.inform.trust.visitmeta.dataservice.util.ConfigParameter;
import de.fhhannover.inform.trust.visitmeta.persistence.Writer;
import de.fhhannover.inform.trust.visitmeta.util.PropertiesReaderWriter;

/**
 * When a <tt>UpdateService</tt> is started, it will subscribe for the
 * configured start identifier and after that continuously poll for
 * new information on that subscription.
 *
 * @author Ralf Steuerwald
 *
 */
public class UpdateService implements Runnable {

	private static final Logger log = Logger.getLogger(UpdateService.class);

	protected PropertiesReaderWriter config = Application.getIFMAPConfig();
	protected final String TRUSTSTORE_PATH = config.getProperty(ConfigParameter.IFMAP_TRUSTSTORE_PATH);
	protected final String IFMAP_BASIC_AUTH_URL = config.getProperty(ConfigParameter.IFMAP_BASIC_AUTH_URL);
	protected final String IFMAP_USER = config.getProperty(ConfigParameter.IFMAP_USER);
	protected final String IFMAP_PASS = config.getProperty(ConfigParameter.IFMAP_PASS);
	protected final String START_IDENTIFIER = config.getProperty(ConfigParameter.IFMAP_START_IDENTIFIER);
	protected final String START_IDENTIFIER_TYPE = config.getProperty(ConfigParameter.IFMAP_START_IDENTIFIER_TYPE);
	protected final int MAX_DEPTH = Integer.valueOf(config.getProperty(ConfigParameter.IFMAP_MAX_DEPTH));
	protected final int MAX_SIZE = Integer.valueOf(config.getProperty(ConfigParameter.IFMAP_MAX_SIZE));
	
	protected boolean DUMPING = Boolean.valueOf(config.getProperty(ConfigParameter.IFMAP_SUBSCRIPTION_DUMPING));

	protected final String SUBSCRIPTION_NAME = config.getProperty(ConfigParameter.IFMAP_SUBSCRIPTION_NAME);

	protected final int MAX_RETRY;
	protected final static int DEFAULT_MAX_RETRY = 10;
	protected final int RETRY_INTERVAL;
	protected final static int DEFAULT_RETRY_INTERVAL = 10;

	protected Writer mWriter;
	protected ThreadSafeSsrc mSsrc;

	protected InternalIdentifierFactory mIdentifierFactory;
	protected InternalMetadataFactory mMetadataFactory;

	protected de.fhhannover.inform.trust.visitmeta.ifmap.IfmapJHelper mIfmapJHelper;
	
	private DumpingService mDumpingService;
	private Thread mDumpingThread;

	public class IdentifierData {
		public IdentifierData(String sIdentifierType, String sIdentifier) {
			type = sIdentifierType;
			identifier = sIdentifier;
		}
		String type;
		String identifier;
	}

	private Map<String, IdentifierData> activeSubscriptions = new HashMap<String, IdentifierData>();

	/**
	 * Create a new {@link UpdateService} which uses the given writer to submit new
	 * {@link PollResult}s to the application.
	 *
	 * @param writer
	 * @param identifierFactory
	 * @param metadataFactory
	 */
	public UpdateService(
			Writer writer, InternalIdentifierFactory identifierFactory, InternalMetadataFactory metadataFactory) {
		if (writer == null) {
			throw new IllegalArgumentException("writer cannot be null");
		}
		if (identifierFactory == null) {
			throw new IllegalArgumentException("identifierFactory cannot be null");
		}
		if (metadataFactory == null) {
			throw new IllegalArgumentException("metadataFactory cannot be null");
		}
		mWriter = writer;
		mIdentifierFactory = identifierFactory;
		mMetadataFactory = metadataFactory;

		mIfmapJHelper = new de.fhhannover.inform.trust.visitmeta.ifmap.IfmapJHelper(identifierFactory);

		int tmp = 0;
		try {
			tmp = Integer.parseInt(config.getProperty(ConfigParameter.IFMAP_MAX_RETRY));
		} catch (NumberFormatException e) {
			tmp = DEFAULT_MAX_RETRY;
		}
		MAX_RETRY = tmp;
		try {
			tmp = Integer.parseInt(config.getProperty(ConfigParameter.IFMAP_RETRY_INTERVAL));
		} catch (NumberFormatException e) {
			tmp = DEFAULT_RETRY_INTERVAL;
		}
		RETRY_INTERVAL = tmp;
	}

	private void connectNewSession() {
		boolean connected = false;
		int attempts = 1;

		while (!connected) {
			try {
				log.debug("creating new SSRC session ...");
				mSsrc.newSession();
				connected = true;
			} catch (IfmapErrorResult | IfmapException e) {
				if (attempts >= MAX_RETRY) {
					throw new RuntimeException("could not connect to MAPS", e);
				}
				log.error("could not connect, retry in "+RETRY_INTERVAL+" seconds ...");
				try {
					Thread.sleep(RETRY_INTERVAL * 1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			attempts++;
		}
	}

	protected void setupNewConnection() {
		initSsrc();
		connectNewSession();
		
		if(DUMPING){
			subscribeDeleteAll();
			startDumpingService();
		}else{
			subscribeForStart();
		}
	}
	
	public void startDumpingService(){
		
		if(mDumpingService == null){
			mDumpingService = new DumpingService(mSsrc);
		}
		
		if(mDumpingThread == null){
			mDumpingThread = new Thread(mDumpingService);
		}

		if(!mDumpingThread.isAlive()){
			mDumpingThread.start();
		}
	}
	
	public void stropDumpingService(){
		if(mDumpingThread != null){
			
			mDumpingThread.interrupt();	
			mDumpingThread = null;
		}
	}

	/**
	 * Establish a new {@link ARC} to the MAPS and continuously poll for new data. The
	 * poll results get forwarded to the application.
	 */
	@Override
	public void run() {
		setupNewConnection();
		try {
			ARC arc = mSsrc.getArc();
			log.debug("start polling ...");
			while (!isStopped()) {
				PollTask task = new PollTask(arc, mMetadataFactory, mIfmapJHelper);
				try {
					PollResult pollResult = task.call();
					mWriter.submitPollResult(pollResult);
				} catch (PollException e) {
					//					log.error("error while executing poll task " + e.getMessage());
					//					setupNewConnection();
					//					arc = mSsrc.getArc();
					throw new RuntimeException(e);
				}
			}
		} catch (InitializationException e) {
			throw new RuntimeException(e);
		} finally {
			log.info("shutting down ...");
			try {
				mSsrc.endSession();
				mSsrc.closeTcpConnection();
			} catch (IfmapErrorResult | IfmapException e) {
				log.error("error while ending session: " + e.getMessage());
			}
			log.info("shutdown complete.");
		}
	}

	protected boolean isStopped() {
		return Thread.currentThread().isInterrupted();
	}

	private void initSsrc() {
		TrustManager[] tms;
		try {
			
			tms = IfmapJHelper.getTrustManagers(UpdateService.class.getResourceAsStream(TRUSTSTORE_PATH), IFMAP_PASS);
			mSsrc = new ThreadSafeSsrc(IFMAP_BASIC_AUTH_URL, IFMAP_USER, IFMAP_PASS, tms);
		
		} catch (InitializationException e) {
			throw new RuntimeException("error while initializing SSRC", e);
		}
	}

	protected void subscribeForStart(){
		String[] sNames = SUBSCRIPTION_NAME.split("[;]");
		String[] sIdentifierTypes = START_IDENTIFIER_TYPE.split("[;]");
		String[] sIdentifiers = START_IDENTIFIER.split("[;]");

		if(sNames.length == sIdentifiers.length && sIdentifiers.length == sIdentifierTypes.length){

			for(int i=0; i < sNames.length; i++){

				subscribeUpdate(sNames[i], sIdentifierTypes[i], sIdentifiers[i], MAX_DEPTH, MAX_SIZE);

			}

		}else{

			throw new RuntimeException("could not subscribe for start identifier: invalide length");

		}
	}

	public void subscribeUpdate(String sName, String sIdentifierType, String sIdentifier, int maxDepth, int maxSize) {
		subscribeUpdate(sName, sIdentifierType, sIdentifier, maxDepth, maxSize, null, null, null);
	}

	public void subscribeUpdate(String sName, String sIdentifierType, String sIdentifier, int maxDepth, int maxSize, String linksFilter, String resultFilter, String terminalIdentifierTypes) {
		
		if(mDumpingThread != null && mDumpingThread.isAlive()){
			log.error("No subscribe update when DUMPING is active");
			throw new RuntimeException("Could not subscribe update for identifier, DUMPING is active! ");
		}
		
		SubscribeRequest request = Requests.createSubscribeReq();
		SubscribeUpdate subscribe = Requests.createSubscribeUpdate();

		subscribe.setName(sName);
		subscribe.setMaxDepth(maxDepth);
		subscribe.setMaxSize(maxSize);

		subscribe.setMatchLinksFilter(linksFilter);
		subscribe.setResultFilter(resultFilter);
		subscribe.setTerminalIdentifierTypes(terminalIdentifierTypes);

		subscribe.setStartIdentifier(createStartIdentifier(sIdentifierType, sIdentifier));

		request.addSubscribeElement(subscribe);
		try {
			mSsrc.subscribe(request);
			activeSubscriptions.put(sName, new IdentifierData(sIdentifierType, sIdentifier));
		} catch (IfmapErrorResult | IfmapException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("Could not subscribe update for identifier. Make sure to use a valide JSON-Key for subscriptions: " + e.getMessage());
		}
	}

	protected Identifier createStartIdentifier(String sIdentifierType, String sIdentifier) {
		switch (sIdentifierType) {
		case "device":
			return Identifiers.createDev(sIdentifier);
		case "access-request":
			return Identifiers.createAr(sIdentifier);
		case "ip-address":
			String[] split = sIdentifier.split(",");
			switch (split[0]) {
			case "IPv4":
				return Identifiers.createIp4(split[1]);
			case "IPv6":
				return Identifiers.createIp6(split[1]);
			default:
				throw new RuntimeException("unknown IP address type '"+split[0]+"'");
			}
		case "mac-address":
			return Identifiers.createMac(sIdentifier);

			// TODO identity and extended identifiers

		default:
			throw new RuntimeException("unknown identifier type '"+sIdentifierType+"'");
		}
	}

	public void subscribeDeleteAll(){
		SubscribeRequest request = null;

		for(String sKey: activeSubscriptions.keySet()){

			if(request == null){
				request = Requests.createSubscribeReq();
			}
			
			SubscribeDelete subscribe = Requests.createSubscribeDelete(sKey);
			request.addSubscribeElement(subscribe);

		}

		if(request != null){
			try {
				mSsrc.subscribe(request);
				activeSubscriptions.clear();
			} catch (IfmapErrorResult | IfmapException e) {
				throw new RuntimeException("could not subscribe delete for start identifier: " + e.getMessage());
			}
		}
	}

	public void subscribeDelete(String sName) {
		SubscribeRequest request = Requests.createSubscribeReq();
		SubscribeDelete subscribe = Requests.createSubscribeDelete(sName);

		request.addSubscribeElement(subscribe);
		try {
			mSsrc.subscribe(request);
			activeSubscriptions.remove(sName);
		} catch (IfmapErrorResult | IfmapException e) {
			throw new RuntimeException("could not subscribe delete for start identifier: " + e.getMessage());
		}
	}

	public DumpResult dump(String filter) throws IfmapErrorResult, IfmapException, InterruptedException {

		Result res;
		DumpRequest dreq = new DumpRequestImpl(filter);

		// if SSRC not there, do no request!
		if (mSsrc == null)
			return null;

		res = mSsrc.genericRequestWithSessionId(dreq);

		// If we don't get back a DumpResult instance something is messed up...
		if (!(res instanceof DumpResult)){
			throw new IfmapException("DumpRequestHandler:", "dumpRequest didn't result in dumpResult");
		}
		
		return (DumpResult) res;
	}

	public Set<String> getActiveSubscriptions() {
		return activeSubscriptions.keySet();
	}
}
