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
import de.hshannover.f4.trust.visitmeta.util.ConnectionKey;


public class DataserviceConnection {

	private static final Logger log = Logger.getLogger(Main.class);

	private String mName;
	private String mUrl;

	private boolean mRawXml;

	public DataserviceConnection(){}

	public DataserviceConnection(String name, String url, boolean rawXml){
		setName(name);
		setUrl(url);
		setRawXml(rawXml);
	}

	public List<String> loadRestConnectionNames() throws JSONException, ClientHandlerException, UniformInterfaceException{
		List<String> connections = new ArrayList<String>();

		JSONArray jsonResponse = null;
		jsonResponse = buildWebResource().path("/").accept(MediaType.APPLICATION_JSON).get(JSONArray.class);
		for(int i=0; i < jsonResponse.length(); i++){
			connections.add(jsonResponse.getString(i));
		}
		return connections;
	}

	public List<String> loadActiveRestConnectionNames() throws JSONException, ClientHandlerException, UniformInterfaceException{
		List<String> connections = new ArrayList<String>();

		JSONArray jsonResponse = null;
		jsonResponse = buildWebResource().path("/").queryParam("onlyActive", String.valueOf(true))
				.accept(MediaType.APPLICATION_JSON).get(JSONArray.class);

		for(int i=0; i < jsonResponse.length(); i++){
			connections.add(jsonResponse.getString(i));
		}
		return connections;
	}

	public List<RestConnection> loadRestConnections() throws ClientHandlerException, UniformInterfaceException{
		List<RestConnection> connections = new ArrayList<RestConnection>();

		JSONObject jsonResponse = null;

		jsonResponse = buildWebResource().path("/").queryParam("allValues", String.valueOf(true))
				.accept(MediaType.APPLICATION_JSON).get(JSONObject.class);

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

	public void startDump(String restConnectionName){
		log.trace("send startDump request...");
		String response = buildWebResource().path(restConnectionName).path("dump/start").put(String.class);
		log.info("startDump response: " + response);
	}

	public void stopDump(String restConnectionName){
		log.trace("send stopDump request...");
		String response = buildWebResource().path(restConnectionName).path("dump/stop").put(String.class);
		log.info("stopDump response: " + response);
	}

	public void connect(String restConnectionName){
		log.trace("send connect request...");
		String response = buildWebResource().path(restConnectionName).path("connect").put(String.class);
		log.info("connect response: " + response);
	}

	public void disconnect(String restConnectionName){
		log.trace("send disconnect request...");
		String response = buildWebResource().path(restConnectionName).path("disconnect").put(String.class);
		log.info("disconnect response: " + response);
	}

	public WebResource getGraphResource(String restConnectionName){
		return buildWebResource().path(restConnectionName).path("graph");
	}

	@Override
	public DataserviceConnection clone() {
		DataserviceConnection tmp = copy();
		String tmpName = tmp.getName();
		tmp.setName(tmpName + "(clone)");
		return tmp;
	}

	public DataserviceConnection copy() {
		return new DataserviceConnection(mName, mUrl, mRawXml);
	}

	public void update(DataserviceConnection dataservice) {
		mName = dataservice.getName();
		mUrl = dataservice.getUrl();
		mRawXml = dataservice.isRawXml();
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

	@Override
	public boolean equals(Object o) {
		if(o == this){
			return true;
		}else if(o instanceof DataserviceConnection){
			if(((DataserviceConnection)o).getName().equals(getName())){
				return true;
			}
		}
		return false;
	}
}
