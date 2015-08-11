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
 * This file is part of visitmeta-dataservice, version 0.5.1,
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
package de.hshannover.f4.trust.visitmeta.dataservice.graphservice;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple Holder class that contains a prefix map for each Connection
 * 
 * @author oelsner
 *
 */
public class NamespaceHolder {
	private static Map<String, HashMap<String, String>> maps = new HashMap<String, HashMap<String, String>>();

	/**
	 * 
	 * @param connection
	 * @param prefix
	 * @param value
	 */
	public static void addPrefix(String connection, String prefix, String value) {
		if(!maps.containsKey(connection)) {
			maps.put(connection, new HashMap<String,String>());
		}
		if (maps.get(connection).containsKey(prefix)) {
			if (!maps.get(connection).get(prefix).equals(value)) {
				// todo warning same prefix with different value
			}
		} else {
			maps.get(connection).put(prefix, value);
		}
	}

	/**
	 * 
	 * @param connection
	 * @return Prefix map of the given Connection name
	 */
	public static Map<String, String> getPrefixMap(String connection) {
		if(!maps.containsKey("standard")) {
			HashMap<String, String> standard = new HashMap<String, String>();
			standard.put("meta", "http://www.trustedcomputinggroup.org/2010/IFMAP-METADATA/2");
			maps.put("standard", standard);
		}
		if(maps.containsKey(connection)) {
			return maps.get(connection);
		}
		return maps.get("standard");
	}
}
