package de.hshannover.f4.trust.visitmeta.data;

import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.data.DataserviceData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.MapServerData;

public class DataserviceDataImpl extends DataImpl implements DataserviceData {

	private String mUrl;

	private boolean mRawXml;

	private boolean mConnected;

	private List<MapServerData> mSubDataList;

	private DataserviceDataImpl() {
		mSubDataList = new ArrayList<MapServerData>();
	}

	public DataserviceDataImpl(String name, String url, boolean rawXml) {
		this();
		setName(name);
		setUrl(url);
		setRawXml(rawXml);
	}

	@Override
	public DataserviceData copy() {
		DataserviceData tmpCopy = new DataserviceDataImpl(getName(), getUrl(), isRawXml());
		tmpCopy.setMapServerData(getMapServerData());
		return tmpCopy;
	}

	@Override
	public DataserviceData clone() {
		return (DataserviceData) super.clone();
	}

	@Override
	public void changeData(DataserviceData newData) {
		setName(newData.getName());
		setUrl(newData.getUrl());
		setRawXml(newData.isRawXml());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Data> getSubData() {
		return (List<Data>) (List<?>) getMapServerData();
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
	public void addMapServerData(MapServerData connection) {
		mSubDataList.add(connection);
	}

	@Override
	public void setMapServerData(List<MapServerData> connection) {
		mSubDataList = connection;
	}

	@Override
	public List<MapServerData> getMapServerData() {
		return new ArrayList<MapServerData>(mSubDataList);
	}

	@Override
	public void removeMapServerData(MapServerData connection) {
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

	@Override
	public Class<?> getDataTypeClass() {
		return DataserviceData.class;
	}

}
