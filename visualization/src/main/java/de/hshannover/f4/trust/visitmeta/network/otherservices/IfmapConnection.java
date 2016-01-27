package de.hshannover.f4.trust.visitmeta.network.otherservices;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ifmapj.IfmapJ;
import de.hshannover.f4.trust.ifmapj.channel.SSRC;
import de.hshannover.f4.trust.ifmapj.config.BasicAuthConfig;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ifmapj.messages.PublishRequest;
import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

public class IfmapConnection {

	private Properties mConfig = Main.getConfig();
	
	private Logger logger = Logger.getLogger(IfmapConnection.class);
	
	private SSRC mSSRC;
	
	public IfmapConnection() throws IfmapErrorResult, IfmapException {
		mSSRC = init();
	}

	public void send(PublishRequest request) throws IfmapErrorResult, IfmapException {
		mSSRC.publish(request);
	}

	private SSRC init() throws IfmapErrorResult, IfmapException {
		String url = mConfig.getString(VisualizationConfig.KEY_CONNECTION_IFMAP_URL,
				VisualizationConfig.DEFAULT_VALUE_CONNECTION_IFMAP_URL);
		String username = mConfig.getString(
				VisualizationConfig.KEY_CONNECTION_IFMAP_USERNAME, VisualizationConfig.DEFAULT_VALUE_CONNECTION_IFMAP_USERNAME);
		String password = mConfig.getString(
				VisualizationConfig.KEY_CONNECTION_IFMAP_PASSWORD, VisualizationConfig.DEFAULT_VALUE_CONNECTION_IFMAP_PASSWORD);
		String trustStorePath = mConfig.getString(
				VisualizationConfig.KEY_CONNECTION_IFMAP_TRUSTSTORE_PATH,
				VisualizationConfig.DEFAULT_VALUE_CONNECTION_IFMAP_TRUSTSTORE_PATH);
		String trustStorePassword = mConfig.getString(
				VisualizationConfig.KEY_CONNECTION_IFMAP_TRUSTSTORE_PASSWORD, VisualizationConfig.DEFAULT_VALUE_CONNECTION_IFMAP_TRUSTSTORE_PASSWORD);
		boolean threadSafe = mConfig.getBoolean(
				VisualizationConfig.KEY_CONNECTION_IFMAP_THREADSAFE, VisualizationConfig.DEFAULT_VALUE_CONNECTION_IFMAP_THREADSAFE);
		int initialConnectionTimeout = mConfig.getInt(
				VisualizationConfig.KEY_CONNECTION_IFMAP_INITIALCONNECTIONTIMEOUT, VisualizationConfig.DEFAULT_VALUE_CONNECTION_IFMAP_INITIALCONNECTIONTIMEOUT);
		
		BasicAuthConfig config = new BasicAuthConfig(url, username, password, trustStorePath, trustStorePassword, threadSafe, initialConnectionTimeout);
		logger.debug(config);

		mSSRC = IfmapJ.createSsrc(config);
		mSSRC.newSession();
		logger.info("IF-MAP connection established successfully");
		
		return mSSRC;
	}

}
