package de.hshannover.f4.trust.visitmeta.datawrapper;

import java.net.URI;
import java.util.Iterator;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.visitmeta.graphCalculator.Calculator;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FacadeLogic;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FactoryCalculator;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FactoryCalculator.CalculatorType;
import de.hshannover.f4.trust.visitmeta.gui.GraphConnection;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnection;
import de.hshannover.f4.trust.visitmeta.network.Connection;
import de.hshannover.f4.trust.visitmeta.network.FacadeNetwork;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection.ConnectionType;

/**
 * 
 * @author oelsner
 * 
 */
public class GraphContainer {
	private String mName = null;
	private Connection mConnection = null;
	private Calculator mCalculator = null;
	private RESTConnection mRestConnection = null;
	private FacadeNetwork mFacadeNetwork = null;
	private FacadeLogic mFacadeLogic = null;
	private GraphConnection mGraphConnection = null;
	private TimeHolder mTimeHolder = null;
	private TimeSelector mTimeSelector = null;
	private TimeManagerCreation mTimeManagerCreation = null;
	private TimeManagerDeletion mTimeManagerDeletion = null;
	private SettingManager mSettingManager = null;

	public GraphContainer(String name, DataserviceConnection dc) {
		mName = name;
		mTimeHolder = new TimeHolder(this);
		mTimeSelector = new TimeSelector(this);
		mSettingManager = new SettingManager(this);
		mTimeManagerCreation = new TimeManagerCreation(this);
		mTimeManagerDeletion = new TimeManagerDeletion(this);

		mRestConnection = loadRESTConnections(dc);

		mConnection = FactoryConnection.getConnection(ConnectionType.REST, this);
		mCalculator = FactoryCalculator.getCalculator(CalculatorType.JUNG);
		mFacadeNetwork = new FacadeNetwork(this);
		mFacadeLogic = new FacadeLogic(this);
		mGraphConnection = new GraphConnection(this);

		new Thread(mFacadeNetwork).start();
		new Thread(mFacadeLogic).start();
		new Thread(mTimeManagerCreation).start();
		new Thread(mTimeManagerDeletion).start();
	}

	public GraphContainer(String name, RESTConnection restConn) {
		mName = name;
		mTimeHolder = new TimeHolder(this);
		mTimeSelector = new TimeSelector(this);
		mSettingManager = new SettingManager(this);
		mTimeManagerCreation = new TimeManagerCreation(this);
		mTimeManagerDeletion = new TimeManagerDeletion(this);

		mRestConnection = restConn;

		mConnection = FactoryConnection.getConnection(ConnectionType.REST, this);
		mCalculator = FactoryCalculator.getCalculator(CalculatorType.JUNG);
		mFacadeNetwork = new FacadeNetwork(this);
		mFacadeLogic = new FacadeLogic(this);
		mGraphConnection = new GraphConnection(this);

		new Thread(mFacadeNetwork).start();
		new Thread(mFacadeLogic).start();
		new Thread(mTimeManagerCreation).start();
		new Thread(mTimeManagerDeletion).start();
	}

	public TimeHolder getTimeHolder() {
		return mTimeHolder;
	}

	public TimeSelector getTimeSelector() {
		return mTimeSelector;
	}

	public TimeManagerCreation getTimeManagerCreation() {
		return mTimeManagerCreation;
	}

	public TimeManagerDeletion getTimeManagerDeletion() {
		return mTimeManagerDeletion;
	}

	public SettingManager getSettingManager() {
		return mSettingManager;
	}

	public String getName() {
		return mName;
	}

	public GraphConnection getGraphConnection() {
		return mGraphConnection;
	}

	public FacadeNetwork getFacadeNetwork() {
		return mFacadeNetwork;
	}

	public Calculator getCalculator() {
		return mCalculator;
	}

	public FacadeLogic getFacadeLogic() {
		return mFacadeLogic;
	}

	public Connection getConnection() {
		return mConnection;
	}

	public RESTConnection getRestConnection() {
		return mRestConnection;
	}

	private static RESTConnection loadRESTConnections(DataserviceConnection dc){
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		URI uri_connect = UriBuilder.fromUri(dc.getUrl()).build();
		WebResource temp1 = client.resource(uri_connect);
		JSONObject jsonResponse = temp1.accept(MediaType.APPLICATION_JSON).get(JSONObject.class);

		Iterator<String> i = jsonResponse.keys();
		RESTConnection restConn = null;
		while(i.hasNext()){
			String jKey = i.next();
			JSONObject jsonConnection;
			try {
				jsonConnection = jsonResponse.getJSONObject(jKey);
				restConn = new RESTConnection(dc, jKey);
				restConn.setBasicAuthentication(true);
				restConn.setUrl(jsonConnection.optString("URL"));
				restConn.setUsername(jsonConnection.optString("Username"));
				restConn.setPassword(jsonConnection.optString("Password"));
				restConn.setBasicAuthentication(jsonConnection.optBoolean("BasicAuthentication", true));
				restConn.setConnectAtStartUp(jsonConnection.optBoolean("ConnectAtStartUp", false));
				restConn.setDumping(jsonConnection.optBoolean("Dumping", false));
				restConn.setMaxPollResultSize(jsonConnection.optString("MaxPollResultSize"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return restConn;
	}

	@Override
	public int hashCode() {
		return mName.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof GraphContainer))
			return false;
		return mName.equals((((GraphContainer) o).mName));
	}

}