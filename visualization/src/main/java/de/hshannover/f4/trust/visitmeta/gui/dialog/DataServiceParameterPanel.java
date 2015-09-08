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
 * This file is part of visitmeta-visualization, version 0.5.2,
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
package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.hshannover.f4.trust.visitmeta.gui.util.DocumentChangedListener;
import de.hshannover.f4.trust.visitmeta.gui.util.ParameterPanel;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.data.DataserviceData;

public class DataServiceParameterPanel extends ParameterPanel {

	private static final long serialVersionUID = -4830135051242549298L;

	private JLabel mJlName;
	private JLabel mJlUrl;
	private JLabel mJlRawXml;

	private JTextField mJtfUrl;
	private JTextField mJtfName;

	private JCheckBox mJcbRawXML;

	private DataserviceData mConnectionData;

	private DocumentChangedListener mDocumentChangedListener;

	private ItemListener mItemListener;

	public DataServiceParameterPanel() {
		createPanels();
	}

	public DataServiceParameterPanel(DataserviceData connectionData) {
		mConnectionData = connectionData;

		createPanels();
		updatePanel();
		addChangeListeners();
	}

	private void createPanels() {
		setLayout(new GridBagLayout());

		mJlUrl = new JLabel("Url");
		mJlName = new JLabel("Name");
		mJlRawXml = new JLabel("RAW-XML");

		mJtfUrl = new JTextField();
		mJtfName = new JTextField();
		mJtfName.setEditable(false);

		mJcbRawXML = new JCheckBox();

		// x y w h wx wy
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlUrl, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfUrl, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlRawXml, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJcbRawXML, LayoutHelper.mLblInsets);
	}

	private void addChangeListeners() {
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
		mJcbRawXML.addItemListener(mItemListener);
	}


	private void updatePanel() {
		mJtfName.setText(mConnectionData.getName());
		mJtfUrl.setText(mConnectionData.getUrl());
		mJcbRawXML.setSelected(mConnectionData.isRawXml());
	}

	@Override
	public Data getData() {
		mConnectionData.setName(mJtfName.getText().trim());
		mConnectionData.setUrl(mJtfUrl.getText().trim());
		mConnectionData.setRawXml(mJcbRawXML.isSelected());
		return mConnectionData;
	}

	@Override
	public void setNameTextFieldEditable() {
		mJtfName.setEditable(true);
	}

}
