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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
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
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.gui.dialog.DataServiceParameterPanel;
import de.hshannover.f4.trust.visitmeta.gui.dialog.LayoutHelper;
import de.hshannover.f4.trust.visitmeta.gui.dialog.MapServerParameterPanel;
import de.hshannover.f4.trust.visitmeta.gui.dialog.SubscriptionParameterPanel;
import de.hshannover.f4.trust.visitmeta.gui.util.ConnectionTreeCellRenderer;
import de.hshannover.f4.trust.visitmeta.gui.util.ConnectionTreePopupMenu;
import de.hshannover.f4.trust.visitmeta.gui.util.Dataservices;
import de.hshannover.f4.trust.visitmeta.gui.util.MapServerRestConnectionImpl;
import de.hshannover.f4.trust.visitmeta.gui.util.RESTConnectionTree;
import de.hshannover.f4.trust.visitmeta.gui.util.RestSubscriptionImpl;
import de.hshannover.f4.trust.visitmeta.input.gui.MotionControllerHandler;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

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
	private RESTConnectionTree mConnectionTree = null;
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
	 * Initializes the main panel
	 */
	private void init() {
		this.setLookAndFeel();
		this.setMinimumSize(new Dimension(800, 600));

		Image visitMetaIcon16px = new ImageIcon(MainWindow.class.getClassLoader().getResource(VISITMETA_ICON_16PX)
				.getPath()).getImage();
		Image visitMetaIcon32px = new ImageIcon(MainWindow.class.getClassLoader().getResource(VISITMETA_ICON_32PX)
				.getPath()).getImage();
		Image visitMetaIcon64px = new ImageIcon(MainWindow.class.getClassLoader().getResource(VISITMETA_ICON_64PX)
				.getPath()).getImage();
		Image visitMetaIcon128px = new ImageIcon(MainWindow.class.getClassLoader().getResource(VISITMETA_ICON_128PX)
				.getPath()).getImage();

		List<? extends Image> visitMetaIcons = Arrays.asList(visitMetaIcon16px, visitMetaIcon32px, visitMetaIcon64px,
				visitMetaIcon128px);
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
		try {
			mConnectionTree = new RESTConnectionTree(Main.getDataservicePersister().loadDataserviceConnections());
		} catch (PropertyException e) {
			LOGGER.error(e.toString(), e);
		}

		mConnectionTree.expandAllNodes();
		mConnectionTree.addMouseListener(new ConnectionTreeMainWindowListener(this));


		mTreeRenderer = new ConnectionTreeCellRenderer();
		mConnectionTree.setCellRenderer(mTreeRenderer);

		mConnectionScrollPane = new JScrollPane(mConnectionTree);

		mLeftMainPanel = new JPanel();
		mLeftMainPanel.setLayout(new GridBagLayout());
		// mLeftMainPanel.add(mConnectionScrollPane);

		// x y w h wx wy
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, mLeftMainPanel, mConnectionScrollPane, LayoutHelper.mLblInsets);
	}

	public void changeParameterPanel() {
		if (mLeftMainPanel.getComponents().length > 1) {
			mLeftMainPanel.remove(mLeftMainPanel.getComponents().length - 1);
		}

		JPanel mJpParameter = new JPanel();
		mJpParameter.setLayout(new GridBagLayout());
		mJpParameter.setBorder(BorderFactory.createTitledBorder("Parameter"));

		Object selectedComponent = mConnectionTree.getLastSelectedPathComponent();
		JPanel parameter = new JPanel();

		if (selectedComponent instanceof DataserviceConnection) {
			parameter = new DataServiceParameterPanel();

		} else if (selectedComponent instanceof MapServerConnection) {
			parameter = new MapServerParameterPanel();

		} else if (selectedComponent instanceof Subscription) {
			parameter = new SubscriptionParameterPanel();

		}
		LayoutHelper.addComponent(0, 1, 1, 1, 1.0, 0.0, mJpParameter, parameter, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 1, 1, 1, 1.0, 0.0, mLeftMainPanel, mJpParameter, LayoutHelper.mLblInsets);
		mLeftMainPanel.updateUI();
	}

	/**
	 * Adds a component to a JTabbedPane with a little "close tab" button on the right side of the tab.
	 *
	 * @param cTab the ConnectionTab that should be added
	 */
	private void addClosableTab(final ConnectionTab cTab) {
		if (mCancelIcon == null) {
			mCancelIcon = new ImageIcon[2];
			mCancelIcon[0] = new ImageIcon(MainWindow.class.getClassLoader().getResource("close.png").getPath());
			mCancelIcon[1] = new ImageIcon(MainWindow.class.getClassLoader().getResource("closeHover.png").getPath());
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
	
	private MapServerRestConnectionImpl searchDefaultConnection() {
		Dataservices dataservices = (Dataservices) mConnectionTree.getModel().getRoot();

		for (Data dataservice : dataservices.getSubData()) {
			if (dataservice.getName().equals("default")) {
				for (Data mapServerConnection : dataservice.getSubData()) {
					if (mapServerConnection.getName().equals("default")) {
						if (mapServerConnection instanceof MapServerRestConnectionImpl) {
							return (MapServerRestConnectionImpl) mapServerConnection;
						}
					}
				}
			}
		}

		return null;
	}

	public void showDefaultGraphIfAvailable() {
		final MapServerRestConnectionImpl mapServerConnection = searchDefaultConnection();

		if (mapServerConnection != null) {
			if (!mapServerConnection.isGraphStarted()) {
				mapServerConnection.initGraph();
			}

			addClosableTab(mapServerConnection.getConnectionTab());
			mTabbedConnectionPane.setSelectedComponent(mapServerConnection.getConnectionTab());
		}
	}

	/**
	 * Loads Properties
	 */
	private void loadProperties() {
		setLocation(mConfig.getInt("window.position.x", 0), mConfig.getInt("window.position.y", 0));
		setPreferredSize(new Dimension(mConfig.getInt("window.width", 1280), mConfig.getInt("window.height", 720)));
		mMainSplitPane.setDividerLocation(mConfig.getInt("window.resizeweight", -1));
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
					mConfig.set("window.position.x", (int) getLocationOnScreen().getX());
					mConfig.set("window.position.y", (int) getLocationOnScreen().getY());
					mConfig.set("window.width", getWidth());
					mConfig.set("window.height", getHeight());
					mConfig.set("window.divider", mMainSplitPane.getDividerLocation());
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
				LOGGER.error(e.getMessage(), e);
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
					LOGGER.error(e.getMessage(), e);
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

	public void showAllMapServerConnections(boolean b) {
		mConnectionTree.showAllMapServerConnections(b);
	}

	public void showAllSubscriptions(boolean b) {
		mConnectionTree.showAllSubscriptions(b);
	}

	public void showConnectionTreePopupMenu(int x, int y) {
		TreePath mouseTreePath = mConnectionTree.getClosestPathForLocation(x, y);
		mConnectionTree.setSelectionPath(mouseTreePath);
		Object selectedComponent = mouseTreePath.getLastPathComponent();

		ConnectionTreePopupMenu popUp = new ConnectionTreePopupMenu(mConnectionTree, (Data) selectedComponent);
		popUp.show(mConnectionTree, x, y);
	}

	public void mouseDoubleClicked() {
		Object selectedComponent = mConnectionTree.getLastSelectedPathComponent();
		if (selectedComponent instanceof MapServerRestConnectionImpl) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;

			if (!mapServerConnection.isGraphStarted()) {
				mapServerConnection.initGraph();
			}

			boolean alreadyOpen = false;
			Component tmpComponent = null;
			for (Component t : mTabbedConnectionPane.getComponents()) {
				if (t.equals(mapServerConnection.getConnectionTab())) {
					alreadyOpen = true;
					tmpComponent = t;
				}
			}
			if (!alreadyOpen) {
				addClosableTab(mapServerConnection.getConnectionTab());
			} else {
				mTabbedConnectionPane.setSelectedComponent(tmpComponent);
			}
		} else if (selectedComponent instanceof RestSubscriptionImpl) {
			RestSubscriptionImpl subscription = (RestSubscriptionImpl) selectedComponent;

			if (subscription.isActive()) {
				subscription.stopSubscription();
			} else {
				subscription.startSubscription();
			}

			mConnectionTree.updateUI();
		}
	}
}
