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
 * This file is part of visitmeta visualization, version 0.0.3,
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

import java.util.HashSet;

import org.apache.log4j.Logger;

public class GuiController {
	private static final Logger LOGGER = Logger.getLogger(GraphConnection.class);
	private MainWindow mMainWindow = null;
	private ConnectionTab mSelectedConnection = null;
	private HashSet<ConnectionTab> mConnections = null;

	public GuiController() {
		initMainWindow();
		initConnections();
	}

	/**
	 * Initializes the VisITMeta window
	 */
	private void initMainWindow() {
		mMainWindow = new MainWindow();
		mMainWindow.setJMenuBar(new MenuBar(this));
	}

	private void initConnections() {
		mConnections = new HashSet<ConnectionTab>();
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
	public void addConnection(String name, GraphConnection connection) {
		ConnectionTab tmp = new ConnectionTab(name, connection, mMainWindow);
		this.setSelectedConnectionTab(tmp);
		mMainWindow.addConnection(tmp);
		mConnections.add(tmp);
	}

	/**
	 * Removes a connection
	 * 
	 * @param name
	 *            of the connection that needs to be removed
	 */
	public void removeConnection(String name) {
		mConnections.remove(new ConnectionTab(name, null, null));
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
	 * Open a window with the properties of the node
	 * 
	 * @param pData
	 *            the properties of the node.
	 * @param pX
	 *            the x coordinate of the window.
	 * @param pY
	 *            the y coordinate of the window.
	 */
//	public void showPropertiesOfNode(final Propable pData, final int pX, final int pY) {
//		LOGGER.trace("Method showPropertiesOfNode(" + pData + ", " + pX + ", " + pY + ") called.");
//		mSelectedConnection.showPropertiesOfNode(pData, pX, pY);
//	}

	/**
	 * Hide the property window after a period of time.
	 */
//	public void hidePropertiesOfNode() {
//		LOGGER.trace("Method hidePropertiesOfNode() called.");
//		mSelectedConnection.hidePropertiesOfNode();
//	}

	/**
	 * Hide the property window.
	 */
//	public void hidePropertiesOfNodeNow() {
//		LOGGER.trace("Method hidePropertiesOfNodeNow() called.");
//		mSelectedConnection.hidePropertiesOfNodeNow();
//	}

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
}
