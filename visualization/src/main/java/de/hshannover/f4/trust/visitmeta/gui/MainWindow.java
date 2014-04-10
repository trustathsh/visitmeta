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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;
import de.hshannover.f4.trust.visitmeta.gui.util.ConnectionTreeCellRenderer;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(MainWindow.class);

	private JSplitPane mMainSplitPane = null;
	private JPanel mLeftMainPanel = null;
	private JPanel mRightMainPanel = null;
	private JTabbedPane mTabbedConnectionPane = null;
	private JScrollPane mConnectionScrollPane = null;
	private JTree mConnectionTree = null;
	private DefaultMutableTreeNode mTreeRoot = null;
	private ConnectionTreeCellRenderer mTreeRenderer = null;
	private static List<SupportedLaF> supportedLaFs = new ArrayList<SupportedLaF>();

	/**
	 * 
	 * @param guiController
	 */
	public MainWindow() {
		super("VisITmeta");
		init();
	}

	/**
	 * 
	 * @param connection
	 */
	public void addConnection(ConnectionTab connection) {
		boolean alreadyOpen = false;
		for (Component t : mTabbedConnectionPane.getComponents()) {
			if (t.equals(connection)) {
				alreadyOpen = true;
			}
		}
		if (!alreadyOpen) {
			addConnectionTabTreeNode(connection);
		}
	}

	private void addConnectionTabTreeNode(ConnectionTab connection){
		String dataserviceName = connection.getRestConnection().getDataserviceConnection().getName();

		for(int i=0; i<mTreeRoot.getChildCount(); i++){
			DefaultMutableTreeNode tmpNode = (DefaultMutableTreeNode) mTreeRoot.getChildAt(i);
			DataserviceConnection tmpDataserviceConnection = (DataserviceConnection) tmpNode.getUserObject();
			if(tmpDataserviceConnection.getName().equals(dataserviceName)){
				tmpNode.add(new DefaultMutableTreeNode(connection));
				mConnectionTree.updateUI();
				return;
			}
		}
		// no DataserviceConnection found
		addDataserviceTreeNode(connection);
	}

	private void addDataserviceTreeNode(ConnectionTab connection){
		DataserviceConnection dc = connection.getRestConnection().getDataserviceConnection();
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dc);
		mTreeRoot.add(newNode);
		newNode.add(new DefaultMutableTreeNode(connection));
		mConnectionTree.updateUI();
	}

	/**
	 * 
	 * @param name
	 * @param graphPanel
	 */
	public void addConnection(GraphContainer connection) {
		this.addConnection(new ConnectionTab(connection, this));
	}

	/**
	 * Removes a connection
	 * 
	 * @param connection
	 *            the should be removed
	 */
	public void removeConnection(ConnectionTab connection) {
		// TODO implement
	}

	/**
	 * Initializes the main panel
	 */
	private void init() {
		this.setLookAndFeel();
		this.setMinimumSize(new Dimension(800, 600));

		initLeftHandSide();
		initRightHandSide();

		mMainSplitPane = new JSplitPane();
		mMainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		mMainSplitPane.setRightComponent(mRightMainPanel);
		mMainSplitPane.setLeftComponent(mLeftMainPanel);

		this.getContentPane().add(mMainSplitPane);

		loadProperties();
		setCloseOperation();
	}

	/**
	 * Initializes the left hand side of the main panel
	 */
	private void initLeftHandSide() {
		mTreeRenderer = new ConnectionTreeCellRenderer();
		mTreeRoot = new DefaultMutableTreeNode("Dataservices");

		mConnectionTree = new JTree(mTreeRoot);
		mConnectionTree.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				mConnectionTree.setSelectionRow(mConnectionTree.getClosestRowForLocation(e.getX(), e.getY()));
				if (e.getButton() == MouseEvent.BUTTON3) {
					Object tmp = ((DefaultMutableTreeNode) mConnectionTree
							.getClosestPathForLocation(e.getX(), e.getY()).getLastPathComponent()).getUserObject();
					if (tmp instanceof ConnectionTab) {
						new ConnectionTabListMenu((ConnectionTab) tmp).show(mConnectionTree, e.getX(), e.getY());
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		mConnectionTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				Object tmp = ((DefaultMutableTreeNode) mConnectionTree.getLastSelectedPathComponent()).getUserObject();
				if (tmp instanceof ConnectionTab) {
					ConnectionTab tmpTab = (ConnectionTab) tmp;
					boolean alreadyOpen = false;
					for (Component t : mTabbedConnectionPane.getComponents()) {
						if (t.equals(tmpTab)) {
							alreadyOpen = true;
						}
					}
					if (!alreadyOpen) {
						mTabbedConnectionPane.add(tmpTab.getConnName(), tmpTab);
					} else {
						mTabbedConnectionPane.setSelectedComponent(tmpTab);
					}
				}

			}
		});
		mConnectionTree.setCellRenderer(mTreeRenderer);
		mConnectionScrollPane = new JScrollPane(mConnectionTree);

		mLeftMainPanel = new JPanel();
		mLeftMainPanel.setLayout(new GridLayout());
		mLeftMainPanel.add(mConnectionScrollPane);
	}

	/**
	 * Initializes the right hand side of the main panel
	 */
	private void initRightHandSide() {
		mTabbedConnectionPane = new JTabbedPane();

		mRightMainPanel = new JPanel();
		mRightMainPanel.setLayout(new GridLayout());
		mRightMainPanel.add(mTabbedConnectionPane);
	}

	/**
	 * Loads Properties
	 */
	private void loadProperties() {
		setTitle(PropertiesManager.getProperty("window", "title", "VisITMeta"));
		setLocation(Integer.parseInt(PropertiesManager.getProperty("window", "position.x", "0")),
				Integer.parseInt(PropertiesManager.getProperty("window", "position.y", "0")));
		setPreferredSize(new Dimension(Integer.parseInt(PropertiesManager.getProperty("window", "width", "700")),
				Integer.parseInt(PropertiesManager.getProperty("window", "height", "700"))));
	}

	/**
	 * Adds a listener to the window in order to save the windows' position and
	 * size and sets the close operation
	 */
	private void setCloseOperation() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				PropertiesManager.storeProperty("window", "position.x",
						String.valueOf((int) getLocationOnScreen().getX()));
				PropertiesManager.storeProperty("window", "position.y",
						String.valueOf((int) getLocationOnScreen().getY()));
				PropertiesManager.storeProperty("window", "width", String.valueOf(getWidth()));
				PropertiesManager.storeProperty("window", "height", String.valueOf(getHeight()));
				System.exit(0);
			}
		});
	}

	static class SupportedLaF {
		JCheckBoxMenuItem menuItem;
		String name;
		LookAndFeel laf;

		SupportedLaF(String name, LookAndFeel laf) {
			this.name = name;
			this.laf = laf;
			this.menuItem = new JCheckBoxMenuItem(name);
		}

		@Override
		public String toString() {
			return name;
		}
	}

	/**
	 * Set the look and feel of java depending on the operating system.
	 */
	private void setLookAndFeel() {
		LOGGER.trace("Method setLookAndFeel() called.");

		UIManager.LookAndFeelInfo[] installedLafs = UIManager.getInstalledLookAndFeels();
		for (UIManager.LookAndFeelInfo lafInfo : installedLafs) {
			try {
				@SuppressWarnings("rawtypes")
				Class lnfClass = Class.forName(lafInfo.getClassName());
				LookAndFeel laf = (LookAndFeel) (lnfClass.newInstance());
				if (laf.isSupportedLookAndFeel()) {
					String name = lafInfo.getName();
					supportedLaFs.add(new SupportedLaF(name, laf));

				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				continue;
			}
		}

		for (SupportedLaF lAf : supportedLaFs) {
			if (lAf.name.equals("Windows")) {
				LookAndFeel laf = lAf.laf;
				try {
					UIManager.setLookAndFeel(laf);
					lAf.menuItem.setSelected(true);
				} catch (UnsupportedLookAndFeelException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	public List<SupportedLaF> getSupportedLaFs() {
		return supportedLaFs;
	}
}
