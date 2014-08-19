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
package de.hshannover.f4.trust.metalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.hshannover.f4.trust.metalyzer.gui.characteristics.AnalysePanel;
import de.hshannover.f4.trust.metalyzer.gui.event.CharacteristicSelectionHandler;
import de.hshannover.f4.trust.metalyzer.gui.views.ComboBoxView;
import de.hshannover.f4.trust.metalyzer.gui.views.ViewContainer;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class CategoryPanel extends JPanel {
	
	private MetalyzerGuiController mGuiController;
	private String mCategory;
	
	private LinkedHashMap<String,AnalysePanel> mAnalysePanels;
	private ViewContainer mViewContainer;
	private ComboBoxView<String> mSelectionBoxView;	
	private JScrollPane mSelectionScrollPane;
	
	public CategoryPanel(MetalyzerGuiController guiController, String category) {
		this.mGuiController = guiController;
		this.mCategory = category;
	
		this.setPreferredSize(new Dimension(mGuiController.getViewWidth() - 400, mGuiController.getViewHeight() - 200));
		this.setLayout(new BorderLayout());
		
		mAnalysePanels = new LinkedHashMap<String,AnalysePanel>();
		mSelectionScrollPane = new JScrollPane();
		mSelectionScrollPane.setBorder(BorderFactory.createEmptyBorder());
		mSelectionBoxView = new ComboBoxView<String>("Characteristics: ", true);
		mSelectionBoxView.setViewIndent(110);
		mSelectionBoxView.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		mViewContainer = new ViewContainer();
		mViewContainer.addView(mSelectionBoxView);
		
		this.add(mViewContainer, BorderLayout.NORTH);
	}
	
	/**
	 * Creates an new instace of this panel
	 * @return CategoryPanel instance
	 * */
	public CategoryPanel newInstance() {
		CategoryPanel copy = new CategoryPanel(mGuiController, mCategory);
		Map<String,AnalysePanel> cp = getCharacteristicPanels();
		for(String key : cp.keySet()) {
			copy.addCharacteristicPanel(cp.get(key));
		}
		return copy;
	}
	
	private Map<String,AnalysePanel> getCharacteristicPanels() {
		return mAnalysePanels;
	}
	
	/**
	 * Returns the related category of this CategoryPanel e.g. "Mean" of "Frequency"
	 * for more information of the currently available categories see {@link CategoryLabels}
	 * @return category 
	 * */
	public String getCategory() {
		return mCategory;
	}
	
	/**
	 * Sets the CategorySelectionHandler
	 * @param {@link CharacteristicSelectionHandler} selectionHandler
	 */
	public void setCharacteristicHandler(CharacteristicSelectionHandler selectionHandler) {
		mSelectionBoxView.addListener(selectionHandler);
	}
	
	/**
	 * Sets the description of drop down menu
	 * @param text
	 */
	public void setDescription(String text) {
		mSelectionBoxView.setDescription(text);
	}
	
	/**
	 * Returns the current selected characteristic, e.g "Mean of Links" of "Frequency of Metadata"
	 * for more information of the currently available categories see {@link CharacteristicLabels}
	 * @return selected characteristic
	 * @see JComboBox#getSelectedItem()
	 * */
	public String getSelectedCharacteristic() {
		return mSelectionBoxView.getSelectedItem();
	}
	
	/**
	 * Stores the analysePanel instance in a map
	 * @param analysePanel
	 */
	public void addCharacteristicPanel(AnalysePanel analysePanel) {
		analysePanel.setCategory(mCategory);
		mAnalysePanels.put(analysePanel.getName(), analysePanel);
		mSelectionBoxView.addItem(analysePanel.getName());
	}
	
	/**
	 * Returns a AnalysePanel by given name
	 * @return {@link AnalysePanel} 
	 */
	public AnalysePanel getAnalysePanel(String name) {
		AnalysePanel analysePanel = null;
		if(mAnalysePanels.containsKey(name)) {
			analysePanel = mAnalysePanels.get(name);
			mSelectionScrollPane.setViewportView(analysePanel);
			this.add(mSelectionScrollPane, BorderLayout.CENTER);
			this.revalidate();
			this.repaint();
		}
		return analysePanel;
	}
	
	/**
	 * Returns a AnalysePanel according to current selected Characteristic
	 * @return {@link AnalysePanel} 
	 */
	public AnalysePanel getAnalysePanel() {
		String characteristic = getSelectedCharacteristic();
		return getAnalysePanel(characteristic);
	}
	
	@Override
	public String toString() {
		return mCategory;
	}
}
