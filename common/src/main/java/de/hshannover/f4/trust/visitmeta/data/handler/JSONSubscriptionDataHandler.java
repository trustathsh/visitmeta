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
package de.hshannover.f4.trust.visitmeta.data.handler;

import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.IDENTIFIER_TYPE;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.IS_ACTIVE;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.MATCH_LINKS_FILTER;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.MAX_DEPTH;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.MAX_SIZE;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.RESULT_FILTER;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.START_IDENTIFIER;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.TERMINAL_IDENTIFIER_TYPES;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.USE_AS_STARTUP;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.data.SubscriptionDataImpl;
import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.interfaces.data.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.handler.SubscriptionDataHandler;
import de.hshannover.f4.trust.visitmeta.util.JSONDataKey;

public class JSONSubscriptionDataHandler implements SubscriptionDataHandler<SubscriptionData> {

	@Override
	public JSONObject toJSONObject(SubscriptionData data) throws JSONException {
		JSONObject jsonSubscriptionData = new JSONObject();
		JSONObject jsonData = new JSONObject();

		jsonSubscriptionData.putOpt(START_IDENTIFIER.toString(), data.getStartIdentifier());
		jsonSubscriptionData.putOpt(IDENTIFIER_TYPE.toString(), data.getIdentifierType());
		jsonSubscriptionData.putOpt(MATCH_LINKS_FILTER.toString(), data.getMatchLinksFilter());
		jsonSubscriptionData.putOpt(RESULT_FILTER.toString(), data.getResultFilter());
		jsonSubscriptionData.putOpt(TERMINAL_IDENTIFIER_TYPES.toString(), data.getTerminalIdentifierTypes());
		jsonSubscriptionData.putOpt(USE_AS_STARTUP.toString(), data.isStartupSubscribe());
		jsonSubscriptionData.putOpt(MAX_DEPTH.toString(), data.getMaxDepth());
		jsonSubscriptionData.putOpt(MAX_SIZE.toString(), data.getMaxSize());
		jsonSubscriptionData.putOpt(IS_ACTIVE.toString(), data.isActive());

		jsonData.putOpt(data.getName(), jsonSubscriptionData);

		return jsonData;
	}

	@Override
	public SubscriptionData toData(JSONObject jsonData) throws JSONException, JSONHandlerException {

		String subscriptionName = null;
		JSONObject jsonSubscriptionData = null;
		try {

			subscriptionName = jsonData.names().getString(0);
			jsonSubscriptionData = jsonData.getJSONObject(subscriptionName);

		} catch (JSONException e) {
			new JSONHandlerException(JSONHandlerException.ERROR_MSG_JSON_Object_INVALID_FORMAT);
		}

		SubscriptionData subscriptionData = new SubscriptionDataImpl(subscriptionName);

		JSONArray jsonKeys = jsonSubscriptionData.names();
		if (jsonKeys != null) {

			for (int i = 0; i < jsonKeys.length(); i++) {
				Object jsonKeyObject = jsonKeys.get(i);

				if (jsonKeyObject instanceof String) {
					String jsonKey = (String) jsonKeyObject;
					switch (JSONDataKey.getJSONDataKey(jsonKey)) {
						case START_IDENTIFIER:
							subscriptionData.setStartIdentifier(jsonSubscriptionData.getString(jsonKey));
							break;
						case IDENTIFIER_TYPE:
							subscriptionData.setIdentifierType(jsonSubscriptionData.getString(jsonKey));
							break;
						case MATCH_LINKS_FILTER:
							subscriptionData.setMatchLinksFilter(jsonSubscriptionData.getString(jsonKey));
							break;
						case RESULT_FILTER:
							subscriptionData.setResultFilter(jsonSubscriptionData.getString(jsonKey));
							break;
						case TERMINAL_IDENTIFIER_TYPES:
							subscriptionData.setTerminalIdentifierTypes(jsonSubscriptionData.getString(jsonKey));
							break;
						case USE_AS_STARTUP:
							subscriptionData.setStartupSubscribe(jsonSubscriptionData.getBoolean(jsonKey));
							break;
						case MAX_DEPTH:
							subscriptionData.setMaxDepth(jsonSubscriptionData.getInt(jsonKey));
							break;
						case MAX_SIZE:
							subscriptionData.setMaxSize(jsonSubscriptionData.getInt(jsonKey));
							break;
						case IS_ACTIVE:
							subscriptionData.setActive(jsonSubscriptionData.getBoolean(jsonKey));
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

		return subscriptionData;
	}

	@Override
	public Class<SubscriptionData> handle() {
		return SubscriptionData.class;
	}

}
