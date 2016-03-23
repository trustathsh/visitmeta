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
import javax.swing.SwingConstants;

import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.piccolo2d.PolicyActionGraphicWrapper;
import de.hshannover.f4.trust.visitmeta.gui.dialog.LayoutHelper;
import de.hshannover.f4.trust.visitmeta.gui.nodeinformation.panel.ExtendedNodeInformationPanel;

public class PolicyActionExtendedNodeInformation extends ExtendedNodeInformationPanel {

	private static final long serialVersionUID = -1783049896574768484L;

	private PolicyActionGraphicWrapper mSelectedNode;

	private JPanel mRuleStatus;

	private JPanel mDeviceOverview;

	private JPanel mActionStatus;

	private JPanel mRuleResults;

	private JPanel mjpWildcard;

	private JPanel mjpWildcard2;

	private JLabel mjlTimeStamp;

	/**
	 * 
	 * @param selectedNode
	 * @throws IllegalArgumentException if is a wrong GraphicWrapper for the selected-node. Must be an
	 *             Piccolo2DPolicyActionGraphicWrapper.
	 */
	public PolicyActionExtendedNodeInformation(GraphicWrapper selectedNode) throws IllegalArgumentException {
		if (!(selectedNode instanceof PolicyActionGraphicWrapper)) {
			throw new IllegalArgumentException("Wrong GraphicWrapper for the selected-node");
		}

		mSelectedNode = (PolicyActionGraphicWrapper) selectedNode;

		initPanel();
		initRuleStatus();

	}

	private void initPanel() {
		super.setLayout(new GridBagLayout());
		super.setBorder(BorderFactory.createTitledBorder("policy-action Metadata"));
	}

	private void initRuleStatus() {
		mRuleStatus = new RuleStatus(mSelectedNode);
		mDeviceOverview = new DeviceOverview(mSelectedNode);
		mActionStatus = new ActionStatus(mSelectedNode);
		mRuleResults = new RuleResults(mSelectedNode);
		mjpWildcard = new JPanel();
		mjpWildcard2 = new JPanel();

		mjlTimeStamp = new JLabel(mSelectedNode.getTimeStampDefaultFormat(), SwingConstants.CENTER);
		mjlTimeStamp.setEnabled(false);
		setBOLD(mjlTimeStamp);

		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 0.0, this, mjpWildcard, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 0, 1, 1, 0.0, 0.0, this, mRuleStatus, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(2, 0, 1, 1, 0.0, 0.0, this, mDeviceOverview, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(3, 0, 1, 1, 0.0, 0.0, this, mActionStatus, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(4, 0, 1, 1, 0.0, 0.0, this, mRuleResults, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(5, 0, 1, 1, 1.0, 0.0, this, mjpWildcard2, LayoutHelper.LABEL_INSETS);

		LayoutHelper.addComponent(1, 1, 4, 1, 1.0, 0.0, this, mjlTimeStamp, LayoutHelper.LABEL_INSETS);
	}

	private void setBOLD(JLabel label) {
		label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
	}

}
