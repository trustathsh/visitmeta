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

import java.awt.event.ActionEvent;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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

public class TimeIntervallHandler implements ChangeListener {

	private long value1;
	private long value2;
	private MetalyzerGuiController mGuiController;
	private AnalysePanel mAnalysePanel;
	private TimeSelectionView mTimeSelectionView;

	public TimeIntervallHandler(MetalyzerGuiController controller,
			AnalysePanel panel, TimeSelectionView view) {
		this.mAnalysePanel = panel;
		this.mGuiController = controller;
		this.mTimeSelectionView = view;

		this.value1 = view.getTimeStartValue();
		this.value2 = view.getTimeEndValue();
	}

	/**
	 * Triggers an {@link ChangeEvent} when the user changes the 
	 * values of the {@link TimeSelectionView} intervall selction
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		JSpinner changed = (JSpinner) e.getSource();
		JSpinner start = mTimeSelectionView.getTimeStartSelector();
		JSpinner end = mTimeSelectionView.getTimeEndSelector();

		if (mTimeSelectionView.getSelectionMode() == TimeSelection.Intervall) {
			
			value1 = mTimeSelectionView.getTimeStartValue();
			value2 = mTimeSelectionView.getTimeEndValue();


			if (value1 < value2) {
				mGuiController.updateAnalysePanels(mAnalysePanel, value1, value2);
			} else {
				if (changed.equals(start)) {
					if (start.getPreviousValue() != null)
						start.setValue(start.getPreviousValue());
				}
				if (changed.equals(end)) {
					if (end.getNextValue() != null)
						end.setValue(end.getNextValue());
				}
			}
		}
	}

}
