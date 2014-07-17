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
 * This file is part of visitmeta visualization, version 0.0.7,
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
 * The class holds the timestamps.
 */
public class TimeHolder extends Observable {
	private static final Logger LOGGER = Logger.getLogger(TimeHolder.class);
	private SortedMap<Long, Long> mChangesMap = null;
	private long mTimeStart = 0L;
	private long mTimeEnd = 0L;
	private boolean mIsInitialized = false;

	public TimeHolder(GraphContainer connection) {
	}

	/**
	 * Detects whether the TimeHolder object hat a ChangeMap
	 * 
	 * @return boolean whether the TimeHolder object has a ChangeMap or not
	 */
	public boolean hasChangeMap() {
		LOGGER.trace("Method hasChangeMap() called.");
		return mChangesMap != null && mChangesMap.size() > 0;
	}

	/**
	 * The oldest possible timestamp to select.
	 */
	public synchronized long getBigBang() {
		LOGGER.trace("Method getBigBang() called.");
		if (hasChangeMap()) {
			return mChangesMap.firstKey();
		}
		return 0L;
	}

	/**
	 * The newest possible timestamp to select
	 */
	public synchronized long getNewestTime() {
		LOGGER.trace("Method getNewestTime() called.");
		if (hasChangeMap()) {
			return mChangesMap.lastKey();
		}
		return 0L;
	}
	
	/**
	 * States whether the TimeHolder is initialized or not
	 * @return true if TimeHolder is initialized
	 */
	public synchronized boolean isInitialized() {
		return mIsInitialized;
	}

	public synchronized long getTimeStart() {
		LOGGER.trace("Method getTimeStart() called.");
		return mTimeStart;
	}

	public synchronized long getTimeEnd() {
		LOGGER.trace("Method getTimeEnd() called.");
		return mTimeEnd;
	}

	public synchronized SortedMap<Long, Long> getChangesMap() {
		LOGGER.trace("Method getChangesMap() called.");
		return mChangesMap;
	}

	public synchronized void setChangesMap(SortedMap<Long, Long> pMap) {
		setChangesMap(pMap, true);
	}

	public synchronized void setChangesMap(SortedMap<Long, Long> pMap, boolean pNotify) {
		LOGGER.trace("Method setChangesMap(" + pMap + ", " + pNotify + ") called.");
		mChangesMap = pMap;
		setTimeEnd(mChangesMap.get(mChangesMap.lastKey()));
		setChanged();
		if (pNotify) {
			notifyObservers();
		}
	}

	public synchronized void setTimeStart(long pTime) {
		setTimeStart(pTime, true);
	}

	public synchronized void setTimeStart(long pTime, boolean pNotify) {
		LOGGER.trace("Method setTimeStart(" + pTime + ", " + pNotify + ") called.");
		if (hasChangeMap()) {
			if (mChangesMap.firstKey() <= pTime && pTime <= mChangesMap.lastKey() && mTimeStart != pTime) {
				mTimeStart = pTime;
				setChanged();
				if (pNotify) {
					notifyObservers();
				}
			}
		}
		mIsInitialized = true;
	}

	public synchronized void setTimeEnd(long pTime) {
		setTimeEnd(pTime, true);
	}

	public synchronized void setTimeEnd(long pTime, boolean pNotify) {
		LOGGER.trace("Method setTimeEnd(" + pTime + ", " + pNotify + ") called.");
		if (hasChangeMap()) {
			if (mChangesMap.firstKey() <= pTime && pTime <= mChangesMap.lastKey() && mTimeEnd != pTime) {
				mTimeEnd = pTime;
				setChanged();
				if (pNotify) {
					notifyObservers();
				}
			}
		}
		mIsInitialized = true;
	}
}
