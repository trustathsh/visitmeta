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
package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.hshannover.f4.trust.visitmeta.gui.util.DocumentChangedListener;
import de.hshannover.f4.trust.visitmeta.gui.util.HintTextField;
import de.hshannover.f4.trust.visitmeta.gui.util.ParameterPanel;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.data.MapServerData;

public class MapServerParameterPanel extends ParameterPanel {

	private static final long serialVersionUID = -3686612903315798696L;

	private JLabel mJlName;
	private JLabel mJlUrl;
	private JLabel mJlBasicAuthentication;
	private JLabel mJlUsername;
	private JLabel mJlPassword;
	private JLabel mJlMaxPollResultSize;
	private JLabel mJlConnectingAtStartUp;

	private JTextField mJtfUrl;
	private JTextField mJtfName;
	private JTextField mJtfUsername;
	private JFormattedTextField mJtfMaxPollResultSize;

	private JPasswordField mJtfPassword;

	private JCheckBox mJcbBasicAuthentication;
	private JCheckBox mJcbConnectingAtStartUp;

	private MapServerData mConnectionData;

	private DocumentChangedListener mDocumentChangedListener;

	private ItemListener mItemListener;

	public MapServerParameterPanel() {
		createPanels();
	}

	public MapServerParameterPanel(MapServerData connectionData) {
		mConnectionData = connectionData;

		createPanels();
		updatePanel();
		addChangeListeners();

	}

	private void createPanels() {
		setLayout(new GridBagLayout());

		mJlName = new JLabel("Name");
		mJlUrl = new JLabel("Map-Server Url");
		mJlBasicAuthentication = new JLabel("Basic Authentication");
		mJlBasicAuthentication.setEnabled(false);
		mJlUsername = new JLabel("Username");
		mJlPassword = new JLabel("Password");
		mJlMaxPollResultSize = new JLabel("max-poll-result-size");
		mJlConnectingAtStartUp = new JLabel("Connecting at start-up");

		mJtfName = new JTextField();
		mJtfName.setEditable(false);

		mJtfUrl = new JTextField();
		mJcbBasicAuthentication = new JCheckBox();
		mJcbBasicAuthentication.setEnabled(false);
		mJcbConnectingAtStartUp = new JCheckBox();
		mJtfUsername = new JTextField();
		mJtfPassword = new JPasswordField();
		mJtfMaxPollResultSize = new HintTextField("Optional", NumberFormat.getIntegerInstance());

		// x y w h wx wy
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlUrl, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlBasicAuthentication, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 3, 1, 1, 1.0, 1.0, this, mJlUsername, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 4, 1, 1, 1.0, 1.0, this, mJlPassword, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 5, 1, 1, 1.0, 1.0, this, mJlMaxPollResultSize, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 6, 1, 1, 1.0, 1.0, this, mJlConnectingAtStartUp, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfUrl, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJcbBasicAuthentication, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 3, 1, 1, 1.0, 1.0, this, mJtfUsername, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 4, 1, 1, 1.0, 1.0, this, mJtfPassword, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 5, 1, 1, 1.0, 1.0, this, mJtfMaxPollResultSize, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 6, 1, 1, 1.0, 1.0, this, mJcbConnectingAtStartUp, LayoutHelper.LABEL_INSETS);
	}

	private void addChangeListeners() {
		PropertyChangeListener mPropertyChangeListener = new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				fireParameterChanged();
			}
		};

		mDocumentChangedListener = new DocumentChangedListener() {

			@Override
			protected void dataChanged() {
				fireParameterChanged();
			}
		};

		mItemListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent event) {
				fireParameterChanged();
			}
		};

		mJtfName.getDocument().addDocumentListener(mDocumentChangedListener);
		mJtfUrl.getDocument().addDocumentListener(mDocumentChangedListener);
		mJtfUsername.getDocument().addDocumentListener(mDocumentChangedListener);
		mJtfPassword.getDocument().addDocumentListener(mDocumentChangedListener);
		mJtfMaxPollResultSize.addPropertyChangeListener("value", mPropertyChangeListener);
		mJcbConnectingAtStartUp.addItemListener(mItemListener);
		mJcbBasicAuthentication.addItemListener(mItemListener);
	}

	private void updatePanel() {
		mJtfName.setText(mConnectionData.getName());
		mJtfUrl.setText(mConnectionData.getUrl());
		mJtfUsername.setText(mConnectionData.getUserName());
		mJtfPassword.setText(mConnectionData.getUserPassword());
		mJcbBasicAuthentication.setSelected(mConnectionData.isAuthenticationBasic());
		// TODO TRUSTSTORE_PATH
		// TODO TRUSTSTORE_PASS
		mJcbConnectingAtStartUp.setSelected(mConnectionData.doesConnectOnStartup());
		if (mConnectionData.getMaxPollResultSize() > 0) {
			mJtfMaxPollResultSize.setValue(mConnectionData.getMaxPollResultSize());
		}
	}

	@Override
	public Data getData() {
		mConnectionData.setName(mJtfName.getText().trim());
		mConnectionData.setUrl(mJtfUrl.getText().trim());
		mConnectionData.setUserName(mJtfUsername.getText().trim());
		mConnectionData.setUserPassword(String.valueOf(mJtfPassword.getPassword()).trim());
		mConnectionData.setAuthenticationBasic(mJcbBasicAuthentication.isSelected());
		// TODO TRUSTSTORE_PATH
		// TODO TRUSTSTORE_PASS
		mConnectionData.setStartupConnect(mJcbConnectingAtStartUp.isSelected());
		if (mJtfMaxPollResultSize.getValue() != null) {
			mConnectionData.setMaxPollResultSize(((Number) mJtfMaxPollResultSize.getValue()).intValue());
		}
		return mConnectionData;
	}

	@Override
	public void setNameTextFieldEditable() {
		mJtfName.setEditable(true);
	}
}
