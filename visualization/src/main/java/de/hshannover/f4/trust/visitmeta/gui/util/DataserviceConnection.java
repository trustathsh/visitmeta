package de.hshannover.f4.trust.visitmeta.gui.util;

import de.hshannover.f4.trust.visitmeta.network.DataserviceConnectionFactory;


public class DataserviceConnection {

	private int mProtertyIndex;

	private String mName;
	private String mUrl;

	private boolean mRawXml;


	public DataserviceConnection(String name, String url, boolean rawXml){
		setName(name);
		setUrl(url);
		setRawXml(rawXml);
	}

	@Override
	public DataserviceConnection clone() {
		return new DataserviceConnection(mName, mUrl, mRawXml);
	}

	public void saveInProperty(){
		DataserviceConnectionFactory.saveDataServiceConnection(this);
	}

	public void deleteFromProperty(){
		DataserviceConnectionFactory.removeDataServiceConnection(mProtertyIndex);
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public boolean isRawXml() {
		return mRawXml;
	}

	public void setRawXml(boolean rawXml) {
		mRawXml = rawXml;
	}

	@Override
	public String toString(){
		return mName;
	}

	public int getProtertyIndex() {
		return mProtertyIndex;
	}

	public void setProtertyIndex(int protertyCount) {
		mProtertyIndex = protertyCount;
	}

}
