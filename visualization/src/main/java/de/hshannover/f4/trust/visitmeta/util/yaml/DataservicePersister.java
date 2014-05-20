package de.hshannover.f4.trust.visitmeta.util.yaml;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

	public static void main(String[] args) throws FileNotFoundException {
		String file = DataservicePersister.class.getClassLoader().getResource("dataservices.yml").getPath();
		DataservicePersister persister = new DataservicePersister(file);
		List<DataserviceConnection> test = persister.load();
		System.out.println(test);

		if(test == null){

		}

		DataserviceConnection con = new DataserviceConnection("testCon", "TEstURL", true);

		try {
			persister.add(con);
		} catch (DataservicePersisterException e) {
			log.error(e.toString(), e);
		}

		List<DataserviceConnection> test2 = persister.load();
		System.out.println(test2);

		//		persister.remove(con.getName());

		List<DataserviceConnection> test3 = persister.load();
		System.out.println(test3);
	}


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
		List<DataserviceConnection> dataserviceList = load();
		checkContains(connection, dataserviceList);
		dataserviceList.add(connection);
		persist(mFileName, dataserviceList, mAppend, mRepresenter, mOptions);
	}

	public void update(String oldDataserviceConnectionName, DataserviceConnection dataservice) throws FileNotFoundException, DataservicePersisterException {
		List<DataserviceConnection> dataserviceList = load();
		if(!oldDataserviceConnectionName.equals(dataservice.getName())){
			checkContains(dataservice, dataserviceList);
		}
		find(oldDataserviceConnectionName, dataserviceList).update(dataservice);
		persist(mFileName, dataserviceList, mAppend, mRepresenter, mOptions);
	}

	private DataserviceConnection find(String dataserviceName, List<DataserviceConnection> dataserviceList){
		for(DataserviceConnection data: dataserviceList){
			if(data.getName().equals(dataserviceName)){
				return data;
			}
		}
		return null;
	}

	public void remove(String dataserviceName) throws FileNotFoundException{
		List<DataserviceConnection> dataserviceList = load();
		dataserviceList.remove(find(dataserviceName, dataserviceList));
		persist(mFileName, dataserviceList, mAppend, mRepresenter, mOptions);
	}

	public List<DataserviceConnection> load() throws FileNotFoundException {
		List<DataserviceConnection> newList = new ArrayList<DataserviceConnection>();
		List<Object> map = loadList(mFileName, mConstructor);
		if(map != null){
			for(Object e: map){
				if(e instanceof DataserviceConnection){
					newList.add((DataserviceConnection) e);
				}else{
					log.warn("Warn by load DataserviceConnection Set. Object is not a DataserviceConnection.");
				}
			}
		}
		return newList;
	}

	//	@SuppressWarnings("unchecked")
	//	public Map<String, Map<String, String>> loadAsMap() throws FileNotFoundException {
	//		mConnectionConstructor.setConnectionAsMap(true);
	//		Map<String, Map<String, String>> newMap = new HashMap<String, Map<String, String>>();
	//		Map<String, Object> map = loadMap(mFileName, mConnectionConstructor);
	//		for(Entry<String, Object> e: map.entrySet()){
	//			newMap.put(e.getKey(), (Map<String, String>) e.getValue());
	//		}
	//		return newMap;
	//	}

	public void setMinimalOutput(boolean minimalOutput){
		mMinimalOutput = minimalOutput;
	}

	public boolean isMinimalOutput(){
		return mMinimalOutput;
	}

	private void checkContains(DataserviceConnection connection, List<DataserviceConnection> dataserviceList) throws DataservicePersisterException {
		for(DataserviceConnection data: dataserviceList){
			if(data.equals(connection)){
				throw new DataservicePersisterException("No duplicate Data service Connections allowed");
			}
		}
	}
}
