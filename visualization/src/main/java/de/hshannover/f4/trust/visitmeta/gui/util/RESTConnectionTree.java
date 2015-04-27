package de.hshannover.f4.trust.visitmeta.gui.util;

import java.util.List;

import javax.swing.JTree;
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

	public void updateConnections() {
		((RESTConnectionModel) super.getModel()).updateConnections();
	}

	public void showAllMapServerConnections(boolean b) {
		((RESTConnectionModel) super.getModel()).showAllMapServerConnections(b);
	}

	public void showAllSubscriptions(boolean b) {
		((RESTConnectionModel) super.getModel()).showAllSubscriptions(b);
	}
}
