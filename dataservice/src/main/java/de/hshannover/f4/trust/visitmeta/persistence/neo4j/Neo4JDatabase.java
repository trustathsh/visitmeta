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
 * This file is part of visitmeta dataservice, version 0.1.1,
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
package de.hshannover.f4.trust.visitmeta.persistence.neo4j;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.DummyGraphCache;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.GraphCache;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.SimpleGraphCache;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.hshannover.f4.trust.visitmeta.dataservice.util.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.persistence.Reader;
import de.hshannover.f4.trust.visitmeta.persistence.ThreadedWriter;
import de.hshannover.f4.trust.visitmeta.persistence.Writer;
import de.hshannover.f4.trust.visitmeta.util.PropertiesReaderWriter;

public class Neo4JDatabase {

	private Logger log = Logger.getLogger(Connection.class);

	private PropertiesReaderWriter config = Application.getIFMAPConfig();

	private boolean clearDatabase = Boolean.parseBoolean(config.getProperty(ConfigParameter.NEO4J_CLEAR_DB_ON_STARTUP));

	private boolean dbCaching = Boolean.parseBoolean(config.getProperty(ConfigParameter.DS_CACHE_ENABLE));

	private int dbCachSize = Integer.parseInt(config.getProperty(ConfigParameter.DS_CACHE_SIZE));

	private String neo4JdbPath = config.getProperty(ConfigParameter.NEO4J_DB_PATH);

	private Neo4JConnection neo4jDb;

	private Neo4JRepository neo4jRepo;

	private SimpleGraphService mGraphService;

	private ThreadedWriter mWriter;

	private Thread writerThread;

	private Reader mReader;


	public Neo4JDatabase(String connectionName){
		log.trace("new Neo4JDatabase() ...");

		initNeo4JConnection(connectionName);
		initWriter(connectionName);
		startWriter();
		initGraphService();

		log.trace("... new Neo4JDatabase() OK");
	}

	private void initNeo4JConnection(String connectionName) {
		neo4jDb = new Neo4JConnection(neo4JdbPath + "/connection/" + connectionName);

		if(clearDatabase){

			neo4jDb.ClearDatabase();
		}

		neo4jRepo = new Neo4JRepository(neo4jDb, Application.loadHashAlgorithm());
	}

	private void initWriter(String connectionName) {
		mWriter = new ThreadedWriter(new Neo4JWriter(neo4jRepo, neo4jDb));

		writerThread = new Thread(mWriter, "WriterThread-"+connectionName);
	}

	private void startWriter(){
		writerThread.start();

		log.info("Writer thread started");
	}

	private void initGraphService() {
		mReader = new Neo4JReader(neo4jRepo, neo4jDb);

		GraphCache cache = null;

		if (dbCaching) {
			cache =	new SimpleGraphCache(dbCachSize);
		} else {
			cache = new DummyGraphCache();
		}

		mGraphService = new SimpleGraphService(mReader, cache);
	}

	public Writer getWriter() {
		return mWriter;
	}

	public SimpleGraphService getGraphService() {
		return mGraphService;
	}
}
