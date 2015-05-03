package de.hshannover.f4.trust.visitmeta.interfaces.connections;

import java.util.List;

import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public interface MapServerConnectionData extends Data {

	public String getUrl();

	public void setUrl(String url);

	public String getUserName();

	public void setUserName(String userName);

	public String getUserPassword();

	public void setUserPass(String userPass);

	public String getTruststorePath();

	public void setTruststorePath(String truststorePath);

	public String getTruststorePassword();

	public void setTruststorePassword(String truststorePass);

	public void addMapServerData(MapServerConnectionData connection);

	public void setSubscriptionData(List<Data> connection);

	public List<Data> getSubscriptionData();

	public void removeMapServerData(MapServerConnectionData connection);

	public void removeMapServerData(int index);

	public int getMaxPollResultSize();

	public void setMaxPollResultSize(int maxPollResultSize);

	public void setConnected(boolean connected);

	public boolean isConnected();

	public void setStartupConnect(boolean startupConnect);

	public boolean isStartupConnect();

	public void setAuthenticationBasic(boolean authenticationBasic);

	public boolean isAuthenticationBasic();
}
