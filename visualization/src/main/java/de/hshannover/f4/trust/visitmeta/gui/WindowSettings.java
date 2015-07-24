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
 * This file is part of visitmeta-visualization, version 0.5.0,
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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.SettingManager;

public class WindowSettings extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(WindowSettings.class);
	private SettingManager mSettingManager = null;
	private JPanel mPanel = null;
	private SpringLayout mSpringLayout = null;
	private JTextField mInputNetworkInterval = null;
	private JTextField mInputCalculationInterval = null;
	private JTextField mInputCalculationIterations = null;
	private JTextField mInputHighlightsTimeout = null;
	private JTextField mInputNodeTranslationDuration = null;
	private JLabel mLabelNetwork = null;
	private JLabel mLabelCalculation = null;
	private JLabel mLabelIterations = null;
	private JLabel mLabelHighlights = null;
	private JLabel mLabelNodeTranslationDuration = null;

	private JPanel mPanelButton = null;
	private JButton mButtonSave = null;
	private JButton mButtonCancel = null;

	private String mNetworkInterval = null;
	private String mCalculationInterval = null;
	private String mCalculationIterations = null;
	private String mHighlightsTimeout = null;
	private String mNodeTranslationDuration = null;

	/**
	 * Calls the init() method to initialize the settings window
	 */
	public WindowSettings(SettingManager settingManager) {
		super();
		init(settingManager);
		pack();
	}

	/**
	 * Initializes a settings window
	 * @param settingManager
	 */
	private void init(SettingManager settingManager) {
		LOGGER.trace("Init Settings Window");
		initWindow(settingManager);
		loadSettings();
		initInputFields();
		initSaveButton();
		initCancelButton();
		setElements();
		addElements();
	}

	/**
	 * Initializes the main components
	 * @param settingManager
	 */
	private void initWindow(SettingManager settingManager) {
		mSettingManager = settingManager;
		mPanel = new JPanel();
		mSpringLayout = new SpringLayout();
		setTitle("Timing Settings");
		setMinimumSize(new Dimension(200, 195));
		setPreferredSize(new Dimension(290, 195));
	}

	/**
	 * Loads previous saved settings
	 */
	private void loadSettings() {
		mNetworkInterval = Integer.toString(mSettingManager.getNetworkInterval());
		mCalculationInterval = Integer.toString(mSettingManager.getCalculationInterval());
		mCalculationIterations = Integer.toString(mSettingManager.getCalculationIterations());
		mHighlightsTimeout = Integer.toString(mSettingManager.getHighlightsTimeout());
		mNodeTranslationDuration = Integer.toString(mSettingManager.getNodeTranslationDuration());
	}

	/**
	 * Initializes the input fields and their labels
	 */
	private void initInputFields() {
		mInputNetworkInterval = new JTextField(mNetworkInterval, 10);
		mLabelNetwork = new JLabel("Network Interval");
		mInputCalculationInterval = new JTextField(mCalculationInterval, 10);
		mLabelCalculation = new JLabel("Calculation Interval");
		mInputCalculationIterations = new JTextField(mCalculationIterations, 10);
		mLabelIterations = new JLabel("Calculation Iterations");
		mInputHighlightsTimeout = new JTextField(mHighlightsTimeout, 10);
		mLabelHighlights = new JLabel("Highlights Timeout");
		mInputNodeTranslationDuration = new JTextField(mNodeTranslationDuration, 10);
		mLabelNodeTranslationDuration = new JLabel("Translation Duration");
	}

	/**
	 * Initializes the save button and adds an ActionListener
	 */
	private void initSaveButton() {
		mButtonSave = new JButton("Save");
		mButtonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean hasChanged = false;
				int vNetworkInterval = -1;
				int vCalculationInterval = -1;
				int vCalculationIterations = -1;
				int vHighlightsTimeout = -1;
				int vNodeTranslationDuration = -1;

				if (!mInputNetworkInterval.getText().equals(mNetworkInterval)) {
					try {
						vNetworkInterval = Integer.parseInt(mInputNetworkInterval.getText());
						hasChanged = true;
					} catch (NumberFormatException ex) {
						mInputNetworkInterval.setText(mNetworkInterval);
					}
				}
				if (!mInputCalculationInterval.getText().equals(mCalculationInterval)) {
					try {
						vCalculationInterval = Integer.parseInt(mInputCalculationInterval.getText());
						hasChanged = true;
					} catch (NumberFormatException ex) {
						mInputCalculationInterval.setText(mCalculationInterval);
					}
				}
				if (!mInputCalculationIterations.getText().equals(mCalculationIterations)) {
					try {
						vCalculationIterations = Integer.parseInt(mInputCalculationIterations.getText());
						hasChanged = true;
					} catch (NumberFormatException ex) {
						mInputCalculationIterations.setText(mCalculationIterations);
					}
				}
				if (!mInputHighlightsTimeout.getText().equals(mHighlightsTimeout)) {
					try {
						vHighlightsTimeout = Integer.parseInt(mInputHighlightsTimeout.getText());
						hasChanged = true;
					} catch (NumberFormatException ex) {
						mInputHighlightsTimeout.setText(mHighlightsTimeout);
					}
				}
				if (!mInputNodeTranslationDuration.getText().equals(mNodeTranslationDuration)) {
					try {
						vNodeTranslationDuration = Integer.parseInt(mInputNodeTranslationDuration.getText());
						hasChanged = true;
					} catch (NumberFormatException ex) {
						mInputNodeTranslationDuration.setText(mNodeTranslationDuration);
					}
				}

				if (hasChanged) {
					LOGGER.debug("Save changed window settings.");
					if (vNetworkInterval > 0) {
						mSettingManager.setNetworkInterval(vNetworkInterval);
						mNetworkInterval = mInputNetworkInterval.getText();
					}
					if (vCalculationInterval > 0) {
						mSettingManager.setCalculationInterval(vCalculationInterval);
						mCalculationInterval = mInputCalculationInterval.getText();
					}
					if (vCalculationIterations > 0) {
						mSettingManager.setCalculationIterations(vCalculationIterations);
						mCalculationIterations = mInputCalculationIterations.getText();
					}
					if (vHighlightsTimeout >= 0) {
						mSettingManager.setHighlightsTimeout(vHighlightsTimeout);
						mHighlightsTimeout = mInputHighlightsTimeout.getText();
					}
					if (vNodeTranslationDuration >= 0) {
						mSettingManager.setNodeTranslationDuration(vNodeTranslationDuration);
						mNodeTranslationDuration = mInputNodeTranslationDuration.getText();
					}
					mSettingManager.notifyObservers();
				}
				setVisible(false);
			}
		});

	}

	/**
	 * Initializes the cancel button and adds an ActionListener
	 */
	private void initCancelButton() {
		mButtonCancel = new JButton("Cancel");
		mButtonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mInputNetworkInterval.setText(mNetworkInterval);
				mInputCalculationInterval.setText(mCalculationInterval);
				mInputCalculationIterations.setText(mCalculationIterations);
				mInputHighlightsTimeout.setText(mHighlightsTimeout);
				mInputNodeTranslationDuration.setText(mNodeTranslationDuration);
				setVisible(false);
			}
		});
	}

	/**
	 * Arranges the elements in a SpringLayout
	 */
	private void setElements() {
		mPanel.setLayout(mSpringLayout);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mLabelNetwork, 10, SpringLayout.NORTH, mPanel);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mInputNetworkInterval, 5, SpringLayout.NORTH, mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST, mLabelNetwork, 5, SpringLayout.WEST, mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST, mInputNetworkInterval, 5, SpringLayout.EAST, mLabelIterations);
		mSpringLayout.putConstraint(SpringLayout.EAST, mInputNetworkInterval, -5, SpringLayout.EAST, mPanel);

		mSpringLayout.putConstraint(SpringLayout.NORTH, mLabelCalculation, 10, SpringLayout.SOUTH,
				mInputNetworkInterval);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mInputCalculationInterval, 5, SpringLayout.SOUTH,
				mInputNetworkInterval);
		mSpringLayout.putConstraint(SpringLayout.WEST, mLabelCalculation, 5, SpringLayout.WEST, mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST, mInputCalculationInterval, 5, SpringLayout.EAST,
				mLabelIterations);
		mSpringLayout.putConstraint(SpringLayout.EAST, mInputCalculationInterval, -5, SpringLayout.EAST, mPanel);

		mSpringLayout.putConstraint(SpringLayout.NORTH, mLabelIterations, 10, SpringLayout.SOUTH,
				mInputCalculationInterval);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mInputCalculationIterations, 5, SpringLayout.SOUTH,
				mInputCalculationInterval);
		mSpringLayout.putConstraint(SpringLayout.WEST, mLabelIterations, 5, SpringLayout.WEST, mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST, mInputCalculationIterations, 5, SpringLayout.EAST,
				mLabelIterations);
		mSpringLayout.putConstraint(SpringLayout.EAST, mInputCalculationIterations, -5, SpringLayout.EAST, mPanel);

		mSpringLayout.putConstraint(SpringLayout.NORTH, mLabelHighlights, 10, SpringLayout.SOUTH,
				mInputCalculationIterations);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mInputHighlightsTimeout, 5, SpringLayout.SOUTH,
				mInputCalculationIterations);
		mSpringLayout.putConstraint(SpringLayout.WEST, mLabelHighlights, 5, SpringLayout.WEST, mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST, mInputHighlightsTimeout, 5, SpringLayout.EAST, mLabelIterations);
		mSpringLayout.putConstraint(SpringLayout.EAST, mInputHighlightsTimeout, -5, SpringLayout.EAST, mPanel);

		mSpringLayout.putConstraint(SpringLayout.NORTH, mLabelNodeTranslationDuration, 10, SpringLayout.SOUTH,
				mInputHighlightsTimeout);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mInputNodeTranslationDuration, 5, SpringLayout.SOUTH,
				mInputHighlightsTimeout);
		mSpringLayout.putConstraint(SpringLayout.WEST, mLabelNodeTranslationDuration, 5, SpringLayout.WEST, mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST, mInputNodeTranslationDuration, 5, SpringLayout.EAST,
				mLabelIterations);
		mSpringLayout.putConstraint(SpringLayout.EAST, mInputNodeTranslationDuration, -5, SpringLayout.EAST, mPanel);

		mPanelButton = new JPanel();
		mSpringLayout.putConstraint(SpringLayout.NORTH, mPanelButton, 5, SpringLayout.SOUTH,
				mInputNodeTranslationDuration);
		mSpringLayout.putConstraint(SpringLayout.WEST, mPanelButton, 5, SpringLayout.WEST, mPanel);
		mSpringLayout.putConstraint(SpringLayout.EAST, mPanel, 5, SpringLayout.EAST, mPanelButton);
	}

	/**
	 * Adds the elements to the frame
	 */
	private void addElements() {
		mPanel.add(mLabelNetwork);
		mPanel.add(mInputNetworkInterval);
		mPanel.add(mLabelCalculation);
		mPanel.add(mInputCalculationInterval);
		mPanel.add(mLabelIterations);
		mPanel.add(mInputCalculationIterations);
		mPanel.add(mLabelHighlights);
		mPanel.add(mInputHighlightsTimeout);
		mPanel.add(mLabelNodeTranslationDuration);
		mPanel.add(mInputNodeTranslationDuration);
		mPanel.add(mPanelButton);
		mPanelButton.add(mButtonSave);
		mPanelButton.add(mButtonCancel);
		add(mPanel);
	}
}
