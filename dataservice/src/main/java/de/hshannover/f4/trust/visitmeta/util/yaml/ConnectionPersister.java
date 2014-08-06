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

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Tag;

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
		mConnectionRepresenter.addClassTag(JSONObject.class, Tag.MAP);
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
