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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.hshannover.f4.trust.metalyzer.gui.characteristics.DevicePanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.FrequencyPanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.GraphPanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.IpAddressPanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.MacAddressPanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.MeanPanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.UserPanel;
import de.hshannover.f4.trust.metalyzer.gui.charts.ChartPanelFactory.ChartType;
import de.hshannover.f4.trust.metalyzer.gui.event.CharacteristicSelectionHandler;
import de.hshannover.f4.trust.metalyzer.gui.event.TabChangeHandler;
import de.hshannover.f4.trust.metalyzer.gui.event.TabCloseHandler;
import de.hshannover.f4.trust.metalyzer.gui.labels.CategoryLabels;
import de.hshannover.f4.trust.metalyzer.gui.labels.CharacteristicLabels;
import de.hshannover.f4.trust.metalyzer.gui.labels.InfoLabels;
import de.hshannover.f4.trust.metalyzer.gui.misc.ClosableTabbedPane;

/** 
 * Project: Metalyzer
 * Author: Daniel HÃ¼lse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class MetalyzerPanel extends JPanel {

	private MetalyzerGuiController mGuiController;

	private AnalyseBrowserPanel mBrowserPanel;
	private ClosableTabbedPane mTabs;

	public MetalyzerPanel(MetalyzerGuiController guiController) {
		this.mGuiController = guiController;
		this.setLayout(new BorderLayout());
		initPanel();
	}
	
	private void initPanel() {
		mBrowserPanel = new AnalyseBrowserPanel("Analysis", mGuiController);
		mBrowserPanel.addSubCategory(CategoryLabels.STATISTICS);
		mBrowserPanel.addSubCategory(CategoryLabels.SEMANTICS);

		CategoryPanel frequencyCategory = new CategoryPanel(mGuiController, CategoryLabels.FREQUENCY);
		/* Statisitic Characteristics */
		FrequencyPanel f1 = new FrequencyPanel(mGuiController, CharacteristicLabels.FREQUENCY_IDENTIFER,ChartType.BAR, ChartType.PIE);
		f1.setInfoDescription(InfoLabels.FREQUENCY_IDENTIFER_INFO);
		f1.setPropertyNames("Identifer","Count of Identifer","Percentage of Identifer");
		f1.setValueAxisLabel("Count of Identifer");
		f1.setCategoryAxisLabel("Identifer");
		frequencyCategory.addCharacteristicPanel(f1);
		
		FrequencyPanel f2 = new FrequencyPanel(mGuiController, CharacteristicLabels.FREQUENCY_METADATA,ChartType.BAR, ChartType.PIE);
		f2.setInfoDescription(InfoLabels.FREQUENCY_METADATA_INFO);
		f2.setPropertyNames("Metadata","Count of Metadata","Percentage of Metadata");
		f2.setValueAxisLabel("Count of Metadata");
		f2.setCategoryAxisLabel("Metadata");
		frequencyCategory.addCharacteristicPanel(f2);
		
		FrequencyPanel f3 = new FrequencyPanel(mGuiController, CharacteristicLabels.FREQUENCY_ROLES,ChartType.BAR, ChartType.PIE);
		f3.setInfoDescription(InfoLabels.FREQUENCY_ROLES_INFO);
		f3.setPropertyNames("Roles","Count of Roles","Percentage of Roles");
		f3.setValueAxisLabel("Count of Roles");
		f3.setCategoryAxisLabel("Roles");
		frequencyCategory.addCharacteristicPanel(f3);
		
		// Mean Category
		CategoryPanel meanCategory = new CategoryPanel(mGuiController, CategoryLabels.MEAN);
		
		MeanPanel m1 = new MeanPanel(mGuiController, CharacteristicLabels.MEAN_LINKS);
		m1.setInfoDescription(InfoLabels.MEAN_LINKS_INFO);
		m1.setPropertyNames("Identifer", "Mean of Links per Identifer");
		meanCategory.addCharacteristicPanel(m1);
		
		// Graph Category
		CategoryPanel graphCategory = new CategoryPanel(mGuiController, CategoryLabels.GRAPH);
		
		GraphPanel g1 = new GraphPanel(mGuiController, CharacteristicLabels.GRAPH_NODES, ChartType.LINE);
		g1.setInfoDescription(InfoLabels.GRAPH_NODES_INFO);
		g1.setPropertyNames("Timestamp", "Count of Nodes", "Deviation");
		g1.setValueAxisLabel("Count of Nodes");
		g1.setCategoryAxisLabel("Timestamps");
		graphCategory.addCharacteristicPanel(g1);
		
		GraphPanel g2 = new GraphPanel(mGuiController, CharacteristicLabels.GRAPH_EDGES, ChartType.LINE);
		g2.setInfoDescription(InfoLabels.GRAPH_EDGES_INFO);
		g2.setPropertyNames("Timestamp", "Count of Edges", "Deviation");
		g2.setValueAxisLabel("Count of Edges");
		g2.setCategoryAxisLabel("Timestamps");
		graphCategory.addCharacteristicPanel(g2);
		
		GraphPanel g3 = new GraphPanel(mGuiController, CharacteristicLabels.GRAPH_MEAN, ChartType.LINE);
		g3.setInfoDescription(InfoLabels.GRAPH_MEAN_INFO);
		g3.setPropertyNames("Timestamp", "Mean of Metadata", "Deviation");
		g3.setValueAxisLabel("Mean of Edges per Node");
		g3.setCategoryAxisLabel("Timestamps");
		graphCategory.addCharacteristicPanel(g3);
		
		mBrowserPanel.addCategoryEntry(CategoryLabels.STATISTICS, frequencyCategory);
		mBrowserPanel.addCategoryEntry(CategoryLabels.STATISTICS, meanCategory);
		mBrowserPanel.addCategoryEntry(CategoryLabels.STATISTICS, graphCategory);
	
		// Semantic Characteristics 
		
		// User-Panels
		
		CategoryPanel userCategory = new CategoryPanel(mGuiController, CategoryLabels.USER);
		
		UserPanel u1 = new UserPanel(mGuiController, CharacteristicLabels.USER_IP);
		u1.setInfoDescription(InfoLabels.USER_IP_INFO);
		u1.setPropertyNames("IP-Address", "IP-Version");
		userCategory.addCharacteristicPanel(u1);
		
		UserPanel u2 = new UserPanel(mGuiController, CharacteristicLabels.USER_MAC);
		u2.setInfoDescription(InfoLabels.USER_MAC_INFO);
		u2.setPropertyNames("Mac-Address");
		userCategory.addCharacteristicPanel(u2);
		
		UserPanel u3 = new UserPanel(mGuiController, CharacteristicLabels.USER_DEVICES);
		u3.setInfoDescription(InfoLabels.USER_DEVICES_INFO);
		u3.setPropertyNames("Devices", "Attributes");
		userCategory.addCharacteristicPanel(u3);
		
		UserPanel u4 = new UserPanel(mGuiController, CharacteristicLabels.USER_AUTH);
		u4.setInfoDescription(InfoLabels.USER_AUTH_INFO);
		u4.setPropertyNames("User", "Role");
		userCategory.addCharacteristicPanel(u4);
	
		UserPanel u5 = new UserPanel(mGuiController, CharacteristicLabels.USER_ALL);
		u5.setInfoDescription(InfoLabels.USER_ALL_INFO);
		u5.setPropertyNames("IP-Address", "IP-Version", "Mac-Address", "Device");
		userCategory.addCharacteristicPanel(u5);
		
		// IP-Panels
		
		CategoryPanel ipAddressCategory = new CategoryPanel(mGuiController, CategoryLabels.IP);
		
		IpAddressPanel ip1 = new IpAddressPanel(mGuiController, CharacteristicLabels.IP_USER);
		ip1.setInfoDescription(InfoLabels.IP_USER_INFO);
		ip1.setPropertyNames("User","Role");
		ipAddressCategory.addCharacteristicPanel(ip1);
		
		IpAddressPanel ip2 = new IpAddressPanel(mGuiController, CharacteristicLabels.IP_MAC);
		ip2.setInfoDescription(InfoLabels.IP_MAC_INFO);
		ip2.setPropertyNames("Mac-Address");
		ipAddressCategory.addCharacteristicPanel(ip2);
		
		IpAddressPanel ip3 = new IpAddressPanel(mGuiController, CharacteristicLabels.IP_DEVICES);
		ip3.setInfoDescription(InfoLabels.IP_DEVICES_INFO);
		ip3.setPropertyNames("Devices", "Attributes");
		ipAddressCategory.addCharacteristicPanel(ip3);
		
		IpAddressPanel ip4 = new IpAddressPanel(mGuiController, CharacteristicLabels.IP_AUTH);
		ip4.setInfoDescription(InfoLabels.IP_AUTH_INFO);
		ip4.setPropertyNames("IP-Address", "IP-Version");
		ipAddressCategory.addCharacteristicPanel(ip4);
		
		IpAddressPanel ip5 = new IpAddressPanel(mGuiController, CharacteristicLabels.IP_ALL);
		ip5.setInfoDescription(InfoLabels.IP_ALL_INFO);
		ip5.setPropertyNames("User","Role", "Mac-Address", "Device");
		ipAddressCategory.addCharacteristicPanel(ip5);
		
		// Mac-Panels
		CategoryPanel macAddressCategory = new CategoryPanel(mGuiController, CategoryLabels.MAC);
		
		MacAddressPanel mac1 = new MacAddressPanel(mGuiController, CharacteristicLabels.MAC_USER);
		mac1.setInfoDescription(InfoLabels.MAC_USER_INFO);
		mac1.setPropertyNames("User","Role");
		macAddressCategory.addCharacteristicPanel(mac1);
		
		MacAddressPanel mac2 = new MacAddressPanel(mGuiController, CharacteristicLabels.MAC_IP);
		mac2.setInfoDescription(InfoLabels.MAC_IP_INFO);
		mac2.setPropertyNames("IP-Address", "IP-Version");
		macAddressCategory.addCharacteristicPanel(mac2);
		
		MacAddressPanel mac3 = new MacAddressPanel(mGuiController, CharacteristicLabels.MAC_DEVICES);
		mac3.setInfoDescription(InfoLabels.MAC_DEVICES_INFO);
		mac3.setPropertyNames("Devices", "Attributes");
		macAddressCategory.addCharacteristicPanel(mac3);
	
		MacAddressPanel mac4 = new MacAddressPanel(mGuiController, CharacteristicLabels.MAC_AUTH);
		mac4.setInfoDescription(InfoLabels.MAC_AUTH_INFO);
		mac4.setPropertyNames("Mac-Address");
		macAddressCategory.addCharacteristicPanel(mac4);
	
		MacAddressPanel mac5 = new MacAddressPanel(mGuiController, CharacteristicLabels.MAC_ALL);
		mac5.setInfoDescription(InfoLabels.MAC_ALL_INFO);
		mac5.setPropertyNames("User", "Role", "IP-Address", "IP-Version", "Device");
		macAddressCategory.addCharacteristicPanel(mac5);
		
		// Dev-Panels
		CategoryPanel deviceCategory = new CategoryPanel(mGuiController, CategoryLabels.DEVICE);
		
		DevicePanel dev1 = new DevicePanel(mGuiController, CharacteristicLabels.DEVICE_USER);
		dev1.setInfoDescription(InfoLabels.DEVICE_USER_INFO);
		dev1.setPropertyNames("User","Role");
		deviceCategory.addCharacteristicPanel(dev1);
		
		DevicePanel dev2 = new DevicePanel(mGuiController, CharacteristicLabels.DEVICE_IP);
		dev2.setInfoDescription(InfoLabels.DEVICE_IP_INFO);
		dev2.setPropertyNames("IP-Address", "IP-Version");
		deviceCategory.addCharacteristicPanel(dev2);
		
		DevicePanel dev3 = new DevicePanel(mGuiController, CharacteristicLabels.DEVICE_MAC);
		dev3.setInfoDescription(InfoLabels.DEVICE_MAC_INFO);
		dev3.setPropertyNames("Mac-Address");
		deviceCategory.addCharacteristicPanel(dev3);
		
		DevicePanel dev4 = new DevicePanel(mGuiController, CharacteristicLabels.DEVICE_AUTH);
		dev4.setInfoDescription(InfoLabels.DEVICE_AUTH_INFO);
		dev4.setPropertyNames("Device", "Attributes");
		deviceCategory.addCharacteristicPanel(dev4);
		
		DevicePanel dev5 = new DevicePanel(mGuiController, CharacteristicLabels.DEVICE_ALL);
		dev5.setInfoDescription(InfoLabels.DEVICE_ALL_INFO);
		dev5.setPropertyNames("User","Role", "IP-Address", "IP-Version", "Mac-Address");
		deviceCategory.addCharacteristicPanel(dev5);
		
		mBrowserPanel.addCategoryEntry(CategoryLabels.SEMANTICS, userCategory);
		mBrowserPanel.addCategoryEntry(CategoryLabels.SEMANTICS, ipAddressCategory);
		mBrowserPanel.addCategoryEntry(CategoryLabels.SEMANTICS, macAddressCategory);
		mBrowserPanel.addCategoryEntry(CategoryLabels.SEMANTICS, deviceCategory);
		
		this.add(mBrowserPanel, BorderLayout.WEST);

		mTabs = new ClosableTabbedPane();
		mTabs.addTabChangeListener(new TabChangeHandler(mGuiController, mTabs));
		
		this.add(mTabs, BorderLayout.CENTER);
	}

	/**
	 * Adds a tab with given title and component to {@link MetalyzerPanel}
	 * @param title of the new tab  
	 * @param comp {@link CategoryPanel} child component of the tab
	 * */
	public void addTab(String title, CategoryPanel comp) {
		mTabs.addTab(title, comp, new TabCloseHandler(mGuiController, comp));
	}
	
	/**
	 * Returns the current selected SelectionPanel
	 * @return {@link CategoryPanel}
	 */
	public CategoryPanel getFocusedTab() {
		return (CategoryPanel) mTabs.getSelectedComponent();
	}
	
	/**
	 * Returns the a list of all tabs with SelectionPanel
	 * @return list of {@link CategoryPanel}
	 */
	public List<CategoryPanel> getAllTabs() {
		List<CategoryPanel> tabs = new ArrayList<CategoryPanel>();
		for(int i = 0; i < mTabs.getTabCount(); i++) {
				tabs.add((CategoryPanel) mTabs.getComponentAt(i));
		}
		return tabs;
	}
	
}
