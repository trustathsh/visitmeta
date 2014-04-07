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
 * This file is part of visitmeta visualization, version 0.0.3,
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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;
import de.hshannover.f4.trust.visitmeta.gui.GuiController;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.gui.util.RestConnection;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection.ConnectionType;

/**
 * Class with main-method.
 */
public final class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class);

	private static List<DataserviceConnection> mDataserviceConnections;

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

		loadDataserviceConnections();

		String vConnectionTypeString = PropertiesManager.getProperty("application", "dataservice.connectiontype", "local").toUpperCase();
		ConnectionType vConnectionType = ConnectionType.valueOf(vConnectionTypeString);

		GuiController gui = new GuiController();

		if(vConnectionType == ConnectionType.REST){
			for(DataserviceConnection dc : mDataserviceConnections) {
				for(RestConnection rest: dc.loadRestConnections()){
					GraphContainer tmpCon = new GraphContainer(rest.getConnectionName(), rest);
					gui.addConnection(tmpCon);
				}
			}
			gui.show();
		}
	}

	private static void loadDataserviceConnections(){
		mDataserviceConnections = new ArrayList<DataserviceConnection>();

		int count = Integer.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT, "0"));
		for(int i=1; i<=count; i++){
			String name = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_NAME(i), null);
			String url = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_URL(i), null);
			boolean rawXml = Boolean.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_RAWXML(i), null).toLowerCase());

			DataserviceConnection tmpConnection = new DataserviceConnection(name, url, rawXml);
			mDataserviceConnections.add(tmpConnection);
		}
	}
}