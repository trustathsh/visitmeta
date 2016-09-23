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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import de.hshannover.f4.trust.visitmeta.datawrapper.policy.ConditionElement;
import de.hshannover.f4.trust.visitmeta.datawrapper.policy.ConditionElement.ConditionElementType;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.piccolo2d.PolicyActionGraphicWrapper;
import de.hshannover.f4.trust.visitmeta.gui.dialog.LayoutHelper;

public class RuleResults extends JPanel {


	private static final long serialVersionUID = 1908694561716598784L;

	public static final String LABEL_DEVICE = "Device:";

	private PolicyActionGraphicWrapper mSelectedNode;
	
	private JLabel mjlDevice;

	private JLabel mjlDeviceValue;

	private JScrollPane mjScpMain;

	private JPanel mjpScrollPaneMain;

	private JPanel mjpResultsMain;

	public RuleResults(PolicyActionGraphicWrapper selectedNode) {
		mSelectedNode = selectedNode;

		initPanel();
		initJPanels();
		initRuleResultPanel();

		mjScpMain.setMinimumSize(new Dimension(mjpResultsMain.getPreferredSize().width, 0));
	}

	private void initPanel() {
		super.setLayout(new GridBagLayout());
		super.setBorder(BorderFactory.createTitledBorder("Rule-Results"));
	}

	private void initJPanels() {
		mjlDevice = new JLabel(LABEL_DEVICE);
		mjlDeviceValue = new JLabel(mSelectedNode.getDevice());
		setBOLD(mjlDeviceValue);
		
		mjpResultsMain = new JPanel();
		mjpResultsMain.setLayout(new GridBagLayout());

		mjpScrollPaneMain = new JPanel();
		mjpScrollPaneMain.setLayout(new GridBagLayout());

		mjScpMain = new JScrollPane(mjpScrollPaneMain);

		LayoutHelper.addComponent(0, 0, 1, 1, 0.0, 0.0, mjpScrollPaneMain, mjpResultsMain, LayoutHelper.LABEL_INSETS);

		LayoutHelper.addComponent(0, 0, 1, 1, 0.0, 0.0, this, mjlDevice, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(1, 0, 1, 1, 1.0, 0.0, this, mjlDeviceValue, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(0, 1, 2, 1, 0.0, 1.0, this, mjScpMain, LayoutHelper.LABEL_INSETS);
	}

	private void initRuleResultPanel() {
		List<ConditionElement> results = mSelectedNode.getConditionElementResults();

		JLabel rule = new JLabel("Rule:");
		JLabel ruleId = new JLabel(mSelectedNode.getRuleId());
		JLabel arrowRule = new JLabel("->");
		setBOLD(arrowRule);
		JLabel ruleResult = buildLabelResult(mSelectedNode.getRuleResult());
		
		LayoutHelper.addComponent(0, 0, 1, 1, 0.0, 0.0, mjpResultsMain, rule, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(3, 0, 1, 1, 0.0, 0.0, mjpResultsMain, ruleId, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(4, 0, 1, 1, 0.0, 0.0, mjpResultsMain, arrowRule, LayoutHelper.LABEL_INSETS);
		LayoutHelper.addComponent(5, 0, 1, 1, 0.0, 0.0, mjpResultsMain, ruleResult, LayoutHelper.LABEL_INSETS);
		
		for(int i=1; i<results.size()+1;i++){
			ConditionElement result = results.get(i-1);
			
			if(ConditionElementType.SIGNATURE == result.type){
				JLabel signature = new JLabel("Signature:", SwingConstants.CENTER);
				JLabel signatureId = new JLabel(result.id);
				JLabel arrowSignature = new JLabel("->");
				setBOLD(arrowSignature);
				JLabel signatureResult = buildLabelResult(result.result);
				
				LayoutHelper.addComponent(1, i, 1, 1, 0.0, 0.0, mjpResultsMain, signature, LayoutHelper.LABEL_INSETS);
				LayoutHelper.addComponent(3, i, 1, 1, 0.0, 0.0, mjpResultsMain, signatureId, LayoutHelper.LABEL_INSETS);
				LayoutHelper.addComponent(4, i, 1, 1, 0.0, 0.0, mjpResultsMain, arrowSignature, LayoutHelper.LABEL_INSETS);
				LayoutHelper.addComponent(5, i, 1, 1, 0.0, 0.0, mjpResultsMain, signatureResult, LayoutHelper.LABEL_INSETS);
			}else if(ConditionElementType.ANOMALY == result.type){
				JLabel anomaly = new JLabel("Anomaly:", SwingConstants.CENTER);
				JLabel anomalyId = new JLabel(result.id);
				JLabel arrowAnomaly = new JLabel("->");
				setBOLD(arrowAnomaly);
				JLabel anomalyResult = buildLabelResult(result.result);
				
				LayoutHelper.addComponent(1, i, 1, 1, 0.0, 0.0, mjpResultsMain, anomaly, LayoutHelper.LABEL_INSETS);
				LayoutHelper.addComponent(3, i, 1, 1, 0.0, 0.0, mjpResultsMain, anomalyId, LayoutHelper.LABEL_INSETS);
				LayoutHelper.addComponent(4, i, 1, 1, 0.0, 0.0, mjpResultsMain, arrowAnomaly, LayoutHelper.LABEL_INSETS);
				LayoutHelper.addComponent(5, i, 1, 1, 0.0, 0.0, mjpResultsMain, anomalyResult, LayoutHelper.LABEL_INSETS);
				
				for (int j = 1; j < result.childs.size() + 1; j++) {
					ConditionElement resultHint = result.childs.get(j - 1);
				
					JLabel hint = new JLabel("Hint:", SwingConstants.CENTER);
					JLabel hintId = new JLabel(resultHint.id);
					JLabel arrowHint = new JLabel("->");
					setBOLD(arrowHint);
					JLabel hintResult = buildLabelResult(resultHint.result);

					LayoutHelper.addComponent(2, i + j, 1, 1, 0.0, 0.0, mjpResultsMain, hint, LayoutHelper.LABEL_INSETS);
					LayoutHelper.addComponent(3, i + j, 1, 1, 0.0, 0.0, mjpResultsMain, hintId, LayoutHelper.LABEL_INSETS);
					LayoutHelper.addComponent(4, i + j, 1, 1, 0.0, 0.0, mjpResultsMain, arrowHint, LayoutHelper.LABEL_INSETS);
					LayoutHelper.addComponent(5, i + j, 1, 1, 0.0, 0.0, mjpResultsMain, hintResult, LayoutHelper.LABEL_INSETS);
				}
			}
		}
	}

	private void setBOLD(JLabel label) {
		label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
	}

	private JLabel buildLabelResult(boolean result) {
		JLabel resultLabel = new JLabel(Boolean.toString(result));
		setBOLD(resultLabel);

		if (result) {
			resultLabel.setForeground(new Color(59, 186, 63));
		} else {
			resultLabel.setForeground(Color.RED);
		}

		return resultLabel;
	}
}
