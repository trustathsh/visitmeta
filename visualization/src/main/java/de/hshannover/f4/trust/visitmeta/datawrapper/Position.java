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

import org.apache.log4j.Logger;

/**
 * Class that contains the coordinates of a node.
 */
public class Position extends Observable {

	private static final Logger LOGGER = Logger.getLogger(Position.class);

	private double  mX      = 0.0;
	private double  mY      = 0.0;
	private double  mZ      = 0.0;
	private boolean isInUse = false;

	public Position() {}

	public Position(double x, double y, double z) {
		mX = x;
		mY = y;
		mZ = z;
	}

	public synchronized double getX() {
		LOGGER.trace("Method getX() called.");
		return mX;
	}

	public void setX(double x) {
		LOGGER.trace("Method setX(" + x + ") called.");
		synchronized (this) {
			mX = x;
			setChanged();
		}
		notifyObservers();
	}

	public synchronized double getY() {
		LOGGER.trace("Method getY() called.");
		return mY;
	}

	public void setY(double y) {
		LOGGER.trace("Method setY(" + y + ") called.");
		synchronized (this) {
			mY = y;
			setChanged();
		}
		notifyObservers();
	}

	public synchronized double getZ() {
		LOGGER.trace("Method getZ() called.");
		return mZ;
	}

	public void setZ(double z) {
		LOGGER.trace("Method setZ(" + z + ") called.");
		synchronized (this) {
			mZ = z;
			setChanged();
		}
		notifyObservers();
	}

	public void setPosition(double x, double y, double z) {
		LOGGER.trace("Method setPosition(" + x + ", " + y + ", " + z + ") called.");
		synchronized (this) {
			mX = x;
			mY = y;
			mZ = z;
			setChanged();
		}
		notifyObservers();
	}

	public void setPosition(Position p) {
		LOGGER.trace("Method setPosition(" + p + ") called.");
		synchronized (this) {
			mX = p.getX();
			mY = p.getY();
			mZ = p.getZ();
			setChanged();
		}
		notifyObservers();
	}

	/**
	 * Set if the user is dragging the node.
	 * @param use drags the user the node.
	 */
	public synchronized void setInUse(boolean use) {
		LOGGER.trace("Method setInUse(" + use + ") called.");
		isInUse = use;
	}

	/**
	 * Return if the user is dragging the node.
	 * @return true  = user is dragging the node.
	 *         false = user isn't dragging the node.
	 */
	public synchronized boolean isInUse() {
		LOGGER.trace("Method isInUse() called.");
		return isInUse;
	}
}
