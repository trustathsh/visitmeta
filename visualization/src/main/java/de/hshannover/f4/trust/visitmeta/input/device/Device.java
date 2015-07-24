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
 * This file is part of visitmeta-visualization, version 0.5.0,
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
package de.hshannover.f4.trust.visitmeta.input.device;

import de.hshannover.f4.trust.visitmeta.input.gui.MotionControllerHandler;

/**
 * Interface for external control devices.
 * 
 * @author Bastian Hellmann
 *
 */
public interface Device {

	/**
	 * Method to initialize the {@link Device}, after the instance was
	 * constructed.
	 * 
	 * @return true, if initialization was successful
	 */
	public boolean init();

	/**
	 * Method to properly shutdown the {@link Device}.
	 * 
	 * @return true, if disconnection was successful
	 */
	public boolean shutdown();

	/**
	 * Start operations of the {@link Device}, i.e. start threads etc.
	 */
	public void start();

	/**
	 * Stop operations of the {@link Device}, i.e. stop threads etc.
	 */
	public void stop();

	/**
	 * Sets the {@link MotionControllerHandler} instance, used to bind the
	 * {@link Device} to the Graphical User Interface.
	 * 
	 * @param handler {@link MotionControllerHandler} instance
	 */
	public void setMotionControllerHandler(MotionControllerHandler handler);

	/**
	 * Returns the informational name of the {@link Device}.
	 * 
	 * @return name of the {@link Device}
	 */
	public String getName();
}
