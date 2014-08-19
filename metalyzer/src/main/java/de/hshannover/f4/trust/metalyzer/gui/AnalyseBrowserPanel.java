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
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;
import de.hshannover.f4.trust.metalyzer.gui.event.BrowserSelectionHandler;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class AnalyseBrowserPanel extends JPanel {
	
	private static final Logger log = Logger.getLogger(AnalyseBrowserPanel.class);
	
	private JScrollPane mScrollPane;
	private JTree mTree;
	private DefaultMutableTreeNode mRootCategory;
	private Map<String,DefaultMutableTreeNode> mSubCategories;

	private MetalyzerGuiController mGuiController;
	
	
//	  tree structure
//	   \-- analysis (root category)
//	     \--statistics (sub category)
//	       +-- categorypanel[frequency] (entry )
//	 			+-- frequeny of metadata (charateristics) 
	
	public AnalyseBrowserPanel(String rootCategory, MetalyzerGuiController guiController) {
		this.mGuiController = guiController;
		this.setPreferredSize(new Dimension(200,600));
		this.setLayout(new BorderLayout());

		mRootCategory = new DefaultMutableTreeNode(rootCategory);
		mTree = new JTree(mRootCategory);
		mTree.addMouseListener(new BrowserSelectionHandler(mTree, mGuiController));
		
		mSubCategories = new HashMap<String,DefaultMutableTreeNode>();
		mScrollPane = new JScrollPane(mTree);	
		this.add(mScrollPane);
	}

	/**
	 * Adds a new sub category to the root category
	 * @param subCategoryName the name of the new category
	 * */
	public void addSubCategory(String subCategoryName) {
		DefaultMutableTreeNode subCategory = new DefaultMutableTreeNode(subCategoryName);
		if(!mSubCategories.containsKey(subCategoryName)) {
			mSubCategories.put(subCategoryName, subCategory);
			mRootCategory.add(subCategory);
		} else {
			log.trace("Duplicated Subcategory not allowed.");
		}
	}
	
	/**
	 * Adds a new {@link CategoryPanel} entry to the sub category
	 * @param subCategoryName
	 * @param categoryEntry
	 * */
	public void addCategoryEntry(String subcategory, CategoryPanel categoryEntry) {
		if(mSubCategories.containsKey(subcategory)) {
			DefaultMutableTreeNode subcat = mSubCategories.get(subcategory);
			DefaultMutableTreeNode entry = new DefaultMutableTreeNode(categoryEntry);
			subcat.add(entry);	
		} else {
			log.trace("SubCategory not found.");
		}
	}
}