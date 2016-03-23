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
package de.hshannover.f4.trust.visitmeta.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;

public class GuiModeSelection {

	private Logger mLogger = Logger.getLogger(GuiModeSelection.class);
	
	private JPanel mPanel;
	
	private Map<JRadioButton, GuiMode> mRadioButtons;
	
	private GraphPanel mGraphPanel;

	public GuiModeSelection(GraphPanel graphPanel) {
		mGraphPanel = graphPanel;
		mPanel = new JPanel();
		mRadioButtons = new HashMap<>();
		
		JRadioButton stateSwitchAnalysisMode = new JRadioButton("Anaysis", true);
		stateSwitchAnalysisMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mGraphPanel.setGuiMode(GuiMode.ANALYSIS);
				mLogger.debug("Gui mode changed to " + GuiMode.ANALYSIS);
			}
		});
		JRadioButton stateSwitchMonitoringMode = new JRadioButton("Monitoring", false);
		stateSwitchMonitoringMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mGraphPanel.setGuiMode(GuiMode.MONITORING);
				mLogger.debug("Gui mode changed to " + GuiMode.MONITORING);
			}
		});

		mRadioButtons.put(stateSwitchAnalysisMode, GuiMode.ANALYSIS);
		mRadioButtons.put(stateSwitchMonitoringMode, GuiMode.MONITORING);
		
		ButtonGroup stateSwitchButtonGroup = new ButtonGroup();
		stateSwitchButtonGroup.add(stateSwitchAnalysisMode);
		stateSwitchButtonGroup.add(stateSwitchMonitoringMode);
		
		mPanel.add(stateSwitchAnalysisMode);
		mPanel.add(stateSwitchMonitoringMode);
	}
	
	public JPanel getJPanel() {
		return mPanel;
	}

	public GuiMode getSelectedMode() {
		for (JRadioButton button : mRadioButtons.keySet()) {
			if (button.isSelected()) {
				return mRadioButtons.get(button);
			}
		}
		
		return null;
	}
	
}
