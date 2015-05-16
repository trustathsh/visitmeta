package de.hshannover.f4.trust.visitmeta.handler;

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

import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.ifmap.SubscriptionDataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.interfaces.handler.SubscriptionDataHandler;
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

		SubscriptionData subscriptionData = new SubscriptionDataImpl();
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
