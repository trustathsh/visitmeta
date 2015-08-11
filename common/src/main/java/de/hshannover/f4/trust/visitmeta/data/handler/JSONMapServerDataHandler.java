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
package de.hshannover.f4.trust.visitmeta.data.handler;

import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.AUTHENTICATION_BASIC;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.IFMAP_SERVER_URL;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.IS_CONNECTED;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.MAX_POLL_RESULT_SIZE;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.SUBSCRIPTIONS;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.TRUSTSTORE_PASSWORD;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.TRUSTSTORE_PATH;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.USER_NAME;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.USER_PASSWORD;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.USE_AS_STARTUP;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.data.DataManager;
import de.hshannover.f4.trust.visitmeta.data.MapServerDataImpl;
import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.data.MapServerData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.handler.MapServerDataHandler;
import de.hshannover.f4.trust.visitmeta.util.JSONDataKey;

public class JSONMapServerDataHandler implements MapServerDataHandler<MapServerData> {

	@Override
	public JSONObject toJSONObject(MapServerData data) throws JSONException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		JSONObject jsonConnectionData = new JSONObject();
		JSONObject jsonData = new JSONObject();

		jsonConnectionData.putOpt(IFMAP_SERVER_URL.toString(), data.getUrl());
		jsonConnectionData.putOpt(USER_NAME.toString(), data.getUserName());
		jsonConnectionData.putOpt(USER_PASSWORD.toString(), data.getUserPassword());
		jsonConnectionData.putOpt(AUTHENTICATION_BASIC.toString(), data.isAuthenticationBasic());
		jsonConnectionData.putOpt(TRUSTSTORE_PATH.toString(), data.getTruststorePath());
		jsonConnectionData.putOpt(TRUSTSTORE_PASSWORD.toString(), data.getTruststorePassword());
		jsonConnectionData.putOpt(USE_AS_STARTUP.toString(), data.doesConnectOnStartup());
		jsonConnectionData.putOpt(MAX_POLL_RESULT_SIZE.toString(), data.getMaxPollResultSize());
		jsonConnectionData.putOpt(IS_CONNECTED.toString(), data.isConnected());
		jsonConnectionData.putOpt(SUBSCRIPTIONS.toString(), buildSubscriptionsJSONArray(data));

		jsonData.putOpt(data.getConnectionName(), jsonConnectionData);

		return jsonData;
	}

	private JSONArray buildSubscriptionsJSONArray(MapServerData data) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, JSONException {
		JSONArray jsonSubscriptions = new JSONArray();

		for (Data subscrptionData : data.getSubscriptions()) {
			if (subscrptionData instanceof SubscriptionData) {

				JSONObject jsonSubscriptionData = DataManager.transformData(subscrptionData);
				jsonSubscriptions.put(jsonSubscriptionData);
			}
		}

		return jsonSubscriptions;
	}

	@Override
	public MapServerData toData(JSONObject jsonData) throws JSONHandlerException, JSONException,
			ClassNotFoundException, InstantiationException, IllegalAccessException {

		String connectionName = null;
		JSONObject jsonConnectionData = null;
		try {

			connectionName = jsonData.names().getString(0);
			jsonConnectionData = jsonData.getJSONObject(connectionName);

		} catch (JSONException e) {
			throw new JSONHandlerException(JSONHandlerException.ERROR_MSG_JSON_Object_INVALID_FORMAT);
		}

		MapServerData connectionData = new MapServerDataImpl(connectionName);
		JSONArray jsonKeys = jsonConnectionData.names();
		if (jsonKeys != null) {

			for (int i = 0; i < jsonKeys.length(); i++) {
				Object jsonKeyObject = jsonKeys.get(i);

				if (jsonKeyObject instanceof String) {
					String jsonKey = (String) jsonKeyObject;
					switch (JSONDataKey.getJSONDataKey(jsonKey)) {
						case IFMAP_SERVER_URL:
							connectionData.setUrl(jsonConnectionData.getString(jsonKey));
							break;
						case USER_NAME:
							connectionData.setUserName(jsonConnectionData.getString(jsonKey));
							break;
						case USER_PASSWORD:
							connectionData.setUserPassword(jsonConnectionData.getString(jsonKey));
							break;
						case AUTHENTICATION_BASIC:
							connectionData.setAuthenticationBasic(jsonConnectionData.getBoolean(jsonKey));
							break;
						case TRUSTSTORE_PATH:
							connectionData.setTruststorePath(jsonConnectionData.getString(jsonKey));
							break;
						case TRUSTSTORE_PASSWORD:
							connectionData.setTruststorePassword(jsonConnectionData.getString(jsonKey));
							break;
						case USE_AS_STARTUP:
							connectionData.setStartupConnect(jsonConnectionData.getBoolean(jsonKey));
							break;
						case MAX_POLL_RESULT_SIZE:
							connectionData.setMaxPollResultSize(jsonConnectionData.getInt(jsonKey));
							break;
						case IS_CONNECTED:
							connectionData.setConnected(jsonConnectionData.getBoolean(jsonKey));
							break;
						case SUBSCRIPTIONS:
							connectionData.setSubscriptionData(buildSubscriptionsDataList(jsonConnectionData
									.getJSONArray(jsonKey)));
							break;
						default:
							throw new JSONHandlerException(String.format(
									JSONHandlerException.ERROR_MSG_JSON_KEY_IS_NOT_VALID_FOR_THIS_DATA, jsonKey,
									handle().getSimpleName()));
					}

				} else {
					throw new JSONHandlerException(String.format(
							JSONHandlerException.ERROR_MSG_JSON_KEY_IS_NOT_A_STRING, jsonKeyObject));
				}
			}
		}

		return connectionData;
	}

	private List<SubscriptionData> buildSubscriptionsDataList(JSONArray subscrptionsArray)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, JSONException,
			JSONHandlerException {

		List<SubscriptionData> subscriptionsList = new ArrayList<SubscriptionData>();

		for (int i = 0; i < subscrptionsArray.length(); i++) {
			JSONObject jsonSubscriptionData = subscrptionsArray.getJSONObject(i);

			SubscriptionData subscriptionData = (SubscriptionData) DataManager.transformJSONObject(
					jsonSubscriptionData, SubscriptionData.class);
			subscriptionsList.add(subscriptionData);
		}

		return subscriptionsList;
	}

	@Override
	public Class<MapServerData> handle() {
		return MapServerData.class;
	}

}
