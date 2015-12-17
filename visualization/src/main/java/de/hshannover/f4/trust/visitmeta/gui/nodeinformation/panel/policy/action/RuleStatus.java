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
package de.hshannover.f4.trust.visitmeta.gui.nodeinformation.panel.policy.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.piccolo2d.Piccolo2DPolicyActionGraphicWrapper.RuleStatusEnum;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.piccolo2d.PolicyActionGraphicWrapper;
import de.hshannover.f4.trust.visitmeta.gui.dialog.LayoutHelper;

public class RuleStatus extends JPanel {

	private static final long serialVersionUID = -5535418108978820158L;

	public static final String LABEL_TRUE_COUNT = "true-Count:";

	public static final String LABEL_FALSE_COUNT = "false-Count:";

	public static final String LABEL_LAST_FIRED_DEVICE = "last fired Device:";

	public static final String LABEL_STATUS_OLD = "OLD";

	private PolicyActionGraphicWrapper mSelectedNode;

	private JLabel mjlStatus;

	private JLabel mjlTrueCount;

	private JLabel mjlTrueCountValue;

	private JLabel mjlFalseCount;

	private JLabel mjlFalseCountValue;

	private JLabel mjlLastFiredDevice;
	
	private JLabel mjlLastFiredDeviceValue;

	private JLabel mjlOldStatus;

	private DevicePanelRule mDevicePanel;


	public RuleStatus(PolicyActionGraphicWrapper selectedNode) {
		mSelectedNode = selectedNode;

		initPanel();
		initJLabels();
		initDevicePanel();

		if (!mSelectedNode.isCurrentRuleResult()) {
			mjlOldStatus.setVisible(true);
		}
	}

	private void initPanel() {
		super.setLayout(new GridBagLayout());
		super.setBorder(BorderFactory.createTitledBorder("Rule-Status"));
	}

	private void initJLabels() {
		mjlStatus = buildStatusLabel();
		mjlTrueCount = new JLabel(LABEL_TRUE_COUNT);
		mjlFalseCount = new JLabel(LABEL_FALSE_COUNT);
		mjlLastFiredDevice = new JLabel(LABEL_LAST_FIRED_DEVICE);
		mjlOldStatus = new JLabel(LABEL_STATUS_OLD, SwingConstants.CENTER);
		mjlOldStatus.setVisible(false);
		mjlOldStatus.setForeground(Color.RED);
		setBOLD(mjlOldStatus);

		mjlTrueCountValue = new JLabel(Integer.toString(mSelectedNode.getRuleTrueCount()));
		setBOLD(mjlTrueCountValue);
		mjlFalseCountValue = new JLabel(Integer.toString(mSelectedNode.getRuleFalseCount()));
		setBOLD(mjlFalseCountValue);
		mjlLastFiredDeviceValue = new JLabel(mSelectedNode.getRuleLastFiredDevice());
		setBOLD(mjlLastFiredDeviceValue);

		LayoutHelper.addComponent(0, 0, 2, 1, 0.0, 0.0, this, mjlStatus, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 1, 1, 1, 0.0, 0.0, this, mjlTrueCount, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 1, 1, 1, 1.0, 0.0, this, mjlTrueCountValue, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 2, 1, 1, 0.0, 0.0, this, mjlFalseCount, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 2, 1, 1, 1.0, 0.0, this, mjlFalseCountValue, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 3, 1, 1, 0.0, 0.0, this, mjlLastFiredDevice, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 3, 1, 1, 1.0, 0.0, this, mjlLastFiredDeviceValue, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 4, 2, 1, 0.0, 0.0, this, mjlOldStatus, LayoutHelper.LABEL_INSETS);
	}

	private JLabel buildStatusLabel() {
		JLabel status = new JLabel();
		status.setHorizontalAlignment(SwingConstants.CENTER);
		status.setFont(new Font(status.getFont().getFontName(), Font.BOLD, status.getFont().getSize()));

		RuleStatusEnum ruleStatus = mSelectedNode.getRuleStatus();

		if (RuleStatusEnum.CHANGED_TRUE == ruleStatus) {
			status.setText("changed");
			status.setForeground(new Color(59, 186, 63));
		} else if (RuleStatusEnum.CHANGED_FALSE == ruleStatus) {
			status.setText("changed");
			status.setForeground(Color.RED);
		} else if (RuleStatusEnum.SAME_TRUE == ruleStatus) {
			status.setText("same");
			status.setForeground(new Color(59, 186, 63));
		} else if (RuleStatusEnum.SAME_FALSE == ruleStatus) {
			status.setText("same");
			status.setForeground(Color.RED);
		}

		return status;
	}

	private void initDevicePanel() {
		mDevicePanel = new DevicePanelRule(mSelectedNode);

		LayoutHelper.addComponent(0, 5, 2, 1, 1.0, 1.0, this, mDevicePanel, LayoutHelper.LABEL_INSETS);
	}

	private void setBOLD(JLabel label) {
		label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
	}
}
