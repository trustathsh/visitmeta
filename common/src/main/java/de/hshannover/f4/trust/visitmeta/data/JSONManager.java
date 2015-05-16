package de.hshannover.f4.trust.visitmeta.data;

import de.hshannover.f4.trust.visitmeta.interfaces.handler.JSONHandler;

public class JSONManager {

	private static final String POST_CLASS_PATH = "de.hshannover.f4.trust.visitmeta.handler.";
	
	public static JSONHandler<?> getHandlerFor(Class<?> dataClazz) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {

		Class<?> handlerClazz = Class.forName(POST_CLASS_PATH + "JSON" + dataClazz.getSimpleName() + "Handler");
		
		JSONHandler<?> eventHandler = (JSONHandler<?>) handlerClazz.newInstance();

		return eventHandler;
	}
	
}
