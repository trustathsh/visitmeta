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

import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.JSpinner.ListEditor;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.gui.misc.UtilHelper;

/** 
 * Project: Metalyzer
 * Author: Daniel Huelse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class TimeSelectionView extends View {
	
	private static final Logger log = Logger.getLogger(TimeSelectionView.class);
	
	public enum TimeSelection {
		Live,
		Intervall,
		Timestamp,
		None
	}
	
	private DesignGridLayout layout;

	private JSpinner mTimeStartSelector;
	private JSpinner mTimeEndSelector;
	private JSpinner mTimeStampSelector;
	
	private JRadioButton mTimeLive;
	private JRadioButton mTimeStamp;
	private JRadioButton mTimeIntervall;

	private ButtonGroup mButtonGroup;
	private TimeSelection mSelection;
	
	private List<Long> mTime;
	private List<Long> mStart;
	private List<Long> mEnd;

	private RowGroup mIntervallGroup;
	private RowGroup mStampGroup;
	private RowGroup mLiveGroup;
	
	public TimeSelectionView(Set<Long> timestamps) {
		this(timestamps,"yyyy-MM-dd HH:mm:ss");
	}
	
	public TimeSelectionView(Set<Long> timestamps, String dateFormat) {
		layout = new DesignGridLayout(this);
		mButtonGroup = new ButtonGroup();
				
		createTimeIntervallSelection(timestamps, dateFormat);
		createTimestampSelection(timestamps, dateFormat);
		createLiveSelection();
	}
	
	private void createTimeIntervallSelection(Set<Long> timestamps,String dateFormat) {
		mTimeIntervall = new JRadioButton("Time Intervall");
		mButtonGroup.add(mTimeIntervall);
		
	
		mStart = UtilHelper.getStartList(timestamps);
		mEnd = UtilHelper.getEndList(timestamps);
		
		//Catch these exceptions to avoid the termination of the polling service
		try {
			mTimeStartSelector = new JSpinner(new SpinnerListModel(mStart));
		} catch(IllegalArgumentException e) {
			log.error(e.getMessage());
			mTimeStartSelector = new JSpinner(new SpinnerListModel(UtilHelper.createDummyList()));
		} finally {
			mTimeStartSelector.setEditor(new TimeListEditor(mTimeStartSelector,dateFormat));
			mTimeStartSelector.setEnabled(mTimeIntervall.isSelected());
		}
		
		try {
			mTimeEndSelector = new JSpinner(new SpinnerListModel(mEnd));
		} catch(IllegalArgumentException e) {
			log.error(e.getMessage());
			mTimeEndSelector = new JSpinner(new SpinnerListModel(UtilHelper.createDummyList()));
		} finally {
			mTimeEndSelector.setEditor(new TimeListEditor(mTimeEndSelector,dateFormat));
			mTimeEndSelector.setEnabled(mTimeIntervall.isSelected());
		}

		mIntervallGroup = new RowGroup();
		layout.row().group(mIntervallGroup).grid().indent(70).add(mTimeIntervall).add(mTimeStartSelector).add(mTimeEndSelector).empty(3);
	}
	
	private void createTimestampSelection(Set<Long> timestamps,String dateFormat) {
		mTimeStamp = new JRadioButton("Timestamp");
		mButtonGroup.add(mTimeStamp);
		
		mTime = UtilHelper.convertTimestamps(timestamps);
		//Catch this exception to avoid the termination of the polling service
		try {
			mTimeStampSelector = new JSpinner(new SpinnerListModel(mTime));
		} catch(IllegalArgumentException e) {
			log.error(e.getMessage());
			mTimeStampSelector = new JSpinner(new SpinnerListModel(UtilHelper.createDummyList()));
		} finally {
			mTimeStampSelector.setEditor(new TimeListEditor(mTimeStampSelector,dateFormat));
			mTimeStampSelector.setEnabled(mTimeStamp.isSelected());
		}

		mStampGroup = new RowGroup();
		layout.row().group(mStampGroup).grid().indent(70).add(mTimeStamp).add(mTimeStampSelector).empty(4);
	}
	
	private void createLiveSelection() {
		mTimeLive = new JRadioButton("Live");
		mButtonGroup.add(mTimeLive);
		mLiveGroup = new RowGroup();
		layout.row().group(mLiveGroup).grid().indent(70).add(mTimeLive);
	}
	
	/**
	 * Sets the selection mode of the time selection view
	 * @param selection the mode with time selection view is initialized.<br>
	 * 		e.g TimeSelection.Live for live modus <br>
	 * 		e.g TimeSelection.Timestamp for a single timestamp <br>
	 * 		e.g TimeSelection.Intervall for an intervall of timesamps <br>
	 * */
	public void setSelectionMode(TimeSelection selection) {
		this.mSelection = selection;
		switch(mSelection) {
			case Live:
				mTimeLive.setSelected(true);
				break;
			case Timestamp:
				mTimeStamp.setSelected(true);
				break;
			case Intervall:
				mTimeIntervall.setSelected(true);
				break;
			default: //None
				mTimeLive.setSelected(false);
				mTimeStamp.setSelected(false);
				mTimeIntervall.setSelected(false);
				break;
		}
		
		if(selection != TimeSelection.None) {
			mTimeStartSelector.setEnabled(mTimeIntervall.isSelected());
			mTimeEndSelector.setEnabled(mTimeIntervall.isSelected());
			mTimeStampSelector.setEnabled(mTimeStamp.isSelected());
		} else {
			mTimeStartSelector.setEnabled(false);
			mTimeEndSelector.setEnabled(false);
			mTimeStampSelector.setEnabled(false);
		}
	}
	
	/**
	 * Return the current selection mode of the time selction view
	 * @return selection mode
	 * */
	public TimeSelection getSelectionMode() {
		return mSelection;
	}
	
	/**
	 * Sets the timestamps of the TimeSelectionView
	 * @param timestamps reinitializes the {@link JSpinner} of the TimeSelectionView
	 * */
	public void setTimestampList(SortedSet<Long> timestamps) {
		if(mTimeStartSelector != null && mTimeEndSelector != null) {
			long oldTime = getTimeStampValue();
			long oldStart = getTimeStartValue();
			long oldEnd = getTimeEndValue();
			
			mTime = UtilHelper.convertTimestamps(timestamps);
			mStart = UtilHelper.getStartList(timestamps);
			mEnd = UtilHelper.getEndList(timestamps);
			
			SpinnerListModel mTimeStampModel = (SpinnerListModel)mTimeStampSelector.getModel();
			
			//Catch these exceptions to avoid the termination of the polling service
			try {
				mTimeStampModel.setList(mTime);
			} catch(IllegalArgumentException e) {
				log.error(e.getMessage());
				mTimeStampModel.setList(UtilHelper.createDummyList());
			} 

			if(mTime.contains(oldTime)) {
				mTimeStampModel.setValue(oldTime);
			}
			
			SpinnerListModel mTimeStartModel = (SpinnerListModel)mTimeStartSelector.getModel();
			try {
				mTimeStartModel.setList(mStart);
			} catch(IllegalArgumentException e) {
				log.error(e.getMessage());
				mTimeStartModel.setList(UtilHelper.createDummyList());
			} 
			
			if(mStart.contains(oldStart)) {
				mTimeStartModel.setValue(oldStart);
			}

		
			SpinnerListModel mTimeEndModel = (SpinnerListModel)mTimeEndSelector.getModel();
			try {
				mTimeEndModel.setList(mEnd);
			} catch(IllegalArgumentException e) {
				log.error(e.getMessage());
				mTimeEndModel.setList(UtilHelper.createDummyList());
			} 

			if(mEnd.contains(oldEnd)) {
				mTimeEndModel.setValue(oldEnd);
			}
		}
	}
	
	/**
	 * Adds a {@link ChangeListener} to the timestamp intervall selector {@link JSpinner}
	 * @param listener the spinner changelisteners
	 * @see JSpinner#addChangeListener(ItemListener)
	 * */
	public void addTimeIntervallListener(ChangeListener listener) {
		mTimeStartSelector.addChangeListener(listener);
		mTimeEndSelector.addChangeListener(listener);
	}
	
	/**
	 * Adds a {@link ChangeListener} to the timestamp selector {@link JSpinner}
	 * @param listener the spinner changelisteners
	 * @see JSpinner#addChangeListener(ItemListener)
	 * */
	public void addTimeStampListener(ChangeListener listener) {
		mTimeStampSelector.addChangeListener(listener);
	}
	
	/**
	 * Adds a {@link ChangeListener} to the timestamp selector {@link JSpinner}
	 * @param listener the spinner changelisteners
	 * @see JSpinner#addChangeListener(ItemListener)
	 * */
	public void addTimeSelectionListener(ItemListener listener) {
		mTimeLive.addItemListener(listener);
		mTimeStamp.addItemListener(listener);
		mTimeIntervall.addItemListener(listener);
	}
	
	/**
	 * Returns the status of the live {@link JRadioButton}
	 * @return true if the live is checked, false otherwise 
	 * @see JRadioButton#isSelected()
	 * */
	public boolean isLive() {
		return (mTimeLive == null) ? false : mTimeLive.isSelected();
	}
	
	/**
	 * Returns an instance of the timestamp live button {@link JRadioButton}
	 * @return TimeLive {@link JRadioButton}
	 * */
	public JRadioButton getTimeLiveButton() {
		return mTimeLive;
	}
	
	/**
	 * Returns an instance of the timestamp stamp button {@link JRadioButton}
	 * @return TimeStamp {@link JRadioButton}
	 * */
	public JRadioButton getTimeStampButton() {
		return mTimeStamp;
	}
	
	/**
	 * Returns an instance of the timestamp intervall button {@link JRadioButton}
	 * @return TimeIntervall {@link JRadioButton}
	 * */
	public JRadioButton getTimeIntervallButton() {
		return mTimeIntervall;
	}
	
	/**
	 * Returns an instance of the timestamp selector {@link JSpinner}
	 * @return TimeStart {@link JSpinner}
	 * */
	public JSpinner getTimeStampSelector() {
		return mTimeStampSelector;
	}
	
	/**
	 * Returns an instance of the start timestamp selector{@link JSpinner}
	 * @return TimeStart {@link JSpinner}
	 * */
	public JSpinner getTimeStartSelector() {
		return mTimeStartSelector;
	}
	
	/**
	 * Returns an instance of the end timestamp selector {@link JSpinner}
	 * @return TimeEnd {@link JSpinner}
	 * */
	public JSpinner getTimeEndSelector() {
		return mTimeEndSelector;
	}
	
	/**
	 * Sets the time stamp value if the value exists in the current spinner list, 
	 * otherwise the value will be irgnored,
	 * */
	public void setTimeStampValue(long value) {
		if(mTime != null && mTime.contains(value)) {
			mTimeStampSelector.setValue(value);
		}
	}
	
	/**
	 * Returns the value of the time start spinner
	 * @return timestamp selector value
	 * @see JSpinner#getValue()
	 * */
	public long getTimeStampValue() {
		long value = 0;
		if(mTimeStampSelector.getValue() != null) {
			value = (long) mTimeStampSelector.getValue();
		}
		return value;
	}
	
	/**
	 * Sets the time start value if the value exists in the current spinner list, 
	 * otherwise the value will be irgnored,
	 * */
	public void setTimeStartValue(long value) {
		if(mStart != null && mStart.contains(value)) {
			mTimeStartSelector.setValue(value);
		}
	}
	
	/**
	 * Returns the value of the time start spinner
	 * @return start timestamp
	 * @see JSpinner#getValue()
	 * */
	public long getTimeStartValue() {
		long start = 0;
		if(mTimeStartSelector.getValue() != null) {
			start = (long) mTimeStartSelector.getValue();
		}
		return start;
	}
	
	/**
	 * Sets the time end value if the value exists in the current spinner list, 
	 * otherwise the value will be irgnored,
	 * */
	public void setTimeEndValue(long value) {
		if(mEnd != null && mEnd.contains(value)) {
			mTimeEndSelector.setValue(value);
		}
	}
	
	/**
	 * Returns the value of the time end spinner
	 * @return end timestamp
	 * @see JSpinner#getValue()
	 * */
	public long getTimeEndValue() {
		long end = 0;
		if(mTimeEndSelector.getValue() != null) {
			end = (long) mTimeEndSelector.getValue();
		}
		return end;
	}
	
	
	/**
	 * Hides all TimeSelector elements
	 * */
	public void hideTimeSelectors() {
		mIntervallGroup.hide();	
		mStampGroup.hide();
		mLiveGroup.hide();
		setSelectionMode(TimeSelection.None);
	}
	
	/**
	 * Hides all TimeSelector elements without the specified one
	 * @param selection keep this TimeSelector hides the rest
	 * */
	public void hideTimeSelectors(TimeSelection selection) {
		switch(selection) {
			case Intervall:
				mStampGroup.hide();
				mLiveGroup.hide();
				break;
			case Timestamp:
				mIntervallGroup.hide();	
				mLiveGroup.hide();	
				break;
			case Live:
				mIntervallGroup.hide();
				mStampGroup.hide();	
				break;	
		}
	}

	/* Formats the value of the jspinner list as date string */
	class TimeListEditor extends ListEditor {

		public TimeListEditor(JSpinner spinner, String format) {
			super(spinner);
	        DateFormat displayFormat = new SimpleDateFormat(format);
	        DateFormatter displayFormatter = new DateFormatter(displayFormat);
	        
	        JFormattedTextField ftf = getTextField();
	        ftf.setEditable(true);
	        ftf.setFormatterFactory(new DefaultFormatterFactory(displayFormatter));
		}
		
	}
}
