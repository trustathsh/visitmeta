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
 * This file is part of visitmeta-visualization, version 0.5.0,
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
package de.hshannover.f4.trust.visitmeta.gui.util;

import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

import de.hshannover.f4.trust.visitmeta.data.DataserviceDataImpl;
import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.data.DataserviceData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.MapServerData;
import de.hshannover.f4.trust.visitmeta.util.StringHelper;

public class DataserviceRestConnectionImpl extends DataserviceDataImpl implements DataserviceConnection {

	private static final Logger LOGGER = Logger.getLogger(DataserviceRestConnectionImpl.class);

	private boolean mNotPersised;

	private boolean mConnected;

	private DataserviceData mOldData;

	public DataserviceRestConnectionImpl(String name, String url, boolean rawXml) {
		super(name, url, rawXml);
	}

	public DataserviceRestConnectionImpl(DataserviceData connectionData) {
		super(connectionData.getName(), connectionData.getUrl(), connectionData.isRawXml());
	}

	@Override
	public DataserviceRestConnectionImpl copy() {
		DataserviceData dataCopy = super.copy();
		DataserviceRestConnectionImpl tmpCopy = new DataserviceRestConnectionImpl(dataCopy);
		tmpCopy.setNotPersised(true);
		return tmpCopy;
	}

	@Override
	public DataserviceRestConnectionImpl clone() {
		return (DataserviceRestConnectionImpl) super.clone();
	}

	@Override
	public void connect() throws ConnectionException {
		setConnected(true);
	}

	@Override
	public void disconnect() throws ConnectionException {
		setConnected(false);
	}

	@Override
	public String getConnectionName() {
		return super.getName();
	}

	public void resetData() {
		if (mOldData != null) {
			super.changeData(mOldData);
			mOldData = null;
		}
	}

	public List<MapServerConnection> loadMapServerConnections() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, JSONHandlerException, JSONException, ConnectionException {

		return RestHelper.loadMapServerConnections(this);
	}

	public void connectMapServer(String mapServerConnectionName) throws ConnectionException {
		for (Data d : super.getMapServerData()) {
			MapServerConnection connection = (MapServerConnection) d;
			if (connection.getConnectionName().equals(mapServerConnectionName)) {
				connection.connect();
				break;
			}
		}
	}

	public void disconnectMapServer(String mapServerConnectionName) throws ConnectionException {
		for (Data d : super.getMapServerData()) {
			MapServerConnection connection = (MapServerConnection) d;
			if (connection.getConnectionName().equals(mapServerConnectionName)) {
				connection.disconnect();
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void update() throws ConnectionException {
		List<MapServerData> updateList = null;
		try {
			updateList = (List<MapServerData>) (List<?>) loadMapServerConnections();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONHandlerException
				| JSONException e) {
			LOGGER.error(e.toString());
			JOptionPane.showMessageDialog(null, StringHelper.breakLongString(e.toString(), 80), e.getClass()
					.getSimpleName(), JOptionPane.ERROR_MESSAGE);
		}
		super.setMapServerData(updateList);
	}

	@Override
	public void setConnected(boolean connected) {
		mConnected = connected;
	}

	@Override
	public boolean isConnected() {
		return mConnected;
	}

	public boolean isNotPersised() {
		return mNotPersised;
	}

	public void setNotPersised(boolean b) {
		mNotPersised = b;
	}

	public void setOldData(DataserviceData oldData) {
		mOldData = oldData;
	}

	public DataserviceData getOldData() {
		return mOldData;
	}
}
