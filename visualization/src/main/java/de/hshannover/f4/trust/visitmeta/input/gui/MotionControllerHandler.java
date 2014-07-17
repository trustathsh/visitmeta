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
 * Copyright (C) 2012 - 2014 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.input.gui;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.hshannover.f4.trust.visitmeta.gui.ConnectionTab;
import de.hshannover.f4.trust.visitmeta.input.device.Device;

/**
 * Component that handles and encapsulates the connection between input devices
 * and the GUI.
 * GUI components only report to this class,
 * this class creates and manages {@link MotionController} instances for that
 * specific GUI component and
 * motion control devices can retrieve the current active
 * {@link MotionController} instance from this class.
 * 
 * @author Bastian Hellmann
 *
 */
public class MotionControllerHandler {

	private MotionController mCurrentMotionController = null;
	private Map<ConnectionTab, MotionController> mMotionControllerMap;

	public MotionControllerHandler() {
		mMotionControllerMap = new ConcurrentHashMap<>();
	}

	/**
	 * Method for GUI components to report the current {@link ConnectionTab}
	 * instance that shall be target for incoming motion information by
	 * {@link Device}s.
	 * 
	 * @param tab the {@link ConnectionTab} instance, reported by the GUI
	 */
	public synchronized void setCurrentConnectionTab(ConnectionTab tab) {
		MotionController motionController = null;

		if (mMotionControllerMap.containsKey(tab)) {
			motionController = mMotionControllerMap.get(tab);
		} else {
			motionController = MotionControllerFactory.create(tab);
			mMotionControllerMap.put(tab, motionController);
		}

		mCurrentMotionController = motionController;
	}

	/**
	 * Returns the current active {@link MotionController} instance, so that
	 * arbitrary input devices can send their motion information to this
	 * controller.
	 * 
	 * @return current active {@link MotionController} instance;
	 * returns <code>null</code> if there is currently not active
	 * {@link MotionController}
	 */
	public synchronized MotionController getCurrentMotionController() {
		return mCurrentMotionController;
	}

	/**
	 * Removes the given {@link ConnectionTab} from the internal list, if
	 * existing.
	 * 
	 * @param tab {@link ConnectionTab}
	 */
	public synchronized void removeConnectionTab(ConnectionTab tab) {
		MotionController controller = mMotionControllerMap.get(tab);
		if (controller != null) {
			mMotionControllerMap.remove(tab);

			if (mCurrentMotionController == controller) {
				mCurrentMotionController = null;
			}
		}
	}

}
