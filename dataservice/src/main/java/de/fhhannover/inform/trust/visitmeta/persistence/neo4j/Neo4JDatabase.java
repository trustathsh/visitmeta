package de.fhhannover.inform.trust.visitmeta.persistence.neo4j;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.dataservice.Application;
import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.DummyGraphCache;
import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.GraphCache;
import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.SimpleGraphCache;
import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.fhhannover.inform.trust.visitmeta.dataservice.util.ConfigParameter;
import de.fhhannover.inform.trust.visitmeta.ifmap.Connection;
import de.fhhannover.inform.trust.visitmeta.persistence.Reader;
import de.fhhannover.inform.trust.visitmeta.persistence.ThreadedWriter;
import de.fhhannover.inform.trust.visitmeta.persistence.Writer;
import de.fhhannover.inform.trust.visitmeta.util.PropertiesReaderWriter;

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
		neo4jDb = new Neo4JConnection(neo4JdbPath + "/" + connectionName);

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
