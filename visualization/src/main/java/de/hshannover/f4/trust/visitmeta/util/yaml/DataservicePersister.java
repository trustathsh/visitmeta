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
 * This file is part of visitmeta-visualization, version 0.3.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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

import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.util.ConnectionKey;
import de.hshannover.f4.trust.visitmeta.util.properties.Properties;
import de.hshannover.f4.trust.visitmeta.util.properties.PropertyException;

public class DataservicePersister extends Properties{

	/**
	 * Create a JyamlPersister for data-services
	 *
	 * @param fileName
	 * @param append
	 */
	public DataservicePersister(String fileName){
		super(fileName);
	}

	public void persist(DataserviceConnection connection) throws PropertyException {
		String connectionName = connection.getName();
		String dataserviceRestUrl = connection.getUrl();
		boolean rawXml = connection.isRawXml();

		setPropertyDataserviceRestUrl(connectionName, dataserviceRestUrl);
		setPropertyRawXml(connectionName, rawXml);
	}

	public List<DataserviceConnection> loadDataserviceConnections() throws PropertyException {
		List<DataserviceConnection> dataserviceConnectionList = new ArrayList<DataserviceConnection>();
		for(String connectionName: getKeySet()){
			// read values from property
			String dataserviceRestUrl = getPropertyDataserviceRestUrl(connectionName);
			boolean rawXml = getPropertyRawXml(connectionName);
			DataserviceConnection tmpDataserviceConnection = new DataserviceConnection(connectionName, dataserviceRestUrl, rawXml);
			dataserviceConnectionList.add(tmpDataserviceConnection);
		}
		return dataserviceConnectionList;
	}

	private boolean getPropertyRawXml(String connectionName) throws PropertyException {
		return super.get(connectionName).getBoolean(ConnectionKey.RAW_XML);
	}

	private String getPropertyDataserviceRestUrl(String connectionName) throws PropertyException {
		return super.get(connectionName).getString(ConnectionKey.DATASERVICE_REST_URL);
	}

	private void setPropertyRawXml(String connectionName, boolean rawXml) throws PropertyException {
		super.set(connectionName + "." + ConnectionKey.RAW_XML, rawXml);
	}

	private void setPropertyDataserviceRestUrl(String connectionName, String dataserviceRestUrl) throws PropertyException {
		super.set(connectionName + "." + ConnectionKey.DATASERVICE_REST_URL, dataserviceRestUrl);
	}

}
