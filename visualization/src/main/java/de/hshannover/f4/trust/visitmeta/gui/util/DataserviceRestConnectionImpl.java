package de.hshannover.f4.trust.visitmeta.gui.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

import de.hshannover.f4.trust.visitmeta.connections.DataserviceConnectionDataImpl;
import de.hshannover.f4.trust.visitmeta.exceptions.JSONHandlerException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.data.DataserviceData;

public class DataserviceRestConnectionImpl extends DataserviceConnectionDataImpl implements DataserviceConnection {

	private static final Logger LOGGER = Logger.getLogger(DataserviceRestConnectionImpl.class);

	private boolean mNotPersised;

	private DataserviceData mOldData;

	public DataserviceRestConnectionImpl(String name, String url, boolean rawXml) {
		super(name, url, rawXml);
	}

	public DataserviceRestConnectionImpl(DataserviceData connectionData) {
		super(connectionData.getName(), connectionData.getUrl(), connectionData.isRawXml());
		super.setConnected(connectionData.isConnected());
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
		update();
		super.setConnected(true);
	}

	@Override
	public void disconnect() throws ConnectionException {
		update(); // TODO muss das sein?
		super.setConnected(false);
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

	public List<Data> loadMapServerConnections() throws ClassNotFoundException, InstantiationException,
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

	public void update() throws ConnectionException {
		List<Data> updateList = null;
		try {
			updateList = loadMapServerConnections();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONHandlerException
				| JSONException e) {
			LOGGER.error(e.toString());
		}
		super.setMapServerData(updateList);
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
