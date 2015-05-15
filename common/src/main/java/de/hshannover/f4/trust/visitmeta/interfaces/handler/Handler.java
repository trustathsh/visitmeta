package de.hshannover.f4.trust.visitmeta.interfaces.handler;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public interface Handler<T extends Data> {

	public JSONObject toJSONObject(T data) throws JSONException;

	public T toData(JSONObject jsonData) throws JSONException, JSONHandlerException;

	public Class<T> handle();
}
