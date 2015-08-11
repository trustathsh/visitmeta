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
 * This file is part of visitmeta-common, version 0.5.1,
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
package de.hshannover.f4.trust.visitmeta.util;

import java.util.Iterator;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;

/**
 * A simple helper Class
 * 
 * @author Marcel Reichenbach
 */
public class Check {

	/**
	 * If obj is null throw a {@link NullPointerException} with message.
	 * 
	 * @param obj any Object
	 * @param msg NullPointer-Error massage
	 * @throws NullPointerException with massage msg
	 */
	public static void ifNull(Object obj, String msg) throws NullPointerException {
		if (obj == null) {
			throw new NullPointerException(msg);
		}
	}

	/**
	 * Check if the JSONObject contains the JSONEventKey.
	 * 
	 * @param jsonData
	 * @param jsonKey
	 * @return true, if the jsonEvent contains the JSONEventKey, otherwise false.
	 * @throws JSONException
	 */
	public static boolean containsJSONEventKey(JSONObject jsonData, JSONDataKey jsonKey) throws JSONException {
		try {

			jsonKey.getContainingString(jsonData);

		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;

	}

	/**
	 * Check if the JSONObject contains the JSON-Key. If not throws JSONHandlerException.
	 * 
	 * @param jsonData
	 * @param jsonKey
	 * @throws JSONHandlerException
	 * @throws JSONException
	 */
	public static void existJSONKey(JSONObject jsonData, JSONDataKey jsonKey) throws JSONHandlerException,
			JSONException {
		if (!containsJSONEventKey(jsonData, jsonKey)) {
			throw new JSONHandlerException(String.format(
					JSONHandlerException.ERROR_MSG_JSON_OBJECT_MISSING_JSON_EVENT_KEY, jsonKey));
		}
	}

	/**
	 * Check if the JSONObject contains all default {@link JSONDataKey} (NAME). If not throws
	 * JSONHandlerException.
	 * 
	 * @param jsonData
	 * @throws JSONHandlerException
	 * @throws JSONException
	 */
	public static void defaultJSONKeys(JSONObject jsonData) throws JSONHandlerException, JSONException {
		existJSONKey(jsonData, JSONDataKey.NAME);
	}

	/**
	 * All timeStamps which are smaller than presenceTimeStamp must have the same changes and contains in both
	 * changeMaps.
	 * 
	 * @param presenceTimeStamp
	 * @param knownChangeMap
	 * @param newChangeMap
	 * @return
	 */
	public static boolean chageMapHasChangeInThePast(long presenceTimeStamp, Map<Long, Long> knownChangeMap,
			Map<Long, Long> newChangeMap) {
		Iterator<Long> iterator = newChangeMap.keySet().iterator();
		while (iterator.hasNext()) {
			Long key = iterator.next();
			if (presenceTimeStamp >= key) {
				if (knownChangeMap.get(key) == null
						|| knownChangeMap.get(key).longValue() != newChangeMap.get(key).longValue()) {
					return true;
				}
			} else {
				return false;
			}
		}
		return false;
	}

}
