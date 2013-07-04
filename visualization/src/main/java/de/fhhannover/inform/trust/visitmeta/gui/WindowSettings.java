package de.fhhannover.inform.trust.visitmeta.gui;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * This file is part of VisITMeta, version 0.0.1, implemented by the Trust@FHH 
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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

import de.fhhannover.inform.trust.visitmeta.datawrapper.SettingManager;

public class WindowSettings extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(WindowSettings.class);
	private GuiController  mController                   = null;
	private SettingManager mSettingManager               = null;
	private JPanel         mPanel                        = null;
	private SpringLayout   mSpringLayout                 = null;
	private JTextField     mInputNetworkInterval         = null;
	private JTextField     mInputCalculationInterval     = null;
	private JTextField     mInputCalculationIterations   = null;
	private JTextField     mInputHighlightsTimeout       = null;
	private JTextField     mInputNodeTranslationDuration = null;
	private JButton        mButtonSave                   = null;
	private JButton        mButtonCancel                 = null;

	private String mNetworkInterval         = null;
	private String mCalculationInterval     = null;
	private String mCalculationIterations   = null;
	private String mHighlightsTimeout       = null;
	private String mNodeTranslationDuration = null;

	public WindowSettings(GuiController pController) {
		mController     = pController;
		mSettingManager = SettingManager.getInstance();
		mPanel          = new JPanel();
		mSpringLayout   = new SpringLayout();
		setTitle("Timing Settings");
		setMinimumSize(new Dimension(200, 195));
		setPreferredSize(new Dimension(290, 195));
		/* Get default settings */
		mNetworkInterval         = Integer.toString(mSettingManager.getNetworkInterval());
		mCalculationInterval     = Integer.toString(mSettingManager.getCalculationInterval());
		mCalculationIterations   = Integer.toString(mSettingManager.getCalculationIterations());
		mHighlightsTimeout       = Integer.toString(mSettingManager.getHighlightsTimeout());
		mNodeTranslationDuration = Integer.toString(mSettingManager.getNodeTranslationDuration());
		/* Text input and labels */
		mInputNetworkInterval                = new JTextField(mNetworkInterval, 10);
		JLabel vLabelNetwork                 = new JLabel("Network Interval");
		mInputCalculationInterval            = new JTextField(mCalculationInterval, 10);
		JLabel vLabelCalculation             = new JLabel("Calculation Interval");
		mInputCalculationIterations          = new JTextField(mCalculationIterations, 10);
		JLabel vLabelIterations              = new JLabel("Calculation Iterations");
		mInputHighlightsTimeout              = new JTextField(mHighlightsTimeout, 10);
		JLabel vLabelHighlights              = new JLabel("Highlights Timeout");
		mInputNodeTranslationDuration        = new JTextField(mNodeTranslationDuration, 10);
		JLabel vLabelNodeTranslationDuration = new JLabel("Translation Duration");
		/* Save and Cancel buttons */
		mButtonSave = new JButton("Save");
		mButtonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean hasChanged           = false;
				int vNetworkInterval         = -1;
				int vCalculationInterval     = -1;
				int vCalculationIterations   = -1;
				int vHighlightsTimeout       = -1;
				int vNodeTranslationDuration = -1;
				/* Try to parse input */
				if(!mInputNetworkInterval.getText().equals(mNetworkInterval)) {
					try {
						vNetworkInterval = Integer.parseInt(mInputNetworkInterval.getText());
						hasChanged = true;
					} catch (NumberFormatException ex) {
						mInputNetworkInterval.setText(mNetworkInterval);
					}
				}
				if(!mInputCalculationInterval.getText().equals(mCalculationInterval)) {
					try {
						vCalculationInterval = Integer.parseInt(mInputCalculationInterval.getText());
						hasChanged = true;
					} catch (NumberFormatException ex) {
						mInputCalculationInterval.setText(mCalculationInterval);
					}
				}
				if(!mInputCalculationIterations.getText().equals(mCalculationIterations)) {
					try {
						vCalculationIterations = Integer.parseInt(mInputCalculationIterations.getText());
						hasChanged = true;
					} catch (NumberFormatException ex) {
						mInputCalculationIterations.setText(mCalculationIterations);
					}
				}
				if(!mInputHighlightsTimeout.getText().equals(mHighlightsTimeout)) {
					try {
						vHighlightsTimeout = Integer.parseInt(mInputHighlightsTimeout.getText());
						hasChanged = true;
					} catch (NumberFormatException ex) {
						mInputHighlightsTimeout.setText(mHighlightsTimeout);
					}
				}
				if(!mInputNodeTranslationDuration.getText().equals(mNodeTranslationDuration)) {
					try {
						vNodeTranslationDuration = Integer.parseInt(mInputNodeTranslationDuration.getText());
						hasChanged = true;
					} catch (NumberFormatException ex) {
						mInputNodeTranslationDuration.setText(mNodeTranslationDuration);
					}
				}
				/* Save changes and notify observers */
				if(hasChanged) {
					if(vNetworkInterval > 0) {
						mSettingManager.setNetworkInterval(vNetworkInterval);
						mNetworkInterval = mInputNetworkInterval.getText();
					}
					if(vCalculationInterval > 0) {
						mSettingManager.setCalculationInterval(vCalculationInterval);
						mCalculationInterval= mInputCalculationInterval.getText();
					}
					if(vCalculationIterations > 0) {
						mSettingManager.setCalculationIterations(vCalculationIterations);
						mCalculationIterations = mInputCalculationIterations.getText();
					}
					if(vHighlightsTimeout >= 0) {
						mSettingManager.setHighlightsTimeout(vHighlightsTimeout);
						mHighlightsTimeout = mInputHighlightsTimeout.getText();
					}
					if(vNodeTranslationDuration >= 0) {
						mSettingManager.setNodeTranslationDuration(vNodeTranslationDuration);
						mNodeTranslationDuration = mInputNodeTranslationDuration.getText();
					}
					mSettingManager.notifyObservers();
				}
				setVisible(false);
			}
		});
		mButtonCancel = new JButton("Cancel");
		mButtonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/* Restore previous values */
				mInputNetworkInterval.setText(mNetworkInterval);
				mInputCalculationInterval.setText(mCalculationInterval);
				mInputCalculationIterations.setText(mCalculationIterations);
				mInputHighlightsTimeout.setText(mHighlightsTimeout);
				mInputNodeTranslationDuration.setText(mNodeTranslationDuration);
				setVisible(false);
			}
		});
		/* Arrange elements in layout */
		mPanel.setLayout(mSpringLayout);
		mSpringLayout.putConstraint(SpringLayout.NORTH, vLabelNetwork,         10, SpringLayout.NORTH, mPanel);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mInputNetworkInterval,  5, SpringLayout.NORTH, mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST,  vLabelNetwork,          5, SpringLayout.WEST,  mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST,  mInputNetworkInterval,  5, SpringLayout.EAST,  vLabelIterations);
		mSpringLayout.putConstraint(SpringLayout.EAST,  mInputNetworkInterval, -5, SpringLayout.EAST,  mPanel);

		mSpringLayout.putConstraint(SpringLayout.NORTH, vLabelCalculation,         10, SpringLayout.SOUTH, mInputNetworkInterval);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mInputCalculationInterval,  5, SpringLayout.SOUTH, mInputNetworkInterval);
		mSpringLayout.putConstraint(SpringLayout.WEST,  vLabelCalculation,          5, SpringLayout.WEST,  mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST,  mInputCalculationInterval,  5, SpringLayout.EAST,  vLabelIterations);
		mSpringLayout.putConstraint(SpringLayout.EAST,  mInputCalculationInterval, -5, SpringLayout.EAST,  mPanel);

		mSpringLayout.putConstraint(SpringLayout.NORTH, vLabelIterations,            10, SpringLayout.SOUTH, mInputCalculationInterval);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mInputCalculationIterations,  5, SpringLayout.SOUTH, mInputCalculationInterval);
		mSpringLayout.putConstraint(SpringLayout.WEST,  vLabelIterations,             5, SpringLayout.WEST,  mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST,  mInputCalculationIterations,  5, SpringLayout.EAST,  vLabelIterations);
		mSpringLayout.putConstraint(SpringLayout.EAST,  mInputCalculationIterations, -5, SpringLayout.EAST,  mPanel);

		mSpringLayout.putConstraint(SpringLayout.NORTH, vLabelHighlights,        10, SpringLayout.SOUTH, mInputCalculationIterations);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mInputHighlightsTimeout,  5, SpringLayout.SOUTH, mInputCalculationIterations);
		mSpringLayout.putConstraint(SpringLayout.WEST,  vLabelHighlights,         5, SpringLayout.WEST,  mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST,  mInputHighlightsTimeout,  5, SpringLayout.EAST,  vLabelIterations);
		mSpringLayout.putConstraint(SpringLayout.EAST,  mInputHighlightsTimeout, -5, SpringLayout.EAST,  mPanel);

		mSpringLayout.putConstraint(SpringLayout.NORTH, vLabelNodeTranslationDuration, 10, SpringLayout.SOUTH, mInputHighlightsTimeout);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mInputNodeTranslationDuration,  5, SpringLayout.SOUTH, mInputHighlightsTimeout);
		mSpringLayout.putConstraint(SpringLayout.WEST,  vLabelNodeTranslationDuration,  5, SpringLayout.WEST,  mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST,  mInputNodeTranslationDuration,  5, SpringLayout.EAST,  vLabelIterations);
		mSpringLayout.putConstraint(SpringLayout.EAST,  mInputNodeTranslationDuration, -5, SpringLayout.EAST,  mPanel);

		JPanel vPanelButton = new JPanel();
		mSpringLayout.putConstraint(SpringLayout.NORTH, vPanelButton,  5, SpringLayout.SOUTH, mInputNodeTranslationDuration);
		mSpringLayout.putConstraint(SpringLayout.WEST,  vPanelButton,  5, SpringLayout.WEST,  mPanel);
		mSpringLayout.putConstraint(SpringLayout.EAST,  mPanel,        5, SpringLayout.EAST,  vPanelButton);
		/* Add elements to panel*/
		mPanel.add(vLabelNetwork);
		mPanel.add(mInputNetworkInterval);
		mPanel.add(vLabelCalculation);
		mPanel.add(mInputCalculationInterval);
		mPanel.add(vLabelIterations);
		mPanel.add(mInputCalculationIterations);
		mPanel.add(vLabelHighlights);
		mPanel.add(mInputHighlightsTimeout);
		mPanel.add(vLabelNodeTranslationDuration);
		mPanel.add(mInputNodeTranslationDuration);
		mPanel.add(vPanelButton);
		vPanelButton.add(mButtonSave);
		vPanelButton.add(mButtonCancel);
//		mPanel.add(mButtonSave);
//		mPanel.add(mButtonCancel);
		add(mPanel);
		pack();
	}
}
