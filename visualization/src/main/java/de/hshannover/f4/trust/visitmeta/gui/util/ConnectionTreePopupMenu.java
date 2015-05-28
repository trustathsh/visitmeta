package de.hshannover.f4.trust.visitmeta.gui.util;

import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.DATASERVICE_CONNECTED_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.DATASERVICE_DISCONNECTED_ICON;
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

import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.gui.MainWindow;
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

	private JCheckBoxMenuItem mOpen;

	private MainWindow mMainWindow;

	private RESTConnectionTree mConnectionTree;

	private Data mSelectedData;

	public ConnectionTreePopupMenu(RESTConnectionTree connectionTree, Data selectedData) {
		mSelectedData = selectedData;
		mConnectionTree = connectionTree;

		if (mSelectedData instanceof DataserviceConnection) {
			initConnectButton();
			initDisconnectButton();
		} else if (mSelectedData instanceof MapServerConnection) {
			initConnectButton();
			initDisconnectButton();
			initOnlyActiveButton();
		} else if (mSelectedData instanceof Subscription) {
			initActiveButton();
			initInactiveButton();
			initOnlyActiveButton();
		}

	}

	public ConnectionTreePopupMenu(RESTConnectionTree connectionTree, MainWindow mainWindow, Data selectedComponent) {
		this(connectionTree, selectedComponent);
		mMainWindow = mainWindow;

		if (mSelectedData instanceof DataserviceConnection) {

		} else if (mSelectedData instanceof MapServerConnection) {
			initOpenButton();
		} else if (mSelectedData instanceof Subscription) {

		}
	}

	private void initOpenButton() {
		mOpen = new JCheckBoxMenuItem("Open");

		Object selectedComponent = mConnectionTree.getLastSelectedPathComponent();
		if (selectedComponent instanceof MapServerRestConnectionImpl) {
			MapServerRestConnectionImpl mapServerConnection = (MapServerRestConnectionImpl) selectedComponent;

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

		super.add(mOpen, 0);
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
					LOGGER.error(e.toString(), e);
				}

				mConnectionTree.updateUI();
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
					LOGGER.error(e.toString(), e);
				}

				mConnectionTree.updateUI();
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
					LOGGER.error(e.toString(), e);
				}

				mConnectionTree.updateUI();
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

				mConnectionTree.updateUI();
			}
		});

		super.add(mDeactivate);
	}

	private void initDeleteButton() {
		mDelete = new JCheckBoxMenuItem("Delete");
		mDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO delete the Connection

				mConnectionTree.updateUI();
			}
		});
	}
}
