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
 * This file is part of visitmeta-visualization, version 0.5.1,
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class RESTConnectionModel implements TreeModel {

	private Set<TreeModelListener> mTreeModelListeners;

	private Dataservices mRootNode;

	private boolean mOnlyActiveMapServerConnections;

	private boolean mOnlyActiveSubscriptions;

	private RESTConnectionModel() {
		mTreeModelListeners = new HashSet<TreeModelListener>();
	}

	public RESTConnectionModel(List<DataserviceConnection> dataserviceList) {
		this();
		mRootNode = new Dataservices();
		mRootNode.mList = dataserviceList;
	}

	public void updateConnections(List<DataserviceConnection> graphNodes) {
		Dataservices oldRoot = mRootNode;

		mRootNode = new Dataservices();
		mRootNode.mList = graphNodes;

		fireTreeStructureChanged(oldRoot);
	}

	protected void fireTreeStructureChanged(Object oldRoot) {
		TreeModelEvent e = new TreeModelEvent(this, new Object[] {oldRoot});
		for (TreeModelListener tml : mTreeModelListeners) {
			tml.treeStructureChanged(e);
		}
	}

	public void showAllMapServerConnections(boolean b) {
		mOnlyActiveMapServerConnections = b;
	}

	public void showAllSubscriptions(boolean b) {
		mOnlyActiveSubscriptions = b;
	}

	public boolean isOnlyActiveMapServerConnections() {
		return mOnlyActiveMapServerConnections;
	}

	public boolean isOnlyActiveSubscriptions() {
		return mOnlyActiveSubscriptions;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		mTreeModelListeners.add(l);
	}

	@Override
	public Object getChild(Object parent, int index) {
		Data element = (Data) parent;
		Data child = element.getSubDataAt(index);
		return child;
	}

	@Override
	public int getChildCount(Object parent) {
		Data element = (Data) parent;
		return element.getSubDataCount();
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		Data element = (Data) parent;
		return element.getIndexOfSubData((Data) child);
	}

	@Override
	public Object getRoot() {
		return mRootNode;
	}

	@Override
	public boolean isLeaf(Object node) {
		Data element = (Data) node;
		return element.getSubDataCount() == 0;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		mTreeModelListeners.remove(l);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		fireTreeStructureChanged(mRootNode);
	}

}
