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

import java.text.DecimalFormat;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import de.hshannover.f4.trust.metalyzer.gui.charts.data.StatisticData;
import de.hshannover.f4.trust.metalyzer.gui.charts.renderer.PieColorRenderer;

public class PieChartPanel extends AbstractChartPanel {

	private DefaultPieDataset mPieDataset;
	private PieColorRenderer mPieRenderer;
	private PiePlot mPiePlot;

	@Override
	public JFreeChart createChart() {
		mPieDataset = new DefaultPieDataset();
		mChart = ChartFactory.createPieChart("PieChart", mPieDataset, false,
				true, false);

		mPieRenderer = new PieColorRenderer();
		mPiePlot = (PiePlot) mChart.getPlot();
		mPiePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {2}", new DecimalFormat(), new DecimalFormat("0.##%")));
		return mChart;
	}

	/**
	 * Sets a value to the PieDatase of the PieChart
	 * 
	 * @param values
	 *            a Map of StatisticData
	 * @see StatisticData
	 * @see PieDataset
	 */
	@Override
	public void setValues(Map<String, StatisticData> values) {
		for (String key : values.keySet()) {
			StatisticData data = values.get(key);
			mPieDataset.setValue(key, data.getSingleValue());
		}
		mPieRenderer.generateColors(mPieDataset.getItemCount());
		mPieRenderer.setPieSectionPaint(mPiePlot, mPieDataset);
	}

	/**
	 * Clears all data of the pie chart
	 * 
	 * @see PieDataset#clear()
	 */
	@Override
	public void clearDataset() {
		mPieDataset.clear();
	}
}