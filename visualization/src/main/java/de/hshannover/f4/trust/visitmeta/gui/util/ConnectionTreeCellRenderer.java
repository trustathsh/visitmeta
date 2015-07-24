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

import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.DATASERVICES_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.DATASERVICE_CONNECTED_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.DATASERVICE_DISCONNECTED_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.DATASERVICE_NOT_PERSISTED_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.MAPSERVER_CONNECTED_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.MAPSERVER_DISCONNECTED_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.MAPSERVER_NOT_PERSISTED_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.SUBSCRIPTION_ACTIVE_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.SUBSCRIPTION_INACTIVE_ICON;
import static de.hshannover.f4.trust.visitmeta.util.ImageIconLoader.SUBSCRIPTION_NOT_PERSISTED_ICON;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class ConnectionTreeCellRenderer extends DefaultTreeCellRenderer {
	
	private static final long serialVersionUID = 7918592799908870099L;

	public ConnectionTreeCellRenderer() {
		super();
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
		if (data instanceof DataserviceRestConnectionImpl) {
			if (((DataserviceRestConnectionImpl) data).isNotPersised()) {
				return DATASERVICE_NOT_PERSISTED_ICON;
			} else if (((DataserviceRestConnectionImpl) data).isConnected()) {
				return DATASERVICE_CONNECTED_ICON;
			}
			return DATASERVICE_DISCONNECTED_ICON;
		} else if (data instanceof MapServerRestConnectionImpl) {
			if (((MapServerRestConnectionImpl) data).isNotPersised()) {
				return MAPSERVER_NOT_PERSISTED_ICON;
			} else if (((MapServerRestConnectionImpl) data).isConnected()) {
				return MAPSERVER_CONNECTED_ICON;
			}
			return MAPSERVER_DISCONNECTED_ICON;
		} else if (data instanceof RestSubscriptionImpl) {
			if (((RestSubscriptionImpl) data).isNotPersised()) {
				return SUBSCRIPTION_NOT_PERSISTED_ICON;
			} else if (((RestSubscriptionImpl) data).isActive()) {
				return SUBSCRIPTION_ACTIVE_ICON;
			}
			return SUBSCRIPTION_INACTIVE_ICON;
		}
		return DATASERVICES_ICON;
	}
}
