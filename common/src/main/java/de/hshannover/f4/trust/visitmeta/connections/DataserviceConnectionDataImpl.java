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
		return new DataserviceConnectionDataImpl(getName(), mUrl, mRawXml);
	}

	@Override
	public List<Data> getSubData() {
		return new ArrayList<Data>(mSubDataList);
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
	public void removeMapServerData(MapServerConnectionData connection) {
		mSubDataList.remove(connection);
	}

	@Override
	public void removeMapServerData(int index) {
		mSubDataList.remove(index);
	}

}
