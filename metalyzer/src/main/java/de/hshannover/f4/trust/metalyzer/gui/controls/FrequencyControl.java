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
 * This file is part of visitmeta metalyzer, version 0.0.1,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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
package de.hshannover.f4.trust.metalyzer.gui.controls;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.AnalysePanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.FrequencyPanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.StatisticPanel;
import de.hshannover.f4.trust.metalyzer.gui.labels.CharacteristicLabels;
import de.hshannover.f4.trust.metalyzer.gui.views.TimeSelectionView;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencySelection;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencyType;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class FrequencyControl extends StatisticControl {

	private static final Logger log = Logger.getLogger(FrequencyControl.class);
	private Map<String, Map<String, Double>> frequencyData;
	private Map<String, Double> absoluteFrequency = null;
	private Map<String, Double> relativeFrequency = null;
	private FrequencyPanel mFrequencyPanel;
	private TimeSelectionView mTimeSelectionView;

	public FrequencyControl(MetalyzerGuiController guiController) {
		super(guiController);
		frequencyData = new HashMap<String, Map<String, Double>>();
		log.debug("FrequencyControl initialized");
	}

	@Override
	public void fillControl(AnalysePanel panel, long t1, long t2) {
		updateControl(panel, t1, t2);
	}

	/**
	 * Fills the frequency components of the StatisticPanel
	 * 
	 * @param statisticPanel
	 * @param t1
	 * @param t2
	 */

	@Override
	public void updateControl(AnalysePanel panel, long t1, long t2) {
		this.mFrequencyPanel = (FrequencyPanel) panel;
		this.mTimeSelectionView = mFrequencyPanel.getTimeSelectionView();
		String characteristic = mFrequencyPanel.getCharacteristic();
		try {
			if (characteristic.equals(CharacteristicLabels.FREQUENCY_IDENTIFER)) {
				switch (mTimeSelectionView.getSelectionMode()) {
					case Intervall:
						relativeFrequency = mConnection.getFrequencyFromTo(t1, t2, FrequencyType.IDENTIFIER,FrequencySelection.RELATIVE_FREQUENCY);
						absoluteFrequency = mConnection.getFrequencyFromTo(t1, t2, FrequencyType.IDENTIFIER,FrequencySelection.ABSOLUTE_FREQUENCY);
						break;
					case Live:
						relativeFrequency = mConnection.getFrequencyCurrent(FrequencyType.IDENTIFIER,FrequencySelection.RELATIVE_FREQUENCY);
						absoluteFrequency = mConnection.getFrequencyCurrent(FrequencyType.IDENTIFIER,FrequencySelection.ABSOLUTE_FREQUENCY);
						break;
					case Timestamp:
						relativeFrequency = mConnection.getFrequencyTimestamp(t2, FrequencyType.IDENTIFIER,FrequencySelection.RELATIVE_FREQUENCY);
						absoluteFrequency = mConnection.getFrequencyTimestamp(t2, FrequencyType.IDENTIFIER,FrequencySelection.ABSOLUTE_FREQUENCY);
						break;
					default:
						break;
				}
			}
			if (characteristic.equals(CharacteristicLabels.FREQUENCY_METADATA)) {
				switch (mTimeSelectionView.getSelectionMode()) {
					case Intervall:
						relativeFrequency = mConnection.getFrequencyFromTo(t1, t2, FrequencyType.METADATA,FrequencySelection.RELATIVE_FREQUENCY);
						absoluteFrequency = mConnection.getFrequencyFromTo(t1, t2, FrequencyType.METADATA,FrequencySelection.ABSOLUTE_FREQUENCY);
						break;
					case Live:
						relativeFrequency = mConnection.getFrequencyCurrent(FrequencyType.METADATA,FrequencySelection.RELATIVE_FREQUENCY);
						absoluteFrequency = mConnection.getFrequencyCurrent(FrequencyType.METADATA,FrequencySelection.ABSOLUTE_FREQUENCY);
						break;
					case Timestamp:
						relativeFrequency = mConnection.getFrequencyTimestamp(t2, FrequencyType.METADATA,FrequencySelection.RELATIVE_FREQUENCY);
						absoluteFrequency = mConnection.getFrequencyTimestamp(t2, FrequencyType.METADATA,FrequencySelection.ABSOLUTE_FREQUENCY);
						break;
					default:
						break;
				}
			}
			if (characteristic.equals(CharacteristicLabels.FREQUENCY_ROLES)) {
				switch (mTimeSelectionView.getSelectionMode()) {
					case Intervall:
						relativeFrequency = mConnection.getFrequencyFromTo(t1, t2, FrequencyType.ROLES,FrequencySelection.RELATIVE_FREQUENCY);
						absoluteFrequency = mConnection.getFrequencyFromTo(t1, t2, FrequencyType.ROLES,FrequencySelection.ABSOLUTE_FREQUENCY);
						break;
					case Live:
						relativeFrequency = mConnection.getFrequencyCurrent(FrequencyType.ROLES,FrequencySelection.RELATIVE_FREQUENCY);
						absoluteFrequency = mConnection.getFrequencyCurrent(FrequencyType.ROLES,FrequencySelection.ABSOLUTE_FREQUENCY);
						break;
					case Timestamp:
						relativeFrequency = mConnection.getFrequencyTimestamp(t2, FrequencyType.ROLES,FrequencySelection.RELATIVE_FREQUENCY);
						absoluteFrequency = mConnection.getFrequencyTimestamp(t2, FrequencyType.ROLES,FrequencySelection.ABSOLUTE_FREQUENCY);
						break;
					default:
						break;
				}
			}
			frequencyData.put("relFrequency", relativeFrequency);
			frequencyData.put("absFrequency", absoluteFrequency);
		} catch (ExecutionException e) {
			log.error("Connection could not be established or the data for the "
					+ "requested method is not available");
		} catch (TimeoutException e) {
			log.error("Request timeout exceeded");
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
		fillFrequencyData(mFrequencyPanel, frequencyData);
		mFrequencyPanel.setVisible(true);

	}

	private void fillFrequencyData(FrequencyPanel frequencyPanel,
			Map<String, Map<String, Double>> frequencies) {
		if (frequencies != null) {
			frequencyPanel.clearProperties();
			if (frequencyPanel.isChartPanelEnabled()) {
				frequencyPanel.setChartValues(0,
						frequencies.get("absFrequency"));
				frequencyPanel.setChartValues(1,
						frequencies.get("relFrequency"));
			}
			Map<String, Double> absFrequency = frequencies.get("absFrequency");
			Map<String, Double> relFrequency = frequencies.get("relFrequency");

			for (String col1 : relFrequency.keySet()) {
				Double abs = absFrequency.get(col1);
				Double rel = relFrequency.get(col1);
				frequencyPanel.addPropertyData(col1, abs, rel);
			}
			frequencies.clear();
			frequencyPanel.setPropertyPercantage(2, 1, 2);

		} else {
			log.debug("Statisitic data is null");
		}
	}

	@Override
	public void updateChartScrollArea(StatisticPanel statisticPanel,
			int lowerBoundary, int upperBoundary) {
		// TODO Auto-generated method stub
		
	}
}
