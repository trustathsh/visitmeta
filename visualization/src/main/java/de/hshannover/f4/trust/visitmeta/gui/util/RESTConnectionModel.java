package de.hshannover.f4.trust.visitmeta.gui.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class RESTConnectionModel implements TreeModel {

	private static final Logger LOGGER = Logger.getLogger(RESTConnectionModel.class);

	private Set<TreeModelListener> mTreeModelListeners;

	// private List<DataserviceConnection> mGraphNodes;

	private Dataservices mRootNode;

	private RESTConnectionModel() {
		mTreeModelListeners = new HashSet<TreeModelListener>();
	}

	public RESTConnectionModel(List<Data> graphNodes) {
		this();
		// mGraphNodes = graphNodes;
		mRootNode = new Dataservices();
		mRootNode.mList = graphNodes;
	}

	public void updateConnections() {
		// TODO Auto-generated method stub

	}

	public void showAllMapServerConnections(boolean b) {
		// TODO Auto-generated method stub

	}

	public void showAllSubscriptions(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		mTreeModelListeners.add(l);
	}

	@Override
	public Object getChild(Object parent, int index) {
		Data element = (Data) parent;
		return element.getSubDataAt(index);
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
		// Not used by this model.
		LOGGER.warn("Methode not used by this model! [ valueForPathChanged() ]");
	}

}
