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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SortedSet;

import javax.swing.JComboBox;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.event.ItemSelectionHandler;
import de.hshannover.f4.trust.metalyzer.gui.misc.UtilHelper;
import de.hshannover.f4.trust.metalyzer.gui.views.ComboBoxView;
import de.hshannover.f4.trust.metalyzer.gui.views.LabelView;
/** 
 * Project: Metalyzer
 * Author: Anton Zaiser
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class SemanticPanel extends AnalysePanel {
	
	private static final Logger log = Logger.getLogger(SemanticPanel.class);
		
	protected ComboBoxView<String> mComboBoxView;
	protected LabelView mLabelView;
	private List<Long> mTimestampList;

	public SemanticPanel(MetalyzerGuiController guiController, String characteristic) {
		super(guiController, characteristic);
		mViewContainer.addView(mTimeSelectionView);
		
		mComboBoxView = new ComboBoxView<String>();
		mComboBoxView.setViewIndent(69);
		mComboBoxView.addListener(new ItemSelectionHandler(guiController, this));
		mViewContainer.addView(mComboBoxView);
		
		mLabelView = new LabelView();
		mLabelView.setViewIndent(70);
		mViewContainer.addView(mLabelView);
		mViewContainer.addView(mPropertiesView);
	}

	/**
	 * Fills a {@link ComboBox} with an {@link ArrayList} of Unixtimestamps
	 * @param timestamps ArrayList of timestamps
	 */
	public void setTimestampItems(List<Long> timestamps) {
		String datestring;
		for(Long timestamp : timestamps) {
			datestring = UtilHelper.convertTimestampToDatestring(timestamp);
			if(!mComboBoxView.containsItem(datestring)) {
				mComboBoxView.addItem(datestring);
			}
		}
	}

	/**
	 * Sets the {@link SortedSet} which contains all timestamps
	 * @param mTimestampList
	 */
	public void setTimestamps(List<Long> timestampList) {
		mTimestampList = timestampList;
	}
	
	/**
	 * @return The {@link SortedSet} which contains all timestamps
	 */
	public List<Long> getTimestamps() {
		return mTimestampList;
	}

	/**
	 * @return The first Item in a {@link JComboBox}
	 */
	public String getDefaultItem() {
		return (String)mComboBoxView.getSelectedItem();
	}
	
	/**
	 * Sets the description text to the ComboBox
	 * @param label text
	 */
	public void setComboBoxLabel(String label) {
		mComboBoxView.setDescription(label);
	}
	
	/**
	 * Returns the description text to the ComboBox
	 * @param label text
	 */
	public String getComboBoxLabel() {
		return mComboBoxView.getDescription();
	}
	
	/**
	 * Returns all currently existing labels with description and value
	 * @return a map of String (description and value)
	 * */
	public LinkedHashMap<String, String> getLabelData() {
		return mLabelView.getLabelData();
	}
}
