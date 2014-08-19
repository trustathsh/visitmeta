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

import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.AnalysePanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.StatisticPanel;
import de.hshannover.f4.trust.metalyzer.gui.network.AnalyseConnection;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public abstract class StatisticControl implements Control {

	protected AnalyseConnection mConnection;
	protected MetalyzerGuiController mGuiController;

	public StatisticControl(MetalyzerGuiController guiController) {
		this.mGuiController = guiController;
		this.mConnection = mGuiController.getConnection();
	}
	
	/**
	 * Updates the given {@link StatisticPanel}
	 * to adjust the scroll area that is modified by the {@link ChartAdjustmentHandler}
	 * @param statisticPanel the panel that should be updated e.g. {@link GraphPanel}
	 * @param lowerBoundary the minimum value that is allowed for the chart scrollbar, depends on the scroll range
	 * @param upperBoundary the maximum value that is allowed for the chart scrollbar, depends on the scroll range
	 * */
	public abstract void updateChartScrollArea(StatisticPanel statisticPanel, int lowerBoundary, int upperBoundary);
	
	@Override
	public abstract void fillControl(AnalysePanel panel, long t1, long t2);

	@Override
	public abstract void updateControl(AnalysePanel panel, long t1, long t2);

}
