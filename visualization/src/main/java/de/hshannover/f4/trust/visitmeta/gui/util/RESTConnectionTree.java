package de.hshannover.f4.trust.visitmeta.gui.util;

import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class RESTConnectionTree extends JTree {

	private static final long serialVersionUID = -5319549107764511043L;

	public RESTConnectionTree(List<Data> graphNodes) {
		super(new RESTConnectionModel(graphNodes));
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

	public void updateConnections(List<Data> graphNodes) {
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
