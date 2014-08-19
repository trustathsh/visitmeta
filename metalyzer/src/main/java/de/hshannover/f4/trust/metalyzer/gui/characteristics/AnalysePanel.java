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
import java.text.Format;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JPanel;

import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.event.TimeIntervallHandler;
import de.hshannover.f4.trust.metalyzer.gui.event.TimeSelectionHandler;
import de.hshannover.f4.trust.metalyzer.gui.event.TimeStampHandler;
import de.hshannover.f4.trust.metalyzer.gui.views.InfoView;
import de.hshannover.f4.trust.metalyzer.gui.views.TableView;
import de.hshannover.f4.trust.metalyzer.gui.views.TimeSelectionView;
import de.hshannover.f4.trust.metalyzer.gui.views.TitleView;
import de.hshannover.f4.trust.metalyzer.gui.views.ViewContainer;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class AnalysePanel extends JPanel implements Observer {
	
	protected ViewContainer mViewContainer;
	
	protected InfoView mInfoView;
	protected TitleView mTitleView;
	protected TimeSelectionView mTimeSelectionView;
	protected TableView mPropertiesView;
	
	private String mCategory;
	private String mCharacteristic;
	private MetalyzerGuiController mGuiController;


	public AnalysePanel(MetalyzerGuiController gc, String c) {
		this.mGuiController = gc;
		this.mCharacteristic = c;
		this.setName(mCharacteristic);
		
		mViewContainer = new ViewContainer();
		
		mTitleView = new TitleView(mCharacteristic);
		mViewContainer.addTopView(mTitleView);
		
		mInfoView = new InfoView();
		mInfoView.setLayoutAlignment(FlowLayout.CENTER);
		mViewContainer.addView(mInfoView);
		
		mTimeSelectionView = new TimeSelectionView(mGuiController.getTimestampList());
		mTimeSelectionView.setLayoutAlignment(FlowLayout.CENTER);
		mTimeSelectionView.setSelectionMode(TimeSelectionView.TimeSelection.Timestamp);
		mTimeSelectionView.addTimeIntervallListener(new TimeIntervallHandler(mGuiController, this, mTimeSelectionView));
		mTimeSelectionView.addTimeStampListener(new TimeStampHandler(mGuiController, this, mTimeSelectionView));
		mTimeSelectionView.addTimeSelectionListener(new TimeSelectionHandler(mGuiController, this));
		mViewContainer.addView(2, mTimeSelectionView);
		
		mGuiController.setTimeSelection(mTimeSelectionView.getSelectionMode());
		
		mPropertiesView = new TableView();
		mPropertiesView.setTableSize(mGuiController.getViewWidth() / 2 + 100, 150);
		mViewContainer.addBottomView(mPropertiesView);
		this.add(mViewContainer);
	}
	
	/**
	 * Sets the AnalysePanels parent category
	 * @param category
	 * */
	public void setCategory(String category) {
		this.mCategory = category;
	}
	
	/**
	 * Returns the AnalysePanels parent category
	 * @return category
	 * */
	public String getCategory() {
		return mCategory;
	}
	
	/**
	 * Sets the text that is displayed as information for the user
	 * @param description 
	 * */
	public void setInfoDescription(String description) {
		mInfoView.setText(description);
	}
	
	/**
	 * Formats the property colum according to the format instance
	 * @param columnIndex specifies the column that s be formated
	 * @param format
	 * */
	public void setPropertyFormat(int columnIndex, Format format) {
		mPropertiesView.setColumnFormat(columnIndex, format);
	}
	
	/**
	 * Returns a map which holds the formats of rows
	 * @return format map
	 * */
	public Map<Integer,Format> getPropertyFormats() {
		return mPropertiesView.getColumnFormats();
	}
	
	/**
	 * Sets the column names of the properties table
	 * @param names, the amount of names, which can be specified, depends on the current column count.
	 */
	public void setPropertyNames(String... names) {
		mPropertiesView.clearTableView();
		mPropertiesView.setColumnNames(names);
	}
	
	/**
	 * Returns an array of all column names of the properties table
	 * @return column names
	 * */
	public String[] getPropertyNames() {
		return mPropertiesView.getColumnNames();
	}
	
	/**
	 * Clears the properties table
	 */
	public void clearProperties() {
		mPropertiesView.clearTableView();
	}
	
	/**
	 * Adds a row of value to the properties table
	 * @param values the amount of values per row, which can be specified, depends on the current column count.
	 */
	public void addPropertyData(Object... values) {
		mPropertiesView.addRow(values);
	}

	/**
	 * Sets the whole data of the properties table at once
	 * @param {@link Map} values
	 */
	public void setPropertyData(Map<String, ?> values) {
		mPropertiesView.clearTableView();
		for(String key : values.keySet()) {
			Object data = values.get(key);
			mPropertiesView.addRow(key, data);
		}
	}
	
	/**
	 * Sets the whole data of the properties table at once
	 * @param {@link Set} values
	 */
	public void setPropertyData(Collection<?> data) {
		mPropertiesView.clearTableView();
		for(Object d : data) {
			mPropertiesView.addRow(d);
		}
	}
	
	/**
	 * Sets the whole data of the properties table at once
	 * @param {@link Set} values
	 */
	public void setPropertyData(List<Collection<?>> data) {
		mPropertiesView.clearTableView();
		mPropertiesView.setValues(data);
	}
	
	/**
	 * Returns the whole table as a list of collections
	 * @return values
	 * */
	public List<Collection<?>> getPropertyData() {
		return mPropertiesView.getValues();
	}
	
	/**
	 * Returns the {@link Characteristic} of this AnalysePanel
	 * @return the characteristic
	 * */
	public String getCharacteristic() {
		return mCharacteristic;
	}
	
	/**
	 * Returns an instance of the TimeSelectionView
	 * @return {@link TimeSelectionView} instance
	 * */
	public TimeSelectionView getTimeSelectionView() {
		return mTimeSelectionView;
	}
	
	/**
	 * Returns the first timestamp of current available timestamps
	 * @return first timestamp
	 */
	public long getStartTimestamp() {
		return mTimeSelectionView.getTimeStartValue();
	}

	/**
	 * Returns the last timestamp of current available timestamps
	 * @return last timestamp
	 */
	public long getEndTimestamp() {
		return mTimeSelectionView.getTimeEndValue();
	}

	@Override
	public void update(Observable ob, Object o) {
		mGuiController.updateAnalysePanels(this, mTimeSelectionView.isLive());
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
