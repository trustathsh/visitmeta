package de.hshannover.f4.trust.visitmeta.gui.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.network.DataserviceConnectionFactory;
import de.hshannover.f4.trust.visitmeta.util.ConnectionKey;


public class DataserviceConnection {

	private static final Logger log = Logger.getLogger(Main.class);

	private int mProtertyIndex;

	private String mName;
	private String mUrl;

	private boolean mRawXml;


	public DataserviceConnection(String name, String url, boolean rawXml){
		setName(name);
		setUrl(url);
		setRawXml(rawXml);
	}

	public List<String> loadRestConnectionNames(){
		List<String> connections = new ArrayList<String>();

		JSONArray jsonResponse = null;

		try{
			jsonResponse = buildWebResource().path("/").accept(MediaType.APPLICATION_JSON).get(JSONArray.class);
		} catch (ClientHandlerException e){
			log.error("error while loadRestConnectionNames()", e);
		} catch (UniformInterfaceException e){
			log.error("error while loadRestConnectionNames()", e);
		}

		for(int i=0; i < jsonResponse.length(); i++){
			try {
				connections.add(jsonResponse.getString(i));
			} catch (JSONException e) {
				log.error("error while loadRestConnectionNames()", e);
			}
		}
		return connections;
	}

	public List<String> loadActiveRestConnectionNames(){
		List<String> connections = new ArrayList<String>();

		JSONArray jsonResponse = null;

		try{
			jsonResponse = buildWebResource().path("/").queryParam("onlyActive", String.valueOf(true))
					.accept(MediaType.APPLICATION_JSON).get(JSONArray.class);
		} catch (ClientHandlerException e){
			log.error("error while loadRestConnectionNames()", e);
		} catch (UniformInterfaceException e){
			log.error("error while loadRestConnectionNames()", e);
		}

		for(int i=0; i < jsonResponse.length(); i++){
			try {
				connections.add(jsonResponse.getString(i));
			} catch (JSONException e) {
				log.error("error while loadRestConnectionNames()", e);
			}
		}
		return connections;
	}

	public List<RestConnection> loadRestConnections(){
		List<RestConnection> connections = new ArrayList<RestConnection>();

		JSONObject jsonResponse = null;

		try{
			jsonResponse = buildWebResource().path("/").queryParam("connectionData", String.valueOf(true))
					.accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
		} catch (ClientHandlerException e){
			log.error("error while loadRestConnections()", e);
		} catch (UniformInterfaceException e){
			log.error("error while loadRestConnections()", e);
		}

		Iterator<String> i = jsonResponse.keys();
		while(i.hasNext()){
			String connectionName = i.next();
			JSONObject jsonConnection;

			try {

				jsonConnection = jsonResponse.getJSONObject(connectionName);
				RestConnection restConn = new RestConnection(this, connectionName);

				restConn.setUrl(jsonConnection.getString(ConnectionKey.URL));
				restConn.setUsername(jsonConnection.getString(ConnectionKey.USER_NAME));
				restConn.setPassword(jsonConnection.getString(ConnectionKey.USER_PASSWORD));
				restConn.setAuthenticationBasic(jsonConnection.getBoolean(ConnectionKey.AUTHENTICATION_BASIC));
				restConn.setTruststorePath(jsonConnection.getString(ConnectionKey.TRUSTSTORE_PATH));
				restConn.setTruststorePass(jsonConnection.getString(ConnectionKey.TRUSTSTORE_PASS));
				restConn.setStartupConnect(jsonConnection.getBoolean(ConnectionKey.STARTUP_CONNECT));
				restConn.setStartupDump(jsonConnection.getBoolean(ConnectionKey.STARTUP_DUMP));
				restConn.setMaxPollResultSize(jsonConnection.getString(ConnectionKey.MAX_POLL_RESULT_SIZE));

				connections.add(restConn);

			} catch (JSONException e) {
				log.error("error while loadRestConnections()", e);
			}
		}
		return connections;
	}

	private WebResource buildWebResource() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_connect = UriBuilder.fromUri(getUrl()).build();
		WebResource resource = client.resource(uri_connect);
		return resource;
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
