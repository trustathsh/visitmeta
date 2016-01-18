package de.hshannover.f4.trust.visitmeta.network.otherservices;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ifmapj.IfmapJ;
import de.hshannover.f4.trust.ifmapj.channel.SSRC;
import de.hshannover.f4.trust.ifmapj.config.BasicAuthConfig;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ifmapj.messages.PublishRequest;
import de.hshannover.f4.trust.ironcommon.properties.Properties;

public class IfmapConnection {

	private Logger logger = Logger.getLogger(IfmapConnection.class);
	
	private final String FILENAME = "";
	private SSRC mSSRC;
	
	public IfmapConnection() throws IfmapErrorResult, IfmapException {
		mSSRC = init();
	}

	public void send(PublishRequest request) throws IfmapErrorResult, IfmapException {
		mSSRC.publish(request);
	}

	private SSRC init() throws IfmapErrorResult, IfmapException {
		Properties configuration = new Properties(FILENAME);
		String url = configuration.getString("visualization.connection.ifmap.url",
				"http://localhost:8443");
		String username = configuration.getString(
				"visualization.connection.ifmap.username", "test");
		String password = configuration.getString(
				"visualization.connection.ifmap.password", "test");
		String trustStorePath = configuration.getString(
				"visualization.connection.ifmap.truststore.path",
				"visitmeta.jks");
		String trustStorePassword = configuration.getString(
				"visualization.connection.ifmap.truststore.password", "visitmeta");
		boolean threadSafe = configuration.getBoolean(
				"visualization.connection.ifmap.threadsafe", true);
		int initialConnectionTimeout = configuration.getInt(
				"visualization.connection.ifmap.initialconnectiontimeout", (120 * 1000));
		
		logger.info("visualization.connection.ifmap.url: " + url);
		logger.info("visualization.connection.ifmap.username: " + username);
		logger.info("visualization.connection.ifmap.password: " + password);
		logger.info("visualization.connection.ifmap.truststore.path: " + trustStorePath);
		logger.info("visualization.connection.ifmap.truststore.password: "
				+ trustStorePassword);
		logger.info("visualization.connection.ifmap.threadsafe: " + threadSafe);
		logger.info("visualization.connection.ifmap.initialconnectiontimeout: "
				+ initialConnectionTimeout);
		
		BasicAuthConfig config = new BasicAuthConfig(url, username, password, trustStorePath, trustStorePassword, threadSafe, initialConnectionTimeout);
		mSSRC = IfmapJ.createSsrc(config);
		mSSRC.newSession();
		
		return mSSRC;
	}

}
