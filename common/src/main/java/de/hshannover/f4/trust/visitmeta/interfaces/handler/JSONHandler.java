package de.hshannover.f4.trust.visitmeta.interfaces.handler;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public interface JSONHandler<T extends Data> extends Handler<T> {

	public JSONObject toJSONObject(T data) throws JSONException;

	public T toData(JSONObject jsonData) throws JSONException, JSONHandlerException;
}
