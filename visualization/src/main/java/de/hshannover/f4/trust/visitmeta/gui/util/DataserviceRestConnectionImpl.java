package de.hshannover.f4.trust.visitmeta.gui.util;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

import de.hshannover.f4.trust.visitmeta.connections.DataserviceConnectionDataImpl;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class DataserviceRestConnectionImpl extends DataserviceConnectionDataImpl implements DataserviceConnection {

	public DataserviceRestConnectionImpl(String name, String url, boolean rawXml) {
		super(name, url, rawXml);
	}

	@Override
	public void connect() throws ConnectionException {
		super.setConnected(true);
		update();
	}

	@Override
	public void disconnect() throws ConnectionException {
		super.setConnected(false);
		update();
	}

	@Override
	public String getConnectionName() {
		return super.getName();
	}

	public List<String> loadMapServerConnectionNames() throws ClientHandlerException, UniformInterfaceException,
			JSONException {
		return RestHelper.loadMapServerConnectionNames(this);
	}

	public List<String> loadActiveMapServerConnectionNames() throws ClientHandlerException, UniformInterfaceException,
			JSONException {
		return RestHelper.loadActiveMapServerConnectionNames(this);
	}

	public List<Data> loadMapServerConnections() {
		if (super.isConnected()) {
			return RestHelper.loadMapServerConnections(this);
		}
		return new ArrayList<Data>();
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

	public void update() {
		List<Data> updateList = loadMapServerConnections();
		super.setMapServerData(updateList);
	}

}
