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
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import de.hshannover.f4.trust.metalyzer.gui.charts.data.StatisticData;

public class BoxWhiskerChartPanel extends AbstractChartPanel {

	private DefaultBoxAndWhiskerCategoryDataset mBoxAndWhiskerDataset;

	@Override
	public JFreeChart createChart() {
		mBoxAndWhiskerDataset = new DefaultBoxAndWhiskerCategoryDataset();
		mChart = ChartFactory.createBoxAndWhiskerChart("BoxWhiskerChart", "X",
				"Y", mBoxAndWhiskerDataset, false);
		return mChart;
	}

	/**
	 * Sets a value to the BoxAndWhiskerCategoryDataset of the BoxWhiskerChart
	 * @param values a Map of StatisticData
	 * @see StatisticData
	 * @see BoxAndWhiskerCategoryDataset
	 */
	@Override
	public void setValues(Map<String, StatisticData> values) {
		for(String key : values.keySet()) {
			StatisticData data = values.get(key);
			Map<String, ? extends Number> boxdata = data.getMultiValue();
			mBoxAndWhiskerDataset.add(new BoxAndWhiskerItem(boxdata.get("mean"), 
					boxdata.get("median"), 
					boxdata.get("q1"), 
					boxdata.get("q3"), 
					null,
					null, 
					null, 
					null, 
					null), "series", key);
		}		
	}

	/**
	 * Clears all data of the box whisker chart
	 * @see BoxAndWhiskerCategoryDataset#clear()
	 */
	@Override
	public void clearDataset() {
		mBoxAndWhiskerDataset.clear();
	}

}
