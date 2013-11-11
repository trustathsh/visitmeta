package de.fhhannover.inform.trust.visitmeta.graphCalculator;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * This file is part of VisITMeta, version 0.0.2, implemented by the Trust@FHH 
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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

import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.datawrapper.Position;
import de.fhhannover.inform.trust.visitmeta.datawrapper.PropertiesManager;
import de.fhhannover.inform.trust.visitmeta.datawrapper.SettingManager;
import de.fhhannover.inform.trust.visitmeta.datawrapper.TimeManagerDeletion;
import de.fhhannover.inform.trust.visitmeta.datawrapper.UpdateContainer;
import de.fhhannover.inform.trust.visitmeta.network.FacadeNetwork;

/**
 * Calculates the layout and provides a graph structure to the gui.
 */
public class FacadeLogic extends Observable implements Observer, Runnable {

	private static final Logger  LOGGER  = Logger.getLogger(FacadeLogic.class);

	private FacadeNetwork       mFacadeNetwork   = null;
	private Calculator          mCalculator      = null;
	private UpdateContainer     mUpdateContainer = null;
	private SettingManager      mSettingManager  = null;
	private boolean             mIsDone          = false;
	private boolean             mDoCalculation   = true;
	private int                 mInterval        = 0;
	private int                 mIterations      = 0;
	private TimeManagerDeletion mTimerDeletion   = null;

	public FacadeLogic(FacadeNetwork facadeNetwork, Calculator calculator) {
		mUpdateContainer = new UpdateContainer();
		mFacadeNetwork   = facadeNetwork;
		mCalculator      = calculator;
		mSettingManager  = SettingManager.getInstance();
		mInterval        = mSettingManager.getCalculationInterval();
		mIterations      = mSettingManager.getCalculationIterations();
		mCalculator.setIterations(mIterations);
		mTimerDeletion = TimeManagerDeletion.getInstance();
		mTimerDeletion.setLogic(this);
		mSettingManager.addObserver(this);
		mFacadeNetwork.addObserver(this);
	}

	public synchronized UpdateContainer getUpdate() {
		LOGGER.trace("Method getUpdate() called.");
		return mUpdateContainer.getCopyFlat();
	}

	/**
	 * End the thread.
	 */
	public synchronized void finish() {
		LOGGER.trace("Method finish() called.");
		mIsDone = true;
	}

	/**
	 * Set the new position for a Position-Object.
	 * @param pNode the Object.
	 * @param pNewX the new x coordinate.
	 * @param pNewY the new y coordinate.
	 * @param pNewZ the new z coordinate.
	 */
	public synchronized void updateNode(Position pNode, double pNewX, double pNewY, double pNewZ) {
		LOGGER.trace(
				"Method updateNode(" +
				pNode + ", " +
				pNewX + ", " +
				pNewY + ", " +
				pNewZ + ") called."
		);
		mCalculator.updateNode(pNode, pNewX, pNewY, pNewZ);
	}

	/**
	 * Start the calculation of position in the thread.
	 */
	public synchronized void startCalculation() {
		LOGGER.trace("Method startCalculation() called.");
		mDoCalculation = true;
	}

	/**
	 * Stop the calculation of position in the thread.
	 */
	public synchronized void stopCalculation() {
		LOGGER.trace("Method stopCalculation() called.");
		mDoCalculation = false;
	}

	/**
	 * Return if the thread is calculation positions.
	 * @return False = Motion of graph is Off.
	 *         True  = Motion of graph is On.
	 */
	public boolean isCalculationRunning() {
		LOGGER.trace("Method isCalculationRunning() called.");
		return mDoCalculation;
	}

	/**
	 * Load the initial graph to the timestamp in TimeSelector.
	 * This methode notify the observers.
	 * @see FacadeNetwork#loadInitialGraph()
	 */
	public synchronized void loadInitialGraph() {
		LOGGER.trace("Method loadInitialGraph() called.");
		/* Remove all data */
		clearGraph();
		/* Call for initial graph */
		mFacadeNetwork.loadInitialGraph();
	}

	/**
	 * Load the delta to the timestamps in TimeSelector.
	 * This methode notify the observers.
	 * @see FacadeNetwork#loadDelta()
	 */
	public synchronized void loadDelta() {
		LOGGER.trace("Method loadDelta() called.");
		/* Call for delta */
		mFacadeNetwork.loadDelta();
	}

	/**
	 * Remove all nodes and edges from the calcualtor.
	 */
	public synchronized void clearGraph() {
		LOGGER.trace("Method clearGraph() called.");
		mCalculator.clearGraph();
	}

	/**
	 * Remove a node from the graph.
	 * @param pNode the node.
	 */
	public synchronized void deleteNode(Position pNode) {
		LOGGER.trace("Method deleteNode(" + pNode + ") called.");
		mCalculator.deleteNode(pNode);
	}


	/**
	 * Recalculate the position of all nodes.
	 */
	public synchronized void recalculateGraph() {
		LOGGER.trace("Method recalculateGraph() called.");
		mCalculator.adjustGraphAnew(Integer.parseInt(PropertiesManager.getProperty(
				"visualizationConfig",    // FileName
				"calculation.iterations", // Key
				"100"                     // Alternative
		)));
	}

	@Override
	public void update(Observable o, Object arg) {
		LOGGER.trace("Method update(" + o + ", " + arg + ") called.");
		if(o instanceof FacadeNetwork) {
			synchronized(this) {
				/* Add nodes to calculator. */
				mUpdateContainer = mFacadeNetwork.getUpdate();
				mCalculator.addRemoveNodesLinksMetadatas(mUpdateContainer);
			}
			setChanged();
			notifyObservers();
		} else if(o instanceof SettingManager) {
			mInterval   = mSettingManager.getCalculationInterval();
			mIterations = mSettingManager.getCalculationIterations();
			mCalculator.setIterations(mIterations);
		}
	}

	@Override
	public void run() {
		LOGGER.trace("Method run() called.");
		try {
			synchronized (this) {
				while (!mIsDone) {
					if(mDoCalculation) {
						mCalculator.adjustAllNodes(mIterations, true, true);
					}
					wait(mInterval);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
