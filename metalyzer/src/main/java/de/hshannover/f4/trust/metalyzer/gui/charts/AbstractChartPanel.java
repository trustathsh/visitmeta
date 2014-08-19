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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.data.category.CategoryDataset;

import de.hshannover.f4.trust.metalyzer.gui.charts.data.StatisticData;

/**
 * Project: Metalyzer Author: Daniel Hülse Last Change: by: $Author: $ date:
 * $Date: $ Copyright (c): Hochschule Hannover
 */

public abstract class AbstractChartPanel extends JPanel {

	protected JFreeChart mChart;
	private ChartPanel mChartPanel;
	private ValueAxis mValueAxis;
	private CategoryAxis mCategoryAxis;
	private CategoryPlot mCategoryPlot;
	private JScrollBar mScrollBar;
	private boolean isCategoryPlot = true;

	public AbstractChartPanel() {
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		mChartPanel = initChartPanel();
		mScrollBar = getChartScrollBar();
		mScrollBar.setVisible(false);
		this.setLayout(new BorderLayout());
		this.add(mChartPanel, BorderLayout.CENTER);
		this.add(mScrollBar, BorderLayout.SOUTH);

	}

	private JScrollBar getChartScrollBar() {
		JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 100);
		return scrollBar;
	}
	
	/**
	 * Sets the visibilty of the chart panels scrollbar
	 * @param state of visibilty, if true the scrollbar is shown, otherwise the scrollbar is hidden.
	 * by default the scrollbar is disabled.
	 * */
	public void showChartScrollBar(boolean state) {
		mScrollBar.setVisible(state);
	}

	/**
	 * Sets the boundaries of the scroll range of the chart scrollbar
	 * @param min the lowest value of the scroll range
	 * @param max the highest value of the scroll range
	 * */
	public void setScrollBoundaries(int min, int max) {
		mScrollBar.setMaximum(max);
		mScrollBar.setMinimum(min);
	}
	
	/**
	 * Retuens the lowest value of the scroll range of the chart scrollbar
	 * @return minimum value of the chart scrollbar
	 * */
	public int getScrollMinimum() {
		return mScrollBar.getMinimum();
	}
	
	/**
	 * Retuens the highest value of the scroll range of the chart scrollbar
	 * @return maximum value of the chart scrollbar
	 * */
	public int getScrollMaximum() {
		return mScrollBar.getMaximum();
	}
	
	/**
	 * Adds an {@link ChartAdjustmentListener} to the chartpanel
	 * @param listener the ChartAdjustmentListener, that handles the adhustemnt of the chart
	 * */
	public void addChartAdjustmentListener(AdjustmentListener listener) {
		mScrollBar.addAdjustmentListener(listener);
	}


	/**
	 * Initializes the {@link ChartPanel}, with a given chart, therefore it
	 * calls the hook method
	 * {@link de.hshannover.f4.trust.metalyzer.gui.charts#createChart()}
	 */
	private ChartPanel initChartPanel() {
		mChart = createChart();
		Plot plot = mChart.getPlot();
		if (plot instanceof CategoryPlot) {
			mCategoryPlot = mChart.getCategoryPlot();
			mValueAxis = mCategoryPlot.getRangeAxis();
			mCategoryAxis = mCategoryPlot.getDomainAxis();

			mValueAxis.setTickLabelFont(new Font(Font.DIALOG, Font.PLAIN, 12));
			mValueAxis.setLabelFont(new Font(Font.DIALOG, Font.PLAIN, 13));
			mCategoryAxis
					.setTickLabelFont(new Font(Font.DIALOG, Font.PLAIN, 12));
			mCategoryAxis.setLabelFont(new Font(Font.DIALOG, Font.PLAIN, 13));

			isCategoryPlot = true;
		} else {
			isCategoryPlot = false;
		}
		ChartPanel chartPanel = new ChartPanel(mChart);
		return chartPanel;
	}

	/**
	 * Returns the Chart
	 * 
	 * @return {@linkJFreeChart} instance
	 * */
	public JFreeChart getChart() {
		return mChart;
	}

	/**
	 * Adds a {@link ChartMouseListener} to the AbstractChartPanel
	 * 
	 * @param listener
	 */
	public void addChartMouseListener(ChartMouseListener listener) {
		mChartPanel.addChartMouseListener(listener);
	}

	/**
	 * Enables the mouse wheel support
	 * 
	 * @param flag
	 *            true = enabled, false = disabled
	 */
	public void setMouseWheelEnabled(boolean flag) {
		mChartPanel.setMouseWheelEnabled(flag);
	}

	/**
	 * Sets the title of the Chart
	 * @param {@link String} title
	 */
	public void setChartTitle(String title) {
		mChart.setTitle(title);
	}

	/**
	 * Sets the visiblity of the chart title
	 * @param visible
	 */
	public void setChartTitleVisible(boolean visible) {
		mChart.getTitle().setVisible(visible);
	}

	
	/**
	 * Sets the title of the Value Axis (y-Axis)
	 * @param label
	 */
	public void setValueAxisLabel(String label) {
		if (isCategoryPlot) {
			mValueAxis.setLabel(label);
		}
	}

	/**
	 * Sets the title of the Category Axis (x-Axis)
	 * @param label
	 */
	public void setCategoryAxisLabel(String label) {
		if (isCategoryPlot) {
			mCategoryAxis.setLabel(label);
		}
	}

	/**
	 * Rotates the label of the x axis with a given angle (up rotation)
	 * @param upAngle
	 * @see CategoryAxis#setCategoryLabelPositions(CategoryLabelPositions)
	 * */
	public void setCategoryLabelRotation(double upAngle) {
		if (isCategoryPlot) {
			mCategoryAxis.setCategoryLabelPositions(CategoryLabelPositions
					.createUpRotationLabelPositions((upAngle / 180) * Math.PI));
		}
	}

	/**
	 * Clears all data of the {@link CategoryDataset}
	 */
	public abstract void clearDataset();

	/**
	 * Hook method which returns the a bar chart
	 * @return {@link JFreeChart}
	 */
	public abstract JFreeChart createChart();

	/**
	 * Sets the values of the Chart
	 * @param Map
	 * */
	public abstract void setValues(Map<String, StatisticData> values);

}