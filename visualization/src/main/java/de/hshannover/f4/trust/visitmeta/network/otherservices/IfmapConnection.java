/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of visitmeta-visualization, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
