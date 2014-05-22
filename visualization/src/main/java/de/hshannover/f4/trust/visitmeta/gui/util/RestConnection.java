package de.hshannover.f4.trust.visitmeta.gui.util;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.visitmeta.util.ConnectionKey;


public class RestConnection {

	private static final Logger log = Logger.getLogger(RestConnection.class);

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

	private DataserviceConnection mDataserviceConnection;


	public RestConnection(DataserviceConnection dataConnection, String name){
		setConnectionName(name);
		setDataserviceConnection(dataConnection);
	}

	public void saveInDataservice() throws UniformInterfaceException, JSONException{
		// get required values
		String connectionName = getConnectionName();
		String url = getUrl();
		String userName = getUsername();
		String userPassword = getPassword();

		// get optional values
		boolean authenticationBasic = isAuthenticationBasic();
		String truststorePath = getTruststorePath();
		String truststorePass = getTruststorePass();
		boolean startupConnect = isStartupConnect();
		String maxPollResultSize = getMaxPollResultSize();

		JSONObject jObj = new JSONObject();

		// save required values in JSONObject
		jObj.put(ConnectionKey.NAME, connectionName);
		jObj.put(ConnectionKey.URL, url);
		jObj.put(ConnectionKey.USER_NAME, userName);
		jObj.put(ConnectionKey.USER_PASSWORD, userPassword);

		// save optional values in JSONObject
		if(authenticationBasic){
			jObj.put(ConnectionKey.AUTHENTICATION_BASIC, authenticationBasic);
		}
		if(truststorePath != null && truststorePath.isEmpty()){
			jObj.put(ConnectionKey.TRUSTSTORE_PATH, truststorePath);
		}
		if(truststorePass != null && !truststorePass.isEmpty()){
			jObj.put(ConnectionKey.TRUSTSTORE_PASS, truststorePass);
		}
		if(startupConnect){
			jObj.put(ConnectionKey.STARTUP_CONNECT, startupConnect);
		}
		if(maxPollResultSize != null && !maxPollResultSize.isEmpty()){
			jObj.put(ConnectionKey.MAX_POLL_RESULT_SIZE, maxPollResultSize);
		}

		// build and send request
		buildWebResource().type(MediaType.APPLICATION_JSON).put(jObj);
	}

	private WebResource buildWebResource() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_connect = UriBuilder.fromUri(getDataserviceConnection().getUrl()).build();
		WebResource resource = client.resource(uri_connect);
		return resource;
	}

	@Override
	public RestConnection clone() {
		RestConnection tmp = copy();
		String tmpName = tmp.getConnectionName();
		tmp.setConnectionName(tmpName + "(clone)");
		return tmp;
	}

	public RestConnection copy() {
		RestConnection tmp = new RestConnection(getDataserviceConnection(), getConnectionName());
		tmp.setUrl(getUrl());
		tmp.setUsername(getUsername());
		tmp.setPassword(getPassword());
		tmp.setTruststorePath(getTruststorePath());
		tmp.setTruststorePass(getTruststorePass());
		tmp.setMaxPollResultSize(getMaxPollResultSize());
		tmp.setAuthenticationBasic(isAuthenticationBasic());
		tmp.setStartupConnect(isStartupConnect());

		return tmp;
	}

	public void update(RestConnection restConnection) {
		mConnectionName = restConnection.getConnectionName();
		mUrl = restConnection.getUrl();
		mUserName = restConnection.getUsername();
		mUserPass = restConnection.getPassword();
		mTruststorePath = restConnection.getTruststorePath();
		mTruststorePass = restConnection.getTruststorePass();
		mMaxPollResultSize = restConnection.getMaxPollResultSize();
		mAuthenticationBasic = restConnection.isAuthenticationBasic();
		mStartupConnect = restConnection.isStartupConnect();
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
