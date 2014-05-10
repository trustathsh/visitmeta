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



import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.dataservice.rest.RestService;
import de.hshannover.f4.trust.visitmeta.dataservice.util.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.ifmap.ConnectionManager;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.IdentifierData;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.SubscriptionRepository;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionException;
import de.hshannover.f4.trust.visitmeta.util.PropertiesReaderWriter;
import de.hshannover.f4.trust.visitmeta.util.yaml.ConnectionPersister;

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
	/**
	 * Configuration class for persistent IF-MAPS Connections.
	 */
	private static ConnectionPersister mConnectionPersister;

	private static RestService restService;

	private static Thread restServiceThread;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		log.info("application started");

		initComponents();
		startRestService();

		try {
			loadPersistentConnections();
		} catch (FileNotFoundException e) {
			log.error("error while load persistent connections", e);
		}

		try {
			startupConnect();
		} catch (ConnectionException e) {
			log.error("error while startupConnect", e);
		}

		try {
			startupDump();
		} catch (ConnectionException e) {
			log.error("error while startupDump", e);
		}

		log.info("dataservice started successfully");
	}

	private static void startupDump() throws ConnectionException {
		log.debug("startupDump...");
		ConnectionManager.startupDump();
	}

	private static void startupConnect() throws ConnectionException {
		log.debug("startupConnect...");
		ConnectionManager.startupConnect();
	}

	private static void loadPersistentConnections() throws FileNotFoundException {
		log.info("load persistent connections");
		Map<String, Connection> connectionList = mConnectionPersister.load();

		for(Connection c: connectionList.values()){
			try {
				ConnectionManager.add(c);
			} catch (ConnectionException e) {
				log.error("error while adding connection to the connection pool", e);
			}
		}
	}

	private static void startRestService() {
		log.info("start RestService");
		restService = new RestService();
		restServiceThread = new Thread(restService, "RestService-Thread");

		restServiceThread.start();
	}

	public static void initComponents() {
		try {

			String dbConfig = Application.class.getClassLoader().getResource("config.properties").getPath();
			String ifmapConfig = Application.class.getClassLoader().getResource("config.properties").getPath();
			String dsConfig = Application.class.getClassLoader().getResource("config.properties").getPath();
			String connections = Application.class.getClassLoader().getResource("connections.yml").getPath();
			mDBConfig = new PropertiesReaderWriter(dbConfig, false);
			mIFMAPConfig = new PropertiesReaderWriter(ifmapConfig, false);
			mDSConfig = new PropertiesReaderWriter(dsConfig, false);
			mConnectionPersister = new ConnectionPersister(connections);

			log.info("components initialized");

		} catch (IOException e) {
			String msg = "Error while reading the config files";
			log.fatal(msg);
			throw new RuntimeException(msg, e);
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

	/**
	 * @return
	 */
	public static ConnectionPersister getConnectionPersister() {
		if (mConnectionPersister == null) {
			throw new RuntimeException(
					"ConnectionsConfig has not been initialized. This is not good!");
		}
		return mConnectionPersister;
	}

}
