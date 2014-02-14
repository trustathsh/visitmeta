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

import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.log4j.Logger;

public class ConnectionTab extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ConnectionTab.class);

	private static ImageIcon[] connectionStatusIcon;

	private String mName;
	private boolean mConnected;
	private GraphConnection mConnection = null;

	private PanelTimeLine mTimeLine = null;
	private JSplitPane mSplitPane = null;
	private JPanel mUpperPanel = null;
	private JPanel mLowerPanel = null;

	/**
	 * Initializes a Connection Tab. Sets the Name and arranges the Panel.
	 * 
	 * @param name
	 *            Name of the Connection
	 * @param connection
	 *            Panel Object that represents the Connection
	 */
	public ConnectionTab(String name, GraphConnection connection) {
		super();
		LOGGER.trace("Init ConnectionTab for the Connection " + name);
		if (connectionStatusIcon == null) {
			connectionStatusIcon = new ImageIcon[2];
			connectionStatusIcon[0] = new ImageIcon(getClass().getClassLoader().getResource("ball_green_small.png"));
			connectionStatusIcon[1] = new ImageIcon(getClass().getClassLoader().getResource("ball_red_small.png"));
		}

		this.mName = name;
		this.mConnected = true;
		this.setLayout(new GridLayout());
		mConnection = connection;
		mTimeLine = new PanelTimeLine();

		initPanels();

		this.add(mSplitPane);
	}

	/**
	 * Initializes the panels
	 */
	private void initPanels() {
		mUpperPanel = new JPanel();
		mLowerPanel = new JPanel();

		mUpperPanel.setLayout(new GridLayout());
		mLowerPanel.setLayout(new GridLayout());

		mUpperPanel.add(mConnection.getGraphPanel());
		mLowerPanel.add(mTimeLine);

		mSplitPane = new JSplitPane();
		mSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mSplitPane.setResizeWeight(0.99);
		mSplitPane.setEnabled(false);
		mSplitPane.setLeftComponent(mUpperPanel);
		mSplitPane.setRightComponent(mLowerPanel);
	}

	/**
	 * Returns an ImageIcon object related with its status
	 * 
	 * @return ImageIcon object with the image related to the status
	 */
	public ImageIcon getStatusImage() {
		if (mConnected) {
			return connectionStatusIcon[0];
		}
		return connectionStatusIcon[1];
	}

	/**
	 * Method to determine the connection-status
	 * 
	 * @return boolean value whether the connection is established or not
	 */
	public boolean isConnected() {
		return this.mConnected;
	}

	public GraphConnection getConnection() {
		return this.mConnection;
	}

	/**
	 * TODO implement connect stuff
	 */
	public void connect() {
		this.setConnectionStatus(true);
	}

	/**
	 * TODO implement disconnect stuff
	 */
	public void disconnect() {
		this.setConnectionStatus(false);
	}

	public void setConnectionStatus(boolean status) {
		this.mConnected = status;
	}

	public void setConnName(String name) {
		this.mName = name;
	}

	public String getConnName() {
		return this.mName;
	}

	@Override
	public String toString() {
		return this.mName;
	}

	@Override
	public int hashCode() {
		return this.mName.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof ConnectionTab)) {
			return false;
		}
		ConnectionTab other = (ConnectionTab) o;
		return this.mName.equals(other.mName);
	}
}
