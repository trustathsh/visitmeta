package de.hshannover.f4.trust.visitmeta.data;

import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.handler.JSONHandler;

public class JSONHandlerManager {

	private static final String POST_CLASS_PATH = "de.hshannover.f4.trust.visitmeta.handler.";
	
	public static JSONHandler<?> getHandlerFor(Class<?> dataTypeClazz) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {

		Class<?> handlerClazz = Class.forName(POST_CLASS_PATH + "JSON" + dataTypeClazz.getSimpleName() + "Handler");
		
		JSONHandler<?> eventHandler = (JSONHandler<?>) handlerClazz.newInstance();

		return eventHandler;
	}
	
	public static JSONHandler<?> getHandlerFor(Data data) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		Class<?> handlerClazz = Class.forName(POST_CLASS_PATH + "JSON" + data.getDataTypeClass().getSimpleName()
				+ "Handler");

		JSONHandler<?> eventHandler = (JSONHandler<?>) handlerClazz.newInstance();

		return eventHandler;
	}

}