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
 * This file is part of visitmeta dataservice, version 0.0.6,
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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

import de.hshannover.f4.trust.visitmeta.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.ifmap.exception.ConnectionException;
import de.hshannover.f4.trust.visitmeta.util.yaml.YamlPersist.OPTIONAL;

public class ConnectionConstructor extends Constructor {

	private static final Logger log = Logger.getLogger(ConnectionConstructor.class);

	private boolean mConnectionAsMap = false;

	public ConnectionConstructor() {
		this.yamlConstructors.put(new Tag(ConnectionPersister.CONNECTION_TAG), new ConstructConnection());
	}

	/**
	 * When the key not start with 'm' or a upper case not follow then put 'm' to begin at the key and transform
	 * the next case to upper case.
	 * 
	 * @param key
	 * @return
	 */
	private String transformKey(String key){
		if(key.charAt(0) != 'm' || !Character.isUpperCase(key.charAt(1))){
			char upperSecondChar = Character.toUpperCase(key.charAt(0));
			StringBuilder sb = new StringBuilder("m");
			sb.append(upperSecondChar);
			sb.append(key.substring(1));
			return sb.toString();
		}
		return key;
	}

	private Map<String, Object> getKeyTransformedMapping(Node node) {
		Map<Object, Object> mapping = super.constructMapping((MappingNode) node);
		Map<String, Object> keyTransformMap = new HashMap<String, Object>();

		for(Entry<Object, Object> e: mapping.entrySet()){
			String tmpKey = transformKey((String) e.getKey());
			Object tmpValue = e.getValue();
			keyTransformMap.put(tmpKey, tmpValue);
		}
		return keyTransformMap;
	}

	public boolean isConnectionAsMap() {
		return mConnectionAsMap;
	}

	public void setConnectionAsMap(boolean b) {
		mConnectionAsMap = b;
	}

	private class ConstructConnection extends AbstractConstruct {

		/**
		 * Build a Connection from the node with REQUIRED and OPTIONAL fields.
		 */
		@Override
		public Object construct(Node node) {
			Map<String, Object> connectionDataMap = getKeyTransformedMapping(node);

			if(!mConnectionAsMap){
				return constructConnection(connectionDataMap);
			}else{
				return constructMap(connectionDataMap);
			}
		}

		@SuppressWarnings("unchecked")
		private Connection constructConnection(Map<String, Object> connectionDataMap){
			String connectionName = (String) connectionDataMap.get("mConnectionName");
			String url = (String) connectionDataMap.get("mUrl");
			String userName = (String) connectionDataMap.get("mUserName");
			String userPass = (String) connectionDataMap.get("mUserPass");

			Connection c = null;
			try {
				c = new Connection(connectionName, url, userName, userPass);
			} catch (ConnectionException e) {
				log.error("Error while create a new Connection.", e);
			}

			for (Field field: Connection.class.getDeclaredFields()){
				try {

					if(field.isAnnotationPresent(OPTIONAL.class)){
						field.setAccessible(true);
						String fieldName = field.getName();
						if (connectionDataMap.containsKey(fieldName)){
							field.set(c, connectionDataMap.get(fieldName));
						}
					}

				} catch (IllegalArgumentException | IllegalAccessException e) {
					log.error("Error while set the optional fields in the Connection object.", e);
				}
			}

			Object o = connectionDataMap.get("mSubscribeList");
			if(o instanceof List){
				List<Object> subscribeList = (List<Object>)o;
				for(Object listObject: subscribeList){
					if(listObject instanceof Map){
						JSONObject json = new JSONObject((Map)listObject);
						c.addSubscribe(json);
					}
				}

			}

			return c;
		}

		private Map<String, String> constructMap(Map<String, Object> connectionDataMap){
			Map<String, String> newMap = new HashMap<String, String>();
			for(Entry<String, Object> e: connectionDataMap.entrySet()){
				newMap.put(e.getKey(), e.getValue().toString());
			}
			return newMap;
		}
	}
}
