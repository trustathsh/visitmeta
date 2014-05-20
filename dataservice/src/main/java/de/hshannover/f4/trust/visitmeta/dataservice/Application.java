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
package de.hshannover.f4.trust.visitmeta.dataservice;



import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeUpdate;
import de.hshannover.f4.trust.visitmeta.dataservice.rest.RestService;
import de.hshannover.f4.trust.visitmeta.dataservice.util.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.ifmap.ConnectionManager;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionException;
import de.hshannover.f4.trust.visitmeta.util.PropertiesReaderWriter;

/**
 * Application main class, also provides access to main interfaces. <i>Note:
 * Must only contain static methods and therefore is abstract</i>
 *
 * @author rosso
 *
 */
public abstract class Application {

	private static final Logger log = Logger.getLogger(Application.class);

	/**
	 * Configuration class for the database.
	 */
	private static PropertiesReaderWriter mDBConfig;
	/**
	 * Configuration class for the IFMAP-Server.
	 */
	private static PropertiesReaderWriter mIFMAPConfig;
	/**
	 * Configuration class for the dataservice.
	 */
	private static PropertiesReaderWriter mDSConfig;

	private static RestService restService;

	private static Thread restServiceThread;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		log.info("Application started");

		initComponents();

		log.info("Components initialized");

		startRestService();

		try {
			initDefaultConnection();
		} catch (ConnectionException e) {
			log.error("Error by initDefaultConnection() at startup", e);
		}

		log.info("Dataservice started successfully");

	}

	private static void initDefaultConnection() throws ConnectionException {
		log.trace("init default connection...");

		String TRUSTSTORE_PATH = getIFMAPConfig().getProperty(ConfigParameter.IFMAP_TRUSTSTORE_PATH);
		String TRUSTSTORE_PASS = getIFMAPConfig().getProperty(ConfigParameter.IFMAP_TRUSTSTORE_PASS);
		String IFMAP_BASIC_AUTH_URL = getIFMAPConfig().getProperty(ConfigParameter.IFMAP_BASIC_AUTH_URL);
		String IFMAP_USER = getIFMAPConfig().getProperty(ConfigParameter.IFMAP_USER);
		String IFMAP_PASS = getIFMAPConfig().getProperty(ConfigParameter.IFMAP_PASS);
		int IFMAP_MAX_SIZE = Integer.parseInt(getIFMAPConfig().getProperty(ConfigParameter.IFMAP_MAX_SIZE));

		Connection defaultConnection = ConnectionManager.newConnection("default", IFMAP_BASIC_AUTH_URL, IFMAP_USER, IFMAP_PASS, TRUSTSTORE_PATH, TRUSTSTORE_PASS, IFMAP_MAX_SIZE);

		if(Boolean.valueOf(getIFMAPConfig().getProperty(ConfigParameter.IFMAP_START_CONNECT))){
			log.debug("connecting at startup");
			defaultConnection.connect();

			log.debug("send Subscribe-Update at startup");

			String subscribeName = getIFMAPConfig().getProperty(ConfigParameter.IFMAP_SUBSCRIPTION_NAME);
			String identifierType = getIFMAPConfig().getProperty(ConfigParameter.IFMAP_START_IDENTIFIER_TYPE);
			String identifier = getIFMAPConfig().getProperty(ConfigParameter.IFMAP_START_IDENTIFIER);
			int maxDepth = Integer.parseInt(getIFMAPConfig().getProperty(ConfigParameter.IFMAP_MAX_DEPTH));
			int maxSize = Integer.parseInt(getIFMAPConfig().getProperty(ConfigParameter.IFMAP_MAX_SIZE));

			SubscribeRequest request = Requests.createSubscribeReq();
			SubscribeUpdate subscribe = Requests.createSubscribeUpdate();

			subscribe.setName(subscribeName);
			subscribe.setMaxDepth(maxDepth);
			subscribe.setMaxSize(maxSize);

			Identifier startIdentifier = createStartIdentifier(identifierType, identifier);
			subscribe.setStartIdentifier(startIdentifier);
			request.addSubscribeElement(subscribe);

			defaultConnection.subscribe(request);

		}
	}

	private static void startRestService() {
		restService = new RestService();
		restServiceThread = new Thread(restService, "RestService-Thread");

		restServiceThread.start();
	}

	private static void initComponents() {

		try {
			String dbConfig = Application.class.getClassLoader().getResource("config.properties").getPath();
			String ifmapConfig = Application.class.getClassLoader().getResource("config.properties").getPath();
			String dsConfig = Application.class.getClassLoader().getResource("config.properties").getPath();
			mDBConfig = new PropertiesReaderWriter(dbConfig, false);
			mIFMAPConfig = new PropertiesReaderWriter(ifmapConfig, false);
			mDSConfig = new PropertiesReaderWriter(dsConfig, false);
		} catch (IOException e) {
			String msg = "Error while reading the config files";
			log.fatal(msg);
			throw new RuntimeException(msg, e);
		}

	}

	private static Identifier createStartIdentifier(String sIdentifierType, String sIdentifier) {
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

	public static MessageDigest loadHashAlgorithm() {
		try {
			String algoname = mDBConfig.getProperty(ConfigParameter.NEO4J_HASH_ALGO);
			log.trace("try to load MessageDigest for '"+algoname+"'");
			return MessageDigest.getInstance(algoname);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("could not load requested hash algorithm", e);
		}
	}

	/**
	 * @return
	 */
	public static PropertiesReaderWriter getDBConfig() {
		if (mDBConfig == null) {
			throw new RuntimeException(
					"DBConfig has not been initialized. This is not good!");
		}
		return mDBConfig;
	}

	/**
	 * @return
	 */
	public static PropertiesReaderWriter getIFMAPConfig() {
		if (mIFMAPConfig == null) {
			throw new RuntimeException(
					"IFMAPConfig has not been initialized. This is not good!");
		}
		return mIFMAPConfig;
	}

	/**
	 * @return
	 */
	public static PropertiesReaderWriter getDSConfig() {
		if (mDSConfig == null) {
			throw new RuntimeException(
					"DSConfig has not been initialized. This is not good!");
		}
		return mDSConfig;
	}

}
