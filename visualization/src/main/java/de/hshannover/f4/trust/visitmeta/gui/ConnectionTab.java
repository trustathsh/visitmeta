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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.Timer;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

public class ConnectionTab extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ConnectionTab.class);

	private static ImageIcon[] connectionStatusIcon;

	private String mName;
	private boolean mConnected;
	private GraphContainer mConnection = null;
	private GraphConnection mGraphConnection = null;

	private PanelTimeLine mTimeLine = null;
	private JSplitPane mSplitPane = null;
	private JPanel mUpperPanel = null;
	private JPanel mLowerPanel = null;

	private WindowColorSettings mWindowColorSettings;
	private WindowSettings mWindowSettings;
	private WindowNodeProperties mWindowNodeProperties;
	private Timer mTimerPropertiesShow;
	private Timer mTimerPropertiesHide;

	private RESTConnection mRestConnection;

	/**
	 * Initializes a Connection Tab. Sets the Name and arranges the Panel.
	 * 
	 * @param name
	 *            Name of the Connection
	 * @param connection
	 *            Panel Object that represents the Connection
	 */
	public ConnectionTab(String name, GraphContainer connection, JFrame window) {
		super();
		LOGGER.trace("Init ConnectionTab for the Connection " + name);
		if (connectionStatusIcon == null) {
			connectionStatusIcon = new ImageIcon[2];
			connectionStatusIcon[0] = new ImageIcon(getClass().getClassLoader().getResource("ball_green_small.png"));
			connectionStatusIcon[1] = new ImageIcon(getClass().getClassLoader().getResource("ball_red_small.png"));
		}

		mName = name;
		mConnected = true;
		mConnection = connection;
		mRestConnection = mConnection.getRestConnection();
		mGraphConnection = mConnection.getGraphConnection();
		mGraphConnection.setParentTab(this);
		mTimeLine = new PanelTimeLine(mConnection.getTimeSelector());
		this.setLayout(new GridLayout());

		initPanels();
		initSettingsWindows();
		initNodeSettings(window);

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

		mUpperPanel.add(mGraphConnection.getGraphPanel());
		mLowerPanel.add(mTimeLine);

		mSplitPane = new JSplitPane();
		mSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mSplitPane.setResizeWeight(0.99);
		mSplitPane.setEnabled(false);
		mSplitPane.setLeftComponent(mUpperPanel);
		mSplitPane.setRightComponent(mLowerPanel);
	}

	/**
	 * Initializes the settings windows
	 */
	private void initSettingsWindows() {
		mWindowSettings = new WindowSettings(mConnection.getSettingManager());
		mWindowSettings.setAlwaysOnTop(true);
		mWindowSettings.setVisible(false);
		mWindowColorSettings = new WindowColorSettings(this);
		mWindowColorSettings.setAlwaysOnTop(true);
		mWindowColorSettings.setVisible(false);
	}

	private void initNodeSettings(JFrame window) {
		mWindowNodeProperties = new WindowNodeProperties(window);
		mTimerPropertiesShow = new Timer(750, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				LOGGER.trace("ActionListener actionPerformed(" + pE + ") called.");
				LOGGER.debug("Stop timer and hide WindowNodeProperties.");
				mTimerPropertiesShow.stop();
				mWindowNodeProperties.setVisible(true);
			}
		});
		mTimerPropertiesHide = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				LOGGER.trace("ActionListener actionPerformed(" + pE + ") called.");
				LOGGER.debug("Stop timer and hide WindowNodeProperties.");
				mTimerPropertiesHide.stop();
				mWindowNodeProperties.setVisible(false);
			}
		});
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
		return this.mGraphConnection;
	}

	public void connect() {
		mRestConnection.connect();
		setConnectionStatus(true);
	}

	public void startDump() {
		mRestConnection.startDump();

	}

	public void stopDump() {
		mRestConnection.stopDump();

	}

	public void disconnect() {
		mRestConnection.disconnect();
		setConnectionStatus(false);
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
		return mRestConnection.toString();
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

	public void showColorSettings(JFrame window) {
		mWindowColorSettings.updateWindow();
		mWindowColorSettings.setLocationRelativeTo(window);
		mWindowColorSettings.setVisible(true);

	}

	public void showSettings(JFrame window) {
		mWindowSettings.setLocationRelativeTo(window);
		mWindowSettings.setVisible(true);
	}

	public List<String> getPublisher() {
		return mGraphConnection.getPublisher();
	}

	public void showPropertiesOfNode(final Propable pData, final int pX, final int pY) {
		mTimerPropertiesHide.stop();
		mWindowNodeProperties.fill(pData.getRawData(), mTimerPropertiesHide);
		mWindowNodeProperties.repaint();
		mWindowNodeProperties.setLocation(pX + 1, pY + 1);
		mTimerPropertiesShow.start();
	}

	public void hidePropertiesOfNode() {
		mTimerPropertiesShow.stop();
		mTimerPropertiesHide.start();
	}

	public void hidePropertiesOfNodeNow() {
		mTimerPropertiesShow.stop();
		mTimerPropertiesHide.stop();
		mWindowNodeProperties.setVisible(false);
	}
}
