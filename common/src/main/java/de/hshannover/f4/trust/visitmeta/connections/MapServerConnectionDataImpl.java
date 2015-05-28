package de.hshannover.f4.trust.visitmeta.connections;

import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.data.DataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnectionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class MapServerConnectionDataImpl extends DataImpl implements MapServerConnectionData {
	
	private String mUrl;

	private String mUserName;

	private String mUserPass;
	
	private String mTruststorePath;
	
	private String mTruststorePassword;
	
	private List<Data> mSubscriptionDataList;

	private int mMaxPollResultSize;

	private boolean mConnected;

	private boolean mStartupConnect;

	private boolean mAuthenticationBasic;

	private MapServerConnectionDataImpl() {
		mSubscriptionDataList = new ArrayList<Data>();
	}

	public MapServerConnectionDataImpl(String name) {
		this();
		setName(name);
	}

	public MapServerConnectionDataImpl(String name, String url, String userName, String userPassword) {
		this();
		setName(name);
		setUrl(url);
		setUserName(userName);
		setUserPassword(userPassword);
	}

	@Override
	public Data copy() {
		MapServerConnectionData data = new MapServerConnectionDataImpl(getName(), getUrl(), getUserName(), getUserPassword());
		data.setTruststorePath(getTruststorePath());
		data.setTruststorePassword(getTruststorePassword());
		data.setSubscriptionData(getSubscriptions());
		data.setMaxPollResultSize(getMaxPollResultSize());
		data.setConnected(isConnected());
		data.setStartupConnect(doesConnectOnStartup());
		data.setAuthenticationBasic(isAuthenticationBasic());
		return data;
	}

	@Override
	public void changeData(MapServerConnectionData newData) {
		setName(newData.getName());
		setUrl(newData.getUrl());
		setUserName(newData.getUserName());
		setUserPassword(newData.getUserPassword());
		setTruststorePath(newData.getTruststorePath());
		setTruststorePassword(newData.getTruststorePassword());
		setSubscriptionData(newData.getSubscriptions());
		setMaxPollResultSize(newData.getMaxPollResultSize());
		setConnected(newData.isConnected());
		setStartupConnect(newData.doesConnectOnStartup());
		setAuthenticationBasic(newData.isAuthenticationBasic());
	}

	@Override
	public String getConnectionName() {
		return super.getName();
	}

	@Override
	public void setConnectionName(String connectionName) {
		super.setName(connectionName);
	}

	@Override
	public List<Data> getSubData() {
		return getSubscriptions();
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
	public void setUserPassword(String userPassword) {
		mUserPass = userPassword;
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
	public void setSubscriptionData(List<Data> connection) {
		mSubscriptionDataList = connection;
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
	public boolean doesConnectOnStartup() {
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

	@Override
	public void addSubscription(Subscription subscription) {
		mSubscriptionDataList.add(subscription);
	}

	@Override
	public void updateSubscription(SubscriptionData newData) {
		for (Data subData : mSubscriptionDataList) {
			if (subData.getName().equals(newData.getName())) {
				((SubscriptionData) subData).changeData(newData);
				break;
			}
		}
	}

	@Override
	public void deleteSubscription(String subscriptionName) {
		for (Data subData : getSubscriptions()) {
			if (subData.getName().equals(subscriptionName)) {
				mSubscriptionDataList.remove(subData);
				break;
			}
		}
	}

	@Override
	public void deleteAllSubscriptions() {
		mSubscriptionDataList.clear();
	}

	@Override
	public List<Data> getActiveSubscriptions() {
		List<Data> newActiveList = new ArrayList<Data>();
		for (Data subData : getSubscriptions()) {
			if (((Subscription) subData).isActive()) {
				newActiveList.add(subData);
			}
		}

		return newActiveList;
	}

	@Override
	public List<Data> getSubscriptions() {
		return new ArrayList<Data>(mSubscriptionDataList);
	}

	@Override
	public Class<?> getDataTypeClass() {
		return MapServerConnectionData.class;
	}

}
