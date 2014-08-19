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

import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import de.hshannover.f4.trust.metalyzer.gui.charts.data.StatisticData;

public class LineChartPanel extends AbstractChartPanel {

	private CategoryPlot mLinePlot;
	private DefaultCategoryDataset mCategoryDataset;

	public JFreeChart createChart() {
		mCategoryDataset = new DefaultCategoryDataset();
		mChart = ChartFactory.createLineChart("LineChart", "X",
				"Y", mCategoryDataset, PlotOrientation.VERTICAL, false,
				true, false);
		
		mLinePlot = mChart.getCategoryPlot();
		mLinePlot.setDomainGridlinesVisible(true);
		
	    LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer)mLinePlot.getRenderer();   
	    lineRenderer.setSeriesShapesVisible(0, true);       

        mLinePlot.setRenderer(lineRenderer);
		
		return mChart;
	}

	/**
	 * Sets a value to the CategoryDataset of the LineChart
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
	}

	/**
	 * Clears all data of the line chart
	 * @see CategoryDataset#clear()
	 */
	@Override
	public void clearDataset() {
		mCategoryDataset.clear();
	}
}
