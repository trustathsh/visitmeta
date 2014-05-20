package de.hshannover.f4.trust.visitmeta.util.yaml;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;

import de.hshannover.f4.trust.visitmeta.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.ifmap.ConnectionManager;

public class ConnectionPersister extends YamlPersister {

	private static final Logger log = Logger.getLogger(ConnectionPersister.class);

	public static final String CONNECTION_TAG = "!Connection";


	private String mFileName;

	private boolean mMinimalOutput;

	private boolean mAppend;


	private DumperOptions mOptions;

	private ConnectionRepresenter mConnectionRepresenter;

	private ConnectionConstructor mConnectionConstructor;


	/**
	 * Create a JyamlPersister for Connections with default minimalOutput = true
	 *
	 * @param fileName
	 * @param append
	 */
	public ConnectionPersister(String fileName){
		log.trace("new ConnectionPersister()...");
		mFileName = fileName;
		mAppend = false;
		mMinimalOutput = true;
		mConnectionRepresenter = new ConnectionRepresenter();
		mConnectionConstructor = new ConnectionConstructor();
		mOptions = buildDumperOptions();
	}

	private DumperOptions buildDumperOptions(){
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setExplicitStart(true);
		return options;
	}

	public void persistConnections() throws FileNotFoundException {
		Map<String, Connection> connectionMap = ConnectionManager.getConnectionPool();
		mConnectionRepresenter.setMinoutput(mMinimalOutput);
		persist(mFileName, connectionMap, mAppend, mConnectionRepresenter, mOptions);
	}

	public Map<String, Connection> load() throws FileNotFoundException {
		mConnectionConstructor.setConnectionAsMap(false);
		Map<String, Connection> newMap = new HashMap<String, Connection>();
		Map<String, Object> map = loadMap(mFileName, mConnectionConstructor);
		if(map != null){
			for(Entry<String, Object> e: map.entrySet()){
				newMap.put(e.getKey(), (Connection) e.getValue());
			}
		}
		return newMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Map<String, String>> loadAsMap() throws FileNotFoundException {
		mConnectionConstructor.setConnectionAsMap(true);
		Map<String, Map<String, String>> newMap = new HashMap<String, Map<String, String>>();
		Map<String, Object> map = loadMap(mFileName, mConnectionConstructor);
		if(map != null){
			for(Entry<String, Object> e: map.entrySet()){
				newMap.put(e.getKey(), (Map<String, String>) e.getValue());
			}
		}
		return newMap;
	}

	public void setMinimalOutput(boolean minimalOutput){
		mMinimalOutput = minimalOutput;
	}

	public boolean isMinimalOutput(){
		return mMinimalOutput;
	}
}
