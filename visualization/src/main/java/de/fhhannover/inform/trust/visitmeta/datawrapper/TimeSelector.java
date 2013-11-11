package de.fhhannover.inform.trust.visitmeta.datawrapper;

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
 * This file is part of VisITMeta, version 0.0.1, implemented by the Trust@FHH 
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
/* Imports ********************************************************************/
import java.util.Observable;

import org.apache.log4j.Logger;
/* Class **********************************************************************/
/**
 * The class holds the next timestamps.
 */
public class TimeSelector extends Observable {
/* Attributes *****************************************************************/
	private static final Logger LOGGER = Logger.getLogger(TimeSelector.class);
	/** Singleton */
	private static TimeSelector mInstance = null;
	private TimeHolder mTimeHolder = null;
	private long       mTimeStart  = 0L;
	private long       mTimeEnd    = 0L;
	private boolean    mLiveView   = true;
/* Constructors ***************************************************************/
	private TimeSelector() {
		mTimeHolder = TimeHolder.getInstance();
	}
/* Methods ********************************************************************/
	public synchronized boolean hasTimeStartChanged() {
		LOGGER.trace("Method hasTimeStartChanged() called.");
		return mTimeStart != mTimeHolder.getTimeStart();
	}
	public synchronized boolean hasTimeEndChanged() {
		LOGGER.trace("Method hasTimeEndChanged() called.");
		return mTimeEnd != mTimeHolder.getTimeEnd();
	}
	public synchronized boolean isLiveView() {
		LOGGER.trace("Method isLiveView() called.");
		return mLiveView;
	}
/* Methods - Getter ***********************************************************/
	/**
	 * Singleton Thread-Safe
	 * @return the instance of TimeSelector.
	 */
	public static TimeSelector getInstance() {
		LOGGER.trace("Method getInstance() called.");
		if(mInstance == null) { // DoubleCheck
			synchronized (TimeSelector.class) {
				if (mInstance == null) {
					mInstance = new TimeSelector();
				}
			}
		}
		return mInstance;
	}
	public synchronized long getTimeStart() {
		LOGGER.trace("Method getTimeStart() called.");
		return mTimeStart;
	}
	public synchronized long getTimeEnd() {
		LOGGER.trace("Method getTimeEnd() called.");
		return mTimeEnd;
	}
/* Methods - Setter ***********************************************************/
	public synchronized void setTimeStart(long pTime) {
		setTimeStart(pTime, true);
	}
	public synchronized void setTimeStart(long pTime, boolean pNotify) {
		LOGGER.trace("Method setTimeStart(" + pTime + ", " + pNotify +") called.");
		if (mTimeStart != pTime) {// && mTimeHolder.getTimeStart() != pTime) {
			mTimeStart = pTime;
			setChanged();
			if(pNotify) {
				notifyObservers();
			}
		}
	}
	public synchronized void setTimeEnd(long pTime) {
		setTimeEnd(pTime, true);
	}
	public synchronized void setTimeEnd(long pTime, boolean pNotify) {
		LOGGER.trace("Method setTimeEnd(" + pTime + ", " + pNotify +") called.");
		if (mTimeEnd != pTime) {// && mTimeHolder.getTimeEnd() != pTime) {
			mTimeEnd = pTime;
			setChanged();
			if(pNotify) {
				notifyObservers();
			}
		}
	}
	public synchronized void setLiveView(boolean pLive) {
		setLiveView(pLive, true);
	}
	public synchronized void setLiveView(boolean pLive, boolean pNotify) {
		LOGGER.trace("Method setLiveView(" + pLive + ", " + pNotify + ") called.");
		if(mLiveView != pLive) {
			mLiveView = pLive;
			setChanged();
			if(pNotify) {
				notifyObservers();
			}
		}
	}
}