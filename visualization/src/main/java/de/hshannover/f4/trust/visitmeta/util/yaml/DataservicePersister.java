package de.hshannover.f4.trust.visitmeta.util.yaml;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;

public class DataservicePersister extends YamlPersister {

	private static final Logger log = Logger.getLogger(DataservicePersister.class);

	public static final String DATASERVICE_TAG = "!Dataservice";


	private String mFileName;

	private boolean mMinimalOutput;

	private boolean mAppend;


	private DumperOptions mOptions;

	private Representer mRepresenter;

	private Constructor mConstructor;

	/**
	 * Create a JyamlPersister for Dataservices with default minimalOutput = true
	 *
	 * @param fileName
	 * @param append
	 */
	public DataservicePersister(String fileName){
		log.trace("new DataservicePersister()...");
		mFileName = fileName;
		mAppend = false;
		mMinimalOutput = true;
		mOptions = buildDumperOptions();
		mRepresenter = new Representer();
		mConstructor = new Constructor();
		mConstructor.addTypeDescription(new TypeDescription(DataserviceConnection.class, DATASERVICE_TAG));
		mRepresenter.addClassTag(DataserviceConnection.class, new Tag(DATASERVICE_TAG));
	}

	private DumperOptions buildDumperOptions(){
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setExplicitStart(true);
		return options;
	}

	public void add(DataserviceConnection connection) throws FileNotFoundException, DataservicePersisterException {
		Map<String, DataserviceConnection> dataserviceMap = load();
		checkContains(connection, dataserviceMap);
		dataserviceMap.put(connection.getName(), connection);
		persist(mFileName, dataserviceMap, mAppend, mRepresenter, mOptions);
	}

	public void update(String oldDataserviceConnectionName, DataserviceConnection dataservice) throws FileNotFoundException, DataservicePersisterException {
		Map<String, DataserviceConnection> dataserviceMap = load();
		DataserviceConnection oldDataservice = search(oldDataserviceConnectionName, dataserviceMap);
		dataserviceMap.remove(oldDataserviceConnectionName);
		if(oldDataservice != null){
			oldDataservice.update(dataservice);
			dataserviceMap.put(dataservice.getName(), dataservice);
			persist(mFileName, dataserviceMap, mAppend, mRepresenter, mOptions);
		}
	}

	private DataserviceConnection search(String dataserviceName, Map<String, DataserviceConnection> dataserviceMap){
		DataserviceConnection dataservice = dataserviceMap.get(dataserviceName);
		if(dataservice != null){
			return dataservice;
		}else{
			log.warn("Warn by search DataserviceConnection, is null.");
		}
		return null;
	}

	public void remove(String dataserviceName) throws FileNotFoundException{
		Map<String, DataserviceConnection> dataserviceList = load();
		dataserviceList.remove(search(dataserviceName, dataserviceList));
		persist(mFileName, dataserviceList, mAppend, mRepresenter, mOptions);
	}

	public Map<String, DataserviceConnection> load() throws FileNotFoundException {
		Map<String, DataserviceConnection> newMap = new HashMap<String, DataserviceConnection>();
		Map<String, Object> map = loadMap(mFileName, mConstructor);
		if(map != null){
			for(Entry<String, Object> entry: map.entrySet()){
				if(entry.getValue() instanceof DataserviceConnection){
					newMap.put(entry.getKey(), (DataserviceConnection) entry.getValue());
				}else{
					log.warn("Warn by load DataserviceConnection Set. Object is not a DataserviceConnection.");
				}
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

	private void checkContains(DataserviceConnection connection, Map<String, DataserviceConnection> dataserviceMap) throws DataservicePersisterException {
		for(DataserviceConnection data: dataserviceMap.values()){
			if(data.equals(connection)){
				throw new DataservicePersisterException("No duplicate Data service Connections allowed");
			}
		}
	}
}
