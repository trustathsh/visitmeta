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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;

public class VisITMetaWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(VisITMetaWindow.class);

	private JMenuBar mMenuBar = null;
	private JSplitPane mMainSplitPane = null;
	private JPanel mLeftMainPanel = null;
	private JPanel mRightMainPanel = null;
	private JList<ConnectionListItem> mConnectionList = null;
	private Vector<ConnectionListItem> mListItems = null;
	private JTabbedPane mTabbedConnectionPane = null;

	public VisITMetaWindow(GuiController pController, JComponent pGraphPanel) {
		super("VisITmeta");
		this.setLookAndFeel();
		this.setMinimumSize(new Dimension(800, 600));

		mMenuBar = new MenuBar(pController);
		this.setJMenuBar(mMenuBar);

		// Initializing left hand side
		mConnectionList = new JList<ConnectionListItem>();
		mConnectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mListItems = new Vector<ConnectionListItem>();
		mListItems.add(new ConnectionListItem("TestConn1", new ConnectionTab("TestConn1", pGraphPanel)));
		mListItems.add(new ConnectionListItem("TestConn2", new ConnectionTab("TestConn2", pGraphPanel)));
		mConnectionList.setListData(mListItems);
		mConnectionList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				ConnectionListItem conn = (ConnectionListItem) mConnectionList.getSelectedValue();
				boolean alreadyOpen = false;
				for (Component t : mTabbedConnectionPane.getComponents()) {
					if (t.equals(conn.getConnectionTab())) {
						alreadyOpen = true;
					}
				}
				if (!alreadyOpen) {
					mTabbedConnectionPane.add(conn.getName(), conn.getConnectionTab());
				} else {
					mTabbedConnectionPane.setSelectedComponent(conn.getConnectionTab());
				}
			}
		});

		mLeftMainPanel = new JPanel();
		mLeftMainPanel.setLayout(new GridLayout());
		mLeftMainPanel.add(mConnectionList);

		// Initializing right hand side
		mTabbedConnectionPane = new JTabbedPane();
		mTabbedConnectionPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				ConnectionTab tab = (ConnectionTab) mTabbedConnectionPane.getComponentAt(mTabbedConnectionPane.getSelectedIndex());
				if (tab != null) {
					mConnectionList.setSelectedIndex(mListItems.indexOf(new ConnectionListItem(tab.getTabName(), tab)));
				}
			}
		});

		mRightMainPanel = new JPanel();
		mRightMainPanel.setLayout(new GridLayout());
		mRightMainPanel.add(mTabbedConnectionPane);

		// Initializing main component
		mMainSplitPane = new JSplitPane();
		mMainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		mMainSplitPane.setRightComponent(mRightMainPanel);
		mMainSplitPane.setResizeWeight(0.15);
		mMainSplitPane.setLeftComponent(mLeftMainPanel);

		this.getContentPane().add(mMainSplitPane);

		// Load Properties
		setTitle(PropertiesManager.getProperty("window", "title", "VisITMeta"));
		setLocation(Integer.parseInt(PropertiesManager.getProperty("window", "position.x", "0")),
				Integer.parseInt(PropertiesManager.getProperty("window", "position.y", "0")));
		setPreferredSize(new Dimension(Integer.parseInt(PropertiesManager.getProperty("window", "width", "700")), Integer.parseInt(PropertiesManager
				.getProperty("window", "height", "700"))));
		// Close Listener
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				PropertiesManager.storeProperty("window", "position.x", String.valueOf((int) getLocationOnScreen().getX()));
				PropertiesManager.storeProperty("window", "position.y", String.valueOf((int) getLocationOnScreen().getY()));
				PropertiesManager.storeProperty("window", "width", String.valueOf(getWidth()));
				PropertiesManager.storeProperty("window", "height", String.valueOf(getHeight()));
				System.exit(0);
			}
		});
	}

	/**
	 * Set the look and feel of java depending on the operating system.
	 */
	private void setLookAndFeel() {
		logger.trace("Method setLookAndFeel() called.");
		try {
			switch (System.getProperty("os.name").toLowerCase()) {
			case "win": // Windows
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				break;
			case "linux": // Linux
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
				break;
			// case "nix": // Unix
			// break;
			// case "mac": // Mac
			// break;
			// case "sunos": // Solaris
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			// break;
			default:
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				break;
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}