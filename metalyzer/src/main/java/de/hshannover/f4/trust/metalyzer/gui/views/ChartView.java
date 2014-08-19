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
package de.hshannover.f4.trust.metalyzer.gui.views;

import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;

import de.hshannover.f4.trust.metalyzer.gui.charts.AbstractChartPanel;
import de.hshannover.f4.trust.metalyzer.gui.charts.ChartPanelFactory;
import de.hshannover.f4.trust.metalyzer.gui.charts.ChartPanelFactory.ChartType;
import de.hshannover.f4.trust.metalyzer.gui.charts.data.StatisticData;

/** 
 * Project: Metalyzer
 * Author: Daniel Huelse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class ChartView extends View {
	
	private ArrayList<AbstractChartPanel> chartPanels;
	
	public ChartView(ChartType... types) {
		chartPanels = new ArrayList<AbstractChartPanel>();
		addCharts(types);
	}
	
	public ChartView() {
		chartPanels = new ArrayList<AbstractChartPanel>();
	}
	
	/**
	 * Adds a specified chart type at the given index
	 * @param index order where the chart is stored e.g [0 = first chart]
	 * @param type the type of the chart that will be added, for more details see {@link ChartType}
	 * */
	public void addChart(int index, ChartType type) {
		if(chartPanels != null) {
			AbstractChartPanel chart = ChartPanelFactory.createChart(type);
			chart.setChartTitleVisible(false);
			chartPanels.add(index, chart);
			this.add(chart);
		}
	}
	
	/**
	 * Adds a bunch of chart types
	 * @param types the types of the charts that will be added, for more details see {@link ChartType}
	 * */
	public void addCharts(ChartType... types) {
		if(chartPanels != null) {
			for(ChartType type : types) {
				AbstractChartPanel chart = ChartPanelFactory.createChart(type);
				chart.setChartTitleVisible(false);
				chartPanels.add(chart);
				this.add(chart);
			}
		}
	}
	
	/**
	 * Sets the visibilty of the chart panels scrollbar
	 * @param index of the chart where the scrollbar should be shown
	 * @param state of visibilty, if true the scrollbar is shown, otherwise the scrollbar is hidden.
	 * by default the scrollbar is disabled.
	 * */
	public void showChartScrollBar(int index, boolean state) {
		AbstractChartPanel chartPanel = chartPanels.get(index);
		if(chartPanel != null) {
			chartPanel.showChartScrollBar(state);
		}
	}
	
	/**
	 * Retuens the lowest value of the scroll range of the chart scrollbar
	 * @return minimum value of the chart scrollbar
	 * */
	public int getScrollMinimum() {
		AbstractChartPanel chartPanel = chartPanels.get(0);
		if(chartPanel != null) {
			return chartPanel.getScrollMinimum();
		}
		return 0;
	}
	
	/**
	 * Retuens the highest value of the scroll range of the chart scrollbar
	 * @return maximum value of the chart scrollbar
	 * */
	public int getScrollMaximum() {
		AbstractChartPanel chartPanel = chartPanels.get(0);
		if(chartPanel != null) {
			return chartPanel.getScrollMaximum();
		}
		return 0;
	}
	
	/**
	 * Sets the boundaries of the scroll range of the chart scrollbar
	 * @param min the lowest value of the scroll range
	 * @param max the highest value of the scroll range
	 * */
	public void setScrollBoundaries(int min, int max) {
		if(chartPanels != null) {
			for(AbstractChartPanel chartPanel : chartPanels) {
				chartPanel.setScrollBoundaries(min, max);
			}
		}
	}
	
	/**
	 * Adds an {@link ChartAdjustmentListener} to the chartpanel
	 * @param listener the ChartAdjustmentListener, that handles the adhustemnt of the chart
	 * */
	public void addChartAdjustmentListener(AdjustmentListener listener) {
		if(chartPanels != null) {
			for(AbstractChartPanel chartPanel : chartPanels) {
				chartPanel.addChartAdjustmentListener(listener);
			}
		}
	}
	
	/**
	 * Returns a list of charts that are attacht to this chart view
	 * @return list of {@link JFreeChart}
	 * */
	public List<JFreeChart> getCharts() {
		List<JFreeChart> charts = new ArrayList<JFreeChart>();
		for(AbstractChartPanel cp : chartPanels) {
			charts.add(cp.getChart());
		}	
		return charts;
	}
	
	/**
	 * Returns a chart that are attacht to this chart view
	 * @param index the index of the chart that will be choosen
	 * @return a single {@link JFreeChart} selected by the given index
	 * */
	public JFreeChart getChart(int index) {
		AbstractChartPanel chartPanel = chartPanels.get(index);
		if(chartPanel != null) {
			return chartPanel.getChart();
		}
		return null;
	}
	
	/**
	 * Sets the title of all charts
	 * @param title the text that is displayed above each chart
	 * */
	public void setChartTitle(String title) {
		for(AbstractChartPanel chartPanel : chartPanels) {
			chartPanel.setChartTitle(title);
		}
	}
	
	
	/**
	 * Sets the visiblity of the chart title
	 * @param visible
	 */
	public void setChartTitleVisible(int index, boolean visible) {
		AbstractChartPanel chartPanel = chartPanels.get(index);
		if(chartPanel != null) {
			chartPanel.setChartTitleVisible(visible);
		}
	}
	
	/**
	 * Sets the value axis label of first chart (y-Axis)
	 * @param label the text that is displayed along the y axis
	 */
	public void setValueAxisLabel(String label) {
		setValueAxisLabel(0, label);
	}
	
	/**
	 * Sets the value axis label
	 * @param index of the chart 
	 * @param label the text that is displayed along the y axis
	 */
	public void setValueAxisLabel(int index, String label) {
		AbstractChartPanel chartPanel = chartPanels.get(index);
		if(chartPanel != null) {
			chartPanel.setValueAxisLabel(label);
		}
	}
	
	/**
	 * Sets the category axis label of first chart  (x-Axis)
	 * @param label the text that is displayed along the x axis
	 */
	public void setCategoryAxisLabel(String label) {
		setCategoryAxisLabel(0,label);
	}
	
	/**
	 * Sets the label of the category axis (x-Axis)
	 * @param index of the chart 
	 * @param label the text that is displayed along the x axis
	 */
	public void setCategoryAxisLabel(int index, String label) {
		AbstractChartPanel chartPanel = chartPanels.get(index);
		if(chartPanel != null) {
			chartPanel.setCategoryAxisLabel(label);
		}
	}
	
	
	/**
	 * Rotates the label of the x axis of the first chart with a given angle (up rotation)
	 * @param upAngle in degrees
	 * @see CategoryAxis#setCategoryLabelPositions(CategoryLabelPositions)
	 * */
	public void setCategoryLabelRotation(double upAngle) {
		setCategoryLabelRotation(0,upAngle);
	}
	
	/**
	 * Rotates the label of the x axis with a given angle (up rotation)
	 * @param index of the chart 
	 * @param upAngle in degrees
	 * @see CategoryAxis#setCategoryLabelPositions(CategoryLabelPositions)
	 * */
	public void setCategoryLabelRotation(int index, double upAngle) {
		AbstractChartPanel chartPanel = chartPanels.get(index);
		if(chartPanel != null) {
			chartPanel.setCategoryLabelRotation(upAngle);
		}
	}
	
	/**
	 * Sets the data of the chart
	 * @param data an Map that holds the data of the chart
	 * */
	public void setData(Map<String, ? extends Number> data) {
		setData(0, data);
	}
	
	/**
	 * Sets the data of the chart
	 * @param data an Map that holds the data of the chart
	 * */
	public void setData(int index, Map<String, ? extends Number> data) {
		AbstractChartPanel chartPanel = chartPanels.get(index);
		if(chartPanel != null) {
			LinkedHashMap<String, StatisticData> statisticData = new LinkedHashMap<String, StatisticData>();
			for(String key : data.keySet()) {
				statisticData.put(key, new StatisticData(data.get(key)));
			}
			chartPanel.setValues(statisticData);
		}
	}
	
	/**
	 * Clears the all chart data
	 * */
	public void clearChartData() {
		for(AbstractChartPanel chart : chartPanels) {
			chart.clearDataset();
		}
	}
	
	/**
	 * Clears the data of specified chart
	 * @param index of the chart
	 * */
	public void clearChartData(int index) {
		AbstractChartPanel chart = chartPanels.get(index);
		if(chart != null) {
			chart.clearDataset();
		}
	}
	

}
