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
package de.hshannover.f4.trust.visitmeta.input.gui;

import de.hshannover.f4.trust.visitmeta.graphDrawer.Piccolo2DPanel;
import de.hshannover.f4.trust.visitmeta.gui.ConnectionTab;

/**
 * Factory to create {@link MotionController} suitable for given
 * {@link ConnectionTab} and the underlying GUI implementation.
 * 
 * @author Bastian Hellmann
 *
 */
public class MotionControllerFactory {

	/**
	 * Creates a new {@link MotionController} instance for the given
	 * {@link ConnectionTab} instance.
	 * Throws an {@link IllegalArgumentException} if no {@link MotionController}
	 * implementation is available.
	 * 
	 * @param tab {@link ConnectionTab}
	 * @return {@link MotionController} instance
	 */
	public static MotionController create(ConnectionTab tab) {
		if (tab.getGraphPanel() instanceof Piccolo2DPanel) {
			return new MotionControllerPiccolo2D(tab);
		} else {
			throw new IllegalArgumentException("No GUI available for given panel instance type");
		}
	}

}
