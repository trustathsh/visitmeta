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
 * This file is part of visitmeta-visualization, version 0.5.2,
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

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

public abstract class TimeManager implements Runnable, Observer {

	/**
	 * Simple Pair class. (copied from ifmapj)
	 *
	 * @author aw
	 *
	 * @param <T>
	 * @param <V>
	 */
	private class Pair<T, V> {

		public T first;
		public V second;

		public Pair(T first, V second) {
			this.first = first;
			this.second = second;
		}
	}

	private static final Logger LOGGER = Logger.getLogger(TimeManager.class);

	protected ArrayList<Pair<Long, Position>> mNodes = null;
	private GraphContainer mConnection = null;
	private SettingManager mSettingManger = null;
	private TimeHolder mTimeHolder = null;
	private boolean mIsDone = false;
	private int mTimeout = 0;
	private boolean mLiveView = true;

	public TimeManager(GraphContainer connection) {
		mConnection = connection;
		mSettingManger = mConnection.getSettingManager();
		mTimeHolder = mConnection.getTimeHolder();
		mNodes = new ArrayList<>();
		mTimeout = mSettingManger.getHighlightsTimeout();
		mLiveView = mTimeHolder.isLiveView();
		mSettingManger.addObserver(this);
		mTimeHolder.addObserver(this);
	}

	/**
	 * Add a node to the list.
	 *
	 * @param pNode
	 *            the node.
	 */
	public void addNode(Position pNode) {
		LOGGER.trace("Method addNewNode("
				+ pNode + ") called.");
		synchronized (this) {
			mNodes.add(new Pair<Long, Position>(System.currentTimeMillis(), pNode));
		}
	}

	/**
	 * Delete all nodes in the list and call for each
	 * {@link TimeManager#processNode(Position)}.
	 */
	public void removeAll() {
		LOGGER.trace("Method removeAll() called.");
		synchronized (this) {
			for (Pair<Long, Position> vNode : mNodes) {
				processNode(vNode.second);
			}
			mNodes.clear();
		}
	}

	/**
	 * Delete all node in the list without calling
	 * {@link TimeManager#processNode(Position)} for each node in the list.
	 */
	public void dropAll() {
		LOGGER.trace("Method dropAll() called.");
		mNodes.clear();
	}

	/**
	 * Stop the loop of the run()-Method.
	 */
	public void finish() {
		LOGGER.trace("Method finish() called.");
		synchronized (this) {
			mIsDone = true;
		}
	}

	@Override
	public synchronized void update(Observable pO, Object pArg) {
		LOGGER.trace("Method update("
				+ pO + ", " + pArg + ") called.");
		if (pO instanceof TimeHolder) {
			synchronized (this) {
				boolean vLiveView = mTimeHolder.isLiveView();
				// if(mLiveView != vLiveView) {
				// removeAll();
				mLiveView = vLiveView;
				// }
				// notify();
			}
		} else if (pO instanceof SettingManager) {
			synchronized (this) {
				mTimeout = mSettingManger.getHighlightsTimeout();
			}
			// notify();
		}
	}

	@Override
	public void run() {
		LOGGER.trace("Method run() called.");
		try {
			synchronized (this) {
				while (!mIsDone) {
					if (mNodes.size() > 0
							&& mLiveView) {
						long vTime = mNodes.get(0).first
								+ mTimeout - System.currentTimeMillis();
						if (vTime > 0) {
							/* Wait */
							wait(vTime);
						} else {
							/* Process node */
							processNode(mNodes.get(0).second);
							mNodes.remove(0);
						}
					} else {
						if (mTimeout > 0) {
							wait(mTimeout);
						} else {
							wait(1_000);
						}
					}
				}
			}
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * Is called by {@link TimeManager#removeAll()} and
	 * {@link TimeManager#run()}.
	 */
	protected abstract void processNode(Position pNode);
}
