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
package de.hshannover.f4.trust.visitmeta.gui.util;

import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.ADD_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.CLONE_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.CONNECTED_TREE_UPDATE_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.DATASERVICE_CONNECTED_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.DATASERVICE_DISCONNECTED_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.DELETE_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.MAPSERVER_CONNECTED_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.MAPSERVER_DISCONNECTED_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.SUBSCRIPTION_ACTIVE_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.SUBSCRIPTION_INACTIVE_ICON;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.exceptions.RESTException;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.gui.ConnectionTab;
import de.hshannover.f4.trust.visitmeta.gui.MainWindow;
import de.hshannover.f4.trust.visitmeta.gui.dialog.ConnectionDialog;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.Connection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class ConnectionTreePopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 3346892280397457506L;

	private static final Logger LOGGER = Logger.getLogger(ConnectionTreePopupMenu.class);

	private JCheckBoxMenuItem mConnect;

	private JCheckBoxMenuItem mDisconnect;

	private JCheckBoxMenuItem mActivate;

	private JCheckBoxMenuItem mDeactivate;

	private JCheckBoxMenuItem mDelete;

	private JCheckBoxMenuItem mOnlyActive;

	private JCheckBoxMenuItem mUpdateTree;

	private JCheckBoxMenuItem mOpen;

	private JCheckBoxMenuItem mAdd;

	private JCheckBoxMenuItem mClone;

	private MainWindow mMainWindow;

	private ConnectionDialog mConnectionDialog;

	private RESTConnectionTree mConnectionTree;

	private Data mSelectedData;

	private ConnectionTreePopupMenu(RESTConnectionTree connectionTree, Data selectedData){
		mSelectedData = selectedData;
		mConnectionTree = connectionTree;
	}

	public ConnectionTreePopupMenu(RESTConnectionTree connectionTree, ConnectionDialog connectionDialog,
			Data selectedData) {
		this(connectionTree, selectedData);
		mConnectionDialog = connectionDialog;

		if (mSelectedData instanceof Dataservices) {
			initAddButton("Dataservice");

		}else if (mSelectedData instanceof DataserviceConnection){
			initCloneButton("Dataservice");
			initDeleteButton("Dataservice");
			super.addSeparator();
			initAddButton("Connection");
			
		}else if (mSelectedData instanceof MapServerConnection){
			initCloneButton("Connection");
			initDeleteButton("Connection");
			super.addSeparator();
			initAddButton("Subscription");
	
		}else if (mSelectedData instanceof Subscription) {
			initCloneButton("Subscription");
			initDeleteButton("Subscription");
			
		}
			
		super.addSeparator();

		initDefaultButton(mConnectionDialog);
	}

	public ConnectionTreePopupMenu(RESTConnectionTree connectionTree, MainWindow mainWindow, Data selectedData) {
		this(connectionTree, selectedData);
		mMainWindow = mainWindow;

		if (mSelectedData instanceof MapServerConnection) {
			initOpenButton();
		}

		initDefaultButton(mMainWindow);
	}

	private void initDefaultButton(Component component) {
		if (mSelectedData instanceof DataserviceConnection) {
			initConnectButton();
			initDisconnectButton();
			super.addSeparator();
		} else if (mSelectedData instanceof MapServerConnection) {
			initConnectButton();
			initDisconnectButton();
			initOnlyActiveButton();
			super.addSeparator();
		} else if (mSelectedData instanceof Subscription) {
			initActiveButton();
			initInactiveButton();
			initOnlyActiveButton();
			super.addSeparator();
		}
		initUpdateTreeButton(component);
	}

	private void initOpenButton() {
		mOpen = new JCheckBoxMenuItem("Open");

		if (mSelectedData instanceof MapServerRestConnectionImpl) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) mSelectedData;

			boolean alreadyOpen = false;
			for (Component t : mMainWindow.getTabbedConnectionPane().getComponents()) {
				if (t.equals(mapServerConnection.getConnectionTab())) {
					alreadyOpen = true;
				}
			}

			mOpen.setSelected(alreadyOpen);
		}

		mOpen.setAccelerator(KeyStroke.getKeyStroke('X', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				mMainWindow.openMapServerConnectionTab();

			}
		});

		super.add(mOpen);
	}

	private void initOnlyActiveButton() {
		mOnlyActive = new JCheckBoxMenuItem("show only active");
		mOnlyActive.setEnabled(false);
		if (mSelectedData instanceof MapServerConnection) {
			mOnlyActive.setSelected(mConnectionTree.isShowAllMapServerConnections());
		} else if (mSelectedData instanceof Subscription) {
			mOnlyActive.setSelected(mConnectionTree.isShowAllSubscriptions());
		}
		mOnlyActive.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mOnlyActive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (mSelectedData instanceof MapServerConnection) {
					mConnectionTree.showAllMapServerConnections(mOnlyActive.isSelected());
				} else if (mSelectedData instanceof Subscription) {
					mConnectionTree.showAllSubscriptions(mOnlyActive.isSelected());
				}
			}
		});

		super.add(mOnlyActive);
	}

	private void initConnectButton() {
		if (mSelectedData instanceof DataserviceConnection) {
			mConnect = new JCheckBoxMenuItem("Connect", DATASERVICE_CONNECTED_ICON);
		} else if (mSelectedData instanceof MapServerConnection) {
			mConnect = new JCheckBoxMenuItem("Connect", MAPSERVER_CONNECTED_ICON);
		}
		mConnect.setSelected(((Connection) mSelectedData).isConnected());
		mConnect.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					((Connection) mSelectedData).connect();
				} catch (ConnectionException e) {
					LOGGER.warn(e.toString());
					LOGGER.info("Start retry connection...");
					String title = "Connecting to " + ((Connection) mSelectedData).getConnectionName();
					RetryConnectionDialog retryDialog = new RetryConnectionDialog(title, (Connection) mSelectedData,
							mMainWindow);
					retryDialog.showDialog();
					retryDialog.connect();
				}
			}
		});

		super.add(mConnect);
	}

	private void initDisconnectButton() {
		if (mSelectedData instanceof DataserviceConnection) {
			mDisconnect = new JCheckBoxMenuItem("Disconnect", DATASERVICE_DISCONNECTED_ICON);
		} else if (mSelectedData instanceof MapServerConnection) {
			mDisconnect = new JCheckBoxMenuItem("Disconnect", MAPSERVER_DISCONNECTED_ICON);
		}
		mDisconnect.setSelected(!((Connection) mSelectedData).isConnected());
		mDisconnect.setAccelerator(KeyStroke.getKeyStroke('D', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mDisconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					((Connection) mSelectedData).disconnect();
				} catch (ConnectionException e) {
					LOGGER.error(e.toString());
				}
			}
		});

		super.add(mDisconnect);
	}

	private void initActiveButton() {
		mActivate = new JCheckBoxMenuItem("Activate", SUBSCRIPTION_ACTIVE_ICON);
		mActivate.setSelected(((Subscription) mSelectedData).isActive());
		mActivate.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mActivate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					((Subscription) mSelectedData).startSubscription();
				} catch (ConnectionException e) {
					LOGGER.error(e.toString());
				}
			}
		});

		super.add(mActivate);
	}

	private void initInactiveButton() {
		mDeactivate = new JCheckBoxMenuItem("Deactivate", SUBSCRIPTION_INACTIVE_ICON);
		mDeactivate.setSelected(!((Subscription) mSelectedData).isActive());
		mDeactivate.setAccelerator(KeyStroke.getKeyStroke('D', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mDeactivate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					((Subscription) mSelectedData).stopSubscription();
				} catch (ConnectionException e) {
					LOGGER.error(e.toString(), e);
				}
			}
		});

		super.add(mDeactivate);
	}

	private void initAddButton(String expression) {
		mAdd = new JCheckBoxMenuItem("Add " + expression, ADD_ICON);
		mAdd.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				mConnectionDialog.eventNewData();
			}
		});

		super.add(mAdd);
	}

	private void initCloneButton(String expression) {
		mClone = new JCheckBoxMenuItem("Clone " + expression, CLONE_ICON);
		mClone.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mClone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				mConnectionDialog.eventCloneData();
			}
		});

		super.add(mClone);
	}

	private void initUpdateTreeButton(final Component component) {
		mUpdateTree = new JCheckBoxMenuItem("Update Tree", CONNECTED_TREE_UPDATE_ICON);
		mUpdateTree.setAccelerator(KeyStroke.getKeyStroke('U', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mUpdateTree.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					mConnectionTree.updateConnections(Main.getDataservicePersister().loadDataserviceConnections(
							component));
				} catch (PropertyException e) {
					LOGGER.error(e.toString());
				}

				mConnectionTree.expandAllNodes();

				reopenConnectionTabs();
			}
		});

		super.add(mUpdateTree);
	}

	private void initDeleteButton(String expression) {
		mDelete = new JCheckBoxMenuItem("Delete " + expression, DELETE_ICON);
		mDelete.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					mConnectionDialog.eventDeleteData();
				} catch (PropertyException | RESTException e) {
					LOGGER.error(e.toString());
				}
			}
		});

		super.add(mDelete);
	}

	private void reopenConnectionTabs() {
		if (mMainWindow != null) {
			for (Component c : mMainWindow.getTabbedConnectionPane().getComponents()) {
				if (c instanceof ConnectionTab) {
					ConnectionTab connectionTab = (ConnectionTab) c;
					mMainWindow.closeConnectionTab(connectionTab);
				}
			}

			mMainWindow.openConnectedMapServerConnections();
		}
	}
}
