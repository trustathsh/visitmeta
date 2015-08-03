package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.edgerenderer;

import java.awt.BasicStroke;
import java.awt.geom.Point2D;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import edu.umd.cs.piccolo.nodes.PPath;

public class StraightDashedLinePiccolo2dEdgeRenderer implements Piccolo2dEdgeRenderer {

	@Override
	public void drawEdge(PPath pEdge, Point2D vStart, Point2D vEnd, Metadata metadata,
			Identifier identifier) {
		drawStraightDashedLine(pEdge, vStart, vEnd);
	}

	public static void drawStraightDashedLine(PPath pEdge, Point2D vStart, Point2D vEnd) {
		float[] dash = {10.0f};
		pEdge.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
		pEdge.moveTo((float) vStart.getX(), (float) vStart.getY());
		pEdge.lineTo((float) vEnd.getX(), (float) vEnd.getY());
	}

}
