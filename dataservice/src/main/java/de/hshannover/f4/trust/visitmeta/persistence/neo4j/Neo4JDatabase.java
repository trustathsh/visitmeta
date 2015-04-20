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
 * This file is part of visitmeta-dataservice, version 0.4.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
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

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.DummyGraphCache;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.GraphCache;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.SimpleGraphCache;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.hshannover.f4.trust.visitmeta.persistence.Executor;
import de.hshannover.f4.trust.visitmeta.persistence.Reader;
import de.hshannover.f4.trust.visitmeta.persistence.ThreadedWriter;
import de.hshannover.f4.trust.visitmeta.persistence.Writer;

public class Neo4JDatabase {

	private Logger log = Logger.getLogger(Neo4JDatabase.class);

	private Properties mConfig = Application.getConfig();

	private boolean mClearDatabase;

	private boolean mDbCaching;

	private int mDbCachSize;

	private String mNeo4JdbPath;

	private Neo4JConnection neo4jDb;

	private Neo4JRepository neo4jRepo;

	private SimpleGraphService mGraphService;

	private ThreadedWriter mWriter;

	private Thread writerThread;

	private Reader mReader;

	private Executor mExecutor;

	public Neo4JDatabase(String connectionName) {
		log.trace("new Neo4JDatabase() ...");

		try {
			mClearDatabase = mConfig.getBoolean("database.erase");
			mDbCaching = mConfig.getBoolean("database.cache.enable");
			mDbCachSize = mConfig.getInt("database.cache.size");
			mNeo4JdbPath = mConfig.getString("database.path");
		} catch (PropertyException e) {
			log.fatal(e.toString(), e);
			throw new RuntimeException("could not load requested properties", e);
		}

		initNeo4JConnection(connectionName);
		initWriter(connectionName);
		startWriter();
		initGraphService();

		log.trace("... new Neo4JDatabase() OK");
	}

	private void initNeo4JConnection(String connectionName) {
		neo4jDb = new Neo4JConnection(mNeo4JdbPath + "/connection/"
				+ connectionName);

		if (mClearDatabase) {

			neo4jDb.ClearDatabase();
		}

		neo4jRepo = new Neo4JRepository(neo4jDb,
				Application.loadHashAlgorithm());
	}

	private void initWriter(String connectionName) {
		mWriter = new ThreadedWriter(new Neo4JWriter(neo4jRepo, neo4jDb));

		writerThread = new Thread(mWriter, "WriterThread-" + connectionName);
	}

	private void startWriter() {
		writerThread.start();

		log.info("Writer thread started");
	}

	private void initGraphService() {
		mReader = new Neo4JReader(neo4jRepo, neo4jDb);
		mExecutor = new Neo4JExecutor(neo4jDb);

		GraphCache cache = null;

		if (mDbCaching) {
			cache = new SimpleGraphCache(mDbCachSize);
		} else {
			cache = new DummyGraphCache();
		}

		mGraphService = new SimpleGraphService(mReader, mExecutor, cache);
	}

	public Writer getWriter() {
		return mWriter;
	}

	public SimpleGraphService getGraphService() {
		return mGraphService;
	}
}
