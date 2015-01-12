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
 * This file is part of visitmeta-visualization, version 0.3.0,
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

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.gui.ConnectionTab;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;

/**
 * An implementation of the {@link MotionController} interface for Piccolo2D.
 * Translates incoming motion input to operations on the {@link PCamera} of
 * Piccolo2D.
 *
 * @author Bastian Hellmann
 * @author Oleg Wetzler
 *
 */
public class MotionControllerPiccolo2D implements MotionController {

	private PCamera vCamera;
	private double zfactor;
	private double xyfactor;
	private MotionInformationPane motionInformationPane;

	private static final Logger logger = Logger
			.getLogger(MotionControllerPiccolo2D.class);

	private static final Properties mConfig = Main.getConfig();

	public MotionControllerPiccolo2D(ConnectionTab tab) {
		this.motionInformationPane = tab.getMotionInformationPane();

		this.vCamera = ((PCanvas) tab.getGraphPanel().getPanel()).getCamera();
		this.zfactor = mConfig.getDouble("guicontroller.zfactor", 0.0);
		this.xyfactor = mConfig.getDouble("guicontroller.xyfactor", 0.0);
	}

	@Override
	public void moveCamera(double x, double y) {
		vCamera.translateView(x * xyfactor, y * xyfactor);
		logger.trace("moveCamera -> x: " + x + ", y: ");
	}

	@Override
	public void zoom(double z) {
		vCamera.scaleViewAboutPoint(1 - z * zfactor, vCamera.getViewBounds()
				.getCenterX(), vCamera.getViewBounds().getCenterY());
		logger.trace("zoom -> z: " + z);
	}

	@Override
	public void moveCamera(double x, double y, double z) {
		moveCamera(x, y);
		zoom(z);
		logger.trace("moveCamera -> x: " + x + ", y: " + ", z: " + z);
	}

	@Override
	public void makeInvisible() {
		motionInformationPane.hideMotionInformationPanel();
	}

	@Override
	public void repaint() {
		motionInformationPane.repaint();
	}

	@Override
	public void setCameraPosition(double x, double y) {
		// not implemented
	}

	@Override
	public void setCameraPositon(double x, double y, double z) {
		// not implemented
	}

	@Override
	public void setCameraRotation(double x, double y, double z) {
		// not implemented
	}

	@Override
	public void setCursorPosition(double x, double y, double z) {
		motionInformationPane.setCursorPosition(x, y, z);
		logger.trace("setCursorPosition -> x: " + x + ", y: " + ", z: " + z);
	}
}
