package de.hshannover.f4.trust.visitmeta.connections;

import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.data.DataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class DataserviceConnectionDataImpl extends DataImpl implements DataserviceConnectionData {

	private String mUrl;

	private boolean mRawXml;

	private boolean mConnected;

	private List<Data> mSubDataList;

	private DataserviceConnectionDataImpl() {
		mSubDataList = new ArrayList<Data>();
	}

	public DataserviceConnectionDataImpl(String name, String url, boolean rawXml) {
		this();
		setName(name);
		setUrl(url);
		setRawXml(rawXml);
	}

	@Override
	public Data copy() {
		DataserviceConnectionDataImpl tmpCopy = new DataserviceConnectionDataImpl(getName(), getUrl(), isRawXml());
		tmpCopy.setMapServerData(getMapServerData());
		return tmpCopy;
	}

	@Override
	public List<Data> getSubData() {
		return getMapServerData();
	}

	@Override
	public String getUrl() {
		return mUrl;
	}

	@Override
	public void setUrl(String url) {
		mUrl = url;
	}

	@Override
	public boolean isRawXml() {
		return mRawXml;
	}

	@Override
	public void setRawXml(boolean rawXml) {
		mRawXml = rawXml;
	}

	@Override
	public void addMapServerData(MapServerConnectionData connection) {
		mSubDataList.add(connection);
	}

	@Override
	public void setMapServerData(List<Data> connection) {
		mSubDataList = connection;
	}

	@Override
	public List<Data> getMapServerData() {
		return new ArrayList<Data>(mSubDataList);
	}

	@Override
	public void removeMapServerData(MapServerConnectionData connection) {
		mSubDataList.remove(connection);
	}

	@Override
	public void removeMapServerData(int index) {
		mSubDataList.remove(index);
	}

	@Override
	public void setConnected(boolean connected) {
		mConnected = connected;
	}

	@Override
	public boolean isConnected() {
		return mConnected;
	}

}
