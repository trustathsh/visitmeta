package de.hshannover.f4.trust.metalyzer.gui;

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
 * Copyright (C) 2012 - 2013 Trust@FHH
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

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.gui.characteristics.AnalysePanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.SemanticPanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.StatisticPanel;
import de.hshannover.f4.trust.metalyzer.gui.controls.Control;
import de.hshannover.f4.trust.metalyzer.gui.controls.ControlManager;
import de.hshannover.f4.trust.metalyzer.gui.controls.DeviceControl;
import de.hshannover.f4.trust.metalyzer.gui.controls.FrequencyControl;
import de.hshannover.f4.trust.metalyzer.gui.controls.GraphControl;
import de.hshannover.f4.trust.metalyzer.gui.controls.IpAddressControl;
import de.hshannover.f4.trust.metalyzer.gui.controls.MacAddressControl;
import de.hshannover.f4.trust.metalyzer.gui.controls.MeanControl;
import de.hshannover.f4.trust.metalyzer.gui.controls.SemanticControl;
import de.hshannover.f4.trust.metalyzer.gui.controls.StatisticControl;
import de.hshannover.f4.trust.metalyzer.gui.controls.UserControl;
import de.hshannover.f4.trust.metalyzer.gui.event.CharacteristicSelectionHandler;
import de.hshannover.f4.trust.metalyzer.gui.labels.CategoryLabels;
import de.hshannover.f4.trust.metalyzer.gui.network.AnalyseConnection;
import de.hshannover.f4.trust.metalyzer.gui.network.TimePollService;
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

public class MetalyzerGuiController extends Observable implements Observer {

	private static final Logger log = Logger.getLogger(MetalyzerGuiController.class);
	private AnalyseConnection mConnection;
	private MetalyzerPanel mMetalyzerPanel;
	private StatisticPanel mStatisticPanel;
	private SemanticPanel mSemanticPanel;
	private SortedSet<Long> mTimestampList;
	private TimePollService mTimePollService;
	
	private int mViewWidth = 1024; //default width
	private int mViewHeight = 768; //default height
	private TimeSelection mTimeSelection;
	
	public MetalyzerGuiController(int viewWidth, int viewHeight, AnalyseConnection connection, TimePollService pollService) {
		this.mViewWidth = viewWidth;
		this.mViewHeight = viewHeight;
		
		this.mConnection = connection;
		
		this.mTimestampList = new TreeSet<Long>();
		this.mTimePollService = pollService;
		this.mTimePollService.start();
		this.mTimePollService.addObserver(this);
		
		this.mMetalyzerPanel = new MetalyzerPanel(this);
		initControls();
	}
	
	public MetalyzerGuiController(AnalyseConnection connection, TimePollService pollService) {
		this.mConnection = connection;
		
		this.mTimestampList = new TreeSet<Long>();
		this.mTimePollService = pollService;
		this.mTimePollService.start();
		this.mTimePollService.addObserver(this);
		
		this.mMetalyzerPanel = new MetalyzerPanel(this);
		initControls();
	}

	/**
	 * Initializes the Controls
	 * */
	private void initControls() {	
		ControlManager.registerControl(CategoryLabels.FREQUENCY, new FrequencyControl(this));
		ControlManager.registerControl(CategoryLabels.MEAN, new MeanControl(this));
		ControlManager.registerControl(CategoryLabels.GRAPH, new GraphControl(this));
		ControlManager.registerControl(CategoryLabels.USER, new UserControl(this));
		ControlManager.registerControl(CategoryLabels.IP, new IpAddressControl(this));
		ControlManager.registerControl(CategoryLabels.MAC, new MacAddressControl(this));
		ControlManager.registerControl(CategoryLabels.DEVICE, new DeviceControl(this));
	}
	
	/**
	 * Returns an instance of the {@link MetalyzerPanel}
	 * @return {@link MetalyzerPanel}
	 * */
	public MetalyzerPanel getMetalyzerView() {
		return mMetalyzerPanel;
	}

	/**
	 * Initializes the {@link CategoryPanel}
	 * 
	 * @param selectionPath
	 * */
	public void selectCategory(TreePath selectionPath) {
		CategoryPanel selectedCategory = null;
		DefaultMutableTreeNode categoryNode = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
		if(categoryNode.getUserObject() instanceof CategoryPanel) {
			selectedCategory = (CategoryPanel) categoryNode.getUserObject();
		}
		if(selectedCategory != null) {
			//a new instance of this panel is required, so it can be added more than one time to a swing component
			CategoryPanel categoryPanel = selectedCategory.newInstance();
			categoryPanel.setCharacteristicHandler(new CharacteristicSelectionHandler(this, selectedCategory.getCategory()));
			mMetalyzerPanel.addTab(categoryPanel.getCategory(), categoryPanel);
			fillAnalysePanel(categoryPanel.getCategory(), categoryPanel.getSelectedCharacteristic());
		} else {
			log.warn("No category");
		}
	}
	
	@Override
	public void update(Observable ob, Object o) {
		if(o instanceof SortedSet) {
			mTimestampList = (SortedSet<Long>)o;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	/**
	 * Updates the {@link TimeSelectionView} of the given {@link AnalysePanel} and the panel itself.
	 * @param analysePanel 
	 * @param isLive if true the given {@link AnalysePanel} will be updated
	 * */
	public void updateAnalysePanels(AnalysePanel analysePanel, boolean isLive) {
		TimeSelectionView timeSelectionView = analysePanel.getTimeSelectionView();
		timeSelectionView.setTimestampList(getTimestampList());
		if(isLive) {
			log.debug(analysePanel.getName()+" time live: "+getLastTimestamp());
			updateAnalysePanels(analysePanel, 0, getLastTimestamp());
		} else {
			if(timeSelectionView.getSelectionMode() == TimeSelection.Timestamp) {
				log.debug("time update: "+timeSelectionView.getTimeStampValue());
			} else if(timeSelectionView.getSelectionMode() == TimeSelection.Intervall) {
				log.debug("time update from: "+timeSelectionView.getTimeStartValue()+" to: "+timeSelectionView.getTimeEndValue());
			}
		}
	}

	/**
	 * Updates the given {@link AnalysePanel} with given timestamp intervall t1 and t2
	 * @param analaysePanel the panel that should be updated e.g. {@link StatisticPanel} or a {@link SemanticPanel}
	 * @param t1 a unix timestamp, and the first timestamp of the time intervall
	 * @param t2 a unix timestamp, and the last timestamp of the time intervall
	 * */
	public void updateAnalysePanels(AnalysePanel analysePanel, long t1, long t2) {
		TimeSelectionView timeSelectionView = analysePanel.getTimeSelectionView();
		
		int metadataCount = 0;
		int identifierCount = 0;
		
		if(timeSelectionView.getSelectionMode() == TimeSelection.Intervall) {
			metadataCount = getMetadataCount(t1, t2);
			identifierCount = getIdentiferCount(t1, t2);
		} else if(timeSelectionView.getSelectionMode() == TimeSelection.Timestamp) {
			metadataCount = getMetadataCount(t2);
			identifierCount = getIdentiferCount(t2);
		} else if(timeSelectionView.getSelectionMode() == TimeSelection.Live){
			metadataCount = getMetadataCount();
			identifierCount = getIdentiferCount();
		}
		
		String category = analysePanel.getCategory();
		if (analysePanel instanceof StatisticPanel) {
			mStatisticPanel = (StatisticPanel) analysePanel;
			mStatisticPanel.setMetadataCount(metadataCount);
			mStatisticPanel.setIdentifierCount(identifierCount);

			ControlManager.getControl(category).updateControl(mStatisticPanel, t1, t2);
		} 
		if (analysePanel instanceof SemanticPanel) {
			mSemanticPanel = (SemanticPanel) analysePanel;
			ControlManager.getControl(category).updateControl(mSemanticPanel, t1, t2);
		}
	}
	
	/**
	 * Updates the given {@link AnalysePanel} with given an item
	 * @param analaysePanel the panel that should be updated e.g. {@link UserPanel}
	 * @param item e.g. the name of a user or an ip-address
	 * */
	public void updateAnalysePanels(AnalysePanel analaysePanel, String item) {
		String category = analaysePanel.getCategory();
		if (analaysePanel instanceof SemanticPanel) {
			mSemanticPanel = (SemanticPanel) analaysePanel;
			Control control = ControlManager.getControl(category);
			if(control instanceof SemanticControl) {
				SemanticControl semanticControl = (SemanticControl) control;
				semanticControl.updateItem(mSemanticPanel, item);
			}
		}
	}
	
	/**
	 * Updates the given {@link AnalysePanel} with given statisticPanel with a chart
	 * to adjust the scroll area that is modified by the {@link ChartAdjustmentHandler}
	 * @param analaysePanel the panel that should be updated e.g. {@link StatisticPanel}
	 * @param lowerBoundary the minimum value that is allowed for the chart scrollbar, depends on the scroll range
	 * @param upperBoundary the maximum value that is allowed for the chart scrollbar, depends on the scroll range
	 * */
	public void updateAnalysePanels(AnalysePanel analaysePanel, int lowerBoundary, int upperBoundary) {
		String category = analaysePanel.getCategory();
		if (analaysePanel instanceof StatisticPanel) {
			mStatisticPanel = (StatisticPanel) analaysePanel;
			Control control = ControlManager.getControl(category);
			if(control instanceof StatisticControl) {
				StatisticControl statisticControl = (StatisticControl) control;
				statisticControl.updateChartScrollArea(mStatisticPanel, lowerBoundary, upperBoundary);
			}
		}
	}

	/**
	 * Fills the {@link AnalysePanel} for the first time with data
	 * @param category the category of the analysePanel that will be filled
	 * @param characteristic the characteristic that is currently selected
	 * */
	public void fillAnalysePanel(String category, String characteristic) {
		CategoryPanel selectionPanel = mMetalyzerPanel.getFocusedTab();
		AnalysePanel analysePanel = selectionPanel.getAnalysePanel();
		TimeSelectionView timeSelectionView = analysePanel.getTimeSelectionView();
		
		int metadataCount = 0;
		int identifierCount = 0;
		
		long t1 = 0;
		long t2 = 0;
		if(timeSelectionView.getSelectionMode() == TimeSelection.Intervall) {
			t1 = timeSelectionView.getTimeStartValue();
			t2 = timeSelectionView.getTimeEndValue();
			metadataCount = getMetadataCount(t1, t2);
			identifierCount = getIdentiferCount(t1, t2);
		} else if(timeSelectionView.getSelectionMode() == TimeSelection.Timestamp) {
			t1 = 0;
			t2 = timeSelectionView.getTimeStampValue();
			metadataCount = getMetadataCount(t2);
			identifierCount = getIdentiferCount(t2);
		} else if(timeSelectionView.getSelectionMode() == TimeSelection.Live){
			t1 = 0;
			t2 = getLastTimestamp();
			metadataCount = getMetadataCount();
			identifierCount = getIdentiferCount();
		}

		if (analysePanel instanceof StatisticPanel) {
			// Statistic
			StatisticPanel statisticPanel = (StatisticPanel) analysePanel;
			this.addObserver(statisticPanel);

			statisticPanel.setMetadataCount(metadataCount);
			statisticPanel.setIdentifierCount(identifierCount);

			ControlManager.getControl(category).fillControl(statisticPanel, t1, t2);
		} else {
			// Semantic
			SemanticPanel semanticPanel = (SemanticPanel) analysePanel;
			this.addObserver(semanticPanel);
			ControlManager.getControl(category).fillControl(semanticPanel, t1, t2);
		}
	}

	/**
	 * Returns the first timestamp of current available timestamps
	 * 
	 * @return first timestamp
	 */
	public long getFirstTimestamp() {
		long first = 0;
		if (mTimestampList != null && mTimestampList.size() > 0) {
			first = mTimestampList.first();
		}
		return first;
	}

	/**
	 * Returns the last timestamp of current available timestamps
	 * 
	 * @return last timestamp
	 */
	public long getLastTimestamp() {
		long last = 0;
		if (mTimestampList != null && mTimestampList.size() > 0) {
			last = mTimestampList.last();
		}
		return last;
	}

	/**
	 * Returns the metadata count according the given timestamps t1 and t2
	 * 
	 * @param t1
	 * @param t2
	 * @return metadata count
	 */
	private int getMetadataCount(long t1, long t2) {
		int metadataCount = 0;
		try {
			metadataCount = mConnection.getMetadataCount(t1, t2);
		} catch (ExecutionException e) {
			log.error("Metadata count request couldn't be executed.");
			mTimePollService.shutdown();
		} catch (TimeoutException e) {
			log.error("Request timeout exceeded");
		} catch (InterruptedException e) {
			log.error(e.getMessage());
			mTimePollService.shutdown();
		} 
		return metadataCount;
	}
	
	/**
	 * Returns the metadata count according the given timestamp 
	 * @param timestamp
	 * @return metadata count
	 */
	private int getMetadataCount(long timstamp) {
		int metadataCount = 0;
		try {
			metadataCount = mConnection.getMetadataCount(timstamp);
		} catch (ExecutionException e) {
			log.error("Metadata count request couldn't be executed.");
			mTimePollService.shutdown();
		} catch (TimeoutException e) {
			log.error("Request timeout exceeded");
		} catch (InterruptedException e) {
			log.error(e.getMessage());
			mTimePollService.shutdown();
		} 
		return metadataCount;
	}
	
	/**
	 * Returns the metadata count according the current timestamp
	 * @param timestamp
	 * @return metadata count
	 */
	private int getMetadataCount() {
		int metadataCount = 0;
		try {
			metadataCount = mConnection.getMetadataCount();
		} catch (ExecutionException e) {
			log.error("Metadata count request couldn't be executed.");
			mTimePollService.shutdown();
		} catch (TimeoutException e) {
			log.error("Request timeout exceeded");
		} catch (InterruptedException e) {
			log.error(e.getMessage());
			mTimePollService.shutdown();
		} 
		return metadataCount;
	}

	/**
	 * Returns the identifer count according the given timestamps t1 and t2
	 * 
	 * @param t1
	 * @param t2
	 * @return identifer count
	 */
	private int getIdentiferCount(long t1, long t2) {
		int identiderCount = 0;
		try {
			identiderCount = mConnection.getIdentifierCount(t1, t2);
		} catch (ExecutionException e) {
			log.error("Identifer count request couldn't be executed.");
			mTimePollService.shutdown();
		} catch (TimeoutException e) {
			log.error("Request timeout exceeded");
		} catch (InterruptedException e) {
			log.error(e.getMessage());
			mTimePollService.shutdown();
		} 
		return identiderCount;
	}
	
	/**
	 * Returns the identifer count according the given timestamp
	 * 
	 * @param t1
	 * @param t2
	 * @return identifer count
	 */
	private int getIdentiferCount(long timestamp) {
		int identiderCount = 0;
		try {
			identiderCount = mConnection.getIdentifierCount(timestamp);
		} catch (ExecutionException e) {
			log.error("Identifer count request couldn't be executed.");
			mTimePollService.shutdown();
		} catch (TimeoutException e) {
			log.error("Request timeout exceeded");
		} catch (InterruptedException e) {
			log.error(e.getMessage());
			mTimePollService.shutdown();
		} 
		return identiderCount;
	}
	
	/**
	 * Returns the identifer count according to the current timestamp
	 * 
	 * @param t1
	 * @param t2
	 * @return identifer count
	 */
	private int getIdentiferCount() {
		int identiderCount = 0;
		try {
			identiderCount = mConnection.getIdentifierCount();
		} catch (ExecutionException e) {
			log.error("Identifer count request couldn't be executed.");
			mTimePollService.shutdown();
		} catch (TimeoutException e) {
			log.error("Request timeout exceeded");
		} catch (InterruptedException e) {
			log.error(e.getMessage());
			mTimePollService.shutdown();
		} 
		return identiderCount;
	}
	
	/**
	 * Returns the current focused tab
	 * @return the actual focused {@link CategoryPanel}
	 * */
	public CategoryPanel getCurrentSelectionPanel() {
		CategoryPanel selection = mMetalyzerPanel.getFocusedTab();
		return selection;
	}
	
	/**
	 * Returns an instace of the facade
	 * @return {@link AnalyseFacade} instance
	 * */
	public AnalyseConnection getConnection() {
		return mConnection;
	}

	/**
	 * Cleanup is called when a tab is closed. Thus the {@link AnalysePanel} of
	 * the current tab is removed from the observer list of the
	 * {@link TimePollService}.
	 * 
	 * @param panel
	 *            the {@link CategoryPanel} of the current tab
	 * @see Observable#deleteObserver(Oberserver)
	 * */
	public void cleanup(CategoryPanel panel) {
		AnalysePanel p = panel.getAnalysePanel();
		this.deleteObserver(p);
	}

	/**
	 * Returns the timestamp list of the poll service
	 * 
	 * @return timestamp list
	 * */
	public SortedSet<Long> getTimestampList() {
		return mTimestampList;
	}
	
	/**
	 * Sets the size for the views dimensions
	 * @param wndWidth the window width
	 * @param wndHeight the window height
	 * */
	public void setViewSize(int wndWidth, int wndHeight) {
		this.mViewWidth = wndWidth;
		this.mViewHeight = wndHeight;
		mMetalyzerPanel.setPreferredSize(new Dimension(wndWidth, wndHeight));
	}
	
	/**
	 * Returns the width of the views 
	 * @return view width
	 * */
	public int getViewWidth() {
		return mViewWidth;
	}
	
	/**
	 * Returns the height of the views 
	 * @return view height
	 * */
	public int getViewHeight() {
		return mViewHeight;
	}

	/**
	 * Sets the an instance of the {@link TimeSelection}
	 * @param selection the timeSelection
	 * */
	public void setTimeSelection(TimeSelection selection) {
		this.mTimeSelection = selection;
	}
	
	/**
	 * Returns an instance of the {@link TimeSelection}
	 * @return timeSelection instance
	 * */
	public TimeSelection getTimeSelection() {
		return mTimeSelection;
	}
}
