package de.hshannover.f4.trust.visitmeta.data;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.handler.JSONHandler;

public class DataManager {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static JSONObject transformData(Data data) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, JSONException {

		JSONHandler handler = JSONHandlerManager.getHandlerFor(data);

		return handler.toJSONObject(data);
	}

	public static Data transformJSONObject(JSONObject jsonData, Class<?> dataClazz) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, JSONHandlerException, JSONException {

		JSONHandler<?> handler = JSONHandlerManager.getHandlerFor(dataClazz);

		return handler.toData(jsonData);
	}

}