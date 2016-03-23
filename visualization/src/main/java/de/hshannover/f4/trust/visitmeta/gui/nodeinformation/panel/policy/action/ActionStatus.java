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
package de.hshannover.f4.trust.visitmeta.gui.nodeinformation.panel.policy.action;

import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.piccolo2d.PolicyActionGraphicWrapper;
import de.hshannover.f4.trust.visitmeta.gui.dialog.LayoutHelper;

public class ActionStatus extends JPanel {

	private static final long serialVersionUID = -5535418108978820158L;

	public static final String LABEL_ACTION_COUNT = "Action-Count:";

	public static final String LABEL_LAST_EXECUTED_DEVICE = "Last executed Device:";

	private PolicyActionGraphicWrapper mSelectedNode;

	private JLabel mjlActionCount;

	private JLabel mjlActionCountValue;

	private JLabel mjlLastExecutedDevice;
	
	private JLabel mjlLastExecutedDeviceValue;

	private JPanel mjlWildcard;

	private JPanel mjlWildcard2;

	private DevicePanelAction mDevicePanel;


	public ActionStatus(PolicyActionGraphicWrapper selectedNode) {
		mSelectedNode = selectedNode;

		initPanel();
		initJLabels();
		initDevicePanel();
	}

	private void initPanel() {
		super.setLayout(new GridBagLayout());
		super.setBorder(BorderFactory.createTitledBorder("Action-Status"));
	}

	private void initJLabels() {

		mjlActionCount = new JLabel(LABEL_ACTION_COUNT);
		mjlLastExecutedDevice = new JLabel(LABEL_LAST_EXECUTED_DEVICE);

		mjlWildcard = new JPanel();
		mjlWildcard2 = new JPanel();

		mjlActionCountValue = new JLabel(Integer.toString(mSelectedNode.getActionTrueCount()));
		setBOLD(mjlActionCountValue);
		mjlLastExecutedDeviceValue = new JLabel(mSelectedNode.getActionLastExecutedDevice());
		setBOLD(mjlLastExecutedDeviceValue);

		LayoutHelper.addComponent(0, 0, 1, 1, 0.0, 0.0, this, mjlActionCount, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 0, 1, 1, 1.0, 0.0, this, mjlActionCountValue, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 1, 1, 1, 0.0, 0.0, this, mjlLastExecutedDevice, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 1, 1, 1, 1.0, 0.0, this, mjlLastExecutedDeviceValue, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 2, 1, 1, 0.0, 1.0, this, mjlWildcard, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 4, 1, 1, 0.0, 1.0, this, mjlWildcard2, LayoutHelper.LABEL_INSETS);
	}

	private void initDevicePanel() {
		mDevicePanel = new DevicePanelAction(mSelectedNode);

		LayoutHelper.addComponent(0, 3, 2, 1, 1.0, 0.0, this, mDevicePanel, LayoutHelper.LABEL_INSETS);
	}

	private void setBOLD(JLabel label) {
		label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
	}

}
