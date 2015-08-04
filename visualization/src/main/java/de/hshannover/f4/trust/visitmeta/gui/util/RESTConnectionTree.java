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
 * This file is part of visitmeta-visualization, version 0.5.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.gui.util;

import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class RESTConnectionTree extends JTree {

	private static final long serialVersionUID = -5319549107764511043L;

	public RESTConnectionTree(List<DataserviceConnection> dataserviceList) {
		super(new RESTConnectionModel(dataserviceList));
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		// DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		// Icon nullIcon = null;
		// renderer.setLeafIcon(nullIcon);
		// renderer.setClosedIcon(nullIcon);
		// renderer.setOpenIcon(nullIcon);
		// setCellRenderer(renderer);
		// setRootVisible(false);
	}

	public void updateModel() {
		super.getModel().valueForPathChanged(null, null);
		expandAllNodes();
	}

	public void updateConnections(List<DataserviceConnection> graphNodes) {
		((RESTConnectionModel) super.getModel()).updateConnections(graphNodes);
	}

	public void showAllMapServerConnections(boolean b) {
		((RESTConnectionModel) super.getModel()).showAllMapServerConnections(b);
	}

	public void showAllSubscriptions(boolean b) {
		((RESTConnectionModel) super.getModel()).showAllSubscriptions(b);
	}

	public boolean isShowAllMapServerConnections() {
		return ((RESTConnectionModel) super.getModel()).isOnlyActiveMapServerConnections();
	}

	public boolean isShowAllSubscriptions() {
		return ((RESTConnectionModel) super.getModel()).isOnlyActiveSubscriptions();
	}

	public void expandAllNodes() {
		int j = super.getRowCount();
		int i = 0;
		while (i < j) {
			super.expandRow(i);
			i++;
			j = super.getRowCount();
		}
	}

	public Data getSelectedParentData(){
		TreePath selectedTreePath = super.getSelectionPath();
		TreePath parentTreePath = selectedTreePath.getParentPath();

		Object selectedParentData = parentTreePath.getLastPathComponent();

		if (selectedParentData instanceof Data) {
			return (Data) selectedParentData;
		}

		return null;
	}
}
