package de.hshannover.f4.trust.visitmeta.gui.util;

import com.sun.jersey.api.client.WebResource;

import de.hshannover.f4.trust.visitmeta.network.RestConnectionFactory;


public class RESTConnection {

	private static final String DEFAULT_URL = "https://localhost:8443";

	private String mConnectionName;
	private String mUrl;
	private String mUserName ;
	private String mUserPass;
	private String mTruststorePath;
	private String mTruststorePass;
	private String mMaxPollResultSize;
	private boolean mAuthenticationBasic;
	private boolean mStartupConnect;
	private boolean mStartupDump;

	private DataserviceConnection mDataserviceConnection;


	public RESTConnection(DataserviceConnection dataConnection, String name){
		setConnectionName(name);
		setDataserviceConnection(dataConnection);
	}

	public void startDump(){
		RestConnectionFactory.startDump(this);
	}

	public void stopDump(){
		RestConnectionFactory.stopDump(this);
	}

	public void connect(){
		RestConnectionFactory.connect(this);
	}

	public void disconnect(){
		RestConnectionFactory.disconnect(this);
	}

	public WebResource getGraphResource(){
		return RestConnectionFactory.getGraphResource(this);
	}

	public void saveInDataservice(){
		RestConnectionFactory.saveInDataservice(this);
	}

	@Override
	public RESTConnection clone() {
		RESTConnection tmp = new RESTConnection(mDataserviceConnection, getConnectionName() + "(clone)");
		tmp.setUrl(getUrl());
		tmp.setUsername(getUsername());
		tmp.setPassword(getPassword());
		tmp.setTruststorePath(getTruststorePath());
		tmp.setTruststorePass(getTruststorePass());
		tmp.setMaxPollResultSize(getMaxPollResultSize());
		tmp.setAuthenticationBasic(isAuthenticationBasic());
		tmp.setStartupConnect(isStartupConnect());
		tmp.setStartupDump(isStartupDump());

		return tmp;
	}

	public String getConnectionName() {
		return mConnectionName;
	}

	public void setConnectionName(String connectionName) {
		mConnectionName = connectionName;
	}
	public String getUrl() {
		if(mUrl != null){
			return mUrl;
		}else{
			return DEFAULT_URL;
		}
	}

	public void setUrl(String endpoint) {
		mUrl = endpoint;
	}

	public boolean isStartupDump() {
		return mStartupDump;
	}

	public void setStartupDump(boolean dumping) {
		mStartupDump = dumping;
	}

	@Override
	public String toString(){
		return mConnectionName;
	}

	public DataserviceConnection getDataserviceConnection() {
		return mDataserviceConnection;
	}

	private void setDataserviceConnection(DataserviceConnection dataConnection) {
		mDataserviceConnection = dataConnection;
	}

	public boolean isAuthenticationBasic() {
		return mAuthenticationBasic;
	}

	public void setAuthenticationBasic(boolean authenticationBasic) {
		mAuthenticationBasic = authenticationBasic;
	}

	public String getUsername() {
		return mUserName;
	}

	public void setUsername(String username) {
		mUserName = username;
	}

	public String getPassword() {
		return mUserPass;
	}

	public void setPassword(String password) {
		mUserPass = password;
	}

	public String getMaxPollResultSize() {
		return mMaxPollResultSize;
	}

	public void setMaxPollResultSize(String maxPollResultSize) {
		mMaxPollResultSize = maxPollResultSize;
	}

	public void setStartupConnect(boolean startupConnect) {
		mStartupConnect = startupConnect;
	}

	public boolean isStartupConnect() {
		return mStartupConnect;
	}

	public String getTruststorePath() {
		return mTruststorePath;
	}

	public void setTruststorePath(String truststorePath) {
		mTruststorePath = truststorePath;
	}

	public String getTruststorePass() {
		return mTruststorePass;
	}

	public void setTruststorePass(String truststorePass) {
		mTruststorePass = truststorePass;
	}

}
