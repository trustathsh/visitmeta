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
package de.hshannover.f4.trust.visitmeta.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.datawrapper.GraphContainer;
import de.hshannover.f4.trust.visitmeta.gui.util.ConnectionTreeCellRenderer;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.input.gui.MotionControllerHandler;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(MainWindow.class);

	private static final Properties mConfig = Main.getConfig();

	private static final String VISITMETA_ICON_16PX = "visitmeta-icon-16px.png";
	private static final String VISITMETA_ICON_32PX = "visitmeta-icon-32px.png";
	private static final String VISITMETA_ICON_64PX = "visitmeta-icon-64px.png";
	private static final String VISITMETA_ICON_128PX = "visitmeta-icon-128px.png";

	private JSplitPane mMainSplitPane = null;
	private JPanel mLeftMainPanel = null;
	private JPanel mRightMainPanel = null;
	private JTabbedPane mTabbedConnectionPane = null;
	private JScrollPane mConnectionScrollPane = null;
	private JTree mConnectionTree = null;
	private DefaultMutableTreeNode mTreeRoot = null;
	private ConnectionTreeCellRenderer mTreeRenderer = null;
	private static List<SupportedLaF> supportedLaFs = new ArrayList<SupportedLaF>();
	private ImageIcon[] mCancelIcon = null;
	private MotionControllerHandler mMotionControllerHandler = null;

	/**
	 *
	 * @param guiController
	 */
	public MainWindow(MotionControllerHandler motionControllerHandler) {
		super("VisITMeta GUI v" + Main.VISUALIZATION_VERSION);

		mMotionControllerHandler = motionControllerHandler;

		init();
	}

	/**
	 *
	 * @param connection
	 */
	public void addDataserviceConnection(DataserviceConnection dataservice) {
		if (!existsDataservice(dataservice)) {
			addDataserviceTreeNode(dataservice);
			updateRestConnections();
			mConnectionTree.expandPath(new TreePath(mTreeRoot.getPath()));
		}
	}

	public void updateRestConnections() {
		for (int i = 0; i < mTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode tmpNode = (DefaultMutableTreeNode) mTreeRoot
					.getChildAt(i);
			DataserviceConnection treeNodedataservice = (DataserviceConnection) tmpNode
					.getUserObject();
			List<String> restConnections;
			try {
				restConnections = treeNodedataservice.loadRestConnectionNames();

				tmpNode.removeAllChildren();
				for (String restConnectionName : restConnections) {
					GraphContainer graphContainer = new GraphContainer(
							restConnectionName, treeNodedataservice);
					tmpNode.add(new DefaultMutableTreeNode(new ConnectionTab(
							graphContainer, this)));
				}
			} catch (ClientHandlerException | UniformInterfaceException
					| JSONException e) {
				LOGGER.warn("Exception while load rest connection names, from "
						+ treeNodedataservice.getName() + " -> "
						+ e.getMessage());
			}
		}
		mConnectionTree.updateUI();
	}

	private boolean existsDataservice(DataserviceConnection dataservice) {
		String dataserviceName = dataservice.getName();
		for (int i = 0; i < mTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode tmpNode = (DefaultMutableTreeNode) mTreeRoot
					.getChildAt(i);
			DataserviceConnection tmpDataserviceConnection = (DataserviceConnection) tmpNode
					.getUserObject();
			if (tmpDataserviceConnection.getName().equals(dataserviceName)) {
				return true;
			}
		}
		return false;
	}

	private void addDataserviceTreeNode(DataserviceConnection dataservice) {
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(dataservice);
		mTreeRoot.add(newNode);
		mConnectionTree.updateUI();
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

		Image visitMetaIcon16px = new ImageIcon(MainWindow.class
				.getClassLoader().getResource(VISITMETA_ICON_16PX).getPath())
				.getImage();
		Image visitMetaIcon32px = new ImageIcon(MainWindow.class
				.getClassLoader().getResource(VISITMETA_ICON_32PX).getPath())
				.getImage();
		Image visitMetaIcon64px = new ImageIcon(MainWindow.class
				.getClassLoader().getResource(VISITMETA_ICON_64PX).getPath())
				.getImage();
		Image visitMetaIcon128px = new ImageIcon(MainWindow.class
				.getClassLoader().getResource(VISITMETA_ICON_128PX).getPath())
				.getImage();

		List<? extends Image> visitMetaIcons = Arrays.asList(visitMetaIcon16px,
				visitMetaIcon32px, visitMetaIcon64px, visitMetaIcon128px);
		this.setIconImages(visitMetaIcons);

		initLeftHandSide();
		initRightHandSide();

		mMainSplitPane = new JSplitPane();
		mMainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		mMainSplitPane.setResizeWeight(0.125);
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
				mConnectionTree.setSelectionRow(mConnectionTree
						.getClosestRowForLocation(e.getX(), e.getY()));
				if (e.getButton() == MouseEvent.BUTTON3) {
					Object tmp = ((DefaultMutableTreeNode) mConnectionTree
							.getClosestPathForLocation(e.getX(), e.getY())
							.getLastPathComponent()).getUserObject();
					if (tmp instanceof ConnectionTab) {
						new ConnectionTabListMenu((ConnectionTab) tmp).show(
								mConnectionTree, e.getX(), e.getY());
					}
				} else if (e.getButton() == MouseEvent.BUTTON1
						&& e.getClickCount() > 1) {
					Object tmp = ((DefaultMutableTreeNode) mConnectionTree
							.getLastSelectedPathComponent()).getUserObject();
					if (tmp instanceof ConnectionTab) {
						ConnectionTab tmpTab = (ConnectionTab) tmp;
						boolean alreadyOpen = false;
						for (Component t : mTabbedConnectionPane
								.getComponents()) {
							if (t.equals(tmpTab)) {
								alreadyOpen = true;
							}
						}
						if (!alreadyOpen) {
							addClosableTab(tmpTab);
						} else {
							mTabbedConnectionPane.setSelectedComponent(tmpTab);
						}
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

		mConnectionTree.setCellRenderer(mTreeRenderer);
		mConnectionScrollPane = new JScrollPane(mConnectionTree);

		mLeftMainPanel = new JPanel();
		mLeftMainPanel.setLayout(new GridLayout());
		mLeftMainPanel.add(mConnectionScrollPane);
	}

	/**
	 * Adds a component to a JTabbedPane with a little "close tab" button on the
	 * right side of the tab.
	 *
	 * @param cTab
	 *            the ConnectionTab that should be added
	 */
	private void addClosableTab(final ConnectionTab cTab) {
		if (mCancelIcon == null) {
			mCancelIcon = new ImageIcon[2];
			mCancelIcon[0] = new ImageIcon(MainWindow.class.getClassLoader()
					.getResource("close.png").getPath());
			mCancelIcon[1] = new ImageIcon(MainWindow.class.getClassLoader()
					.getResource("closeHover.png").getPath());
		}
		mTabbedConnectionPane.addTab(cTab.getConnName(), cTab);
		int pos = mTabbedConnectionPane.indexOfComponent(cTab);

		FlowLayout f = new FlowLayout(FlowLayout.CENTER, 5, 0);

		JPanel pnlTab = new JPanel(f);
		pnlTab.setOpaque(false);

		JButton btnClose = new JButton();
		btnClose.setOpaque(false);

		btnClose.setRolloverIcon(mCancelIcon[1]);
		btnClose.setRolloverEnabled(true);
		btnClose.setIcon(mCancelIcon[0]);

		btnClose.setBorder(null);
		btnClose.setFocusable(false);

		pnlTab.add(new JLabel(cTab.getConnName()));
		pnlTab.add(btnClose);

		mTabbedConnectionPane.setTabComponentAt(pos, pnlTab);

		/**
		 * Remove the current tab from the tab pane and from the
		 * MotionController if it is closed in the GUI.
		 */
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mTabbedConnectionPane.remove(cTab);
				mMotionControllerHandler.removeConnectionTab(cTab);
			}
		};
		btnClose.addActionListener(listener);

		mTabbedConnectionPane.setSelectedComponent(cTab);
	}

	/**
	 * Initializes the right hand side of the main panel
	 */
	private void initRightHandSide() {
		mTabbedConnectionPane = new JTabbedPane();

		/**
		 * Whenever the current tab inside the GUI changes, the
		 * MotionControllerHandler instance is informed.
		 */
		mTabbedConnectionPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
				ConnectionTab currentTab = (ConnectionTab) sourceTabbedPane
						.getSelectedComponent();
				if (currentTab != null) {
					mMotionControllerHandler
							.setCurrentConnectionTab(currentTab);
					;
					LOGGER.debug("Tab changed to " + currentTab.getConnName());
				}
			}
		});

		mRightMainPanel = new JPanel();
		mRightMainPanel.setLayout(new GridLayout());
		mRightMainPanel.add(mTabbedConnectionPane);
	}

	/**
	 * Loads Properties
	 */
	private void loadProperties() {
		setLocation(mConfig.getInt("window.position.x", 0),
				mConfig.getInt("window.position.y", 0));
		setPreferredSize(new Dimension(mConfig.getInt("window.width", 1280),
				mConfig.getInt("window.height", 720)));
		mMainSplitPane.setDividerLocation(mConfig.getInt("window.resizeweight",
				-1));
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
				try {
					mConfig.set("window.position.x",
							(int) getLocationOnScreen().getX());
					mConfig.set("window.position.y",
							(int) getLocationOnScreen().getY());
					mConfig.set("window.width", getWidth());
					mConfig.set("window.height", getHeight());
					mConfig.set("window.divider",
							mMainSplitPane.getDividerLocation());
				} catch (PropertyException e) {
					LOGGER.fatal(e.toString(), e);
					throw new RuntimeException("could not save properties");
				}
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

		UIManager.LookAndFeelInfo[] installedLafs = UIManager
				.getInstalledLookAndFeels();
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

	public JTree getConnectionTree() {
		return mConnectionTree;
	}
}
