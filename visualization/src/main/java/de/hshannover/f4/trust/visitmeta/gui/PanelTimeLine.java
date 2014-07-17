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
 * This file is part of visitmeta visualization, version 0.1.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedMap;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.ernieyu.swingRangeSlider.RangeSlider;

import de.hshannover.f4.trust.visitmeta.datawrapper.TimeHolder;
import de.hshannover.f4.trust.visitmeta.datawrapper.TimeSelector;

public class PanelTimeLine extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	private static final String mTimeformat = "yyyy-MM-FF HH:mm:ss";

	private SortedMap<Long, Long> mChangesMap = null;
	private TimeSelector mTimeSelector = null;
	private Timer mDelay = null;
	/* GUI components */
	private SpringLayout mSpringLayout = null;
	private JSpinner mSpinnerTimeStart = null;
	private SpinnerDateModel mSpinnerTimeStartDateModel = null;
	private JSpinner mSpinnerTimeEnd = null;
	private SpinnerDateModel mSpinnerTimeEndDateModel = null;
	private RangeSlider mSlider = null;
	private JCheckBox mChckbxLive = null;
	/* Listener */
	private ChangeListener mListenerSpinnerTimeStart = null;
	private ChangeListener mListenerSpinnerTimeEnd = null;
	private ChangeListener mListenerSlider = null;
	private ChangeListener mListenerChckbxLive = null;

	public PanelTimeLine(TimeSelector timeSelector) {
		super();
		setPreferredSize(new Dimension(400, 33));
		setMinimumSize(new Dimension(400, 0));
		/* Add TimeSelector */
		mTimeSelector = timeSelector;
		/* SpringLayout */
		mSpringLayout = new SpringLayout();
		/* spinnerTimeStart */
		mSpinnerTimeStartDateModel = new SpinnerDateModel(new Date(0L), null, null, Calendar.SECOND);
		mSpinnerTimeStart = new JSpinner();
		mSpinnerTimeStart.setToolTipText("Start Time");
		mSpinnerTimeStart.setModel(mSpinnerTimeStartDateModel);
		mSpinnerTimeStart.setEditor(new JSpinner.DateEditor(mSpinnerTimeStart, mTimeformat));
		mSpinnerTimeStart.setEnabled(false);
		/* slider */
		mSlider = new RangeSlider();
		mSlider.setMinimum(0);
		mSlider.setMaximum(0);
		mSlider.setToolTipText("Start Time <---> End Time");
		mSlider.setEnabled(false);
		mSlider.setEnabledLowerDragging(false);
		mSlider.setEnabledUpperDragging(false);
		/* spinnerTimeEnd */
		mSpinnerTimeEndDateModel = new SpinnerDateModel(new Date(System.currentTimeMillis()), null, null,
				Calendar.SECOND);
		mSpinnerTimeEnd = new JSpinner();
		mSpinnerTimeEnd.setToolTipText("End Time");
		mSpinnerTimeEnd.setModel(mSpinnerTimeEndDateModel);
		mSpinnerTimeEnd.setEditor(new JSpinner.DateEditor(mSpinnerTimeEnd, mTimeformat));
		mSpinnerTimeEnd.setEnabled(false);
		/* chckbxCurrent */
		mChckbxLive = new JCheckBox("Live");
		mChckbxLive.setToolTipText("Show the current graph.");
		mChckbxLive.setSelected(true);
		/* SpringLayout */
		setLayout(mSpringLayout);
		/* Add spinnerTimeStart in SpringLayout */
		mSpringLayout.putConstraint(SpringLayout.NORTH, mSpinnerTimeStart, 6, SpringLayout.NORTH, this);
		mSpringLayout.putConstraint(SpringLayout.WEST, mSpinnerTimeStart, 15, SpringLayout.WEST, this);
		/* Add slider in SpringLayout */
		mSpringLayout.putConstraint(SpringLayout.NORTH, mSlider, 8, SpringLayout.NORTH, this);
		mSpringLayout.putConstraint(SpringLayout.WEST, mSlider, 8, SpringLayout.EAST, mSpinnerTimeStart);
		mSpringLayout.putConstraint(SpringLayout.EAST, mSlider, -8, SpringLayout.WEST, mSpinnerTimeEnd);
		/* Add spinnerTimeEnd in SpringLayout */
		mSpringLayout.putConstraint(SpringLayout.NORTH, mSpinnerTimeEnd, 6, SpringLayout.NORTH, this);
		mSpringLayout.putConstraint(SpringLayout.EAST, mSpinnerTimeEnd, -8, SpringLayout.WEST, mChckbxLive);
		/* Add chckbxCurrent in SpringLayout */
		mSpringLayout.putConstraint(SpringLayout.NORTH, mChckbxLive, 4, SpringLayout.NORTH, this);
		mSpringLayout.putConstraint(SpringLayout.EAST, mChckbxLive, -15, SpringLayout.EAST, this);
		/* Add components to panel. */
		add(mSpinnerTimeStart);
		add(mSlider);
		add(mSpinnerTimeEnd);
		add(mChckbxLive);

		addListener();
		mTimeSelector.getTimeHolder().addObserver(this);
	}

	/**
	 * Add Listener to GUI elements.
	 */
	private void addListener() {
		/* Listener */
		mListenerSpinnerTimeStart = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent pE) {
				mDelay.stop();
				mDelay.start();
			}
		};
		mListenerSpinnerTimeEnd = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent pE) {
				mDelay.stop();
				mDelay.start();
			}
		};
		mListenerSlider = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent pE) {
				Object[] vTimes = mChangesMap.keySet().toArray();
				mSpinnerTimeStartDateModel.setValue(new Date((long) vTimes[mSlider.getValue()]));
				mSpinnerTimeEndDateModel.setValue(new Date((long) vTimes[mSlider.getUpperValue()]));
			}
		};
		mListenerChckbxLive = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent pE) {
				if (mChckbxLive.isSelected()) {
					mSlider.setEnabled(false);
					mSlider.setEnabledLowerDragging(false);
					mSlider.setEnabledUpperDragging(false);
					mSpinnerTimeStart.setEnabled(false);
					mSpinnerTimeEnd.setEnabled(false);
				} else {
					mSlider.setEnabled(true);
					mSlider.setEnabledLowerDragging(true);
					mSlider.setEnabledUpperDragging(true);
					mSpinnerTimeStart.setEnabled(true);
					mSpinnerTimeEnd.setEnabled(true);
				}
				mDelay.stop();
				mDelay.start();
			}
		};
		/* Timer */
		mDelay = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				mTimeSelector.setTimeStart(((Date) mSpinnerTimeStart.getValue()).getTime(), false);
				mTimeSelector.setTimeEnd(((Date) mSpinnerTimeEnd.getValue()).getTime(), false);
				mTimeSelector.setLiveView(mChckbxLive.isSelected(), false);
				mTimeSelector.notifyObservers();
			}
		});
		/* Add Listener */
		mSpinnerTimeStart.addChangeListener(mListenerSpinnerTimeStart);
		mSpinnerTimeEnd.addChangeListener(mListenerSpinnerTimeEnd);
		mSlider.addChangeListener(mListenerSlider);
		mChckbxLive.addChangeListener(mListenerChckbxLive);
	}

	@Override
	public void update(Observable pO, Object pArg) {
		if (pO instanceof TimeHolder) {
			/* Remove Listener */
			mSpinnerTimeStart.removeChangeListener(mListenerSpinnerTimeStart);
			mSpinnerTimeEnd.removeChangeListener(mListenerSpinnerTimeEnd);
			mSlider.removeChangeListener(mListenerSlider);
			mChckbxLive.removeChangeListener(mListenerChckbxLive);
			/* Make changes */
			mChangesMap = mTimeSelector.getTimeHolder().getChangesMap();
			mSlider.setMaximum(mChangesMap.size() - 1);
			// mSpinnerTimeStartDateModel.setStart(new
			// Date(mTimeHolder.getBigBang()));
			// mSpinnerTimeStartDateModel.setEnd(new
			// Date(mTimeHolder.getNewestTime()));
			// mSpinnerTimeEndDateModel.setStart(new
			// Date(mTimeHolder.getBigBang()));
			// mSpinnerTimeEndDateModel.setEnd(new
			// Date(mTimeHolder.getNewestTime()));
			if (mTimeSelector.isLiveView()) {
				mSlider.setUpperValue(mChangesMap.size() - 1);
				mSpinnerTimeStartDateModel.setValue(new Date(mTimeSelector.getTimeHolder().getTimeStart()));
				mSpinnerTimeEndDateModel.setValue(new Date(mTimeSelector.getTimeHolder().getTimeEnd()));
			}
			/* Add Listener */
			mSpinnerTimeStart.addChangeListener(mListenerSpinnerTimeStart);
			mSpinnerTimeEnd.addChangeListener(mListenerSpinnerTimeEnd);
			mSlider.addChangeListener(mListenerSlider);
			mChckbxLive.addChangeListener(mListenerChckbxLive);
		}
	}
}
