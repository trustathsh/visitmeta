package de.hshannover.f4.trust.visitmeta.gui.util;

import com.sun.jersey.api.client.WebResource;

import de.hshannover.f4.trust.visitmeta.network.RestConnectionFactory;


public class RESTConnection {

	private static final String DEFAULT_URL = "https://localhost:8443";

	private String mName;
	private String mUrl;
	private boolean mDumping;
	private boolean mConnectAtStartUp;
	private boolean mBasicAuthentication;
	private String mUsername ;
	private String mPassword;
	private String mMaxPollResultSize;

	private DataserviceConnection mDataserviceConnection;


	public RESTConnection(DataserviceConnection dataConnection, String name){
		setName(name);
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
		RESTConnection tmp = new RESTConnection(mDataserviceConnection, mName + "(clone)");
		tmp.setBasicAuthentication(mBasicAuthentication);
		tmp.setUrl(getUrl());
		tmp.setDumping(isDumping());
		tmp.setUsername(getUsername());
		tmp.setPassword(getPassword());
		return tmp;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
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

	public boolean isDumping() {
		return mDumping;
	}

	public void setDumping(boolean dumping) {
		mDumping = dumping;
	}

	@Override
	public String toString(){
		return mName;
	}

	public DataserviceConnection getDataserviceConnection() {
		return mDataserviceConnection;
	}

	private void setDataserviceConnection(DataserviceConnection dataConnection) {
		mDataserviceConnection = dataConnection;
	}

	public boolean isBasicAuthentication() {
		return mBasicAuthentication;
	}

	public void setBasicAuthentication(boolean basicAuthentication) {
		mBasicAuthentication = basicAuthentication;
	}

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String username) {
		mUsername = username;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		mPassword = password;
	}

	public String getMaxPollResultSize() {
		return mMaxPollResultSize;
	}

	public void setMaxPollResultSize(String maxPollResultSize) {
		mMaxPollResultSize = maxPollResultSize;
	}

	public void setConnectAtStartUp(boolean connectAtStartUp) {
		mConnectAtStartUp = connectAtStartUp;
	}

	public boolean isConnectAtStartUp() {
		return mConnectAtStartUp;
	}

}
