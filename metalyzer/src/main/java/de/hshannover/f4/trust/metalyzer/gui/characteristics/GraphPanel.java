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
package de.hshannover.f4.trust.metalyzer.gui.characteristics;

import java.awt.FlowLayout;

import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.charts.ChartPanelFactory.ChartType;
import de.hshannover.f4.trust.metalyzer.gui.event.ChartAdjustmentHandler;
import de.hshannover.f4.trust.metalyzer.gui.labels.CharacteristicLabels;
import de.hshannover.f4.trust.metalyzer.gui.views.TimeSelectionView.TimeSelection;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class GraphPanel extends StatisticPanel {

	public GraphPanel(MetalyzerGuiController guiController, String characteristic) {
		super(guiController, characteristic, false);
		
		if(characteristic.equals(CharacteristicLabels.GRAPH_NODES)) {
			mLabelView.addLabel("Mean", "Mean of nodes:", 0);
			mLabelView.addLabel("Deviation", "Standard deviation:", 0);
		} else 	if(characteristic.equals(CharacteristicLabels.GRAPH_EDGES)) {
			mLabelView.addLabel("Mean", "Mean of edges:", 0);
			mLabelView.addLabel("Deviation", "Standard deviation:", 0);
		} else 	if(characteristic.equals(CharacteristicLabels.GRAPH_MEAN)) {
			mLabelView.addLabel("Mean", "Mean of Metadata:", 0);
			mLabelView.addLabel("Deviation", "Standard deviation:", 0);
		}
	
		this.getTimeSelectionView().hideTimeSelectors(TimeSelection.Intervall);
	}
	
	public GraphPanel(MetalyzerGuiController guiController, String characteristic, ChartType chart) {
		super(guiController, characteristic, true);
		
		mChartView.addChart(0, chart);
		mChartView.setLayoutAlignment(FlowLayout.CENTER);
		mChartView.setCategoryLabelRotation(30);
		mChartView.showChartScrollBar(0, true);
		mChartView.addChartAdjustmentListener(new ChartAdjustmentHandler(guiController, this, 10));
		
		if(characteristic.equals(CharacteristicLabels.GRAPH_NODES)) {
			mLabelView.addLabel("Mean", "Mean of nodes:", 0);
			mLabelView.addLabel("Deviation", "Standard deviation:", 0);
		} else 	if(characteristic.equals(CharacteristicLabels.GRAPH_EDGES)) {
			mLabelView.addLabel("Mean", "Mean of edges:", 0);
			mLabelView.addLabel("Deviation", "Standard deviation:", 0);
		} else 	if(characteristic.equals(CharacteristicLabels.GRAPH_MEAN)) {
			mLabelView.addLabel("Mean", "Mean of Metadata:", 0);
			mLabelView.addLabel("Deviation", "Standard deviation:", 0);
		}
		
		this.getTimeSelectionView().hideTimeSelectors(TimeSelection.Intervall);	
	}

	/**
	 * Sets the text of the mean label
	 * @param mean of nodes as text
	 * */
	public void setGraphMean(String meanOfNodes) {
		mLabelView.setValueById("Mean", meanOfNodes);	
	}

	/**
	 * Sets the text of the standard deviation label
	 * @param standard deviation as text
	 * */
	public void setGraphStandardDeviation(String standardDeviationOfNodes) {
		mLabelView.setValueById("Deviation", standardDeviationOfNodes);	
	}

}