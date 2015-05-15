package de.hshannover.f4.trust.visitmeta.handler;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.interfaces.handler.SubscriptionDataHandler;

public class JSONSubscriptionDataHandler implements SubscriptionDataHandler<SubscriptionData> {

	@Override
	public JSONObject toJSONObject(SubscriptionData data) throws JSONException {
		JSONObject jsonData = new JSONObject();

		jsonData.putOpt(this.toString(), data.getName());

		return jsonData;
	}

	@Override
	public SubscriptionData toData(JSONObject jsonData) throws JSONException, JSONHandlerException {

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
	public Class<SubscriptionData> handle() {
		return SubscriptionData.class;
	}

}
