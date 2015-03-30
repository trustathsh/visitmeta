/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of visitmeta-visualization, version 0.4.1,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.hshannover.f4.trust.visitmeta.gui.util;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.visitmeta.util.ConnectionKey;


public class RestConnection {

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
		jObj.put(ConnectionKey.CONNECTION_NAME, connectionName);
		jObj.put(ConnectionKey.IFMAP_SERVER_URL, url);
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
			jObj.put(ConnectionKey.TRUSTSTORE_PASSWORD, truststorePass);
		}
		if(startupConnect){
			jObj.put(ConnectionKey.USE_CONNECTION_AS_STARTUP, startupConnect);
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
