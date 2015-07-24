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
 * This file is part of visitmeta-visualization, version 0.4.2,
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
package de.hshannover.f4.trust.visitmeta.gui;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.graphCalculator.LayoutType;
import de.hshannover.f4.trust.visitmeta.gui.util.MapServerRestConnectionImpl;
import de.hshannover.f4.trust.visitmeta.input.gui.MotionControllerHandler;

public class GuiController {

	private static final Logger LOGGER = Logger.getLogger(GraphConnection.class);

	private MainWindow mMainWindow = null;

	private ConnectionTab mSelectedConnection = null;

	public GuiController(MotionControllerHandler motionController) {
		initMainWindow(motionController);
	}

	/**
	 * Initializes the VisITMeta window
	 *
	 * @param motionController
	 */
	private void initMainWindow(MotionControllerHandler motionController) {
		mMainWindow = new MainWindow(motionController);
		mMainWindow.setJMenuBar(new MenuBar(this));

		mMainWindow.getConnectionTree().addTreeSelectionListener(
				new TreeSelectionListener() {
					@Override
					public void valueChanged(TreeSelectionEvent e) {
						Object selectedComponent = e.getPath().getLastPathComponent();
						if (selectedComponent instanceof MapServerRestConnectionImpl) {
							MapServerRestConnectionImpl connection = (MapServerRestConnectionImpl) selectedComponent;
							setSelectedConnectionTab(connection.getConnectionTab());
						}
					}
				});
	}

	public GraphConnection getSelectedConnection() {
		return mSelectedConnection.getConnection();
	}

	public ConnectionTab getSelectedConnectionTab() {
		return mSelectedConnection;
	}

	public void setSelectedConnectionTab(ConnectionTab connection) {
		this.mSelectedConnection = connection;
	}

	/**
	 * Pack and set visible.
	 */
	public void show() {
		LOGGER.trace("Method show() called.");
		mMainWindow.pack();
		mMainWindow.setVisible(true);
		mMainWindow.openConnectedMapServerConnections();
	}

	/**
	 * Set the color settings window visible.
	 */
	public void showColorSettings() {
		LOGGER.trace("Method showColorSettings() called.");
		if (checkForSelectedConnection(
				"A connection must be selected to edit the color settings.", "Color settings")) {
			mSelectedConnection.showColorSettings(mMainWindow);
		}
	}

	/**
	 * Set the settings window visible.
	 */
	public void showSettings() {
		LOGGER.trace("Method showSettings() called.");
		if (checkForSelectedConnection(
				"A connection must be selected to edit the window settings.", "Window settings")) {
			mSelectedConnection.showSettings(mMainWindow);
		}
	}

	/**
	 * Set layout type (e.g., force-directed)
	 *
	 * @param layoutType
	 */
	public void setLayoutType(LayoutType layoutType) {
		if (checkForSelectedConnection(
				"A connection must be selected to change the layout algorithm.", "Layout settings")) {
			mSelectedConnection.getConnection().setLayoutType(layoutType);
		}
	}

	public void redrawGraph() {
		if (checkForSelectedConnection(
				"A connection must be selected to redraw the graph.", "Redraw graph")) {
			mSelectedConnection.getConnection().redrawGraph();
		}
	}

	public String switchGraphMotion() {
		if (checkForSelectedConnection(
				"A connection must be selected to change the animation of the graph.", "Change graph animation")) {
			if (mSelectedConnection.getConnection().isGraphMotion()) {
				mSelectedConnection.getConnection().stopGraphMotion();
				return "Start";
			} else {
				mSelectedConnection.getConnection().startGraphMotion();
				return "Stop";
			}
		} else {
			return null;
		}
	}

	public MainWindow getMainWindow() {
		return mMainWindow;
	}

	private boolean checkForSelectedConnection(String message, String title) {
		if (mSelectedConnection != null) {
			return true;
		} else {
			JOptionPane.showMessageDialog(mMainWindow, message, title, JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}
}
