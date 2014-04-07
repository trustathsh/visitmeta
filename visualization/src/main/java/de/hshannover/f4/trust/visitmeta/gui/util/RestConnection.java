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
	private boolean mStartupDump;

	private DataserviceConnection mDataserviceConnection;


	public RestConnection(DataserviceConnection dataConnection, String name){
		setConnectionName(name);
		setDataserviceConnection(dataConnection);
	}

	public void startDump(){
		log.trace("send startDump request...");
		String response = buildWebResource().path(getConnectionName()).path("dump/start").put(String.class);
		log.info("startDump response: " + response);
	}

	public void stopDump(){
		log.trace("send stopDump request...");
		String response = buildWebResource().path(getConnectionName()).path("dump/stop").put(String.class);
		log.info("stopDump response: " + response);
	}

	public void connect(){
		log.trace("send connect request...");
		String response = buildWebResource().path(getConnectionName()).path("connect").put(String.class);
		log.info("connect response: " + response);
	}

	public void disconnect(){
		log.trace("send disconnect request...");
		String response = buildWebResource().path(getConnectionName()).path("disconnect").put(String.class);
		log.info("disconnect response: " + response);
	}

	public WebResource getGraphResource(){
		return buildWebResource().path(getConnectionName()).path("graph");
	}

	public void saveInDataservice(){
		JSONObject jObj = new JSONObject();
		try {

			jObj.put(ConnectionKey.NAME, getConnectionName());
			jObj.put(ConnectionKey.URL, getUrl());
			jObj.put(ConnectionKey.USER_NAME, getUsername());
			jObj.put(ConnectionKey.USER_PASSWORD, getPassword());
			jObj.put(ConnectionKey.AUTHENTICATION_BASIC, isAuthenticationBasic());
			jObj.put(ConnectionKey.TRUSTSTORE_PATH, getTruststorePath());
			jObj.put(ConnectionKey.TRUSTSTORE_PASS, getTruststorePass());
			jObj.put(ConnectionKey.STARTUP_CONNECT, isStartupConnect());
			jObj.put(ConnectionKey.STARTUP_DUMP, isStartupDump());
			jObj.put(ConnectionKey.MAX_POLL_RESULT_SIZE, getMaxPollResultSize());

		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try{
			buildWebResource().type(MediaType.APPLICATION_JSON).put(jObj);
		}catch (UniformInterfaceException e){
			log.error("error while saveInDataservice()", e);
		}
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
		RestConnection tmp = new RestConnection(getDataserviceConnection(), getConnectionName() + "(clone)");
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
