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
 * This file is part of visitmeta visualization, version 0.0.3,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.visitmeta.datawrapper.ConfigParameter;
import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;
import de.hshannover.f4.trust.visitmeta.datawrapper.TimeManagerCreation;
import de.hshannover.f4.trust.visitmeta.datawrapper.TimeManagerDeletion;
import de.hshannover.f4.trust.visitmeta.graphCalculator.Calculator;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FacadeLogic;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FactoryCalculator;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FactoryCalculator.CalculatorType;
import de.hshannover.f4.trust.visitmeta.gui.GraphConnection;
import de.hshannover.f4.trust.visitmeta.gui.GuiController;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnection;
import de.hshannover.f4.trust.visitmeta.network.Connection;
import de.hshannover.f4.trust.visitmeta.network.FacadeNetwork;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection.ConnectionType;

/**
 * Class with main-method.
 */
public final class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class);

	private static List<DataserviceConnection> mDataserviceConnections;
	private static List<RESTConnection> mRestConnections;

	/**
	 * Wegen der Sicherheit!
	 */
	private Main() {
	}

	/**
	 * Main-Methode
	 * 
	 * @param args
	 *            not used.
	 */
	public static void main(String[] args) {
		LOGGER.trace("Method main(" + args + ") called.");

		loadDataserviceConnections();

		String vConnectionTypeString = PropertiesManager.getProperty("application", "dataservice.connectiontype", "local").toUpperCase();
		ConnectionType vConnectionType = ConnectionType.valueOf(vConnectionTypeString);

		GuiController gui = new GuiController();

		if(vConnectionType == ConnectionType.REST){

			for(DataserviceConnection dc : mDataserviceConnections) {
				GraphContainer tmpCon = new GraphContainer(dc.getName(), dc);
				gui.addConnection(tmpCon);
			}
			
//			for(RESTConnection restCon: mRestConnections){
//				Connection vConnection = FactoryConnection.getConnection(vConnectionType, restCon);
//				Calculator vCalculator = FactoryCalculator.getCalculator(CalculatorType.JUNG);
//
//				FacadeNetwork vNetwork = new FacadeNetwork(vConnection);
//				FacadeLogic vLogic = new FacadeLogic(vNetwork, vCalculator);
//				GraphConnection connController = new GraphConnection(vLogic);
//
//				gui.addConnection(restCon.getName() , connController, restCon);
//
//				Thread vThreadNetwork = new Thread(vNetwork);
//				Thread vThreadLogic = new Thread(vLogic);
//
//				vThreadNetwork.start();
//				vThreadLogic.start();
//			}
		}
//
//		TimeManagerCreation vTimerCreation = TimeManagerCreation.getInstance();
//		TimeManagerDeletion vTimerDeletion = TimeManagerDeletion.getInstance();
//
//		Thread vThreadCreation = new Thread(vTimerCreation);
//		Thread vThreadDeletion = new Thread(vTimerDeletion);
//
//		vThreadCreation.start();
//		vThreadDeletion.start();
		gui.show();

	}

	private static void loadDataserviceConnections(){
		mDataserviceConnections = new ArrayList<DataserviceConnection>();

		int count = Integer.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT, "0"));
		for(int i=0; i<count; i++){
			String name = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_NAME(i), "localhost");
			String url = PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_URL(i), "http://localhost:8000");
			boolean rawXml = Boolean.valueOf(PropertiesManager.getProperty("application", ConfigParameter.VISUALIZATION_USER_CONNECTION_DATASERVICE_COUNT_RAWXML(i), "true").toLowerCase());

			DataserviceConnection tmpConnection = new DataserviceConnection(name, url, rawXml);
			mDataserviceConnections.add(tmpConnection);
		}
	}

//	private static void loadRESTConnections(){
//		mRestConnections = new ArrayList<RESTConnection>();
//
//
//		for(DataserviceConnection dc: mDataserviceConnections){
//
//			ClientConfig config = new DefaultClientConfig();
//			Client client = Client.create(config);
//
//			URI uri_connect = UriBuilder.fromUri(dc.getUrl()).build();
//			WebResource temp1 = client.resource(uri_connect);
//			JSONObject jsonResponse = temp1.accept(MediaType.APPLICATION_JSON).get(JSONObject.class);
//			System.out.println(jsonResponse);
//
//			Iterator<String> ii = jsonResponse.keys();
//			while(ii.hasNext()){
//				String jKey = ii.next();
//				JSONObject jsonConnection;
//				try {
//					jsonConnection = jsonResponse.getJSONObject(jKey);
//					RESTConnection restConn = new RESTConnection(dc, jKey, jsonConnection.getString("URL"), false);
//					restConn.setBasicAuthentication(true);
//					restConn.setUsername(jsonConnection.getString("Username"));
//					restConn.setPassword(jsonConnection.getString("Password"));
//
//					mRestConnections.add(restConn);
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
}
