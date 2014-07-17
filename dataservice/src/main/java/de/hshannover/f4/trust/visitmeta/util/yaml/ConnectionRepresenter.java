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
 * This file is part of visitmeta dataservice, version 0.1.0,
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import de.hshannover.f4.trust.visitmeta.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.util.yaml.YamlPersist.OPTIONAL;
import de.hshannover.f4.trust.visitmeta.util.yaml.YamlPersist.REQUIRED;

public class ConnectionRepresenter extends Representer {

	private static final Logger log = Logger.getLogger(ConnectionRepresenter.class);

	public static boolean mMinOutput = true;

	public ConnectionRepresenter() {
		this.representers.put(Connection.class, new RepresentConnection());
	}

	/**
	 * When the key start with 'm' and a upper case is follow then remove 'm' and start with lower case.
	 * 
	 * @param key
	 * @return
	 */
	private String transformKey(String key){
		if(key.charAt(0) == 'm' && Character.isUpperCase(key.charAt(1))){
			char lowerFirstChar = Character.toLowerCase(key.charAt(1));
			StringBuilder sb = new StringBuilder();
			sb.append(lowerFirstChar);
			sb.append(key.substring(2));
			return sb.toString();
		}
		return key;
	}

	private boolean same(Object one, Object other){
		if (one != null){
			return one.equals(other);
		}else if (other != null) {
			return other.equals(one);
		} else {
			return true;
		}
	}

	public boolean isMinoutput() {
		return mMinOutput;
	}

	public void setMinoutput(boolean maxOutput) {
		mMinOutput = maxOutput;
	}

	private class RepresentConnection implements Represent {

		/**
		 * Return a node with a key value map for all fields with the annotation REQUIRED or OPTIONAL
		 * in Connection.class. Optional field only add to the map, when the value is different to the
		 * default value or maxOutput is true. The default value field name for the optional field must be set.
		 */
		@Override
		public Node representData(Object data) {
			Map<String, Object> dataMap = new HashMap<String, Object>();

			if(data instanceof Connection){
				Connection connection = (Connection) data;

				for (Field field: Connection.class.getDeclaredFields()){
					// find all REQUIRED and OPTIONAL annotations fields
					for(int i=0; i<field.getAnnotations().length; i++){
						try{

							if(field.getAnnotations()[i] instanceof REQUIRED){
								field.setAccessible(true);
								Object fieldValue = field.get(connection);
								dataMap.put(transformKey(field.getName()), fieldValue);

							}else if(field.getAnnotations()[i] instanceof OPTIONAL){
								field.setAccessible(true);
								OPTIONAL optionalAnnotation = (OPTIONAL) field.getAnnotations()[i];
								Object fieldValue = field.get(connection);
								Object oTmp = Connection.class.getField(optionalAnnotation.value()).get(connection);
								if(!same(fieldValue, oTmp) || !mMinOutput){
									dataMap.put(transformKey(field.getName()), fieldValue);
								}

							}

						} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
							log.error("Error while get the optional or required fields from the Connection object.", e);
						}
					}
				}

				// save subscribes
				List<JSONObject> jsonList = connection.getSubscribeList();
				List<Map<String, Object>> subscribeList = new ArrayList<Map<String,Object>>();
				for(JSONObject json: jsonList){
					Map<String,Object> subscribeMap = new TreeMap<String,Object>();
					Iterator<String> i = json.keys();
					while(i.hasNext()){
						String jKey = i.next();
						subscribeMap.put(jKey, json.opt(jKey));
					}
					subscribeList.add(subscribeMap);
				}
				dataMap.put("subscribeList", subscribeList);
			}

			Node nodeMap = representMapping(new Tag(ConnectionPersister.CONNECTION_TAG), dataMap, false);
			return nodeMap;
		}
	}

}
