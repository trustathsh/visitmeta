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
 * This file is part of visitmeta-visualization, version 0.4.0,
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
package de.hshannover.f4.trust.visitmeta.input.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import de.hshannover.f4.trust.visitmeta.input.device.Device;

/**
 * A {@link JPanel} that displays information about the current status
 * of a motion controller {@link Device}.
 * This information may help the user to understand why and how the software
 * interprets the actions of the user with a specific device.
 * 
 * @author Bastian Hellmann
 * @author Oleg Wetzler
 *
 */
public class MotionInformationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int rp = 10;
	private int rm = 40;
	private int x;
	private int y;
	private double gap = 1 / 5.0;
	private double size = 0.5;
	private double z;
	private boolean joystick = true;

	/**
	 * Sets the cursor position.
	 * The x and y coordinates are multiplied with radius/2.
	 * 
	 * @param x x coordinate of the cursor
	 * @param y y coordinate of the cursor
	 * @param z z coordinate of the cursor
	 */
	public void setCursorPosition(double x, double y, double z) {
		this.x = (int) (x * rm / 2);
		this.y = (int) (y * rm / 2);
		this.z = z;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		int width = getWidth();
		int high = getHeight();

		if (joystick) {
			g.setColor(Color.GRAY);
			g.drawOval(width / 2 - rm / 2, high / 2 - rm / 2, rm, rm);
			g.drawLine((int) (width / 2 + rm / 2 + gap * rm),
					high / 2 + rm / 2, (int) (width / 2 + rm / 2 + gap + rm
							* size), high / 2 + rm / 2);
			g.drawLine((int) (width / 2 + rm / 2 + gap * rm),
					high / 2 - rm / 2, (int) (width / 2 + rm / 2 + gap + rm
							* size), high / 2 - rm / 2);
		}

		g.setColor(Color.RED);
		g.fillOval(width / 2 - rp / 2 + x, high / 2 - rp / 2 + y, rp, rp);
		g.drawLine((int) (width / 2 + rm / 2 + gap * rm), (int) (high / 2 - z
				* rm / 2), (int) (width / 2 + rm / 2 + gap + rm * size),
				(int) (high / 2 - z * rm / 2));
	}

}
