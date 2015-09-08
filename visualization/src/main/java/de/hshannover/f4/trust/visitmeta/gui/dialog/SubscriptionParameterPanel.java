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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.hshannover.f4.trust.visitmeta.gui.util.DocumentChangedListener;
import de.hshannover.f4.trust.visitmeta.gui.util.HintTextField;
import de.hshannover.f4.trust.visitmeta.gui.util.ParameterPanel;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.data.SubscriptionData;

public class SubscriptionParameterPanel extends ParameterPanel {

	private static final long serialVersionUID = -3686612903315798696L;

	private JLabel mJlName;
	private JLabel mJlStartIdentifier;
	private JLabel mJlStartIdentifierType;
	private JLabel mJlMatchFilterLinks;
	private JLabel mJlResultFilter;
	private JLabel mJlTerminalIdentifierTypes;
	private JLabel mJlStartupSubscribe;
	private JLabel mJlMaxDepth;
	private JLabel mJlMaxSize;

	private JTextField mJtfName;
	private JTextField mJtfStartIdentifier;
	private JTextField mJtfStartIdentifierType;
	private JTextField mJtfFilterLinks;
	private JTextField mJtfFilterResult;
	private JTextField mJtfTerminalIdentifierTypes;
	private JFormattedTextField mJtfMaxDepth;
	private JFormattedTextField mJtfMaxSize;

	private JCheckBox mJcbStartupSubscribe;

	private SubscriptionData mSubscription;

	private DocumentChangedListener mDocumentChangedListener;

	private ItemListener mItemListener;

	public SubscriptionParameterPanel() {
		createPanels();
	}

	public SubscriptionParameterPanel(SubscriptionData subscription) {
		mSubscription = subscription;

		createPanels();
		updatePanel();
		addChangeListeners();
	}

	private void createPanels() {
		setLayout(new GridBagLayout());

		mJlName = new JLabel("Name");
		mJlStartIdentifier = new JLabel("Start Identifier");
		mJlStartIdentifierType = new JLabel("Start Identifier Type");
		mJlMatchFilterLinks = new JLabel("Match Links Filter");
		mJlResultFilter = new JLabel("Result Filter");
		mJlTerminalIdentifierTypes = new JLabel("Terminal Identifier Types");
		mJlMaxDepth = new JLabel("Max Depth");
		mJlMaxSize = new JLabel("Max Size");
		mJlStartupSubscribe = new JLabel("Subscribe at start-up");

		mJtfName = new JTextField();
		mJtfName.setEditable(false);

		mJtfStartIdentifier = new JTextField();
		mJtfStartIdentifierType = new JTextField();
		mJtfFilterLinks = new JTextField();
		mJtfFilterResult = new JTextField();
		mJtfTerminalIdentifierTypes = new JTextField();

		mJtfMaxDepth = new HintTextField("Optional", NumberFormat.getIntegerInstance());
		mJtfMaxSize = new HintTextField("Optional", NumberFormat.getIntegerInstance());

		mJcbStartupSubscribe = new JCheckBox();

		// x y w h wx wy
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlStartIdentifier, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlStartIdentifierType, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 3, 1, 1, 1.0, 1.0, this, mJlMatchFilterLinks, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 4, 1, 1, 1.0, 1.0, this, mJlResultFilter, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 5, 1, 1, 1.0, 1.0, this, mJlTerminalIdentifierTypes, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 6, 1, 1, 1.0, 1.0, this, mJlMaxDepth, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 7, 1, 1, 1.0, 1.0, this, mJlMaxSize, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 8, 1, 1, 1.0, 1.0, this, mJlStartupSubscribe, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfStartIdentifier, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJtfStartIdentifierType, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 3, 1, 1, 1.0, 1.0, this, mJtfFilterLinks, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 4, 1, 1, 1.0, 1.0, this, mJtfFilterResult, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 5, 1, 1, 1.0, 1.0, this, mJtfTerminalIdentifierTypes, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 6, 1, 1, 1.0, 1.0, this, mJtfMaxDepth, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 7, 1, 1, 1.0, 1.0, this, mJtfMaxSize, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 8, 1, 1, 1.0, 1.0, this, mJcbStartupSubscribe, LayoutHelper.mLblInsets);
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
		mJtfFilterLinks.getDocument().addDocumentListener(mDocumentChangedListener);
		mJtfFilterResult.getDocument().addDocumentListener(mDocumentChangedListener);
		mJtfMaxDepth.addPropertyChangeListener("value", mPropertyChangeListener);
		mJtfMaxSize.addPropertyChangeListener("value", mPropertyChangeListener);
		mJtfStartIdentifier.getDocument().addDocumentListener(mDocumentChangedListener);
		mJtfStartIdentifierType.getDocument().addDocumentListener(mDocumentChangedListener);
		mJtfTerminalIdentifierTypes.getDocument().addDocumentListener(mDocumentChangedListener);
		mJcbStartupSubscribe.addItemListener(mItemListener);
	}


	private void updatePanel() {
		mJtfName.setText(mSubscription.getName());
		mJtfStartIdentifier.setText(mSubscription.getStartIdentifier());
		mJtfStartIdentifierType.setText(mSubscription.getIdentifierType());
		mJtfFilterLinks.setText(mSubscription.getMatchLinksFilter());
		mJtfFilterResult.setText(mSubscription.getResultFilter());
		mJtfTerminalIdentifierTypes.setText(mSubscription.getTerminalIdentifierTypes());
		mJcbStartupSubscribe.setSelected(mSubscription.isStartupSubscribe());
		if (mSubscription.getMaxDepth() > 0) {
			mJtfMaxDepth.setValue(mSubscription.getMaxDepth());
		}

		if (mSubscription.getMaxSize() > 0) {
			mJtfMaxSize.setValue(mSubscription.getMaxSize());
		}
	}

	@Override
	public Data getData() {
		mSubscription.setName(mJtfName.getText().trim().replace(' ', '_'));
		mSubscription.setStartIdentifier(mJtfStartIdentifier.getText().trim());
		mSubscription.setIdentifierType(mJtfStartIdentifierType.getText().trim());
		mSubscription.setStartupSubscribe(mJcbStartupSubscribe.isSelected());

		if (!mJtfFilterLinks.getText().trim().equals("")) {
			mSubscription.setMatchLinksFilter(mJtfFilterLinks.getText().trim());
		}

		if (!mJtfFilterResult.getText().trim().equals("")) {
			mSubscription.setResultFilter(mJtfFilterResult.getText().trim());
		}

		if (!mJtfTerminalIdentifierTypes.getText().trim().equals("")) {
			mSubscription.setTerminalIdentifierTypes(mJtfTerminalIdentifierTypes.getText().trim());
		}

		if (mJtfMaxDepth.getValue() != null) {
			mSubscription.setMaxDepth(((Number) mJtfMaxDepth.getValue()).intValue());
		}

		if (mJtfMaxSize.getValue() != null) {
			mSubscription.setMaxSize(((Number) mJtfMaxSize.getValue()).intValue());
		}
		return mSubscription;
	}

	@Override
	public void setNameTextFieldEditable() {
		mJtfName.setEditable(true);
	}
}
