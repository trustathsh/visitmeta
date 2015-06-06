package de.hshannover.f4.trust.visitmeta.interfaces.connections;

import java.util.List;

import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public interface DataserviceConnectionData extends Data {

	@Override
	public String getName();

	@Override
	public void setName(String name);

	public String getUrl();

	public void setUrl(String url);

	public boolean isRawXml();

	public void setRawXml(boolean rawXml);

	public void addMapServerData(MapServerConnectionData connection);

	public void setMapServerData(List<Data> connection);

	public List<Data> getMapServerData();

	public void removeMapServerData(MapServerConnectionData connection);

	public void removeMapServerData(int index);

	public void setConnected(boolean connected);

	public boolean isConnected();
	
	/**
	 * Only the connection properties. For MapServerConnections use MapServerConnectionData.changeData().
	 * 
	 * @param newData
	 */
	public void changeData(DataserviceConnectionData newData);

	@Override
	public DataserviceConnectionData copy();

	@Override
	public DataserviceConnectionData clone();
}
