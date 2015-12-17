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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.piccolo2d.PolicyActionGraphicWrapper;
import de.hshannover.f4.trust.visitmeta.gui.dialog.LayoutHelper;

public class DevicePanelAction extends JPanel {

	private static final long serialVersionUID = 1224028342347312989L;

	public static final String LABEL_STATUS_OLD = "OLD";

	public static final String LABEL_ACTION_COUNT = "Action-Count";

	private PolicyActionGraphicWrapper mSelectedNode;

	private JLabel mjlActionCount;

	private JLabel mjlActionCountValue;

	private JLabel mjlOldStatus;

	private TitledBorder mTtitel;

	public DevicePanelAction(PolicyActionGraphicWrapper selectedNode) {
		mSelectedNode = selectedNode;

		initPanel();
		initJLabels();
		
		if (!mSelectedNode.isCurrentRuleResultForDevice()) {
			mjlOldStatus.setVisible(true);
		}

		super.setMinimumSize(new Dimension(mTtitel.getMinimumSize(this).width, super.getPreferredSize().width));
	}

	private void initPanel() {
		mTtitel = BorderFactory.createTitledBorder("Device: " + mSelectedNode.getDevice());

		super.setLayout(new GridBagLayout());
		super.setBorder(mTtitel);
	}

	private void setBOLD(JLabel label) {
		label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
	}

	private void initJLabels() {

		mjlActionCount = new JLabel(LABEL_ACTION_COUNT, SwingConstants.CENTER);
		mjlActionCount.setEnabled(false);
		mjlOldStatus = new JLabel(LABEL_STATUS_OLD, SwingConstants.CENTER);
		mjlOldStatus.setVisible(false);
		mjlOldStatus.setForeground(Color.RED);
		setBOLD(mjlOldStatus);

		mjlActionCountValue = new JLabel(Integer.toString(mSelectedNode.getActionTrueCountForDevice()),
				SwingConstants.CENTER);
		setBOLD(mjlActionCountValue);

		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 0.0, this, mjlActionCountValue, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 1, 1, 1, 1.0, 0.0, this, mjlActionCount, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 2, 1, 1, 0.0, 0.0, this, mjlOldStatus, LayoutHelper.LABEL_INSETS);
	}

}
