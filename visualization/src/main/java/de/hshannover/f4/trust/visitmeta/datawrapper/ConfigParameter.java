package de.hshannover.f4.trust.visitmeta.datawrapper;

public final class ConfigParameter {

	public static final String VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT = "visualization.user.connection.dataservice.count";

	private static final String PRE_VISUALIZATION_USER_CONNECTION_DATASERVICE = "visualization.user.connection.dataservice.";

	public static final String VISUALIZATION_DEFAULT_URL = "visualization.default.url";

	public static String VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_NAME(int count){
		return PRE_VISUALIZATION_USER_CONNECTION_DATASERVICE + count + ".name";
	}

	public static String VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_URL(int count){
		return PRE_VISUALIZATION_USER_CONNECTION_DATASERVICE + count + ".url";
	}

	public static String VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_RAWXML(int count){
		return PRE_VISUALIZATION_USER_CONNECTION_DATASERVICE + count + ".rawxml";
	}
}
