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
 * This file is part of visitmeta-visualization, version 0.4.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.util.yaml;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceRestConnectionImpl;
import de.hshannover.f4.trust.visitmeta.gui.util.RetryConnectionDialog;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.util.ConnectionKey;

public class DataservicePersister extends Properties {

	private static final Logger LOGGER = Logger.getLogger(DataservicePersister.class);

	/**
	 * Create a JyamlPersister for data-services
	 *
	 * @param fileName
	 * @param append
	 */
	public DataservicePersister(String fileName) {
		super(fileName);
	}

	public void persist(DataserviceConnection connection) throws PropertyException {
		String connectionName = connection.getName();
		String dataserviceRestUrl = connection.getUrl();
		boolean rawXml = connection.isRawXml();
		boolean connected = connection.isConnected();

		setPropertyDataserviceRestUrl(connectionName, dataserviceRestUrl);
		setPropertyRawXml(connectionName, rawXml);
		setPropertyConnected(connectionName, connected);
	}

	public void removeDataserviceConnection(String connectionName) throws PropertyException {
		Map<String, Object> propertyMap = load();

		for (Entry<String, Object> entry : propertyMap.entrySet()) {
			if (entry.getKey().equals(connectionName)) {
				propertyMap.remove(entry.getKey());
				break;
			}
		}

		save(propertyMap);
	}

	public List<Data> loadDataserviceConnections(Component component) throws PropertyException {
		List<Data> dataserviceConnectionList = new ArrayList<Data>();
		for (String connectionName : getKeySet()) {
			// read values from property
			String dataserviceRestUrl = getPropertyDataserviceRestUrl(connectionName);
			boolean rawXml = getPropertyRawXml(connectionName);
			boolean connected = getPropertyConnected(connectionName);
			DataserviceRestConnectionImpl tmpDataserviceConnection = new DataserviceRestConnectionImpl(connectionName,
					dataserviceRestUrl, rawXml);
			tmpDataserviceConnection.setConnected(connected);

			dataserviceConnectionList.add(tmpDataserviceConnection);
		}

		for (Data data : dataserviceConnectionList) {
			if (data instanceof DataserviceRestConnectionImpl) {
				DataserviceRestConnectionImpl connection = (DataserviceRestConnectionImpl) data;
				if (connection.isConnected()) {
					try {
						connection.update(); // To update all sub connections
					} catch (ConnectionException e) {
						connection.setConnected(false);
						LOGGER.warn(e.toString());
						LOGGER.info("Start retry connection...");
						String title = "Connecting to " + connection.getConnectionName();
						RetryConnectionDialog retryDialog = new RetryConnectionDialog(title, connection, component);
						retryDialog.showDialog();
						retryDialog.connect();
					}
				}
			}
		}

		return dataserviceConnectionList;
	}

	private boolean getPropertyRawXml(String connectionName) throws PropertyException {
		return super.get(connectionName).getBoolean(ConnectionKey.RAW_XML);
	}

	private boolean getPropertyConnected(String connectionName) throws PropertyException {
		return super.get(connectionName).getBoolean(ConnectionKey.CONNECTED);
	}

	private String getPropertyDataserviceRestUrl(String connectionName) throws PropertyException {
		return super.get(connectionName).getString(ConnectionKey.DATASERVICE_REST_URL);
	}

	private void setPropertyRawXml(String connectionName, boolean rawXml) throws PropertyException {
		super.set(connectionName + "." + ConnectionKey.RAW_XML, rawXml);
	}

	private void setPropertyConnected(String connectionName, boolean connected) throws PropertyException {
		super.set(connectionName + "." + ConnectionKey.CONNECTED, connected);
	}

	private void setPropertyDataserviceRestUrl(String connectionName, String dataserviceRestUrl)
			throws PropertyException {
		super.set(connectionName + "." + ConnectionKey.DATASERVICE_REST_URL, dataserviceRestUrl);
	}

}
