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
 * This file is part of visitmeta dataservice, version 0.0.7,
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



import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_HASH;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.NODE_TYPE_KEY;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.util.FileUtils;

/**
 * Connection handling for Neo4J database.
 *
 * @author rosso
 *
 */
public class Neo4JConnection {
	private String mDbPath;
	private GraphDatabaseService mGraphDb;
	private Neo4JTimestampManager mTimestampManager;
	private static final Logger log = Logger.getLogger(Neo4JConnection.class);
/**
 * Establish a connection to the database.
 * @param dbPath Path to the Neo4J database.
 */
	public Neo4JConnection(String dbPath) {
		this();
		mDbPath = dbPath;
		mGraphDb = new GraphDatabaseFactory().
				newEmbeddedDatabaseBuilder(mDbPath).
				setConfig( GraphDatabaseSettings.node_keys_indexable, NODE_TYPE_KEY +","+ KEY_HASH ).
				setConfig( GraphDatabaseSettings.node_auto_indexing, "true" ).
				newGraphDatabase();
		registerShutdownHook(mGraphDb);

		log.trace("... new Neo4JConnection() OK");
	}

	private Neo4JConnection() {
		log.trace("new Neo4JConnection() ...");
		mTimestampManager = new Neo4JTimestampManager(this);
	}

	public GraphDatabaseService getConnection() {
		return mGraphDb;
	}

	public Neo4JTimestampManager getTimestampManager() {
		return mTimestampManager;
	}

	/**
	 * Clears all data in the DB and reconnects to it afterwards.
	 */
	public void ClearDatabase(){
		// Shutdown database before erasing it
		log.debug("Shutting down the database");
		mGraphDb.shutdown();
		try
        {
			log.debug("Erasing the database files");
            FileUtils.deleteRecursively( new File( mDbPath ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
		// Restart database
		log.debug("Reconnecting to database");
		mGraphDb = new GraphDatabaseFactory().
				newEmbeddedDatabaseBuilder(mDbPath).
				setConfig( GraphDatabaseSettings.node_keys_indexable, NODE_TYPE_KEY +","+ KEY_HASH ).
				setConfig( GraphDatabaseSettings.node_auto_indexing, "true" ).
				newGraphDatabase();
		registerShutdownHook(mGraphDb);
	}

	private void registerShutdownHook( final GraphDatabaseService graphDb ) {
		// Shutdown the server if closed unexpectedly
		Runtime.getRuntime().addShutdownHook( new Thread() {
			@Override
			public void run() {
				log.error("Database was forced to shut down");
				graphDb.shutdown();
			}
		} );
	}
}
