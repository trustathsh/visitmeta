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
package de.hshannover.f4.trust.metalyzer.gui.exporter;

import java.io.File;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.JFreeChart;

import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.CategoryPanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.AnalysePanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.SemanticPanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.StatisticPanel;
import de.hshannover.f4.trust.metalyzer.gui.views.TimeSelectionView;
import de.hshannover.f4.trust.metalyzer.gui.views.TimeSelectionView.TimeSelection;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse, Sven Steinbach
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public abstract class Exporter {

	protected MetalyzerGuiController mGuiController;
	private String exportOutput;
	protected JFileChooser mFileChooser;
	protected List<JFreeChart> exportCharts = null;
	protected String[] exportTableColumns;
	protected List<Collection<?>> exportTableData = null;
	protected LinkedHashMap<String, String> exportLabels;
	protected String exportTitle;
	protected Map<Integer, Format> propertyFormats = null;

	protected String exportTimestamp = "";
	protected String exportTimestampFrom = "";
	protected String exportTimestampTo = "";
	protected String exportDesc = "";
	protected String exportItem = "";

	public Exporter(MetalyzerGuiController guiController, String exportOutput) {
		this.mGuiController = guiController;
		this.exportOutput = exportOutput;
		initExportDialog();
	}

	private void resetFields() {
		exportTimestampFrom = "";
		exportTimestampTo = "";
		exportTimestamp = "";
		exportDesc = "";
		exportItem = "";
		exportLabels = null;
	}

	/**
	 * Prepares each {@link CategoryPanel} for export, therefore
	 * all data of the charts, tables an descripions will be collected
	 * */
	protected boolean prepareExportData(CategoryPanel category) {
		resetFields();
		// CategoryPanel selection = mGuiController.getCurrentSelectionPanel();
		if (category == null) {
			return false;
		} else {
			AnalysePanel analysePanel = category.getAnalysePanel();
			exportTitle = analysePanel.getCharacteristic();

			TimeSelectionView timeView = analysePanel.getTimeSelectionView();
			DateFormat displayFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			if (timeView.getSelectionMode() == TimeSelection.Timestamp) {
				exportTimestamp = displayFormat.format(new Date(timeView
						.getTimeStampValue()));
			} else if (timeView.getSelectionMode() == TimeSelection.Intervall) {
				exportTimestampFrom = displayFormat.format(new Date(timeView
						.getTimeStartValue()));
				exportTimestampTo = displayFormat.format(new Date(timeView
						.getTimeEndValue()));
			} else if (timeView.getSelectionMode() == TimeSelection.None) {
				exportTimestampFrom = "";
				exportTimestampTo = "";
				exportTimestamp = "";
			}

			if (analysePanel instanceof StatisticPanel) {
				StatisticPanel statPanel = (StatisticPanel) analysePanel;
				if (statPanel.isChartPanelEnabled()) {
					exportCharts = statPanel.getCharts();
				} else {
					exportCharts = null;
				}
				exportLabels = statPanel.getLabelData();
			}
			if (analysePanel instanceof SemanticPanel) {
				exportCharts = null;
				SemanticPanel semPanel = (SemanticPanel) analysePanel;
				exportDesc = semPanel.getComboBoxLabel();
				exportItem = semPanel.getDefaultItem();
				exportLabels = semPanel.getLabelData();
			}
			propertyFormats = analysePanel.getPropertyFormats();
			exportTableColumns = analysePanel.getPropertyNames();
			exportTableData = analysePanel.getPropertyData();
		}
		return true;
	}

	private void initExportDialog() {
		if (exportOutput == null || exportOutput.isEmpty()) {
			mFileChooser = new JFileChooser();
		} else {
			mFileChooser = new JFileChooser(new File(exportOutput));
		}
		mFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		// Disables all file filter option
		mFileChooser.setAcceptAllFileFilterUsed(false);
	}

	/**
	 * Shows the Export dialog
	 * @see JFileChooser#showSaveDialog(java.awt.Component)
	 * */
	public File showExportDialog(String dialog) {
		mFileChooser.setDialogTitle(dialog);
		mFileChooser.setSelectedFile(new File(dialog));
		int ret = mFileChooser.showDialog(mGuiController.getMetalyzerView(), dialog);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File exportFile = new File(mFileChooser.getSelectedFile().getAbsolutePath() + getExtension(0));
			return exportFile;
		}

		return null;
	}

	/**
	 * Return the extension of the selected export type
	 * @param index
	 *            of {@link FileNameExtensionFilter} e.g if there a bunch of
	 *            image extensions {.png, .jpg, .tiff} an index of 0 would
	 *            return .png
	 * @return an extension selected by index
	 * */
	private String getExtension(int index) {
		FileNameExtensionFilter filter = (FileNameExtensionFilter) mFileChooser
				.getFileFilter();
		String extension = filter.getExtensions()[index];
		return extension;
	}

	/**
	 * Adds an exporter to the choosable file extension list
	 * @param filter
	 *            the file export specified by the
	 *            {@link FileNameExtensionFilter}
	 * @see JFileChooser#addChoosableFileFilter(javax.swing.filechooser.FileFilter)
	 * */
	public void addExportFilter(FileNameExtensionFilter filter) {
		if (mFileChooser != null) {
			mFileChooser.addChoosableFileFilter(filter);
		}
	}

	/**
	 * Specifices the default exporter that will be shown in the list at first
	 * 
	 * @param filter
	 *            the file export specified by the
	 *            {@link FileNameExtensionFilter}
	 * @see JFileChooser#setFileFilter(javax.swing.filechooser.FileFilter)
	 * */
	public void setDefaultFilter(FileNameExtensionFilter filter) {
		if (mFileChooser != null) {
			mFileChooser.setFileFilter(filter);
		}
	}

	/**
	 * Exports a single tab with diagram and table
	 * */
	public abstract void export(File exportFile, CategoryPanel category);

	/**
	 * Exports all current available diagram and table in tabs
	 * */
	public abstract void exportAll(File exportFile,
			List<CategoryPanel> categories);
}
