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
package de.hshannover.f4.trust.metalyzer.gui.charts;

import java.awt.Color;
import java.awt.Paint;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.hshannover.f4.trust.metalyzer.gui.charts.data.StatisticData;
import de.hshannover.f4.trust.metalyzer.gui.charts.renderer.BarColorRenderer;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class BarChartPanel extends AbstractChartPanel {
	
	private DefaultCategoryDataset mCategoryDataset;
	private CategoryPlot mBarBlot;
	private BarColorRenderer mBarRenderer;
	
	/**
	 * Hook method which creates the Chart 
	 * @return {@link JFreeChart} a bar chart
	 */
	@Override
	public JFreeChart createChart() {
		mCategoryDataset = new DefaultCategoryDataset();
		mChart = ChartFactory.createBarChart("BarChart", "X",
				"Y", mCategoryDataset, PlotOrientation.VERTICAL, false,
				true, false);
		mBarBlot = mChart.getCategoryPlot();
		mBarRenderer = new BarColorRenderer();
		mBarRenderer.setMaximumBarWidth(0.15);
		mBarBlot.setRenderer(mBarRenderer);
		
		return mChart;
	}

	/**
	 * Sets a value to the CategoryDataset of the BarChart
	 * @param values a Map of StatisticData
	 * @see StatisticData
	 * @see CategoryDataset
	 */
	@Override
	public void setValues(Map<String, StatisticData> values) {
		for(String key : values.keySet()) {
			StatisticData data = values.get(key);
			mCategoryDataset.addValue(data.getSingleValue(), "series", key);
		}
		mBarRenderer.generateColors(mCategoryDataset.getColumnCount());
	}

	/**
	 * Clears all data of the bar chart
	 * @see CategoryDataset#clear()
	 */
	@Override
	public void clearDataset() {
		mCategoryDataset.clear();
	}


}
