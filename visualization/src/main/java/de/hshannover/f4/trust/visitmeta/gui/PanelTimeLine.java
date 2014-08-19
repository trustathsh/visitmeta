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
 * This file is part of visitmeta visualization, version 0.1.2,
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

/**
 * This class represents the part of the GUI which is responsible for viewing
 * and handling historic instances of a given map-server.
 *
 */
public class PanelTimeLine extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	private static final String TIMEFORMAT = "yyyy-MM-FF HH:mm:ss";

	private SortedMap<Long, Long> mChangesMap = null;
	private TimeHolder mTimeHolder = null;
	private Timer mDelay = null;

	private SpringLayout mSpringLayout = null;
	private JSpinner mSpinnerTimeStart = null;
	private SpinnerDateModel mSpinnerTimeStartDateModel = null;
	private JSpinner mSpinnerTimeEnd = null;
	private SpinnerDateModel mSpinnerTimeEndDateModel = null;
	private RangeSlider mSlider = null;
	private JCheckBox mChckbxLive = null;

	private ChangeListener mListenerSpinnerTimeStart = null;
	private ChangeListener mListenerSpinnerTimeEnd = null;
	private ChangeListener mListenerSlider = null;
	private ChangeListener mListenerChckbxLive = null;

	/**
	 * Constructor that initializes the TimeLine Panel.
	 * 
	 * @param timeHolder
	 *            Manages the timestamps for the historic view.
	 */
	public PanelTimeLine(TimeHolder timeHolder) {
		super();
		setPreferredSize(new Dimension(400, 33));
		setMinimumSize(new Dimension(400, 0));
		mTimeHolder = timeHolder;
		mSpringLayout = new SpringLayout();

		mSpinnerTimeStartDateModel = new SpinnerDateModel(new Date(0L), null, null, Calendar.SECOND);
		mSpinnerTimeStart = new JSpinner();
		mSpinnerTimeStart.setToolTipText("Start Time");
		mSpinnerTimeStart.setModel(mSpinnerTimeStartDateModel);
		mSpinnerTimeStart.setEditor(new JSpinner.DateEditor(mSpinnerTimeStart, TIMEFORMAT));
		mSpinnerTimeStart.setEnabled(false);

		mSlider = new RangeSlider();
		mSlider.setMinimum(0);
		mSlider.setMaximum(0);
		mSlider.setToolTipText("Start Time <---> End Time");
		mSlider.setEnabled(false);
		mSlider.setEnabledLowerDragging(false);
		mSlider.setEnabledUpperDragging(false);

		mSpinnerTimeEndDateModel = new SpinnerDateModel(new Date(0L), null, null, Calendar.SECOND);
		mSpinnerTimeEnd = new JSpinner();
		mSpinnerTimeEnd.setToolTipText("End Time");
		mSpinnerTimeEnd.setModel(mSpinnerTimeEndDateModel);
		mSpinnerTimeEnd.setEditor(new JSpinner.DateEditor(mSpinnerTimeEnd, TIMEFORMAT));
		mSpinnerTimeEnd.setEnabled(false);

		mChckbxLive = new JCheckBox("Live");
		mChckbxLive.setToolTipText("Show the current graph.");
		mChckbxLive.setSelected(true);
		mChckbxLive.setEnabled(false);

		setLayout(mSpringLayout);

		mSpringLayout.putConstraint(SpringLayout.NORTH, mSpinnerTimeStart, 6, SpringLayout.NORTH, this);
		mSpringLayout.putConstraint(SpringLayout.WEST, mSpinnerTimeStart, 15, SpringLayout.WEST, this);

		mSpringLayout.putConstraint(SpringLayout.NORTH, mSlider, 8, SpringLayout.NORTH, this);
		mSpringLayout.putConstraint(SpringLayout.WEST, mSlider, 8, SpringLayout.EAST, mSpinnerTimeStart);
		mSpringLayout.putConstraint(SpringLayout.EAST, mSlider, -8, SpringLayout.WEST, mSpinnerTimeEnd);

		mSpringLayout.putConstraint(SpringLayout.NORTH, mSpinnerTimeEnd, 6, SpringLayout.NORTH, this);
		mSpringLayout.putConstraint(SpringLayout.EAST, mSpinnerTimeEnd, -8, SpringLayout.WEST, mChckbxLive);

		mSpringLayout.putConstraint(SpringLayout.NORTH, mChckbxLive, 4, SpringLayout.NORTH, this);
		mSpringLayout.putConstraint(SpringLayout.EAST, mChckbxLive, -15, SpringLayout.EAST, this);

		add(mSpinnerTimeStart);
		add(mSlider);
		add(mSpinnerTimeEnd);
		add(mChckbxLive);

		addListener();
		mTimeHolder.addObserver(this);
	}

	/**
	 * Add Listener to GUI elements.
	 */
	private void addListener() {
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

		mDelay = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent pE) {
				mTimeHolder.setLiveView(mChckbxLive.isSelected(), false);
				if(mTimeHolder.isLiveView()) {
					mTimeHolder.setDeltaTimeStart(mTimeHolder.getNewestTime(), false);
					mTimeHolder.setDeltaTimeEnd(mTimeHolder.getNewestTime(), false);
				} else {
					mTimeHolder.setDeltaTimeStart(((Date) mSpinnerTimeStart.getValue()).getTime(), false);
					mTimeHolder.setDeltaTimeEnd(((Date) mSpinnerTimeEnd.getValue()).getTime(), false);
				}
				mTimeHolder.notifyObservers();
			}
		});

		mSpinnerTimeStart.addChangeListener(mListenerSpinnerTimeStart);
		mSpinnerTimeEnd.addChangeListener(mListenerSpinnerTimeEnd);
		mSlider.addChangeListener(mListenerSlider);
		mChckbxLive.addChangeListener(mListenerChckbxLive);
	}

	@Override
	public void update(Observable pO, Object pArg) {
		if (pO instanceof TimeHolder) {
			TimeHolder tmpHolder = (TimeHolder) pO;
			mSpinnerTimeStart.removeChangeListener(mListenerSpinnerTimeStart);
			mSpinnerTimeEnd.removeChangeListener(mListenerSpinnerTimeEnd);
			mSlider.removeChangeListener(mListenerSlider);
			mChckbxLive.removeChangeListener(mListenerChckbxLive);

			mChckbxLive.setEnabled(true);
			mChangesMap = mTimeHolder.getChangesMap();
			mSlider.setMaximum(mChangesMap.size() - 1);
			mSpinnerTimeStartDateModel.setStart(new Date(tmpHolder.getBigBang()));
			mSpinnerTimeStartDateModel.setEnd(new Date(tmpHolder.getNewestTime()));
			mSpinnerTimeEndDateModel.setStart(new Date(tmpHolder.getBigBang()));
			mSpinnerTimeEndDateModel.setEnd(new Date(tmpHolder.getNewestTime()));

			if (mTimeHolder.isLiveView()) {
				mSlider.setUpperValue(mChangesMap.size() - 1);
				mSpinnerTimeStartDateModel.setValue(new Date(mTimeHolder.getBigBang()));
				mSpinnerTimeEndDateModel.setValue(new Date(mTimeHolder.getNewestTime()));
			}

			mSpinnerTimeStart.addChangeListener(mListenerSpinnerTimeStart);
			mSpinnerTimeEnd.addChangeListener(mListenerSpinnerTimeEnd);
			mSlider.addChangeListener(mListenerSlider);
			mChckbxLive.addChangeListener(mListenerChckbxLive);
		}
	}
}
