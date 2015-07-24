package de.hshannover.f4.trust.visitmeta.interfaces.data.handler;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.handler.Handler;

public interface JSONHandler<T extends Data> extends Handler<T> {

	public JSONObject toJSONObject(T data) throws JSONException, ClassNotFoundException, InstantiationException,
			IllegalAccessException;

	public T toData(JSONObject jsonData) throws JSONException, JSONHandlerException, ClassNotFoundException,
			InstantiationException, IllegalAccessException;
}
