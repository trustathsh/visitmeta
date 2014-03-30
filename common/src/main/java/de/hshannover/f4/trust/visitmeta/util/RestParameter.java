package de.hshannover.f4.trust.visitmeta.util;

public class RestParameter {

	public static final String DUMP = "dump";
	public static final String START = "start";
	public static final String STOP = "stop";
	public static final String CONNECT = "connect";
	public static final String DISCONNECT = "disconnect";
	public static final String DUMP_START = DUMP + "/" + START;
	public static final String DUMP_STOP = DUMP + "/" + STOP;

	public static final String CONNECTION_NAME = "connection.name";
	public static final String CONNECTION_URL = "connection.url";
	public static final String CONNECTION_USER_NAME = "connection.user.name";
	public static final String CONNECTION_USER_PASSWORD = "connection.user.password";
	public static final String CONNECTION_AUTHENTICATION_BASIC = "connection.authentication.basic";
	public static final String CONNECTION_AUTHENTICATION_CERT = "connection.authentication.cert";
	public static final String CONNECTION_STARTUP_CONNECT = "connection.startup.connect";
	public static final String CONNECTION_STARTUP_DUMP = "connection.startup.dump";
	public static final String CONNECTION_MAX_POLL_RESULT_SIZE = "connection.maxPollResultSize";

	public static final String SUBSCRIBE_NAME = "subscribe.name";
	public static final String SUBSCRIBE_IDENTIFIER = "subscribe.identifier";
	public static final String SUBSCRIBE_IDENTIFIER_TYPE = "subscribe.identifier.type";
	public static final String SUBSCRIBE_FILTER_LINKS = "subscribe.filter.links";
	public static final String SUBSCRIBE_FILTER_RESULT = "subscribe.filter.result";
	public static final String SUBSCRIBE_TERMINAL_IDENTIFIER_TYPES = "subscribe.terminalIdentifierType";
	public static final String SUBSCRIBE_MAX_DEPTH = "subscribe.max.depth";
	public static final String SUBSCRIBE_MAX_SIZE = "subscribe.max.size";

}
