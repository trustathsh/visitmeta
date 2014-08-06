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
 * This file is part of visitmeta visualization, version 0.1.1,
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

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

/**
 * A {@link JLayeredPane} that encapsulates the rendering panel and a
 * {@link MotionInformationPanel} in such a way, that the latter is rendered
 * "above" the rendering panel.
 * 
 * This allows to display information about the motion control device on top
 * of the actual graph representation.
 * 
 * @author Bastian Hellmann
 * @author Oleg Wetzler
 *
 */
public class MotionInformationPane extends JLayeredPane {

	private static final long serialVersionUID = 1L;
	private JComponent component;
	private MotionInformationPanel motionInformationPanel;

	/**
	 * Creates a new {@link MotionInformationPane} that renders
	 * 
	 * @param component the {@link JComponent}, that will be rendered
	 * "at the bottom", with a {@link MotionInformationPanel} "on top".
	 */
	public MotionInformationPane(JComponent component) {
		this.component = component;
		this.motionInformationPanel = new MotionInformationPanel();

		this.add(motionInformationPanel, 101);
		this.add(component, 100);
		motionInformationPanel.setVisible(false);
		repaint();
	}

	/**
	 * Sets the cursor position, un-hide the {@link MotionInformationPanel} and
	 * repaints the {@link JLayeredPane}.
	 * 
	 * @param x x coordinate of the cursor
	 * @param y y coordinate of the cursor
	 * @param z z coordinate of the cursor
	 */
	public void setCursorPosition(double x, double y, double z) {
		motionInformationPanel.setCursorPosition(x, y, z);
		motionInformationPanel.setVisible(true);
		repaint();
	}

	/**
	 * Hides the {@link MotionInformationPanel}.
	 */
	public void hideMotionInformationPanel() {
		motionInformationPanel.setVisible(false);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#repaint()
	 */
	@Override
	public void repaint() {
		super.repaint();
		motionInformationPanel.setBounds(0, 0, getWidth(), getHeight());
		component.setBounds(0, 0, getWidth(), getHeight());
	}
}
