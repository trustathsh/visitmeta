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
package de.hshannover.f4.trust.metalyzer.gui.event;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;

import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.AnalysePanel;
import de.hshannover.f4.trust.metalyzer.gui.views.TimeSelectionView;
import de.hshannover.f4.trust.metalyzer.gui.views.TimeSelectionView.TimeSelection;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class TimeSelectionHandler implements ItemListener {
	
	private AnalysePanel mAnalysePanel;
	private TimeSelectionView mTimeSelectionView;
	private MetalyzerGuiController mGuiController;
	private TimeSelection mPrevSelection;

	public TimeSelectionHandler(MetalyzerGuiController guiControl, AnalysePanel analysePanel) {
		this.mGuiController = guiControl;
		this.mAnalysePanel = analysePanel;
		this.mTimeSelectionView = analysePanel.getTimeSelectionView();
		
		this.mPrevSelection = mTimeSelectionView.getSelectionMode();
	}

	/**
	 * Triggers an {@link ItemEvent} when the user selects one of the three time options:
	 * time intervall, timestamp or live
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		JSpinner timeStart = mTimeSelectionView.getTimeStartSelector();
		JSpinner timeEnd = mTimeSelectionView.getTimeEndSelector();
		JSpinner timeStamp = mTimeSelectionView.getTimeStampSelector();
		
		JRadioButton intervall = mTimeSelectionView.getTimeIntervallButton();
		JRadioButton stamp = mTimeSelectionView.getTimeStampButton();
		JRadioButton live = mTimeSelectionView.getTimeLiveButton();

		timeStamp.setEnabled(stamp.isSelected());
		timeStart.setEnabled(intervall.isSelected());
		timeEnd.setEnabled(intervall.isSelected());
		
		if(intervall.isSelected()) {
			mTimeSelectionView.setSelectionMode(TimeSelection.Intervall);
			if(mPrevSelection == TimeSelection.Timestamp) {
				long start = mTimeSelectionView.getTimeStartValue();
				long timestamp = mTimeSelectionView.getTimeStampValue();

				if(timestamp > start) {
					mTimeSelectionView.setTimeEndValue(timestamp);	
				}
			} else if(mPrevSelection == TimeSelection.Live) {
				mTimeSelectionView.setTimeEndValue(mGuiController.getLastTimestamp());
			}
			
			mGuiController.updateAnalysePanels(mAnalysePanel, mTimeSelectionView.getTimeStartValue(), mTimeSelectionView.getTimeEndValue());
			mPrevSelection = TimeSelection.Intervall;
		} else if(stamp.isSelected()) {
			mTimeSelectionView.setSelectionMode(TimeSelection.Timestamp);
			if(mPrevSelection == TimeSelection.Intervall) {
				mTimeSelectionView.setTimeStampValue(mTimeSelectionView.getTimeStampValue());
			} else if(mPrevSelection == TimeSelection.Live) {
				mTimeSelectionView.setTimeStampValue(mGuiController.getLastTimestamp());
			}
			mGuiController.updateAnalysePanels(mAnalysePanel, 0, mTimeSelectionView.getTimeStampValue());
			mPrevSelection = TimeSelection.Timestamp;
		} else if(live.isSelected())  {
			mTimeSelectionView.setSelectionMode(TimeSelection.Live);
			mPrevSelection = TimeSelection.Live;	
		}
		mGuiController.setTimeSelection(mTimeSelectionView.getSelectionMode());
	}

}
