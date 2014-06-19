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
 * This file is part of visitmeta visualization, version 0.0.6,
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
package de.hshannover.f4.trust.visitmeta.network;

import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.SettingManager;
import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.datawrapper.TimeSelector;
import de.hshannover.f4.trust.visitmeta.datawrapper.UpdateContainer;

/**
 * Network class that handles the connection to the dataservice.
 */
public class FacadeNetwork extends Observable implements Runnable, Observer {

	private static final Logger LOGGER = Logger.getLogger(FacadeNetwork.class);

	private GraphContainer mConnection = null;
	private Connection mNetworkConnection = null;
	private TimeSelector mTimeSelector = null;
	private SettingManager mSettingManager = null;
	private boolean mIsDone = false;
	private boolean mLoadNewest = true;
	private int mInterval = 0;

	public FacadeNetwork(GraphContainer connection) {
		mConnection = connection;
		mNetworkConnection = mConnection.getConnection();
		mSettingManager = mConnection.getSettingManager();
		mTimeSelector = mConnection.getTimeSelector();
		mInterval = mSettingManager.getNetworkInterval();
		mSettingManager.addObserver(this);
		mTimeSelector.addObserver(this);
	}

	public synchronized UpdateContainer getUpdate() {
		LOGGER.trace("Method getUpdate() called.");
		return mNetworkConnection.getUpdate();
	}

	/**
	 * Stop the loop of the run()-Method.
	 */
	public synchronized void finish() {
		LOGGER.trace("Method finish() called.");
		mIsDone = true;
	}

	/**
	 * Load the initial graph to the timestamp in TimeSelector. This methode
	 * notify the observers.
	 * 
	 * @see Connection#loadInitialGraph()
	 */
	public synchronized void loadInitialGraph() {
		LOGGER.trace("Method loadInitialGraph() called.");
		clearGraph();
		mNetworkConnection.loadInitialGraph();
		setChanged();
		notifyObservers();
	}

	/**
	 * Load the delta to the timestamps in TimeSelector. This methode notify the
	 * observers.
	 * 
	 * @see Connection#loadDelta()
	 */
	public synchronized void loadDelta() {
		LOGGER.trace("Method loadDelta() called.");
		mNetworkConnection.loadDelta();
		setChanged();
		notifyObservers();
	}

	/**
	 * Clear the pools.
	 * 
	 * @see PoolNodeIdentifier#clear()
	 * @see PoolNodeMetadata#clear()
	 * @see PoolExpandedLink#clear()
	 */
	public synchronized void clearGraph() {
		LOGGER.trace("Method clearGraph() called.");
		PoolNodeIdentifier.clear();
		PoolNodeMetadata.clear();
		PoolExpandedLink.clear();
	}

	@Override
	public void run() {
		LOGGER.trace("Method run() called.");
		try {
			synchronized (this) {
				while (!mIsDone) {
					mNetworkConnection.loadChangesMap();
					if (mLoadNewest && mNetworkConnection.updateGraph()) {
						setChanged();
						notifyObservers();
					}
					wait(mInterval);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable pO, Object pArg) {
		LOGGER.trace("Method update(" + pO + ", " + pArg + ") called.");
		synchronized (this) {
			if (pO instanceof TimeSelector) {
				mLoadNewest = mTimeSelector.isLiveView();
				// if (mConnection.delta()) {
				// setChanged();
				// notifyObservers();
				// }
			} else if (pO instanceof SettingManager) {
				mInterval = mSettingManager.getNetworkInterval();
			}
		}
	}
}
