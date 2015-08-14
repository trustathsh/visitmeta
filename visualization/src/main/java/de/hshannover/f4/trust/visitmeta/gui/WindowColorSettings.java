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
 * This file is part of visitmeta-visualization, version 0.5.1,
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

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeType;
import de.hshannover.f4.trust.visitmeta.graphDrawer.ColorHelper;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

public class WindowColorSettings extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger
			.getLogger(WindowColorSettings.class);
	private static final Properties mConfig = Main.getConfig();
	private JPanel mPanel = null;
	private JColorChooser mColorChooser = null;
	private Timer mDelay = null;
	private JComboBox<String> mSelectPublisher = null;
	private ButtonGroup mGroup = null;
	private List<String> mPublisher = null;
	private SpringLayout mSpringLayout = null;
	private ConnectionTab mConnection = null;

	public WindowColorSettings(ConnectionTab connection) {
		super();
		mPanel = new JPanel();
		mConnection = connection;
		mPublisher = mConnection.getPublisher();

		setTitle("Color Settings");
		mSpringLayout = new SpringLayout();
		setMinimumSize(new Dimension(570, 270));
		setPreferredSize(new Dimension(650, 300));

		mSelectPublisher = new JComboBox<String>(
				mPublisher.toArray(new String[0]));
		mSelectPublisher.addItem("identifier: access-request");
		mSelectPublisher.addItem("identifier: device");
		mSelectPublisher.addItem("identifier: identity");
		mSelectPublisher.addItem("identifier: ip-address");
		mSelectPublisher.addItem("identifier: mac-address");
		mSelectPublisher.addItem("identifier: extended");
		mSelectPublisher.addItem("default metadata");
		mSelectPublisher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				ButtonModel vButton = mGroup.getElements().nextElement()
						.getModel();
				mGroup.setSelected(vButton, true);
				setColorChooserColor();
			}
		});

		mDelay = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				if (mSelectPublisher.getItemCount() > 0) {
					String vParam = mGroup.getSelection().getActionCommand();
					NodeType vType;
					String vPublisher = "";
					String vProperty = "";
					if (mSelectPublisher.getSelectedIndex() == 0) {
						/*
						 * First item is the identifier color for
						 * access-requests
						 */
						vType = NodeType.IDENTIFIER;
						vProperty = "color."
								+ vType.toString().toLowerCase()
								+ "." + IfmapStrings.ACCESS_REQUEST_EL_NAME
								+ "." + vParam;
					} else if (mSelectPublisher.getSelectedIndex() == 1) {
						/* Second item is the identifier color for devices */
						vType = NodeType.IDENTIFIER;
						vProperty = "color."
								+ vType.toString().toLowerCase()
								+ "." + IfmapStrings.DEVICE_EL_NAME + "."
								+ vParam;
					} else if (mSelectPublisher.getSelectedIndex() == 2) {
						/* Third item is the identifier color for identities */
						vType = NodeType.IDENTIFIER;
						vProperty = "color."
								+ vType.toString().toLowerCase()
								+ "." + IfmapStrings.IDENTITY_EL_NAME + "."
								+ vParam;
					} else if (mSelectPublisher.getSelectedIndex() == 3) {
						/* Fourth item is the identifier color for ip-addresses */
						vType = NodeType.IDENTIFIER;
						vProperty = "color."
								+ vType.toString().toLowerCase()
								+ "." + IfmapStrings.IP_ADDRESS_EL_NAME + "."
								+ vParam;
					} else if (mSelectPublisher.getSelectedIndex() == 4) {
						/* Fifth item is the identifier color for mac-addresses */
						vType = NodeType.IDENTIFIER;
						vProperty = "color."
								+ vType.toString().toLowerCase()
								+ "." + IfmapStrings.MAC_ADDRESS_EL_NAME + "."
								+ vParam;
					} else if (mSelectPublisher.getSelectedIndex() == 5) {
						/*
						 * Sixth item is the identifier color for
						 * extended-identifiers
						 */
						vType = NodeType.IDENTIFIER;
						vProperty = "color."
								+ vType.toString().toLowerCase()
								+ ".extended." + vParam;
					} else if (mSelectPublisher.getSelectedIndex() == 6) {
						/* Seventh item is the default metadata color */
						vType = NodeType.METADATA;
						vProperty = "color."
								+ vType.toString().toLowerCase()
								+ "." + vParam;
					} else {
						vType = NodeType.METADATA;
						vPublisher = cleanUpPublisherString((String) mSelectPublisher
								.getSelectedItem());
						vProperty = "color."
								+ vPublisher + "." + vParam;
					}
					String vColor = "0x"
							+ Integer.toHexString(
									mColorChooser.getColor().getRGB())
									.substring(2);
					try {
						mConfig.set(vProperty, vColor);
						ColorHelper.propertyChanged(vProperty, vColor);
					} catch (PropertyException e) {
						LOGGER.fatal(e.toString(), e);
						throw new RuntimeException("could not save property");
					}
					mConnection.getConnection().repaintNodes(vType);
				}
				mDelay.stop();
			}

		});

		/* Color Chooser */
		mColorChooser = new JColorChooser(Color.BLACK);
		mColorChooser.setPreviewPanel(new JPanel());
		mColorChooser.getSelectionModel().addChangeListener(
				new ChangeListener() {
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
		mSpringLayout.putConstraint(SpringLayout.NORTH, mSelectPublisher, 5,
				SpringLayout.NORTH, mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST, mSelectPublisher, 5,
				SpringLayout.WEST, mPanel);
		mSpringLayout.putConstraint(SpringLayout.NORTH, mColorChooser, 5,
				SpringLayout.SOUTH, mSelectPublisher);
		mSpringLayout.putConstraint(SpringLayout.EAST, mColorChooser, -5,
				SpringLayout.EAST, mPanel);
		mSpringLayout.putConstraint(SpringLayout.WEST, mColorChooser, 20,
				SpringLayout.EAST, vColorOutside);
		mSpringLayout.putConstraint(SpringLayout.SOUTH, mColorChooser, -5,
				SpringLayout.SOUTH, mPanel);

		mSpringLayout.putConstraint(SpringLayout.NORTH, vColorInside, 5,
				SpringLayout.SOUTH, mSelectPublisher);
		mSpringLayout.putConstraint(SpringLayout.NORTH, vColorOutside, 5,
				SpringLayout.SOUTH, vColorInside);
		mSpringLayout.putConstraint(SpringLayout.NORTH, vColorText, 5,
				SpringLayout.SOUTH, vColorOutside);
		mSpringLayout.putConstraint(SpringLayout.NORTH, vColorBorder, 5,
				SpringLayout.SOUTH, vColorText);

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

	private String cleanUpPublisherString(String selectedItem) {
		if (selectedItem != null) {
			if (selectedItem.contains("Identifier:")) {
				return selectedItem.replace("Identifier: ", "identifier.");
			} else if (selectedItem.contains("Default Metadata")) {
				return selectedItem.replace("Default Metadata", "metadata");
			} else if (selectedItem.contains("Publisher-ID:")) {
				return selectedItem.replace("Publisher-ID: ", "");
			} else {
				return selectedItem;
			}
		} else {
			return "";
		}
	}

	/**
	 * Updates the list of known publishers.
	 */
	public void updateWindow() {
		LOGGER.trace("Method updateWindow() called.");
		mSelectPublisher.removeAllItems();
		mSelectPublisher.addItem("Identifier: access-request");
		mSelectPublisher.addItem("Identifier: device");
		mSelectPublisher.addItem("Identifier: identity");
		mSelectPublisher.addItem("Identifier: ip-address");
		mSelectPublisher.addItem("Identifier: mac-address");
		mSelectPublisher.addItem("Identifier: extended");
		mSelectPublisher.addItem("Default Metadata");
		for (String s : mPublisher) {
			mSelectPublisher.addItem("Publisher-ID: "
					+ s);
		}
	}

	/**
	 * Set the displayed color of the ColorChooser.
	 */
	private void setColorChooserColor() {
		LOGGER.trace("Method setColorChooserColor() called.");
		ButtonModel vButton = mGroup.getSelection();

		String selectedItem = cleanUpPublisherString((String) mSelectPublisher
				.getSelectedItem());
		String actionCommand = vButton.getActionCommand();

		String vKey = "color."
				+ selectedItem + "." + actionCommand;
		String vDefault = mConfig.getString(VisualizationConfig.KEY_COLOR_METADATA_PREFIX
				+ actionCommand,
				VisualizationConfig.DEFAULT_VALUE_COLOR_METADATA);
		Color vColor = Color.decode(mConfig.getString(vKey, vDefault));
		mColorChooser.setColor(vColor);
		mDelay.stop();
	}

	@Override
	public void actionPerformed(ActionEvent pE) {
		setColorChooserColor();
	}
}
