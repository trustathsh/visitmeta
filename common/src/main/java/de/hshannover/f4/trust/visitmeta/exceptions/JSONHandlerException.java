package de.hshannover.f4.trust.visitmeta.exceptions;

public class JSONHandlerException extends HandlerException {

	private static final long serialVersionUID = -2633470400214487237L;

	public static String ERROR_MSG_JSON_KEY_IS_NOT_A_STRING = "The JSONDataKey(%s) is not a String.";

	public static String ERROR_MSG_JSON_OBJECT_MISSING_JSON_EVENT_KEY = "Missing JSONDataKey for this Data! The JSONObject-data musst have a '%s' key.";

	public JSONHandlerException(String msg) {
		super(msg);
	}

}
