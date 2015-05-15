package de.hshannover.f4.trust.visitmeta.handler;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.handler.MapServerConnectionDataHandler;

public class JSONMapServerConnectionDataHandler implements MapServerConnectionDataHandler<MapServerConnectionData> {

	@Override
	public JSONObject toJSONObject(MapServerConnectionData data) throws JSONException {
		JSONObject jsonData = new JSONObject();

		jsonData.putOpt(this.toString(), data.getName());

		return jsonData;
	}

	@Override
	public MapServerConnectionData toData(JSONObject jsonData) throws JSONException, JSONHandlerException {

		JSONArray jsonKeys = jsonData.names();
		if (jsonKeys != null) {

			for (int i = 0; i < jsonKeys.length(); i++) {
				Object jsonKeyObject = jsonKeys.get(i);

				if (jsonKeyObject instanceof String) {
					String jsonKey = (String) jsonKeyObject;

				} else {
					throw new JSONHandlerException(String.format(
							JSONHandlerException.ERROR_MSG_JSON_KEY_IS_NOT_A_STRING, jsonKeyObject));
				}
			}
		}

		return null;
	}

	@Override
	public Class<MapServerConnectionData> handle() {
		return MapServerConnectionData.class;
	}

}
