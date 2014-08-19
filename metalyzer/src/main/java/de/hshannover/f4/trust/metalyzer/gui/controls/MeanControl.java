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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.AnalysePanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.MeanPanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.StatisticPanel;
import de.hshannover.f4.trust.metalyzer.gui.labels.CharacteristicLabels;
import de.hshannover.f4.trust.metalyzer.gui.views.TimeSelectionView;
import de.hshannover.f4.trust.metalyzer.statistic.MeanType;

/**
 * Project: Metalyzer Author: Daniel Hülse Last Change: by: $Author: $ date:
 * $Date: $ Copyright (c): Hochschule Hannover
 */

public class MeanControl extends StatisticControl {

	private static final Logger log = Logger.getLogger(MeanControl.class);
	private HashMap<String, Double> means = new HashMap<String, Double>();
	private MeanPanel mMeanPanel;
	private TimeSelectionView mTimeSelectionView;

	public MeanControl(MetalyzerGuiController guiController) {
		super(guiController);
		log.debug("MeanControl initialized");
	}

	@Override
	public void fillControl(AnalysePanel panel, long t1, long t2) {
		updateControl(panel, t1, t2);
	}

	/**
	 * Fills the mean components of the StatisticPanel
	 * 
	 * @param statisticPanel
	 * @param t1
	 * @param t2
	 */
	@Override
	public void updateControl(AnalysePanel panel, long t1, long t2) {
		this.mMeanPanel = (MeanPanel) panel;
		this.mTimeSelectionView = mMeanPanel.getTimeSelectionView();
		String characteristic = mMeanPanel.getCharacteristic();
		try {
			if (characteristic.equals(CharacteristicLabels.MEAN_LINKS)) {

				double meanOfAll = 0;
				double meanOfMacAddresses = 0;
				double meanOfAccessRequests = 0;
				double meanOfIdentities = 0;
				double meanOfIpAddresses = 0;
				double meanOfDevices = 0;
				
				switch (mTimeSelectionView.getSelectionMode()) {
					case Intervall:
						meanOfAll = mConnection.getMeanFromTo(t1, t2,StandardIdentifierType.ALL, MeanType.LFI);
						meanOfIpAddresses = mConnection.getMeanFromTo(t1, t2,StandardIdentifierType.IP_ADDRESS, MeanType.LFI);
						meanOfMacAddresses = mConnection.getMeanFromTo(t1, t2,StandardIdentifierType.MAC_ADDRESS, MeanType.LFI);
						meanOfDevices = mConnection.getMeanFromTo(t1, t2,StandardIdentifierType.DEVICE, MeanType.LFI);
						meanOfAccessRequests = mConnection.getMeanFromTo(t1, t2,StandardIdentifierType.ACCESS_REQUEST,MeanType.LFI);
						meanOfIdentities = mConnection.getMeanFromTo(t1, t2,StandardIdentifierType.IDENTITY, MeanType.LFI);
						break;
					case Live:
						meanOfAll = mConnection.getMeanCurrent(StandardIdentifierType.ALL, MeanType.LFI);
						meanOfIpAddresses = mConnection.getMeanCurrent(StandardIdentifierType.IP_ADDRESS, MeanType.LFI);
						meanOfMacAddresses = mConnection.getMeanCurrent(StandardIdentifierType.MAC_ADDRESS, MeanType.LFI);
						meanOfDevices = mConnection.getMeanCurrent(StandardIdentifierType.DEVICE, MeanType.LFI);
						meanOfAccessRequests = mConnection.getMeanCurrent(StandardIdentifierType.ACCESS_REQUEST,MeanType.LFI);
						meanOfIdentities = mConnection.getMeanCurrent(StandardIdentifierType.IDENTITY, MeanType.LFI);
						break;
					case Timestamp:
						meanOfAll = mConnection.getMeanTimestamp(t2,StandardIdentifierType.ALL, MeanType.LFI);
						meanOfIpAddresses = mConnection.getMeanTimestamp(t2,StandardIdentifierType.IP_ADDRESS, MeanType.LFI);
						meanOfMacAddresses = mConnection.getMeanTimestamp(t2,StandardIdentifierType.MAC_ADDRESS, MeanType.LFI);
						meanOfDevices = mConnection.getMeanTimestamp(t2,StandardIdentifierType.DEVICE, MeanType.LFI);
						meanOfAccessRequests = mConnection.getMeanTimestamp(t2,StandardIdentifierType.ACCESS_REQUEST,MeanType.LFI);
						meanOfIdentities = mConnection.getMeanTimestamp(t2,StandardIdentifierType.IDENTITY, MeanType.LFI);
						break;
					default:
						break;
				}

				means.put("All", meanOfAll);
				means.put("IP-Adresses", meanOfIpAddresses);
				means.put("Mac-Addresses", meanOfMacAddresses);
				means.put("Devices", meanOfDevices);
				means.put("Access-Requests", meanOfAccessRequests);
				means.put("Identities", meanOfIdentities);
			}
		} catch (ExecutionException e) {
			log.error("Connection could not be established or the data for the "
					+ "requested method is not available");
		} catch (TimeoutException e) {
			log.error("Request timeout exceeded");
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}

		fillMeanData(mMeanPanel, means);
		mMeanPanel.setVisible(true);
	}

	/**
	 * Sets the data of the chart and properites table of the
	 * {@link StatisticPanel}
	 * 
	 * @param statisticPanel
	 *            the instance of the {@link StatisticPanel}, that data is
	 *            assigned
	 * @param means
	 * @param statisticData
	 *            the statistical data for the chart and the properties table
	 */
	private void fillMeanData(MeanPanel meanPanel, HashMap<String, Double> means) {
		if (means != null) {
			meanPanel.clearProperties();
			for (String label : means.keySet()) {
				meanPanel.addPropertyData(label, means.get(label));
			}
			meanPanel.setPropertyPrecsion(1, 2, 3);
		} else {
			log.debug("Statisitic data is null");
		}
	}

	@Override
	public void updateChartScrollArea(StatisticPanel statisticPanel,
			int lowerBoundary, int upperBoundary) {

	}
}
