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
 * This file is part of visitmeta visualization, version 0.1.2,
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
package de.hshannover.f4.trust.visitmeta.datawrapper;

import java.util.Observable;
import java.util.SortedMap;

import org.apache.log4j.Logger;

/**
 * This class manages timestamps for a connection. The timestamps contained in
 * this class have two purposes. One of which is managing the continuous updates
 * during live view. The other purpose is to determine the correct delta for a
 * historic snapshot.
 */
public class TimeHolder extends Observable {
	private static final Logger LOGGER = Logger.getLogger(TimeHolder.class);
	private SortedMap<Long, Long> mChangesMap = null;
	private long mDeltaTimeStart = 0L;
	private long mDeltaTimeEnd = 0L;
	private boolean mIsInitialized = false;
	private boolean mLiveView = true;
	private boolean mIsSetLive = false;

	/**
	 * No idempotent call! When this method is called the isSetLive flag will be
	 * set to false.
	 * 
	 * @return Whether or not the view switched to live.
	 */
	public boolean isSetLive() {
		boolean tmpIsSetLive = mIsSetLive;
		mIsSetLive = false;
		return tmpIsSetLive;
	}

	/**
	 * 
	 * @return Whether or not a changesMap exists.
	 */
	public boolean hasChangeMap() {
		return mChangesMap != null && mChangesMap.size() > 0;
	}

	/**
	 * @return The oldest timestamp.
	 */
	public synchronized long getBigBang() {
		if (hasChangeMap()) {
			return mChangesMap.firstKey();
		}
		return 0L;
	}

	/**
	 * @return The newest timestamp.
	 */
	public synchronized long getNewestTime() {
		if (hasChangeMap()) {
			return mChangesMap.lastKey();
		}
		return 0L;
	}

	public synchronized boolean isInitialized() {
		return mIsInitialized;
	}

	public synchronized SortedMap<Long, Long> getChangesMap() {
		return mChangesMap;
	}

	/**
	 * @param changesMap
	 *            A SortedMap that maps timestamps to the number of changes that
	 *            occurred at that time in ascending order.
	 */
	public synchronized void setChangesMap(SortedMap<Long, Long> changesMap) {
		setChangesMap(changesMap, true);
	}

	/**
	 * @param changesMap
	 *            A SortedMap that maps timestamps to the number of changes that
	 *            occurred at that time in ascending order.
	 * @param notify
	 *            Decides whether to notify its observers or not.
	 */
	public synchronized void setChangesMap(SortedMap<Long, Long> changesMap, boolean notify) {
		if (changesMap != null && changesMap.size() > 0) {
			if (!changesMap.equals(mChangesMap)) {
				mChangesMap = changesMap;
				if (!mIsInitialized) {
					setDeltaTimeStart(mChangesMap.lastKey(), false);
					setDeltaTimeEnd(mChangesMap.lastKey(), false);
					mIsInitialized = true;
					LOGGER.info("TimeHoler is initialized successfully.");
				} else {
					setDeltaTimeStart(mChangesMap.firstKey(), false);
				}
				setChanged();
				if (notify) {
					notifyObservers();
				}
			}
		}
	}

	public synchronized boolean isLiveView() {
		return mLiveView;
	}

	public synchronized long getDeltaTimeStart() {
		return mDeltaTimeStart;
	}

	public synchronized long getDeltaTimeEnd() {
		return mDeltaTimeEnd;
	}

	/**
	 * @param deltaTimeStart
	 *            Timestamp which indicates the start of a delta.
	 */
	public synchronized void setDeltaTimeStart(long deltaTimeStart) {
		setDeltaTimeStart(deltaTimeStart, true);
	}

	/**
	 * @param deltaTimeStart
	 *            Timestamp which indicates the start of a delta.
	 * @param notify
	 *            Decides whether to notify its observers or not.
	 */
	public synchronized void setDeltaTimeStart(long deltaTimeStart, boolean notify) {
		if (mDeltaTimeStart != deltaTimeStart) {
			mDeltaTimeStart = deltaTimeStart;
			setChanged();
			if (notify) {
				notifyObservers();
			}
		}
	}

	/**
	 * @param deltaTimeEnd
	 *            Timestamp which indicates the end of a delta.
	 */
	public synchronized void setDeltaTimeEnd(long deltaTimeEnd) {
		setDeltaTimeEnd(deltaTimeEnd, true);
	}

	/**
	 * @param deltaTimeEnd
	 *            Timestamp which indicates the end of a delta.
	 * @param notify
	 *            Decides whether to notify its observers or not.
	 */
	public synchronized void setDeltaTimeEnd(long deltaTimeEnd, boolean notify) {
		if (mDeltaTimeEnd != deltaTimeEnd) {
			mDeltaTimeEnd = deltaTimeEnd;
			setChanged();
			if (notify) {
				notifyObservers();
			}
		}
	}

	/**
	 * @param isLive
	 *            Contains information whether the view is live or not.
	 */
	public synchronized void setLiveView(boolean isLive) {
		setLiveView(isLive, true);
	}

	/**
	 * 
	 * @param isLive
	 *            Contains information whether the view is live or not.
	 * @param notify
	 *            Decides whether to notify its observers or not.
	 */
	public synchronized void setLiveView(boolean isLive, boolean notify) {
		if (mLiveView != isLive) {
			mLiveView = isLive;
			if (mLiveView) {
				mIsSetLive = true;
			}
			setChanged();
			if (notify) {
				notifyObservers();
			}
		}
	}
}
