package de.hshannover.f4.trust.visitmeta.gui.util;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;




public class RESTConnection {

	private String mName;
	private String mUrl;
	private boolean mDumping;
	private boolean mConnectAtStartUp;
	private boolean mBasicAuthentication;
	private String mUsername ;
	private String mPassword;
	private String mMaxPollResultSize;

	private DataserviceConnection mDataserviceConnection;

	public RESTConnection(String name, String url, boolean dumping){
		this(null, name, url, dumping);
	}

	public RESTConnection(DataserviceConnection dataConnection, String name, String url, boolean dumping){
		setName(name);
		setUrl(url);
		setDumping(dumping);
		setDataserviceConnection(dataConnection);
	}

	public void startDump(){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_start_dump = UriBuilder.fromUri(mDataserviceConnection.getUrl()).build();
		WebResource temp2 = client.resource(uri_start_dump);
		System.out.println("Dataservice: " + temp2.path(mName).path("dump/start").put(String.class));
	}

	public void stopDump(){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_start_dump = UriBuilder.fromUri(mDataserviceConnection.getUrl()).build();
		WebResource temp2 = client.resource(uri_start_dump);
		System.out.println("Dataservice: " + temp2.path(mName).path("dump/stop").put(String.class));
	}

	public void connect(){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_start_dump = UriBuilder.fromUri(mDataserviceConnection.getUrl()).build();
		WebResource temp2 = client.resource(uri_start_dump);
		System.out.println("Dataservice: " + temp2.path(mName).path("connect").put(String.class));
	}

	public void disconnect(){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_start_dump = UriBuilder.fromUri(mDataserviceConnection.getUrl()).build();
		WebResource temp2 = client.resource(uri_start_dump);
		System.out.println("Dataservice: " + temp2.path(mName).path("disconnect").put(String.class));
	}

	public WebResource getGraphResource(){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri = UriBuilder.fromUri(mDataserviceConnection.getUrl() + "/" + mName + "/graph").build(); // FIXME HotFix for new Rest Interface
		return client.resource(uri);
	}

	@Override
	public RESTConnection clone() {
		RESTConnection tmp = new RESTConnection(mDataserviceConnection, mName + "(clone)", mUrl, mDumping);
		tmp.setBasicAuthentication(mBasicAuthentication);
		tmp.setUsername(mUsername);
		tmp.setPassword(mPassword);
		return tmp;
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

	public void update(String name, String url, boolean dumping) {
		setName(name);
		setUrl(url);
		setDumping(dumping);
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
