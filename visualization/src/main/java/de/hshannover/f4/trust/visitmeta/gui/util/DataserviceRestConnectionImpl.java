package de.hshannover.f4.trust.visitmeta.gui.util;

import java.util.List;

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

	public void resetData(){
		if(mOldData != null){
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
