package de.hshannover.f4.trust.visitmeta.datawrapper;

public final class ConfigParameter {

	public static final String VISUALIZATION_USER_CONNECTION_COUNT = "visualization.user.connection.count";
	public static final String VISUALIZATION_DEFAULT_URL = "visualization.default.url";

	private static final String PRE_VISUALIZATION_USER_CONNECTION = "visualization.user.connection.";

	public static String VISUALIZATION_USER_CONNECTION_COUNT_NAME(int count){
		return PRE_VISUALIZATION_USER_CONNECTION + count + ".name";
	}

	public static String VISUALIZATION_USER_CONNECTION_COUNT_URL(int count){
		return PRE_VISUALIZATION_USER_CONNECTION + count + ".url";
	}

	public static String VISUALIZATION_USER_CONNECTION_COUNT_DUMPING(int count){
		return PRE_VISUALIZATION_USER_CONNECTION + count + ".dumping";
	}

	public static String VISUALIZATION_USER_CONNECTION_COUNT_RAWXML(int count){
		return PRE_VISUALIZATION_USER_CONNECTION + count + ".rawxml";
	}
}
