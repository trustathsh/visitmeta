package de.hshannover.f4.trust.visitmeta.graphDrawer.nodepainter;

import java.awt.Color;
import java.awt.Paint;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

/**
 * A {@link NodePainter} implementation that paints the node that is hovered over by the user with the mouse pointer.
 *
 * @author Bastian Hellmann
 *
 */
public class MouseOverNodePainter implements NodePainter {

	private static final Properties mConfig = Main.getConfig();

	protected GraphPanel mGraphPanel;

	protected Paint mColorMouseOverNode;

	public MouseOverNodePainter(GraphPanel panel) {
		mGraphPanel = panel;

		String vColorMouseOverNode = mConfig.getString(VisualizationConfig.KEY_COLOR_NODE_MOUSE_OVER,
				VisualizationConfig.DEFAULT_VALUE_COLOR_NODE_MOUSE_OVER);

		mColorMouseOverNode = Color.decode(vColorMouseOverNode);
	}

	@Override
	public void paintMetadataNode(Metadata metadata, GraphicWrapper graphic) {
		Propable mouseOverNode = mGraphPanel.getMouseOverNode();
		if (mouseOverNode != null) {
			boolean isMouseOver = isMouseOver(mouseOverNode, metadata);
			if (isMouseOver) {
				graphic.setPaint(mColorMouseOverNode);
			}
		}
	}

	@Override
	public void paintIdentifierNode(Identifier identifier, GraphicWrapper graphic) {
		Propable mouseOverNode = mGraphPanel.getMouseOverNode();
		if (mouseOverNode != null) {
			boolean isMouseOver = isMouseOver(mouseOverNode, identifier);
			if (isMouseOver) {
				graphic.setPaint(mColorMouseOverNode);
			}
		}
	}

	protected boolean isMouseOver(Propable mouseOverNode, Propable toTest) {
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

}
