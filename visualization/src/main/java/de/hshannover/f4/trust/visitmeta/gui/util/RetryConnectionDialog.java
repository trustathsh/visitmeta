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
 * This file is part of visitmeta-visualization, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.gui.dialog.LayoutHelper;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.Connection;

public class RetryConnectionDialog extends JDialog {

	private static final long serialVersionUID = -4809177642344881613L;

	private Thread mConnectionThread;

	private JPanel mJpMain;

	private JPanel mJpSouth;

	private JPanel mJpMessage;

	private JButton mJbCancel;

	private JButton mJbClose;

	private JButton mJbTryAgain;

	private Connection mConnection;

	public RetryConnectionDialog(String title, Connection connection, Component component) {
		mConnection = connection;

		createDialog(title, component);
		createPanels();

		super.pack();
	}

	private void createDialog(String title, Component c) {
		setTitle(title);
		setModal(true);
		setMinimumSize(new Dimension(270, 200));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(c);
	}

	private void createPanels() {
		getContentPane().setLayout(new GridBagLayout());

		initMainPanel();
		initSouthPanel();
		initMessagePanel();

		// x y w h wx wy
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, getContentPane(), mJpMain, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 1, 1, 1, 0.0, 0.0, getContentPane(), mJpSouth, LayoutHelper.LABEL_INSETS);
	}

	private void initMainPanel() {
		mJpMain = new JPanel();
		mJpMain.setLayout(new GridBagLayout());
	}

	private void initMessagePanel() {
		mJpMessage = new JPanel();
		mJpMessage.setLayout(new GridBagLayout());

		// x y w h wx wy
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, mJpMain, mJpMessage, LayoutHelper.LABEL_INSETS);
	}

	private void initSouthPanel() {
		mJpSouth = new JPanel();
		mJpSouth.setLayout(new FlowLayout(FlowLayout.CENTER));

		mJbCancel = new JButton("Cancel");
		mJbCancel.setEnabled(false);
		mJbCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				mConnectionThread.interrupt();
			}
		});

		mJbClose = new JButton("Close");
		mJbClose.setEnabled(false);
		mJbClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}
		});

		mJbTryAgain = new JButton("Try Again");
		mJbTryAgain.setEnabled(false);
		mJbTryAgain.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				mJbClose.setEnabled(false);
				mJbTryAgain.setEnabled(false);
				resetMessagePanel();
				connect();
			}
		});

		mJpSouth.add(mJbTryAgain);
		mJpSouth.add(mJbCancel);
		mJpSouth.add(mJbClose);
	}

	private void resetMessagePanel() {
		mJpMessage.removeAll();
		mJpMain.removeAll();
		initMessagePanel();
		super.pack();
	}

	private void appendMassage(String massage, Color color, int x, int y) {
		JLabel jlMassage = new JLabel(massage);
		jlMassage.setForeground(color);
		jlMassage.setFont(new Font(jlMassage.getFont().getFontName(), Font.BOLD, jlMassage.getFont().getSize()));

		// x y w h wx wy
		LayoutHelper.addComponent(x, y, 1, 1, 0.0, 0.0, mJpMessage, jlMassage, LayoutHelper.LABEL_INSETS);

		super.pack();
	}

	private void retryConnect(Connection connection, int retry) throws ConnectionException, InterruptedException {
		for (int i = 0; i < retry; i++) {
			appendMassage("Try to connect("
					+ (i
							+ 1)
					+ "/" + retry + ")...", Color.BLACK, 0,
					mJpMessage.getComponentCount());
			try {
				connection.connect();
				appendMassage("Connected!", new Color(59, 186, 63), 1, mJpMessage.getComponentCount()
						- 1);
				mJbClose.setEnabled(true);
				mJbCancel.setEnabled(false);
				break;
			} catch (ConnectionException e) {
				if (i
						+ 1 < retry) {
					appendMassage(e.getClass().getSimpleName(), Color.RED, 1, mJpMessage.getComponentCount()
							- 1);
					appendMassage("wait 3 seconds...", Color.BLACK, 0, mJpMessage.getComponentCount());
					Thread.sleep(3000);
				} else {
					throw e;
				}
			}
		}
	}

	public void connect() {
		mConnectionThread = new Thread(new Runnable() {

			@Override
			public void run() {
				mJbCancel.setEnabled(true);

				try {
					retryConnect(mConnection, 3);
				} catch (ConnectionException | InterruptedException e) {
					appendMassage(e.getClass().getSimpleName(), Color.RED, 1, mJpMessage.getComponentCount()
							- 1);
					mJbCancel.setEnabled(false);
					mJbClose.setEnabled(true);
					mJbTryAgain.setEnabled(true);
				}
			}
		});

		mConnectionThread.start();
	}

	public void showDialog() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				setVisible();
			}
		}).start();
	}

	private void setVisible() {
		super.setVisible(true);
	}
}
