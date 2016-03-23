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
 * This file is part of visitmeta-visualization, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
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

import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.datawrapper.SettingManager;
import de.hshannover.f4.trust.visitmeta.datawrapper.TimeHolder;
import de.hshannover.f4.trust.visitmeta.datawrapper.UpdateContainer;

/**
 * Main class for the graph management. The class updates and manages the graph
 * during the programs life cycle.
 */
public class FacadeNetwork extends Observable implements Runnable, Observer {
	private static final Logger LOGGER = Logger.getLogger(FacadeNetwork.class);

	private GraphContainer mConnection = null;
	private GraphNetworkConnection mNetworkConnection = null;
	private TimeHolder mTimeHolder = null;
	private GraphPool mGraphPool = null;
	private SettingManager mSettingManager = null;
	private boolean mIsDone = false;
	private boolean mLoadNewest = true;
	private int mInterval = 0;

	/**
	 * Initializes the FacadeNetwork and connects all needed classes.
	 *
	 * @param container
	 *            Contains information about the Connection.
	 */
	public FacadeNetwork(GraphContainer container) {
		mConnection = container;
		mNetworkConnection = mConnection.getConnection();
		mSettingManager = mConnection.getSettingManager();
		mTimeHolder = mConnection.getTimeHolder();
		mInterval = mSettingManager.getNetworkInterval();
		mSettingManager.addObserver(this);
		mTimeHolder.addObserver(this);
		mGraphPool = container.getGraphPool();
	}

	public synchronized UpdateContainer getUpdate() {
		return mNetworkConnection.getUpdate();
	}

	/**
	 * Ends the main loop.
	 */
	public void finish() {
		mIsDone = true;
	}

	/**
	 * Loads the initial graph. This method notifies the observer.
	 *
	 * @see GraphNetworkConnection#loadGraphAtDeltaStart()
	 */
	public synchronized void loadGraphAtDeltaStart() {
		clearGraph();
		try {
			mNetworkConnection.loadGraphAtDeltaStart();
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage());
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Loads the current graph. This method notifies the observer.
	 *
	 * @see GraphNetworkConnection#loadCurrentGraph()
	 */
	public synchronized void loadCurrentGraph() {
		clearGraph();
		try {
			mNetworkConnection.loadCurrentGraph();
		} catch (InterruptedException e) {
			LOGGER.debug("Interrupted loadCurrentGraph()");
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Loads the delta. This method notifies the observer.
	 *
	 * @see GraphNetworkConnection#loadDelta()
	 */
	public synchronized void loadDelta() {
		mNetworkConnection.loadDelta();
		setChanged();
		notifyObservers();
	}

	/**
	 * Clears all data from the pools.
	 */
	public synchronized void clearGraph() {
		mGraphPool.clearGraph();
	}

	@Override
	public void run() {
		synchronized (this) {
			loadCurrentGraph();
			while (!mIsDone) {
				mNetworkConnection.loadChangesMap();

				try {
					if (mLoadNewest
							&& mNetworkConnection.updateGraph()) {
						setChanged();
						notifyObservers();
					}
				} catch (InterruptedException e) {
					LOGGER.debug("Interrupted updateGraph()");
				}

				try {
					wait(mInterval);
				} catch (InterruptedException e) {
					LOGGER.debug("Interrupted wait()");
				}
			}
		}
		LOGGER.info("Loop which updates the Graph has ended.");
	}

	@Override
	public void update(Observable observable, Object obj) {
		if (observable instanceof TimeHolder) {
			synchronized (this) {
				mLoadNewest = mTimeHolder.isLiveView();
			}
		} else if (observable instanceof SettingManager) {
			mInterval = mSettingManager.getNetworkInterval();
		}
	}
}
