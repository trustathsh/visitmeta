package de.hshannover.f4.trust.visitmeta.gui.util;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
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

	private ImageIcon mDataserviceConnectedIcon;

	private ImageIcon mDataserviceDisconnectedIcon;

	private ImageIcon mMapServerConnectedIcon;

	private ImageIcon mMapServerDisconnectedIcon;

	private ImageIcon mSubscriptionActiveIcon;

	private ImageIcon mSubscriptionInactiveIcon;

	private RESTConnectionTree mConnectionTree;

	private Data mSelectedData;

	public ConnectionTreePopupMenu(RESTConnectionTree connectionTree, Data selectedData) {
		mSelectedData = selectedData;
		mConnectionTree = connectionTree;

		mDataserviceConnectedIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("DataserviceConnectedIcon.png").getPath());

		mDataserviceDisconnectedIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("DataserviceDisconnectedIcon.png").getPath());

		mMapServerConnectedIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("MapServerConnectedIcon.png").getPath());

		mMapServerDisconnectedIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("MapServerDisconnectedIcon.png").getPath());

		mSubscriptionActiveIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("SubscriptionActiveIcon.png").getPath());

		mSubscriptionInactiveIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("SubscriptionInactiveIcon.png").getPath());

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

	private void initOnlyActiveButton() {
		mOnlyActive = new JCheckBoxMenuItem("only active");
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
			mConnect = new JCheckBoxMenuItem("Connect", mDataserviceConnectedIcon);
		} else if (mSelectedData instanceof MapServerConnection) {
			mConnect = new JCheckBoxMenuItem("Connect", mMapServerConnectedIcon);
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
			mDisconnect = new JCheckBoxMenuItem("Disconnect", mDataserviceDisconnectedIcon);
		} else if (mSelectedData instanceof MapServerConnection) {
			mDisconnect = new JCheckBoxMenuItem("Disconnect", mMapServerDisconnectedIcon);
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
		mActivate = new JCheckBoxMenuItem("Activate", mSubscriptionActiveIcon);
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
		mDeactivate = new JCheckBoxMenuItem("Deactivate", mSubscriptionInactiveIcon);
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
