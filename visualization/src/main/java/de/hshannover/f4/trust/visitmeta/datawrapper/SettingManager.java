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
 * This file is part of visitmeta-visualization, version 0.4.2,
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
package de.hshannover.f4.trust.visitmeta.datawrapper;

import java.util.Observable;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

/**
 * Manage the settings of the application.
 */
public class SettingManager extends Observable {
	private static final Logger LOGGER = Logger.getLogger(SettingManager.class);

	private static final Properties mConfig = Main.getConfig();

	private int mNetworkInterval = 0;
	private int mCalculationInterval = 0;
	private int mCalculationIterations = 0;
	private int mHighlightsTimeout = 0;
	private int mNodeTranslationDuration = 0;

	private GraphContainer mConnection = null;

	public SettingManager(GraphContainer connection) {
		LOGGER.debug("Load settings.");
		mConnection = connection;
		mConnection.getName();
		mNetworkInterval = mConfig.getInt(VisualizationConfig.KEY_NETWORK_INTERVAL,
				VisualizationConfig.DEFAULT_VALUE_NETWORK_INTERVAL);
		mCalculationInterval = mConfig.getInt(
				VisualizationConfig.KEY_CALCULATION_INTERVAL, VisualizationConfig.DEFAULT_VALUE_CALCULATION_INTERVAL);
		mCalculationIterations =
				mConfig.getInt(
						VisualizationConfig.KEY_CALCUCATION_ITERATIONS,
						VisualizationConfig.DEFAULT_VALUE_CALCUCATION_ITERATIONS);
		mHighlightsTimeout = mConfig.getInt(VisualizationConfig.KEY_HIGHTLIGHT_TIMEOUT,
				VisualizationConfig.DEFAULT_VALUE_HIGHTLIGHT_TIMEOUT);
		mNodeTranslationDuration =
				mConfig.getInt(
						VisualizationConfig.KEY_NODE_TRANSLATION_DURATION,
						VisualizationConfig.DEFAULT_VALUE_NODE_TRANSLATION_DURATION);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		LOGGER.debug("Save settings.");
		mConfig.set(VisualizationConfig.KEY_NETWORK_INTERVAL, mNetworkInterval);
		mConfig.set(VisualizationConfig.KEY_CALCULATION_INTERVAL, mCalculationInterval);
		mConfig.set(VisualizationConfig.KEY_CALCUCATION_ITERATIONS,
				mCalculationIterations);
		mConfig.set(VisualizationConfig.KEY_HIGHTLIGHT_TIMEOUT, mHighlightsTimeout);
		mConfig.set(VisualizationConfig.KEY_NODE_TRANSLATION_DURATION,
				mNodeTranslationDuration);
	}

	public synchronized int getNetworkInterval() {
		LOGGER.trace("Method getNetworkInterval() called.");
		return mNetworkInterval;
	}

	public synchronized int getCalculationInterval() {
		LOGGER.trace("Method getCalculationInterval() called.");
		return mCalculationInterval;
	}

	public synchronized int getCalculationIterations() {
		LOGGER.trace("Method getCalculationIterations() called.");
		return mCalculationIterations;
	}

	public synchronized int getHighlightsTimeout() {
		LOGGER.trace("Method getHighlightsTimeout() called.");
		return mHighlightsTimeout;
	}

	public int getNodeTranslationDuration() {
		LOGGER.trace("Method getNodeTranslationDuration() called.");
		return mNodeTranslationDuration;
	}

	public synchronized void setNetworkInterval(int pNetworkInterval) {
		LOGGER.trace("Method setNetworkInterval("
				+ pNetworkInterval
				+ ") called.");
		mNetworkInterval = pNetworkInterval;
		setChanged();
	}

	public synchronized void setCalculationInterval(int pCalculationInterval) {
		LOGGER.trace("Method setCalculationInterval("
				+ pCalculationInterval
				+ ") called.");
		mCalculationInterval = pCalculationInterval;
		setChanged();
	}

	public synchronized void setCalculationIterations(int pCalculationIterations) {
		LOGGER.trace("Method setCalculationIterations("
				+ pCalculationIterations + ") called.");
		mCalculationIterations = pCalculationIterations;
		setChanged();
	}

	public synchronized void setHighlightsTimeout(int pHighlightsTimeout) {
		LOGGER.trace("Method setHighlightsTimeout("
				+ pHighlightsTimeout
				+ ") called.");
		mHighlightsTimeout = pHighlightsTimeout;
		setChanged();
	}

	public void setNodeTranslationDuration(int pNodeTranslationDuration) {
		LOGGER.trace("Method setNodeTranslationDuration("
				+ pNodeTranslationDuration + ") called.");
		mNodeTranslationDuration = pNodeTranslationDuration;
		setChanged();
	}

}
