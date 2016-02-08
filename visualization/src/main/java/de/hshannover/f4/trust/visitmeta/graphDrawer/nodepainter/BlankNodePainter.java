package de.hshannover.f4.trust.visitmeta.graphDrawer.nodepainter;

import java.awt.Color;
import java.awt.Paint;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

public class BlankNodePainter implements NodePainter {

	private static final Properties mConfig = Main.getConfig();

	private Paint mColorBlankNode = null;
	private Paint mColorText;
	private Color mColorStroke;

	public BlankNodePainter(GraphPanel panel) {
		String vColorSelectedNode = mConfig.getString(VisualizationConfig.KEY_COLOR_NODE_BLANK,
				VisualizationConfig.DEFAULT_VALUE_COLOR_NODE_BLANK);
		String vColorText = VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_TEXT;
		String vColorStroke = VisualizationConfig.DEFAULT_VALUE_COLOR_IDENTIFIER_BORDER;
		
		mColorBlankNode = Color.decode(vColorSelectedNode);
		mColorText = Color.decode(vColorText);
		mColorStroke = Color.decode(vColorStroke);
	}

	@Override
	public void paintMetadataNode(Metadata metadata, GraphicWrapper graphic) {
		graphic.setPaint(mColorBlankNode);
		graphic.setTextPaint(mColorText);
		graphic.setStrokePaint(mColorStroke);
	}

	@Override
	public void paintIdentifierNode(Identifier identifier, GraphicWrapper graphic) {
		graphic.setPaint(mColorBlankNode);
		graphic.setTextPaint(mColorText);
		graphic.setStrokePaint(mColorStroke);
	}

}
