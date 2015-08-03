package de.hshannover.f4.trust.visitmeta.graphDrawer.noderenderer;

import java.awt.Color;
import java.awt.Paint;
import java.util.Arrays;
import java.util.List;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeIdentifier;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

public class MouseOverNodeRenderer implements NodeRenderer {

	private static final Properties mConfig = Main.getConfig();

	private GraphPanel mGraphPanel;

	private Paint mColorMouseOverNode;

	private List<String> mConnectedNodeTypeNames =
			Arrays.asList(new String[] {IfmapStrings.IP_ADDRESS_EL_NAME, IfmapStrings.MAC_ADDRESS_EL_NAME, "ip-mac"});

	public MouseOverNodeRenderer(GraphPanel panel) {
		mGraphPanel = panel;

		String vColorMouseOverNode = mConfig.getString(VisualizationConfig.KEY_COLOR_NODE_MOUSE_OVER,
				VisualizationConfig.DEFAULT_VALUE_COLOR_NODE_MOUSE_OVER);

		mColorMouseOverNode = Color.decode(vColorMouseOverNode);
	}

	@Override
	public void paintMetadataNode(Metadata metadata, NodeMetadata m, GraphicWrapper graphic) {
		Propable mouseOverNode = mGraphPanel.getMouseOverNode();
		if (mouseOverNode != null) {
			boolean isMouseOver = isMouseOver(mouseOverNode, metadata);
			boolean isConnected = isConnected(mouseOverNode, metadata);
			if (isMouseOver
					|| isConnected) {
				graphic.setPaint(mColorMouseOverNode);
			}
		}
	}

	@Override
	public void paintIdentifierNode(Identifier identifier, NodeIdentifier i, GraphicWrapper graphic) {
		Propable mouseOverNode = mGraphPanel.getMouseOverNode();
		if (mouseOverNode != null) {
			boolean isMouseOver = isMouseOver(mouseOverNode, identifier);
			boolean isConnected = isConnected(mouseOverNode, identifier);
			if (isMouseOver
					|| isConnected) {
				graphic.setPaint(mColorMouseOverNode);
			}
		}
	}

	private boolean isMouseOver(Propable mouseOverNode, Propable toTest) {
		if (mouseOverNode == null) {
			return false;
		} else {
			if (mouseOverNode == toTest) {
				return true;
			} else {
				return false;
			}
		}
	}

	private boolean isConnected(Propable mouseOverNode, Propable toTest) {
		String mouseOverTypeName = mouseOverNode.getTypeName();
		String toTestTypeName = toTest.getTypeName();

		if (mConnectedNodeTypeNames.contains(toTestTypeName)
				&& mConnectedNodeTypeNames.contains(mouseOverTypeName)) {
			return true;
		} else {
			return false;
		}
	}

}
