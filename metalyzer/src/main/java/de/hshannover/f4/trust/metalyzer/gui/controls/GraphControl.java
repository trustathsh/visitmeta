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

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.AnalysePanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.GraphPanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.StatisticPanel;
import de.hshannover.f4.trust.metalyzer.gui.labels.CharacteristicLabels;
import de.hshannover.f4.trust.metalyzer.gui.misc.UtilHelper;
import de.hshannover.f4.trust.metalyzer.gui.views.TimeSelectionView.TimeSelection;
import de.hshannover.f4.trust.metalyzer.statistic.EdgeResult;
import de.hshannover.f4.trust.metalyzer.statistic.MeanOfEdgeResult;
import de.hshannover.f4.trust.metalyzer.statistic.NodeResult;
import de.hshannover.f4.trust.metalyzer.statistic.ResultObject;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class GraphControl extends StatisticControl {
	
	private static final Logger log = Logger.getLogger(GraphControl.class);
	
	private int mLowerBoundary;
	private int mUpperBoundary;
	private ArrayList<NodeResult> mNodeResultList;
	private ArrayList<EdgeResult> mEdgeResultList;
	private ArrayList<MeanOfEdgeResult> mMeanResultList;
	private GraphPanel mGraphPanel;

	public GraphControl(MetalyzerGuiController guiController) {
		super(guiController);
	}

	@Override
	public void fillControl(AnalysePanel panel, long t1, long t2) {
		updateControl(panel, t1, t2);
	}

	@Override
	public void updateControl(AnalysePanel panel, long t1, long t2) {
		this.mGraphPanel = (GraphPanel)panel;
		
		//Sets the selection on the only valid selction for this panel
		mGraphPanel.getTimeSelectionView().setSelectionMode(TimeSelection.Intervall);
	
		String characteristic = mGraphPanel.getCharacteristic();
		try {
			if (characteristic.equals(CharacteristicLabels.GRAPH_NODES)) {
				ResultObject<NodeResult> result = mConnection.getNodeCharecteristic(t1, t2);
				if(result != null) {
					mGraphPanel.setGraphMean(UtilHelper.getPrecsion(result.getMean(),1,3));
					mGraphPanel.setGraphStandardDeviation(UtilHelper.getPrecsion(result.getStandardDeviation(),1,3));
					mNodeResultList = result.getResultList();
					//sets the range for the scrollbar
					mGraphPanel.setScrollBoundaries(0, mNodeResultList.size());
					fillNodeData(mGraphPanel,  mNodeResultList);
				}
			} else if (characteristic.equals(CharacteristicLabels.GRAPH_EDGES)) {
				ResultObject<EdgeResult> result = mConnection.getEdgeCharecteristic(t1, t2);
				if(result != null) {
					mGraphPanel.setGraphMean(UtilHelper.getPrecsion(result.getMean(),1,3));
					mGraphPanel.setGraphStandardDeviation(UtilHelper.getPrecsion(result.getStandardDeviation(),1,3));
					mEdgeResultList = result.getResultList();
					mGraphPanel.setScrollBoundaries(0, mEdgeResultList.size());
					fillEdgeData(mGraphPanel,  mEdgeResultList);
				}
			} else if (characteristic.equals(CharacteristicLabels.GRAPH_MEAN)) {
				ResultObject<MeanOfEdgeResult> result = mConnection.getMeanOfEdgeCharecteristic(t1, t2);
				if(result != null) {
					mGraphPanel.setGraphMean(UtilHelper.getPrecsion(result.getMean(),1,3));
					mGraphPanel.setGraphStandardDeviation(UtilHelper.getPrecsion(result.getStandardDeviation(),1,3));
					mMeanResultList = result.getResultList();
					mGraphPanel.setScrollBoundaries(0, mMeanResultList.size());
					fillMeanData(mGraphPanel,  mMeanResultList);
				}
			}
		} catch (ExecutionException e) {
			log.error("Connection could not be established or the data for the "
					+ "requested method is not available");
		} catch (TimeoutException e) {
			log.error("Request timeout exceeded");
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
		updateChartScrollArea(mGraphPanel, mLowerBoundary, mUpperBoundary);
	}

	private void fillNodeData(GraphPanel graphPanel, ArrayList<NodeResult> resultList) {
		if(graphPanel != null) {
			graphPanel.clearProperties();
			graphPanel.setPropertyPrecsion(2, 3, 3);

			for(NodeResult nodeResult : resultList)  {
				String timestamp = UtilHelper.convertTimestampToDatestring(nodeResult.getTimestamp());
				long nodes = nodeResult.getNumbersOfIdentifer();
				graphPanel.addPropertyData(timestamp, nodes, nodeResult.getDeviation());
			}
		}
	}
	
	private void fillEdgeData(GraphPanel graphPanel, ArrayList<EdgeResult> resultList) {
		if(graphPanel != null) {
			graphPanel.clearProperties();
			graphPanel.setPropertyPrecsion(2, 3, 3);
			
			for(EdgeResult edgeResult : resultList)  {
				String timestamp = UtilHelper.convertTimestampToDatestring(edgeResult.getTimestamp());
				long edges = edgeResult.getNumbersOfEgde();
				graphPanel.addPropertyData(timestamp, edges, edgeResult.getDeviation());
			}
		}
	}
	
	private void fillMeanData(GraphPanel graphPanel, ArrayList<MeanOfEdgeResult> resultList) {
		if(graphPanel != null) {
			graphPanel.clearProperties();
			graphPanel.setPropertyPrecsion(1, 3, 3);
			graphPanel.setPropertyPrecsion(2, 3, 3);
			
			for(MeanOfEdgeResult meanResult : resultList)  {
				String timestamp = UtilHelper.convertTimestampToDatestring(meanResult.getTimestamp());
				double mean = meanResult.getMeanOfEdges();
				graphPanel.addPropertyData(timestamp, mean, meanResult.getDeviation());
			}

		}
	}

	@Override
	public void updateChartScrollArea(StatisticPanel statisticPanel, int lowerBoundary, int upperBoundary) {
		mLowerBoundary = lowerBoundary;
		mUpperBoundary = upperBoundary;
		String characteristic = mGraphPanel.getCharacteristic();
		if (characteristic.equals(CharacteristicLabels.GRAPH_NODES)) {
			if(mNodeResultList != null) {
				statisticPanel.clearChart();
				TreeMap<String,Long> chartData = new TreeMap<String,Long>();
				for(int i = lowerBoundary; i < upperBoundary; i++) {
					NodeResult nodeResult = mNodeResultList.get(i);
					String timestamp = UtilHelper.convertTimestampToDatestring(nodeResult.getTimestamp());
					long nodes = nodeResult.getNumbersOfIdentifer();
					chartData.put(timestamp, nodes);
				}
				statisticPanel.setChartValues(chartData);
			}
		} else if (characteristic.equals(CharacteristicLabels.GRAPH_EDGES)) {
			if(mEdgeResultList != null) {
				statisticPanel.clearChart();
				TreeMap<String,Long> chartData = new TreeMap<String,Long>();
				for(int i = lowerBoundary; i < upperBoundary; i++) {
					EdgeResult edgeResult = mEdgeResultList.get(i);
					String timestamp = UtilHelper.convertTimestampToDatestring(edgeResult.getTimestamp());
					long edges = edgeResult.getNumbersOfEgde();
					chartData.put(timestamp, edges);
				}
				statisticPanel.setChartValues(chartData);
			}
		} else if (characteristic.equals(CharacteristicLabels.GRAPH_MEAN)) {
			if(mMeanResultList != null) {
				statisticPanel.clearChart();
				TreeMap<String,Double> chartData = new TreeMap<String,Double>();
				for(int i = lowerBoundary; i < upperBoundary; i++) {
					MeanOfEdgeResult meanResult = mMeanResultList.get(i);
					String timestamp = UtilHelper.convertTimestampToDatestring(meanResult.getTimestamp());
					double mean = meanResult.getMeanOfEdges();
					chartData.put(timestamp, mean);
				}
				statisticPanel.setChartValues(chartData);
			}
		}
	}

}
