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
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jfree.chart.JFreeChart;

import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.views.ChartView;
import de.hshannover.f4.trust.metalyzer.gui.views.LabelView;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class StatisticPanel extends AnalysePanel {
	
	protected ChartView mChartView;
	protected LabelView mDataView;
	protected LabelView mLabelView;
	private boolean isChartEnabled = false;
	
	public StatisticPanel(MetalyzerGuiController guiController, String characteristic, boolean chartEnabled) {
		super(guiController, characteristic);
		
		this.isChartEnabled = chartEnabled;
		
		if(isChartEnabled) {
			mChartView = new ChartView();
			mViewContainer.addView(2,mChartView);
		}
		
		mDataView = new LabelView("Graph Status");
		mDataView.setLayoutAlignment(FlowLayout.CENTER);
		mDataView.setViewIndent(15);
		mDataView.addLabel("Metadata","Count of Metadata:", 0);
		mDataView.addLabel("Identifer","Count of Identifer:", 0);
		mViewContainer.addView(3,mDataView);
		
		mLabelView = new LabelView();
		mLabelView.setViewIndent(60);
		this.mViewContainer.addView(5, mLabelView);

	}
	
	/**
	 * Sets the visibilty of the chart panels scrollbar
	 * @param index of the chart where the scrollbar should be shown
	 * @param state of visibilty, if true the scrollbar is shown, otherwise the scrollbar is hidden.
	 * by default the scrollbar is disabled.
	 * */
	public void showChartScrollBar(int index, boolean state) {
		if(isChartEnabled) {
			mChartView.showChartScrollBar(index, state);
		}
	}
	
	/**
	 * Sets the boundaries of the scroll range of the chart scrollbar
	 * @param min the lowest value of the scroll range
	 * @param max the highest value of the scroll range
	 * */
	public void setScrollBoundaries(int min, int max) {
		if(isChartEnabled) {
			mChartView.setScrollBoundaries(min, max);
		}
	}
	
	/**
	 * Retuens the lowest value of the scroll range of the chart scrollbar
	 * @return minimum value of the chart scrollbar
	 * */
	public int getScrollMinimum() {
		return mChartView.getScrollMinimum();
	}
	
	/**
	 * Retuens the highest value of the scroll range of the chart scrollbar
	 * @return maximum value of the chart scrollbar
	 * */
	public int getScrollMaximum() {
		return mChartView.getScrollMaximum();
	}
	
	
	/**
	 * Sets the precision of a specified property column
	 * @param columnIndex the index of the column of where the precision is applied to.
	 * @param minPrecsion the minimal digit fraction
	 * @param maxPrecsion the maximal digit fraction
	 * @see NumberFormat
	 * */
	public void setPropertyPrecsion(int columnIndex, int minPrecsion, int maxPrecsion) {
		Locale loacale = Locale.getDefault();
		NumberFormat nf = NumberFormat.getInstance(loacale);
		nf.setMaximumFractionDigits(maxPrecsion);
		nf.setMinimumFractionDigits(minPrecsion);
		this.setPropertyFormat(columnIndex, nf);
	}
	
	/**
	 * Sets the percantage of a specified property column
	 * @param columnIndex the index of the column of where the precision is applied to.
	 * @param minPrecsion the minimal digit fraction
	 * @param maxPrecsion the maximal digit fraction
	 * */
	public void setPropertyPercantage(int columnIndex, int minPrecsion, int maxPrecsion) {
		Locale loacale = Locale.getDefault();
		NumberFormat nf = NumberFormat.getPercentInstance(loacale);
		nf.setMaximumFractionDigits(maxPrecsion);
		nf.setMinimumFractionDigits(minPrecsion);
		this.setPropertyFormat(columnIndex, nf);
	}
	
	
	/**
	 * Returns a list of charts that are attacht to this chart view
	 * @return list of {@link JFreeChart}
	 * */
	public List<JFreeChart> getCharts() {
		if(isChartEnabled) {
			return mChartView.getCharts();
		}
		return null;
	}
	
	/**
	 * Returns a chart that are attacht to this chart view
	 * @param index the index of the chart that will be choosen
	 * @return a single {@link JFreeChart} selected by the given index
	 * */
	public JFreeChart getChart(int index) {
		if(isChartEnabled) {
			return mChartView.getChart(index);
		}
		return null;
	}
	

	/**
	 * Sets the title of the BarChartPanels
	 * @param title
	 */
	public void setChartTitle(String title) {
		if(isChartEnabled) {
			mChartView.setChartTitle(title);
		}
	}

	/**
	 * Sets the values of the chart
	 * @param {@link Map} values
	 */
	public void setChartValues(Map<String, ? extends Number > values) {
		if(isChartEnabled) {
			mChartView.setData(values);
		}
	}
	
	/**
	 * Sets the values of the chart by given idex
	 * @param chart index
	 * @param {@link Map} values
	 */
	public void setChartValues(int index, Map<String, ? extends Number > values) {
		if(isChartEnabled) {
			mChartView.setData(index, values);
		}
	}

	/**
	 * Sets the label of the Value Axis (y-Axis)
	 * @param label
	 */
	public void setValueAxisLabel(String label) {
		if(isChartEnabled) {
			mChartView.setValueAxisLabel(label);
		}
	}

	/**
	 * Sets the title of the Category Axis (x-Axis)
	 * @param label
	 */
	public void setCategoryAxisLabel(String label) {
		if(isChartEnabled) {
			mChartView.setCategoryAxisLabel(label);
		}
	}
	
	/**
	 * Sets the count of the metadata count label
	 * @param count
	 */
	public void setMetadataCount(int count) {
		mDataView.setValueById("Metadata", count);
	}
	
	/**
	 * Sets the count of the identifer count label
	 * @param count
	 */
	public void setIdentifierCount(int count) {
		mDataView.setValueById("Identifer", count);
	}

	/**
	 * Determines whether the {@link ChartPanel} is enabled.
	 * @return true if the {@link ChartPanel} is enabled, false otherwise
	 */
	public boolean isChartPanelEnabled() {
		return this.isChartEnabled;
	}
	
	/**
	 * Returns all currently existing labels with description and value
	 * @return a map of String (description and value)
	 * */
	public LinkedHashMap<String, String> getLabelData() {
		return mLabelView.getLabelData();
	}
	
	/**
	 * Clears the chart values 
	 */
	public void clearChart() {
		mChartView.clearChartData();
	}
	
	/**
	 * Clears the properties table and the chart values if 
	 * available
	 */
	@Override
	public void clearProperties() {
		super.clearProperties();
		if(isChartEnabled) {
			mChartView.clearChartData();
		}
	}
}
