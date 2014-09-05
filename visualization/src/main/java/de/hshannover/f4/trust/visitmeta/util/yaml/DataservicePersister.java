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
 * This file is part of visitmeta-visualization, version 0.2.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.util.yaml;

import java.io.IOException;
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

public class DataservicePersister {

	private static final Logger log = Logger.getLogger(DataservicePersister.class);

	public static final String DATASERVICE_TAG = "!Dataservice";


	private String mFileName;

	private boolean mMinimalOutput;


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

	public void add(DataserviceConnection connection) throws DataservicePersisterException, IOException {
		Map<String, DataserviceConnection> dataserviceMap = load();
		checkContains(connection, dataserviceMap);
		dataserviceMap.put(connection.getName(), connection);
		YamlWriter.persist(mFileName, dataserviceMap, mRepresenter, mOptions);
	}

	public void update(String oldDataserviceConnectionName, DataserviceConnection dataservice) throws DataservicePersisterException, IOException {
		Map<String, DataserviceConnection> dataserviceMap = load();
		DataserviceConnection oldDataservice = search(oldDataserviceConnectionName, dataserviceMap);
		dataserviceMap.remove(oldDataserviceConnectionName);
		if(oldDataservice != null){
			oldDataservice.update(dataservice);
			dataserviceMap.put(dataservice.getName(), dataservice);
			YamlWriter.persist(mFileName, dataserviceMap, mRepresenter, mOptions);
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

	public void remove(String dataserviceName) throws IOException{
		Map<String, DataserviceConnection> dataserviceList = load();
		dataserviceList.remove(search(dataserviceName, dataserviceList));
		YamlWriter.persist(mFileName, dataserviceList, mRepresenter, mOptions);
	}

	public Map<String, DataserviceConnection> load() throws IOException {
		Map<String, DataserviceConnection> newMap = new HashMap<String, DataserviceConnection>();
		Map<String, Object> map = YamlReader.loadMap(mFileName, mConstructor);
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
