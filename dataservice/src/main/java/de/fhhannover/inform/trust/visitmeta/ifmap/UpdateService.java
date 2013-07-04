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

import javax.net.ssl.TrustManager;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.ifmapj.IfmapJ;
import de.fhhannover.inform.trust.ifmapj.IfmapJHelper;
import de.fhhannover.inform.trust.ifmapj.channel.ARC;
import de.fhhannover.inform.trust.ifmapj.channel.SSRC;
import de.fhhannover.inform.trust.ifmapj.exception.IfmapErrorResult;
import de.fhhannover.inform.trust.ifmapj.exception.IfmapException;
import de.fhhannover.inform.trust.ifmapj.exception.InitializationException;
import de.fhhannover.inform.trust.ifmapj.identifier.Identifier;
import de.fhhannover.inform.trust.ifmapj.identifier.Identifiers;
import de.fhhannover.inform.trust.ifmapj.messages.Requests;
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
	protected final String TRUSTSTORE_PATH = config
			.getProperty(ConfigParameter.IFMAP_TRUSTSTORE_PATH);
	protected final String IFMAP_BASIC_AUTH_URL = config
			.getProperty(ConfigParameter.IFMAP_BASIC_AUTH_URL);
	protected final String IFMAP_USER = config
			.getProperty(ConfigParameter.IFMAP_USER);
	protected final String IFMAP_PASS = config
			.getProperty(ConfigParameter.IFMAP_PASS);
	protected final String START_IDENTIFIER = config
			.getProperty(ConfigParameter.IFMAP_START_IDENTIFIER);
	protected final String START_IDENTIFIER_TYPE = config
			.getProperty(ConfigParameter.IFMAP_START_IDENTIFIER_TYPE);
	protected final int MAX_DEPTH = Integer.valueOf(config
			.getProperty(ConfigParameter.IFMAP_MAX_DEPTH));
	protected final int MAX_SIZE = Integer.valueOf(config
			.getProperty(ConfigParameter.IFMAP_MAX_SIZE));

	protected final String SUBSCRIPTION_NAME = config
			.getProperty(ConfigParameter.IFMAP_SUBSCRIPTION_NAME);

	protected final int MAX_RETRY;
	protected final static int DEFAULT_MAX_RETRY = 10;
	protected final int RETRY_INTERVAL;
	protected final static int DEFAULT_RETRY_INTERVAL = 10;

	protected Writer mWriter;
	protected SSRC mSsrc;

	protected InternalIdentifierFactory mIdentifierFactory;
	protected InternalMetadataFactory mMetadataFactory;

	protected de.fhhannover.inform.trust.visitmeta.ifmap.IfmapJHelper mIfmapJHelper;

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

		mIfmapJHelper =
				new de.fhhannover.inform.trust.visitmeta.ifmap.IfmapJHelper(identifierFactory);

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

	private void setupNewConnection() {
		initSsrc();
		connectNewSession();
		subscribeForStartIdentifier();
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
			tms = IfmapJHelper.getTrustManagers(
					UpdateService.class.getResourceAsStream(TRUSTSTORE_PATH),
					IFMAP_PASS);
			mSsrc = IfmapJ.createSSRC(IFMAP_BASIC_AUTH_URL, IFMAP_USER,
					IFMAP_PASS, tms);
		} catch (InitializationException e) {
			throw new RuntimeException("error while initializing SSRC", e);
		}
	}

	protected void subscribeForStartIdentifier() {
		SubscribeRequest request = Requests.createSubscribeReq();
		SubscribeUpdate subscribe = Requests.createSubscribeUpdate();
		subscribe.setName(SUBSCRIPTION_NAME);
		subscribe.setMaxDepth(MAX_DEPTH);
		subscribe.setMaxSize(MAX_SIZE);

		subscribe.setStartIdentifier(createStartIdentifier());

		request.addSubscribeElement(subscribe);
		try {
			mSsrc.subscribe(request);
		} catch (IfmapErrorResult | IfmapException e) {
			throw new RuntimeException("could not subscribe for start identifier: " +
					e.getMessage());
		}
	}

	protected Identifier createStartIdentifier() {
		switch (START_IDENTIFIER_TYPE) {
		case "device":
			return Identifiers.createDev(START_IDENTIFIER);
		case "access-request":
			return Identifiers.createAr(START_IDENTIFIER);
		case "ip-address":
			String[] split = START_IDENTIFIER.split(",");
			switch (split[0]) {
			case "IPv4":
				return Identifiers.createIp4(split[1]);
			case "IPv6":
				return Identifiers.createIp6(split[1]);
			default:
				throw new RuntimeException("unknown IP address type '"+split[0]+"'");
			}
		case "mac-address":
			return Identifiers.createMac(START_IDENTIFIER);

		// TODO identity and extended identifiers

		default:
			throw new RuntimeException("unknown identifier type '"+START_IDENTIFIER_TYPE+"'");
		}
	}
}
