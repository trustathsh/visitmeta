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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.hshannover.f4.trust.metalyzer.gui.CategoryPanel;
import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.AnalysePanel;
import de.hshannover.f4.trust.metalyzer.gui.misc.ClosableTabbedPane;
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

public class TabChangeHandler implements ChangeListener {

	private MetalyzerGuiController mGuiController;
	private ClosableTabbedPane mCloseTabPane;
	private int curTabIndex = 0;
	private int prevTabIndex = 0;
	
	private TimeSelection prevSelection;
	private long prevTimeStamp = 0;
	private long prevTimeStart = 0;
	private long prevTimeEnd = 0;
	
	public TabChangeHandler(MetalyzerGuiController guiController, ClosableTabbedPane closeTabPane) {
		this.mGuiController = guiController;
		this.mCloseTabPane = closeTabPane;
		this.prevSelection = mGuiController.getTimeSelection();
	}
	
	/**
	 * Triggers a {@link ChangeEvent}, when a the tab changes. Therefore the preferences of the previous tab
	 * where transfered to the current tab
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		prevTabIndex = curTabIndex;
		curTabIndex = mCloseTabPane.getSelectedIndex();
		
		//Prevents Crash when closing tabs
		prevTabIndex = Math.min(prevTabIndex, mCloseTabPane.getTabCount() - 1);
		if(prevTabIndex != curTabIndex && prevTabIndex != -1) {
			CategoryPanel prevSelectionPanel = (CategoryPanel) mCloseTabPane.getComponentAt(prevTabIndex);
			AnalysePanel prevAnalysePanel = prevSelectionPanel.getAnalysePanel();
			TimeSelectionView prevTimeView = prevAnalysePanel.getTimeSelectionView();

			if(prevSelection != TimeSelection.None) {
				prevSelection = prevTimeView.getSelectionMode();
				prevTimeStamp = prevTimeView.getTimeStampValue();
				prevTimeStart = prevTimeView.getTimeStartValue();
				prevTimeEnd = prevTimeView.getTimeEndValue();
			}
		}
		
		if(curTabIndex != -1) {
			CategoryPanel curSelectionPanel = (CategoryPanel) mCloseTabPane.getComponentAt(curTabIndex);
			AnalysePanel curAnalysePanel = curSelectionPanel.getAnalysePanel();
			TimeSelectionView curTimeView = curAnalysePanel.getTimeSelectionView();
		
			if(prevSelection != TimeSelection.None) {
				curTimeView.setSelectionMode(prevSelection);
				if(prevTimeStamp != 0) {
					curTimeView.setTimeStampValue(prevTimeStamp);
				}
				if(prevTimeStart != 0) {
					curTimeView.setTimeStartValue(prevTimeStart);
				}
				if(prevTimeEnd != 0) {
					curTimeView.setTimeEndValue(prevTimeEnd);
				}
			}
		}

	}

}
