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
package de.hshannover.f4.trust.visitmeta.gui;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.graphCalculator.jung.LayoutType;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
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
	 * @param motionController
	 */
	private void initMainWindow(MotionControllerHandler motionController) {
		mMainWindow = new MainWindow(motionController);
		mMainWindow.setJMenuBar(new MenuBar(this));

		mMainWindow.getConnectionTree().addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				if (node.getUserObject() instanceof ConnectionTab) {
					setSelectedConnectionTab((ConnectionTab)node.getUserObject());
				}
			}
		});
	}

	public void updateRestConnections() {
		mMainWindow.updateRestConnections();
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
	 * Adds a connection to the GuiController
	 * 
	 * @param name
	 *            of the connection
	 * @param connection
	 *            ConnectionController object
	 */
	public void addDataserviceConnection(DataserviceConnection dataservice) {
		mMainWindow.addDataserviceConnection(dataservice);
	}

	/**
	 * Pack and set visible.
	 */
	public void show() {
		LOGGER.trace("Method show() called.");
		mMainWindow.pack();
		mMainWindow.setVisible(true);
	}

	/**
	 * Set the color settings window visible.
	 */
	public void showColorSettings() {
		LOGGER.trace("Method showColorSettings() called.");
		mSelectedConnection.showColorSettings(mMainWindow);
	}

	/**
	 * Set the settings window visible.
	 */
	public void showSettings() {
		LOGGER.trace("Method showSettings() called.");
		mSelectedConnection.showSettings(mMainWindow);
	}

	/**
	 * Set layout type (e.g., force-directed)
	 * @param layoutType
	 */
	public void setLayoutType(LayoutType layoutType) {
		mSelectedConnection.getConnection().setLayoutType(layoutType);
	}

	public void redrawGraph() {
		mSelectedConnection.getConnection().redrawGraph();
	}

	public void startGraphMotion() {
		mSelectedConnection.getConnection().startGraphMotion();
	}

	public void stopGraphMotion() {
		mSelectedConnection.getConnection().stopGraphMotion();
	}

	public boolean isGraphMotion() {
		return mSelectedConnection.getConnection().isGraphMotion();
	}

	public MainWindow getMainWindow() {
		return mMainWindow;
	}
		
}
