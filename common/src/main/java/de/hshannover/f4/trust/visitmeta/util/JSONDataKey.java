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
 * This file is part of visitmeta-common, version 0.5.2,
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

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public enum JSONDataKey {

	NAME("name", "connectionName", "subscriptionName", "connection_Name", "subscription_Name"),
	USE_AS_STARTUP("useAsStartup", "use_As_Startup"),

	// connection keys
	USER_NAME("userName", "user_Name"),
	USER_PASSWORD("userPassword", "user_Password"),
	AUTHENTICATION_BASIC("authenticationBasic", "authentication_Basic"),
	AUTHENTICATION_CERT("authenticationCert", "authentication_Cert"),
	IFMAP_SERVER_URL("ifmapServerUrl", "ifmap_Server_Url"),
	TRUSTSTORE_PATH("truststorePath", "truststore_Path"),
	TRUSTSTORE_PASSWORD("truststorePassword", "truststore_Password"),
	MAX_POLL_RESULT_SIZE("maxPollResultSize", "max_Poll_Result_Size"),
	SUBSCRIPTIONS("subscriptions"),
	IS_CONNECTED("isConnected", "is_Connected"),

	// subscription keys
	START_IDENTIFIER("startIdentifier", "start_Identifier"),
	IDENTIFIER_TYPE("identifierType", "identifier_Type"),
	MATCH_LINKS_FILTER("matchLinksFilter", "match_Links_Filter"),
	RESULT_FILTER("resultFilter", "result_Filter"),
	TERMINAL_IDENTIFIER_TYPES("terminalIdentifierTypes", "terminal_Identifier_Types"),
	MAX_DEPTH("maxDepth", "max_Depth"),
	MAX_SIZE("maxSize", "max_Size"),
	IS_ACTIVE("isActive", "is_Active");

	private String[] mValues;

	JSONDataKey(String... values) {
		this.mValues = values;
	}

	/**
	 * Return the JSONDataKey and ignore case.
	 * 
	 * @param dataKey JSONDataKey String
	 * @return JSONDataKey
	 * @throws IllegalArgumentException if no Enum specified for this key
	 */
	public static JSONDataKey getJSONDataKey(String dataKey) {
		Check.ifNull(dataKey, "For methode getJSONEventKey(String) the String is null!");
		for (JSONDataKey b : JSONDataKey.values()) {
			for (String value : b.mValues) {
				if (dataKey.equalsIgnoreCase(value)) {
					return b;
				}
			}
		}
		throw new IllegalArgumentException("No JSONDataKey specified for this string('" + dataKey + "')");
	}

	/**
	 * Get the right key-String for this JSONDataKey
	 * 
	 * @param jsonObject
	 * @return The key that contains in the JSONObject
	 * @throws IllegalArgumentException If the JSONObject doesn't contains this JSONDataKey
	 * @throws JSONException
	 */
	public String getContainingString(JSONObject jsonObject) throws IllegalArgumentException, JSONException {
		Check.ifNull(jsonObject, "For methode getContainingString(JSONObject) the JSONObject is null!");

		JSONArray jsonKeys = jsonObject.names();
		if (jsonKeys != null) {

			for (int i = 0; i < jsonKeys.length(); i++) {
				Object jsonKeyObject = jsonKeys.get(i);

				if (jsonKeyObject instanceof String) {
					String jsonKey = (String) jsonKeyObject;

					JSONDataKey otherKey = null;
					try {

						otherKey = getJSONDataKey(jsonKey);

						if (this == otherKey) {
							return jsonKey;
						}

					} catch (IllegalArgumentException e) {
						// skip other invalid-keys, only this key is necessary
					}

				} else {
					// skip other invalid-keys, only this key is necessary
				}
			}
		}

		throw new IllegalArgumentException("No '" + this + "' contains in the JSONObject(" + jsonObject + ")");
	}

	@Override
	public String toString() {
		return this.mValues[0];
	}
}
