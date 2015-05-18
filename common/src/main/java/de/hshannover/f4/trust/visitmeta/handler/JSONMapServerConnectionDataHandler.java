package de.hshannover.f4.trust.visitmeta.handler;

import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.AUTHENTICATION_BASIC;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.IFMAP_SERVER_URL;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.IS_CONNECTED;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.MAX_POLL_RESULT_SIZE;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.TRUSTSTORE_PASSWORD;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.TRUSTSTORE_PATH;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.USER_NAME;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.USER_PASSWORD;
import static de.hshannover.f4.trust.visitmeta.util.JSONDataKey.USE_AS_STARTUP;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.connections.MapServerConnectionDataImpl;
import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.handler.MapServerConnectionDataHandler;
import de.hshannover.f4.trust.visitmeta.util.JSONDataKey;

public class JSONMapServerConnectionDataHandler implements MapServerConnectionDataHandler<MapServerConnectionData> {

	@Override
	public JSONObject toJSONObject(MapServerConnectionData data) throws JSONException {
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

		jsonData.putOpt(data.getConnectionName(), jsonConnectionData);

		return jsonData;
	}

	@Override
	public MapServerConnectionData toData(JSONObject jsonData) throws JSONHandlerException, JSONException {

		String connectionName = null;
		JSONObject jsonConnectionData = null;
		try {

			connectionName = jsonData.names().getString(0);
			jsonConnectionData = jsonData.getJSONObject(connectionName);

		} catch (JSONException e) {
			new JSONHandlerException(JSONHandlerException.ERROR_MSG_JSON_Object_INVALID_FORMAT);
		}

		MapServerConnectionData connectionData = new MapServerConnectionDataImpl(connectionName);
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

	@Override
	public Class<MapServerConnectionData> handle() {
		return MapServerConnectionData.class;
	}

}
