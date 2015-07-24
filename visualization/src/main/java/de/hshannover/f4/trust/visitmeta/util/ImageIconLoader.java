package de.hshannover.f4.trust.visitmeta.util;

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
