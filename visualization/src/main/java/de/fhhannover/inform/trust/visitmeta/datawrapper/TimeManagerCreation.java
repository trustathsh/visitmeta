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
import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.gui.GuiController;
/* Class **********************************************************************/
public class TimeManagerCreation extends TimeManager {
/* Attributes *****************************************************************/
	private static final Logger LOGGER = Logger.getLogger(TimeManagerCreation.class);
	/** Singleton */
	private static TimeManagerCreation mInstance = null;

	private GuiController mController = null;
/* Constructors ***************************************************************/
	private TimeManagerCreation() {
		super();
	}
/* Methods ********************************************************************/
	/**
	 * Singleton Thread-Safe
	 * @return the instance of TimeManagerCreation.
	 */
	public static TimeManagerCreation getInstance() {
		LOGGER.trace("Method getInstance() called.");
		if(mInstance == null) { // DoubleCheck
			synchronized (TimeManagerCreation.class) {
				if (mInstance == null) {
					mInstance = new TimeManagerCreation();
				}
			}
		}
		return mInstance;
	}
	public void setController(GuiController pController) {
		mController = pController;
	}
	@Override
	protected void processNode(Position pNode) {
		LOGGER.trace("Method processNode(" + pNode + ") called.");
		if(mController != null) {
			mController.removeHighlight(pNode);
		}
	}
}
