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
 * This file is part of visitmeta-visualization, version 0.4.2,
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
import javax.swing.tree.DefaultTreeCellRenderer;

import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.MapServerConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class ConnectionTreeCellRenderer extends DefaultTreeCellRenderer {
	
	private static final long serialVersionUID = 7918592799908870099L;

	private ImageIcon mMapServerConnectedIcon;
	
	private ImageIcon mMapServerDisconnectedIcon;
	
	private ImageIcon mRrootIcon;
	
	private ImageIcon mDataserviceConnectedIcon;
	
	private ImageIcon mDataserviceDisconnectedIcon;
	
	private ImageIcon mSubscriptionActiveIcon;
	
	private ImageIcon mSubscriptionInactiveIcon;

	public ConnectionTreeCellRenderer() {
		super();
		if (mRrootIcon == null) {
			mRrootIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
					.getResource("ConnectionTreeRoot.png").getPath());
		}

		if (mDataserviceConnectedIcon == null) {
			mDataserviceConnectedIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
					.getResource("DataserviceConnectedIcon.png").getPath());
		}

		if (mDataserviceDisconnectedIcon == null) {
			mDataserviceDisconnectedIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
					.getResource("DataserviceDisconnectedIcon.png").getPath());
		}

		if (mMapServerConnectedIcon == null) {
			mMapServerConnectedIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
					.getResource("MapServerConnectedIcon.png").getPath());
		}
		
		if (mMapServerDisconnectedIcon == null) {
			mMapServerDisconnectedIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
					.getResource("MapServerDisconnectedIcon.png").getPath());
		}

		if (mSubscriptionActiveIcon == null) {
			mSubscriptionActiveIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
					.getResource("SubscriptionActiveIcon.png").getPath());
		}
		
		if (mSubscriptionInactiveIcon == null) {
			mSubscriptionInactiveIcon = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
					.getResource("SubscriptionInactiveIcon.png").getPath());
		}
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		if (value instanceof Data) {
			setIcon(getStatusIcon((Data) value));
		}
		return this;
	}

	private ImageIcon getStatusIcon(Data data) {
		if (data instanceof DataserviceConnection) {
			if (((DataserviceConnection) data).isConnected()) {
				return mDataserviceConnectedIcon;
			}
			return mDataserviceDisconnectedIcon;
		} else if (data instanceof MapServerConnection) {
			if (((MapServerConnection) data).isConnected()) {
				return mMapServerConnectedIcon;
			}
			return mMapServerDisconnectedIcon;
		} else if (data instanceof Subscription) {
			if (((Subscription) data).isActive()) {
				return mSubscriptionActiveIcon;
			}
			return mSubscriptionInactiveIcon;
		}
		return mRrootIcon;
	}
}
