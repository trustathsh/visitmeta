package de.hshannover.f4.trust.visitmeta.gui.util;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.hshannover.f4.trust.visitmeta.gui.ConnectionTab;

public class ConnectionTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;
	private ImageIcon[] connectionStatusIcon = null;
	private ImageIcon dataserviceIcon = null;
	private ImageIcon connectionIcon = null;

	public ConnectionTreeCellRenderer() {
		super();
		if (dataserviceIcon == null) {
			dataserviceIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
					.getResource("dataservice.png").getPath());
		}

		if (connectionIcon == null) {
			connectionIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
					.getResource("connection.png").getPath());
		}

		if (connectionStatusIcon == null) {
			connectionStatusIcon = new ImageIcon[2];
			connectionStatusIcon[0] = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
					.getResource("connected.png").getPath());
			connectionStatusIcon[1] = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
					.getResource("disconnected.png").getPath());
		}
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		Object tmpValue = ((DefaultMutableTreeNode) value).getUserObject();
		if (!leaf) {
			if (tmpValue instanceof String) {
				if (((String) tmpValue).equals("Dataservices")) {
					setIcon(dataserviceIcon);
				}
			} else if (tmpValue instanceof DataserviceConnection) {
				setIcon(connectionIcon);
			}
		} else {
			if (tmpValue instanceof ConnectionTab) {
				ConnectionTab tmp = (ConnectionTab) tmpValue;
				setIcon(getStatusIcon(tmp.isConnected()));
			}
		}

		return this;
	}

	private ImageIcon getStatusIcon(boolean status) {
		if (status) {
			return connectionStatusIcon[0];
		} else {
			return connectionStatusIcon[1];
		}
	}
}
