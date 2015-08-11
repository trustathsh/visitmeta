package de.hshannover.f4.trust.visitmeta.graphDrawer.nodepainter;

import java.util.Arrays;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

/**
 * A {@link NodePainter} implementation that paints all node that are hovered over by the user with the mouse pointer,
 * and all nodes that included in a list of typenames.
 * As an example, this class paints IP address and MAC address identifier and ip-mac medadata objects, when one of them
 * is hovered over.
 *
 * @author Bastian Hellmann
 *
 */
public class SubgraphIpMacMouseOverNodePainter extends MouseOverNodePainter {

	private List<String> mConnectedNodeTypeNames =
			Arrays.asList(new String[] {IfmapStrings.IP_ADDRESS_EL_NAME, IfmapStrings.MAC_ADDRESS_EL_NAME, "ip-mac"});

	public SubgraphIpMacMouseOverNodePainter(GraphPanel panel) {
		super(panel);
	}

	@Override
	public void paintMetadataNode(Metadata metadata, GraphicWrapper graphic) {
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
	public void paintIdentifierNode(Identifier identifier, GraphicWrapper graphic) {
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
