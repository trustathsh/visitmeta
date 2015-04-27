package de.hshannover.f4.trust.visitmeta.connections;

import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.data.DataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class MapServerConnectionDataImpl extends DataImpl implements MapServerConnectionData {
	
	private String mUrl;

	private String mUserName;

	private String mUserPass;
	
	private String mTruststorePath;
	
	private String mTruststorePassword;
	
	private List<Data> mSubDataList;

	private int mMaxPollResultSize;

	private boolean mConnected;

	private boolean mStartupConnect;

	private boolean mAuthenticationBasic;

	private MapServerConnectionDataImpl() {
		mSubDataList = new ArrayList<Data>();
	}

	public MapServerConnectionDataImpl(String name) {
		this();
		setName(name);
	}

	public MapServerConnectionDataImpl(String name, String url, String userName, String userPass) {
		this();
		setName(name);
		setUrl(url);
		setUserName(userName);
		setUserPass(userPass);
	}

	@Override
	public Data copy() {
		MapServerConnectionData data = new MapServerConnectionDataImpl(getName(), getUrl(), getUserName(), getUserPassword());
		data.setTruststorePath(mTruststorePath);
		data.setTruststorePassword(mTruststorePassword);
		data.setMapServerData(mSubDataList);
		data.setMaxPollResultSize(mMaxPollResultSize);
		data.setConnected(mConnected);
		data.setStartupConnect(mStartupConnect);
		data.setAuthenticationBasic(mAuthenticationBasic);
		return data;
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
	public String getUserName() {
		return mUserName;
	}

	@Override
	public void setUserName(String userName) {
		mUserName = userName;
	}

	@Override
	public String getUserPassword() {
		return mUserPass;
	}

	@Override
	public void setUserPass(String userPass) {
		mUserPass = userPass;
	}

	@Override
	public String getTruststorePath() {
		return mTruststorePath;
	}

	@Override
	public void setTruststorePath(String truststorePath) {
		mTruststorePath = truststorePath;
	}

	@Override
	public String getTruststorePassword() {
		return mTruststorePassword;
	}

	@Override
	public void setTruststorePassword(String truststorePass) {
		mTruststorePassword = truststorePass;
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

	@Override
	public int getMaxPollResultSize() {
		return mMaxPollResultSize;
	}

	@Override
	public void setMaxPollResultSize(int maxPollResultSize) {
		mMaxPollResultSize = maxPollResultSize;
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
	public void setStartupConnect(boolean startupConnect) {
		mStartupConnect = startupConnect;
	}

	@Override
	public boolean isStartupConnect() {
		return mStartupConnect;
	}

	@Override
	public void setAuthenticationBasic(boolean authenticationBasic) {
		mAuthenticationBasic = authenticationBasic;
	}

	@Override
	public boolean isAuthenticationBasic() {
		return mAuthenticationBasic;
	}

}
