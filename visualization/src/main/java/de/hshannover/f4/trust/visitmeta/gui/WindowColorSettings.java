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
 * This file is part of visitmeta visualization, version 0.0.3,
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;

/**
 * A Window for defining color settings of different publishers.
 */
public class WindowColorSettings extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(WindowColorSettings.class);
	private GuiController mController = null;
	private JPanel mPanel = null;
	private JColorChooser mColorChooser = null;
	private Timer mDelay = null;
	private JComboBox<String> mSelectPublisher = null;
	private ButtonGroup mGroup = null;
	private List<String> mPublisher = null;
	private SpringLayout mSpringLayout = null;

	/**
	 * 
	 * @param pController
	 */
	public WindowColorSettings(GuiController controller) {
		super();
		mController = controller;
		mPanel = new JPanel();
		mPublisher = mController.getSelectedConnection().getPublisher();

		setTitle("Color Settings");
		mSpringLayout = new SpringLayout();
		setMinimumSize(new Dimension(570, 270));
		setPreferredSize(new Dimension(650, 300));

		/* publisher select box */
		mSelectPublisher = new JComboBox<String>(mPublisher.toArray(new String[0]));
		mSelectPublisher.addItem("identifier");
		mSelectPublisher.addItem("default metadata");
		mSelectPublisher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				ButtonModel vButton = mGroup.getElements().nextElement().getModel();
				mGroup.setSelected(vButton, true);
				setColorChooserColor();
			}
		});

		mDelay = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				if (mSelectPublisher.getItemCount() > 0) {
					String vParam = mGroup.getSelection().getActionCommand();
					String vType = "";
					String vPublisher = "";
					String vProperty = "";
					if (mSelectPublisher.getSelectedIndex() == 0) {
						/* First item is the default identifier color */
						vType = "identifier";
						vProperty = "color." + vType + "." + vParam;
					} else if (mSelectPublisher.getSelectedIndex() == 1) {
						/* Second item is the default metadata color */
						vType = "metadata";
						vProperty = "color." + vType + "." + vParam;
					} else {
						vType = "metadata";
						vPublisher = (String) mSelectPublisher.getSelectedItem();
						vProperty = "color." + vPublisher + "." + vParam;
					}
					String vColor = "0x" + Integer.toHexString(mColorChooser.getColor().getRGB()).substring(2);
					PropertiesManager.storeProperty("color", vProperty, vColor);
					mController.getSelectedConnection().repaintNodes(vType, vPublisher);
				}
				mDelay.stop();
			}
		});

		/* Color Chooser */
		mColorChooser = new JColorChooser(Color.BLACK);
		mColorChooser.setPreviewPanel(new JPanel());
		mColorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent pE) {
				mDelay.stop();
				mDelay.start();
			}
		});

		/* Radio buttons */
		JRadioButton vColorInside = new JRadioButton("Inside");
		vColorInside.setActionCommand("inside");
		JRadioButton vColorOutside = new JRadioButton("Outside");
		vColorOutside.setActionCommand("outside");
		JRadioButton vColorText = new JRadioButton("Text");
		vColorText.setActionCommand("text");
		JRadioButton vColorBorder = new JRadioButton("Border");
		vColorBorder.setActionCommand("border");

		/* Group the radio buttons */
		mGroup = new ButtonGroup();
		mGroup.add(vColorInside);
		mGroup.add(vColorOutside);
		mGroup.add(vColorText);
		mGroup.add(vColorBorder);

		vColorInside.addActionListener(this);
		vColorOutside.addActionListener(this);
		vColorText.addActionListener(this);
		vColorBorder.addActionListener(this);

		/* Arrange elements in layout */
		mPanel.setLayout(mSpringLayout);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mSelectPublisher, 5, SpringLayout.NORTH, mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST, mSelectPublisher, 5, SpringLayout.WEST, mPanel);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mColorChooser, 5, SpringLayout.SOUTH, mSelectPublisher);
		mSpringLayout.putConstraint(SpringLayout.EAST, mColorChooser, -5, SpringLayout.EAST, mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST, mColorChooser, 20, SpringLayout.EAST, vColorOutside);
		mSpringLayout.putConstraint(SpringLayout.SOUTH, mColorChooser, -5, SpringLayout.SOUTH, mPanel);

		mSpringLayout.putConstraint(SpringLayout.NORTH, vColorInside, 5, SpringLayout.SOUTH, mSelectPublisher);
		mSpringLayout.putConstraint(SpringLayout.NORTH, vColorOutside, 5, SpringLayout.SOUTH, vColorInside);
		mSpringLayout.putConstraint(SpringLayout.NORTH, vColorText, 5, SpringLayout.SOUTH, vColorOutside);
		mSpringLayout.putConstraint(SpringLayout.NORTH, vColorBorder, 5, SpringLayout.SOUTH, vColorText);

		/* Add elements to panel */
		mPanel.add(mSelectPublisher);
		mPanel.add(mColorChooser);
		mPanel.add(vColorInside);
		mPanel.add(vColorOutside);
		mPanel.add(vColorText);
		mPanel.add(vColorBorder);
		add(mPanel);
		pack();
	}

	/**
	 * Updates the list of known publishers.
	 */
	public void updateWindow() {
		LOGGER.trace("Method updateWindow() called.");
		mSelectPublisher.removeAllItems();
		mSelectPublisher.addItem("identifier");
		mSelectPublisher.addItem("default metadata");
		for (String s : mPublisher) {
			mSelectPublisher.addItem(s);
		}
	}

	/**
	 * Set the displayed color of the ColorChooser.
	 */
	private void setColorChooserColor() {
		LOGGER.trace("Method setColorChooserColor() called.");
		ButtonModel vButton = mGroup.getSelection();
		String vKey = "color." + mSelectPublisher.getSelectedItem() + "." + vButton.getActionCommand();
		String vDefault = PropertiesManager.getProperty("color", "color.metadata." + vButton.getActionCommand(),
				"0xFFFFFF");
		Color vColor = Color.decode(PropertiesManager.getProperty("color", vKey, vDefault));
		mColorChooser.setColor(vColor);
		mDelay.stop();
	}

	@Override
	public void actionPerformed(ActionEvent pE) {
		setColorChooserColor();
	}
}
