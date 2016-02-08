package de.hshannover.f4.trust.visitmeta.graphDrawer.nodepainter;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.util.ExtendedIdentifierHelper;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

public class HighlightNodePainter implements NodePainter {

	private static final Properties mConfig = Main.getConfig();

	private List<String> mHighlightedNodeTypeNames = null;
	
	private Paint mColorHighlightNode = null;
	
	public HighlightNodePainter(GraphPanel panel) {
		String nodeTypenames = mConfig.getString(VisualizationConfig.KEY_COLOR_NODE_HIGHLIGHT_TYPENAMES,
				VisualizationConfig.DEFAULT_VALUE_COLOR_NODE_HIGHLIGHT_TYPENAMES);
		
		if (nodeTypenames.length() != 0) {
			mHighlightedNodeTypeNames = new ArrayList<>();
			String[] split = nodeTypenames.split(",");
			for (String s : split) {
				mHighlightedNodeTypeNames.add(s);
			}
		}
		
		String vColorSelectedNode = mConfig.getString(VisualizationConfig.KEY_COLOR_NODE_HIGHLIGHT,
				VisualizationConfig.DEFAULT_VALUE_COLOR_NODE_HIGHLIGHT);
		
		mColorHighlightNode = Color.decode(vColorSelectedNode);
	}

	@Override
	public void paintMetadataNode(Metadata metadata, GraphicWrapper graphic) {
		if (mHighlightedNodeTypeNames != null) {
			if (mHighlightedNodeTypeNames.contains(metadata.getTypeName())) {
				graphic.setPaint(mColorHighlightNode);
			}
		}
	}

	@Override
	public void paintIdentifierNode(Identifier identifier, GraphicWrapper graphic) {
		if (mHighlightedNodeTypeNames != null) {
			if (ExtendedIdentifierHelper.isExtendedIdentifier(identifier)) {
				if (mHighlightedNodeTypeNames.contains(ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName(identifier))) {
					graphic.setPaint(mColorHighlightNode);
				}
			} else if (mHighlightedNodeTypeNames.contains(identifier.getTypeName())) {
				graphic.setPaint(mColorHighlightNode);
			}
		}
	}

}
