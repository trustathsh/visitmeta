package de.hshannover.f4.trust.visitmeta.util;

import javax.swing.ImageIcon;

import de.hshannover.f4.trust.visitmeta.gui.util.ConnectionTreeCellRenderer;

public class ImageIconLoader {

	public static final ImageIcon DATASERVICES_ICON;

	public static final ImageIcon DATASERVICE_CONNECTED_ICON;

	public static final ImageIcon DATASERVICE_DISCONNECTED_ICON;

	public static final ImageIcon MAPSERVER_CONNECTED_ICON;

	public static final ImageIcon MAPSERVER_DISCONNECTED_ICON;

	public static final ImageIcon SUBSCRIPTION_ACTIVE_ICON;

	public static final ImageIcon SUBSCRIPTION_INACTIVE_ICON;

	static {

		DATASERVICES_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("ConnectionTreeRoot.png").getPath());

		DATASERVICE_CONNECTED_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("DataserviceConnectedIcon.png").getPath());

		DATASERVICE_DISCONNECTED_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("DataserviceDisconnectedIcon.png").getPath());

		MAPSERVER_CONNECTED_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("MapServerConnectedIcon.png").getPath());

		MAPSERVER_DISCONNECTED_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("MapServerDisconnectedIcon.png").getPath());

		SUBSCRIPTION_ACTIVE_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("SubscriptionActiveIcon.png").getPath());

		SUBSCRIPTION_INACTIVE_ICON = new ImageIcon(ConnectionTreeCellRenderer.class.getClassLoader()
				.getResource("SubscriptionInactiveIcon.png").getPath());
	}

}
