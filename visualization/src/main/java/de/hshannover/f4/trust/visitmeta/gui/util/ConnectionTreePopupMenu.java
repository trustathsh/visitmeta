package de.hshannover.f4.trust.visitmeta.gui.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

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

	private JMenuItem mConnect;

	private JMenuItem mDisconnect;

	private JMenuItem mActivate;

	private JMenuItem mDeactivate;

	private JMenuItem mDelete;

	private Data mSelectedData;

	public ConnectionTreePopupMenu(Data selectedData) {
		mSelectedData = selectedData;

		if (mSelectedData instanceof DataserviceConnection) {
			initConnectButton();
			initDisconnectButton();
		} else if (mSelectedData instanceof MapServerConnection) {
			initConnectButton();
			initDisconnectButton();
		} else if (mSelectedData instanceof Subscription) {
			initActiveButton();
			initInactiveButton();
		}

	}

	private void initConnectButton() {
		mConnect = new JMenuItem("Connect");
		mConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					((Connection) mSelectedData).connect();
				} catch (ConnectionException e) {
					LOGGER.error(e.toString(), e);
				}
			}
		});

		super.add(mConnect);
	}

	private void initDisconnectButton() {
		mDisconnect = new JMenuItem("Disconnect");
		mDisconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					((Connection) mSelectedData).disconnect();
				} catch (ConnectionException e) {
					LOGGER.error(e.toString(), e);
				}
			}
		});

		super.add(mDisconnect);
	}

	private void initActiveButton() {
		mActivate = new JMenuItem("Activate");
		mActivate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					((Subscription) mSelectedData).startSubscription();
				} catch (ConnectionException e) {
					LOGGER.error(e.toString(), e);
				}
			}
		});

		super.add(mActivate);
	}

	private void initInactiveButton() {
		mDeactivate = new JMenuItem("Deactivate");
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

	private void initDeleteButton() {
		mDelete = new JMenuItem("Delete");
		mDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO delete the Connection
			}
		});
	}
}
