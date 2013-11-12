package de.fhhannover.inform.trust.visitmeta.dataservice;

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
 * This file is part of VisITMeta, version 0.0.2, implemented by the Trust@FHH 
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


import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyWebContainerFactory;

import de.fhhannover.inform.trust.visitmeta.dataservice.factories.InMemoryIdentifierFactory;
import de.fhhannover.inform.trust.visitmeta.dataservice.factories.InMemoryMetadataFactory;
import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.DummyGraphCache;
import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.GraphCache;
import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.SimpleGraphCache;
import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.fhhannover.inform.trust.visitmeta.dataservice.util.ConfigParameter;
import de.fhhannover.inform.trust.visitmeta.ifmap.UpdateService;
import de.fhhannover.inform.trust.visitmeta.interfaces.GraphService;
import de.fhhannover.inform.trust.visitmeta.persistence.Reader;
import de.fhhannover.inform.trust.visitmeta.persistence.ThreadedWriter;
import de.fhhannover.inform.trust.visitmeta.persistence.Writer;
import de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JConnection;
import de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JReader;
import de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JRepository;
import de.fhhannover.inform.trust.visitmeta.persistence.neo4j.Neo4JWriter;
import de.fhhannover.inform.trust.visitmeta.util.PropertiesReaderWriter;

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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.info("Application started");
		initComponents();
		log.info("Components initialized");
		Thread updateThread = new Thread(mUpdateService, "UpdateThread");
		updateThread.start();
		log.info("UpdateService started");
		Thread writerThread = new Thread(mWriter, "WriterThread");
		writerThread.start();
		log.info("Writer thread started");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if (getDSConfig().getProperty(ConfigParameter.DS_REST_ENABLE).equalsIgnoreCase("true"))
			startRestService();
		log.info("Dataservice started successfully");
		try {
			updateThread.join();
			writerThread.join();
		} catch (InterruptedException e) {
			log.fatal("Main thread got interrupted waiting for updateService");
			e.printStackTrace();
		}
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
		if (getDSConfig().getProperty(ConfigParameter.DS_REST_ENABLE).equalsIgnoreCase("true"))
			startRestService();
		log.info("Dataservice started successfully");
		return mGraphService;
	}

	private static void startRestService() {
		final String url  = Application.getDSConfig().getProperty(ConfigParameter.DS_REST_URL);
		final Map<String, String> params = new HashMap<String, String>();

		params.put("com.sun.jersey.config.property.packages",
				"de.fhhannover.inform.trust.visitmeta.dataservice.rest");

		log.info("starting REST service on "+url+"...");

		try {
			HttpServer server = GrizzlyWebContainerFactory.create(url, params);
			log.debug("REST service running.");
			// TODO shutdown server properly
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 *
	 */
	private static void initComponents() {
		try {
			String dbConfig = Application.class.getClassLoader().
					getResource("config.properties").getPath();
			String ifmapConfig = Application.class.getClassLoader().
					getResource("config.properties").getPath();
			String dsConfig = Application.class.getClassLoader().
					getResource("config.properties").getPath();
			mDBConfig = new PropertiesReaderWriter(dbConfig, false);
			mIFMAPConfig = new PropertiesReaderWriter(ifmapConfig, false);
			mDSConfig = new PropertiesReaderWriter(dsConfig, false);
		} catch (IOException e) {
			String msg = "Error while reading the config files";
			log.fatal(msg);
			throw new RuntimeException(msg, e);
		}

		Neo4JConnection db = new Neo4JConnection(
				mDBConfig.getProperty(ConfigParameter.NEO4J_DB_PATH));
		if (mDBConfig.getProperty(ConfigParameter.NEO4J_CLEAR_DB_ON_STARTUP)
				.equalsIgnoreCase("true")) {
			db.ClearDatabase();
		}
		Neo4JRepository repo = new Neo4JRepository(db, loadHashAlgorithm());
		mWriter = new ThreadedWriter(new Neo4JWriter(repo, db));
		mReader = new Neo4JReader(repo, db);

		mUpdateService = new UpdateService(mWriter,
				new InMemoryIdentifierFactory(), new InMemoryMetadataFactory());
		GraphCache cache = null;
		if (mDSConfig.getProperty(ConfigParameter.DS_CACHE_ENABLE).equalsIgnoreCase("true")) {
			cache =
					new SimpleGraphCache(
							Integer.parseInt(mDSConfig.getProperty(ConfigParameter.DS_CACHE_SIZE)));
		} else {
			cache = new DummyGraphCache();
		}
		mGraphService = new SimpleGraphService(mReader, cache);
	}

	private static MessageDigest loadHashAlgorithm() {
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
		if (mGraphService == null)
			throw new RuntimeException(
					"GraphService has not been initialized. This is not good!");
		return mGraphService;
	}
	
	/**
	 * @return
	 */
	public static UpdateService getUpdateService() {
		if (mUpdateService == null)
			throw new RuntimeException(
					"UpdateService has not been initialized. This is not good!");
		return mUpdateService;
	}

	/**
	 * @return
	 */
	public static Writer getWriter() {
		if (mWriter == null)
			throw new RuntimeException(
					"Writer has not been initialized. This is not good!");
		return mWriter;
	}

	/**
	 * @return
	 */
	public static Reader getReader() {
		if (mReader == null)
			throw new RuntimeException(
					"Reader has not been initialized. This is not good!");
		return mReader;
	}

	/**
	 * @return
	 */
	public static PropertiesReaderWriter getDBConfig() {
		if (mDBConfig == null)
			throw new RuntimeException(
					"DBConfig has not been initialized. This is not good!");
		return mDBConfig;
	}

	/**
	 * @return
	 */
	public static PropertiesReaderWriter getIFMAPConfig() {
		if (mIFMAPConfig == null)
			throw new RuntimeException(
					"IFMAPConfig has not been initialized. This is not good!");
		return mIFMAPConfig;
	}

	/**
	 * @return
	 */
	public static PropertiesReaderWriter getDSConfig() {
		if (mDSConfig == null)
			throw new RuntimeException(
					"DSConfig has not been initialized. This is not good!");
		return mDSConfig;
	}

}
