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
 * This file is part of visitmeta-visualization, version 0.2.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta;


import java.util.List;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.gui.GuiController;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.input.DeviceToGuiConnector;
import de.hshannover.f4.trust.visitmeta.input.device.Device;
import de.hshannover.f4.trust.visitmeta.input.gui.MotionControllerHandler;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection.ConnectionType;
import de.hshannover.f4.trust.visitmeta.util.properties.Properties;
import de.hshannover.f4.trust.visitmeta.util.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.util.yaml.DataservicePersister;

/**
 * Class with main-method.
 */
public final class Main {

	public static final String VISUALIZATION_VERSION = "${project.version}";

	private static final Logger LOGGER = Logger.getLogger(Main.class);

	/**
	 * Configuration class for the application.
	 */
	private static Properties mConfig;

	private static DataservicePersister mDataservicePersister;

	/**
	 * Wegen der Sicherheit!
	 */
	private Main() {
	}

	/**
	 * Main-Methode
	 * 
	 * @param args
	 *            not used.
	 */
	public static void main(String[] args) {
		LOGGER.trace("Method main(" + args + ") called.");
		LOGGER.info("VisITMeta visualization application v" + VISUALIZATION_VERSION + " started.");

		initComponents();

		List<DataserviceConnection> dataserviceConnections = null;
		try {
			dataserviceConnections = getDataservicePersister().loadDataserviceConnections();
		} catch (PropertyException e) {
			LOGGER.error("error while load persistent dataservices", e);
		}

		String vConnectionTypeString = null;

		vConnectionTypeString = getConfig().getString("dataservice.connectiontype", "local").toUpperCase();

		ConnectionType vConnectionType = ConnectionType.valueOf(vConnectionTypeString);

		/**
		 * Load and initialize external control devices, if available.
		 */
		MotionControllerHandler motionControllerHandler = new MotionControllerHandler();
		GuiController gui = new GuiController(motionControllerHandler);
		List<Device> devices = DeviceToGuiConnector.initializeDevices(motionControllerHandler);
		LOGGER.info(devices.size() + " devices were loaded.");

		if(vConnectionType == ConnectionType.REST && dataserviceConnections != null){
			for(DataserviceConnection dc : dataserviceConnections) {
				gui.addDataserviceConnection(dc);
			}
			gui.show();
		}
	}

	public static void initComponents() {
		String config = Main.class.getClassLoader().getResource("visualization_config.yml").getPath();
		String dataservicePath = Main.class.getClassLoader().getResource("dataservices.yml").getPath();

		mConfig = new Properties(config);
		mDataservicePersister = new DataservicePersister(dataservicePath);

		LOGGER.info("components initialized");

	}

	/**
	 * @return
	 */
	public static Properties getConfig() {
		if (mConfig == null) {
			throw new RuntimeException(
					"Application property has not been initialized. This is not good!");
		}
		return mConfig;
	}

	/**
	 * @return
	 */
	public static DataservicePersister getDataservicePersister() {
		if (mDataservicePersister == null) {
			throw new RuntimeException(
					"Dataservice's has not been initialized. This is not good!");
		}
		return mDataservicePersister;
	}
}
