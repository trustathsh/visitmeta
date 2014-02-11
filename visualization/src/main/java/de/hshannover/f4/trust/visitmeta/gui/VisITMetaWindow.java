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
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

public class VisITMetaWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(VisITMetaWindow.class);

	private JMenuBar mMenuBar = null;
	private JSplitPane mMainSplitPane = null;
	private JPanel mLeftMainPanel = null;
	private JPanel mRightMainPanel = null;
	private JList<Object> mConnectionList = null;
	private JTabbedPane mTabbedConnectionPane = null;

	public VisITMetaWindow(GuiController pController, JComponent pGraphPanel) {
		super("VisITmeta");
		this.setLookAndFeel();
		this.setMinimumSize(new Dimension(800, 600));

		mMenuBar = new MenuBar(pController);
		this.setJMenuBar(mMenuBar);
		
		mConnectionList = new JList<Object>();
		Vector<ConnectionListItem> listItems = new Vector<ConnectionListItem>();
		listItems.add(new ConnectionListItem("TestConn1", pGraphPanel));
		listItems.add(new ConnectionListItem("TestConn2", pGraphPanel));
		mConnectionList.setListData(listItems);
		mConnectionList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent event) {
				ConnectionListItem conn = (ConnectionListItem) mConnectionList.getSelectedValue();
				ConnectionTab tab = new ConnectionTab(conn.toString(), new JPanel());
				boolean alreadyOpen = false;
				for(Component t : mTabbedConnectionPane.getComponents()) {
					if(t.equals(tab)) {
						alreadyOpen = true;
					}
				}
				if(!alreadyOpen) {
					mTabbedConnectionPane.add(conn.toString(), conn.getConnectionPanel());
				}
			}
		});
		
		mLeftMainPanel = new JPanel();
		mLeftMainPanel.setLayout(new GridLayout());
		mLeftMainPanel.add(mConnectionList);
		
		mTabbedConnectionPane = new JTabbedPane();

		mRightMainPanel = new JPanel();
		mRightMainPanel.setLayout(new GridLayout());
		mRightMainPanel.add(mTabbedConnectionPane);

		mMainSplitPane = new JSplitPane();
		mMainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		mMainSplitPane.setRightComponent(mRightMainPanel);
		mMainSplitPane.setResizeWeight(0.2);
		mMainSplitPane.setLeftComponent(mLeftMainPanel);

		this.getContentPane().add(mMainSplitPane);

//		/* Components */
//		JSplitPane vSplitPane = new JSplitPane();
//		MenuBar vMenuBar = new MenuBar(pController);
//		JPanel vRightComponent = new JPanel();
//		SpringLayout vSpringLayout = new SpringLayout();
//		PanelTimeLine vTimeLine = new PanelTimeLine();
//		Log4jAppender vLog4jAppender = new Log4jAppender();
//		JComponent vLogPane = vLog4jAppender.getComponent();
//		Logger.getRootLogger().addAppender(vLog4jAppender);
//		/* vSpringLayout */
//		vSpringLayout.putConstraint(SpringLayout.NORTH, vTimeLine, 0, SpringLayout.NORTH, vRightComponent);
//		vSpringLayout.putConstraint(SpringLayout.EAST, vTimeLine, 0, SpringLayout.EAST, vRightComponent);
//		vSpringLayout.putConstraint(SpringLayout.WEST, vTimeLine, 0, SpringLayout.WEST, vRightComponent);
//		vSpringLayout.putConstraint(SpringLayout.NORTH, vLogPane, 0, SpringLayout.SOUTH, vTimeLine);
//		vSpringLayout.putConstraint(SpringLayout.EAST, vLogPane, 0, SpringLayout.EAST, vRightComponent);
//		vSpringLayout.putConstraint(SpringLayout.WEST, vLogPane, 0, SpringLayout.WEST, vRightComponent);
//		vSpringLayout.putConstraint(SpringLayout.SOUTH, vLogPane, 0, SpringLayout.SOUTH, vRightComponent);
//		/* vRightComponent */
//		vRightComponent.setLayout(vSpringLayout);
//		vRightComponent.add(vTimeLine);
//		vRightComponent.add(vLogPane);
//		/* vSplitPane */
//		mSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
//		mSplitPane.setLeftComponent(pGraphPanel);
//		mSplitPane.setRightComponent(vRightComponent);
//		/* mMainWindow */
//		setJMenuBar(vMenuBar);
//		getContentPane().add(mSplitPane);
//		/* Load Properties */
//		setTitle(PropertiesManager.getProperty("window", "title", "VisITMeta"));
//		setLocation(Integer.parseInt(PropertiesManager.getProperty("window", "position.x", "0")), // x
//				Integer.parseInt(PropertiesManager.getProperty("window", "position.y", "0")) // y
//		);
//		setPreferredSize(new Dimension(Integer.parseInt(PropertiesManager.getProperty("window", "width", "700")), // width
//				Integer.parseInt(PropertiesManager.getProperty("window", "height", "700")) // height
//		));
//		mSplitPane.setResizeWeight(Double.parseDouble(PropertiesManager.getProperty("window", "splitpercent", "0.8")));
//		/* Close Listener */
//		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//		addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent we) {
//				PropertiesManager.storeProperty("window", "position.x", String.valueOf((int) getLocationOnScreen().getX())); // x
//				PropertiesManager.storeProperty("window", "position.y", String.valueOf((int) getLocationOnScreen().getY())); // y
//				PropertiesManager.storeProperty("window", "width", String.valueOf(getWidth())); // width
//				PropertiesManager.storeProperty("window", "height", String.valueOf(getHeight())); // height
//				// PropertiesManager.storeProperty("window", "splitpercent",
//				// String.valueOf(mSplitPane.getResizeWeight()));
//				System.exit(0);
//			}
//		});
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