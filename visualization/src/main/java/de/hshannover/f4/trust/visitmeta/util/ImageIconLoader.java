package de.hshannover.f4.trust.visitmeta.util;

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


import javax.swing.ImageIcon;

import de.hshannover.f4.trust.visitmeta.gui.util.ConnectionTreeCellRenderer;

public class ImageIconLoader {

	public static final ImageIcon ADD_ICON;

	public static final ImageIcon CLONE_ICON;
	
	public static final ImageIcon DELETE_ICON;
	
	public static final ImageIcon DATASERVICES_ICON;

	public static final ImageIcon DATASERVICE_CONNECTED_ICON;

	public static final ImageIcon CONNECTED_TREE_UPDATE_ICON;

	public static final ImageIcon DATASERVICE_DISCONNECTED_ICON;

	public static final ImageIcon DATASERVICE_NOT_PERSISTED_ICON;

	public static final ImageIcon MAPSERVER_CONNECTED_ICON;

	public static final ImageIcon MAPSERVER_DISCONNECTED_ICON;

	public static final ImageIcon MAPSERVER_NOT_PERSISTED_ICON;

	public static final ImageIcon SUBSCRIPTION_ACTIVE_ICON;

	public static final ImageIcon SUBSCRIPTION_INACTIVE_ICON;

	public static final ImageIcon SUBSCRIPTION_NOT_PERSISTED_ICON;

	static {

		ADD_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("AddIcon.png").getPath());

		CLONE_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("CloneIcon.png").getPath());
		
		DELETE_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("DeleteIcon.png").getPath());

		DATASERVICES_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("ConnectionTreeRoot.png").getPath());

		DATASERVICE_CONNECTED_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("DataserviceConnectedIcon.png").getPath());

		CONNECTED_TREE_UPDATE_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("ConnectionTreeUpdate.png").getPath());

		DATASERVICE_DISCONNECTED_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("DataserviceDisconnectedIcon.png").getPath());

		DATASERVICE_NOT_PERSISTED_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("DataserviceNotPersistedIcon.png").getPath());

		MAPSERVER_CONNECTED_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("MapServerConnectedIcon.png").getPath());

		MAPSERVER_DISCONNECTED_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("MapServerDisconnectedIcon.png").getPath());

		MAPSERVER_NOT_PERSISTED_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("MapServerNotPersistedIcon.png").getPath());

		SUBSCRIPTION_ACTIVE_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("SubscriptionActiveIcon.png").getPath());

		SUBSCRIPTION_INACTIVE_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("SubscriptionInactiveIcon.png").getPath());

		SUBSCRIPTION_NOT_PERSISTED_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("SubscriptionNotPersistedcon.png").getPath());
	}

}
