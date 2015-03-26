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
 * This file is part of visitmeta-visualization, version 0.4.1,
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
