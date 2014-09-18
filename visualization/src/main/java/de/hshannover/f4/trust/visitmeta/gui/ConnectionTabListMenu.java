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
 * This file is part of visitmeta-visualization, version 0.2.0,
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;

public class ConnectionTabListMenu extends JPopupMenu {
	private static final Logger LOGGER = Logger.getLogger(ConnectionTabListMenu.class);
	private static final long serialVersionUID = 1L;
	private JMenuItem mConnect = null;
	private JMenuItem mDisconnect = null;
	private JMenuItem mDelete = null;
	private ConnectionTab mConnection = null;

	/**
	 * Initializes a new ConnectionTabListMenu instance and arrange its elements
	 * 
	 * @param connection
	 *            ConnectionTap Object that represents the Connection for which
	 *            the context menu is used
	 */
	public ConnectionTabListMenu(ConnectionTab connection) {
		LOGGER.trace("Creating new ConnectionTabListMenu for " + connection);
		this.mConnection = connection;

		initConnectButton();
		initDisconnectButton();
		initDeleteButton();

		this.add(mConnect);
		this.add(mDisconnect);
		this.add(mDelete);
	}

	/**
	 * Initializes the connect button and adds an ActionListener
	 */
	private void initConnectButton() {
		mConnect = new JMenuItem("Connect");
		mConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO connect the Connection
				mConnection.connect();
			}
		});
	}

	/**
	 * Initializes the disconnect button and adds an ActionListener
	 */
	private void initDisconnectButton() {
		mDisconnect = new JMenuItem("Disconnect");
		mDisconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO disconnect the Connection
				mConnection.disconnect();
			}
		});
	}

	/**
	 * Initializes the delete button and adds an ActionListener
	 */
	private void initDeleteButton() {
		mDelete = new JMenuItem("Delete");
		mDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO delete the Connection
			}
		});
	}
}
