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

import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.hshannover.f4.trust.visitmeta.dataservice.rest.RestService;
import de.hshannover.f4.trust.visitmeta.dataservice.util.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.ifmap.ConnectionManager;
import de.hshannover.f4.trust.visitmeta.ifmap.UpdateService;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.persistence.Reader;
import de.hshannover.f4.trust.visitmeta.persistence.ThreadedWriter;
import de.hshannover.f4.trust.visitmeta.persistence.Writer;
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
	 * Interface to obtain IF-Map data graphs.
	 */
	private static SimpleGraphService mGraphService;
	/**
	 * Interface to submit raw IF-Map data into the database.
	 */
	private static ThreadedWriter mWriter;
	/**
	 * Interface to read raw IF-Map data from the database.
	 */
	private static Reader mReader;
	/**
	 * Service that subscribes to an IF-Map server to gather IF-Map data.
	 */
	private static UpdateService mUpdateService;
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

		log.info("Dataservice started successfully");

	}

	public static GraphService initDataservice() {
		log.info("Application started");
		initComponents();
		log.info("Components initialized");
		Thread updateThread = new Thread(mUpdateService, "UpdateThread");
		updateThread.start();
		log.info("UpdateService started");
		Thread writerThread = new Thread(mWriter, "WriterThread");
		writerThread.start();
		log.info("Writer thread started");
		if (getDSConfig().getProperty(ConfigParameter.DS_REST_ENABLE).equalsIgnoreCase("true")) {

			restServiceThread.start();
		}
		log.info("Dataservice started successfully");
		return mGraphService;
	}

	/**
	 *
	 */
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

		restService = new RestService();
		restServiceThread = new Thread(restService, "RestService-Thread");

		restServiceThread.start();

		log.info("save default connection");

		String TRUSTSTORE_PATH = getIFMAPConfig().getProperty(ConfigParameter.IFMAP_TRUSTSTORE_PATH);
		String IFMAP_BASIC_AUTH_URL = getIFMAPConfig().getProperty(ConfigParameter.IFMAP_BASIC_AUTH_URL);
		String IFMAP_USER = getIFMAPConfig().getProperty(ConfigParameter.IFMAP_USER);
		String IFMAP_PASS = getIFMAPConfig().getProperty(ConfigParameter.IFMAP_PASS);

		ConnectionManager.newConnection("default", IFMAP_BASIC_AUTH_URL, IFMAP_USER, IFMAP_PASS, TRUSTSTORE_PATH, IFMAP_PASS);

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
	public static SimpleGraphService getGraphservice() {
		if (mGraphService == null) {
			throw new RuntimeException(
					"GraphService has not been initialized. This is not good!");
		}
		return mGraphService;
	}

	/**
	 * @return
	 */
	public static UpdateService getUpdateService() {
		if (mUpdateService == null) {
			throw new RuntimeException(
					"UpdateService has not been initialized. This is not good!");
		}
		return mUpdateService;
	}

	/**
	 * @return
	 */
	public static Writer getWriter() {
		if (mWriter == null) {
			throw new RuntimeException(
					"Writer has not been initialized. This is not good!");
		}
		return mWriter;
	}

	/**
	 * @return
	 */
	public static Reader getReader() {
		if (mReader == null) {
			throw new RuntimeException(
					"Reader has not been initialized. This is not good!");
		}
		return mReader;
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
