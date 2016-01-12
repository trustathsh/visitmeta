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

public class DevicePanelRule extends JPanel {

	private static final long serialVersionUID = 1224028342347312989L;

	public static final String LABEL_TRUE_COUNT = "TRUE-Count";

	public static final String LABEL_FALSE_COUNT = "FALSE-Count";

	public static final String LABEL_STATUS_OLD = "OLD";

	private PolicyActionGraphicWrapper mSelectedNode;

	private JLabel mjlStatus;

	private JLabel mjlTrueCount;

	private JLabel mjlTrueCountValue;

	private JLabel mjlFalseCount;

	private JLabel mjlFalseCountValue;

	private JLabel mjlOldStatus;

	public DevicePanelRule(PolicyActionGraphicWrapper selectedNode) {
		mSelectedNode = selectedNode;

		initPanel();
		initJLabels();
		
		if (!mSelectedNode.isCurrentRuleResultForDevice()) {
			mjlOldStatus.setVisible(true);
		}
	}

	private void initPanel() {
		super.setLayout(new GridBagLayout());
		super.setBorder(BorderFactory.createTitledBorder("Device: " + mSelectedNode.getDevice()));
	}

	private void setBOLD(JLabel label) {
		label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));

	}

	private void initJLabels() {
		setRuleStatus();
		mjlTrueCount = new JLabel(LABEL_TRUE_COUNT, SwingConstants.CENTER);
		mjlTrueCount.setEnabled(false);
		mjlFalseCount = new JLabel(LABEL_FALSE_COUNT, SwingConstants.CENTER);
		mjlFalseCount.setEnabled(false);
		mjlOldStatus = new JLabel(LABEL_STATUS_OLD, SwingConstants.CENTER);
		mjlOldStatus.setVisible(false);
		mjlOldStatus.setForeground(Color.RED);
		setBOLD(mjlOldStatus);

		mjlTrueCountValue = new JLabel(Integer.toString(mSelectedNode.getRuleTrueCountForDevice()),
				SwingConstants.CENTER);
		setBOLD(mjlTrueCountValue);
		mjlFalseCountValue = new JLabel(Integer.toString(mSelectedNode.getRuleFalseCountForDevice()),
				SwingConstants.CENTER);
		setBOLD(mjlFalseCountValue);

		LayoutHelper.addComponent(0, 0, 2, 1, 0.0, 0.0, this, mjlStatus, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 1, 1, 1, 1.0, 0.0, this, mjlTrueCountValue, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 2, 1, 1, 0.0, 0.0, this, mjlTrueCount, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 1, 1, 1, 1.0, 0.0, this, mjlFalseCountValue, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 2, 1, 1, 0.0, 0.0, this, mjlFalseCount, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 3, 2, 1, 0.0, 0.0, this, mjlOldStatus, LayoutHelper.LABEL_INSETS);
	}

	private void setRuleStatus() {
		mjlStatus = new JLabel();
		mjlStatus.setHorizontalAlignment(SwingConstants.CENTER);
		mjlStatus.setFont(new Font(mjlStatus.getFont().getFontName(), Font.BOLD, mjlStatus.getFont().getSize()));

		RuleStatusEnum status = mSelectedNode.getRuleStatusForDevice();

		if (RuleStatusEnum.CHANGED_TRUE == status) {
			mjlStatus.setText("changed");
			mjlStatus.setForeground(new Color(59, 186, 63));
		} else if (RuleStatusEnum.CHANGED_FALSE == status) {
			mjlStatus.setText("changed");
			mjlStatus.setForeground(Color.RED);
		} else if (RuleStatusEnum.SAME_TRUE == status) {
			mjlStatus.setText("same");
			mjlStatus.setForeground(new Color(59, 186, 63));
		} else if (RuleStatusEnum.SAME_FALSE == status) {
			mjlStatus.setText("same");
			mjlStatus.setForeground(Color.RED);
		}
	}

}
