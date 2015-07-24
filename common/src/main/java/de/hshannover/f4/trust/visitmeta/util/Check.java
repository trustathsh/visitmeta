package de.hshannover.f4.trust.visitmeta.util;

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

}