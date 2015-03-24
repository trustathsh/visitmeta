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
 * This file is part of visitmeta-visualization, version 0.4.0,
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
package de.hshannover.f4.trust.visitmeta.datawrapper;

import de.hshannover.f4.trust.visitmeta.graphCalculator.Calculator;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FacadeLogic;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FactoryCalculator;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FactoryCalculator.CalculatorType;
import de.hshannover.f4.trust.visitmeta.gui.GraphConnection;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.network.Connection;
import de.hshannover.f4.trust.visitmeta.network.FacadeNetwork;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection.ConnectionType;
import de.hshannover.f4.trust.visitmeta.network.GraphPool;

/**
 * This class is a container class, holding the needed information about a
 * connection to a graph.
 *
 * @author oelsner
 *
 */
public class GraphContainer {
	private String mName = null;
	private String mRestConnectionName;
	private Connection mConnection = null;
	private Calculator mCalculator = null;
	private DataserviceConnection mDataserviceConnection = null;
	private FacadeNetwork mFacadeNetwork = null;
	private FacadeLogic mFacadeLogic = null;
	private GraphConnection mGraphConnection = null;
	private GraphPool mGraphPool = null;
	private TimeHolder mTimeHolder = null;
	private TimeManagerCreation mTimeManagerCreation = null;
	private TimeManagerDeletion mTimeManagerDeletion = null;
	private SettingManager mSettingManager = null;
	private String mRestUrl;

	/**
	 * The constructor initializes and connects all classes needed for a
	 * connection to a given graph.
	 *
	 * @param restConnectionName
	 *            is the name of the selected graph
	 * @param dataserviceConnection
	 *            is the connection to the selected dataservice
	 */
	public GraphContainer(String restConnectionName,
			DataserviceConnection dataserviceConnection) {
		mName = dataserviceConnection.getName() + ":" + restConnectionName;
		mRestConnectionName = restConnectionName;
		mRestUrl = dataserviceConnection.getUrl() + "/" + restConnectionName
				+ "/graph";
		mTimeHolder = new TimeHolder();
		mGraphPool = new GraphPool();
		mSettingManager = new SettingManager(this);
		mTimeManagerCreation = new TimeManagerCreation(this);
		mTimeManagerDeletion = new TimeManagerDeletion(this);

		mDataserviceConnection = dataserviceConnection;

		mConnection = FactoryConnection
				.getConnection(ConnectionType.REST, this);
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

	public String getRestConnectionName() {
		return mRestConnectionName;
	}

	public GraphConnection getGraphConnection() {
		return mGraphConnection;
	}

	public FacadeNetwork getFacadeNetwork() {
		return mFacadeNetwork;
	}

	public GraphPool getGraphPool() {
		return mGraphPool;
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

	public DataserviceConnection getDataserviceConnection() {
		return mDataserviceConnection;
	}

	public String getRestUrl() {
		return mRestUrl;
	}

	@Override
	public int hashCode() {
		return mName.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof GraphContainer)) {
			return false;
		}
		return mName.equals((((GraphContainer) o).mName));
	}
}
